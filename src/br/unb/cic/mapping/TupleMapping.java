/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.mapping;

import br.unb.cic.replication.InRdRequest;
import br.unb.cic.replication.UpdateSubSpaceRequest;
import br.unb.cic.replication.RedistribuiTuplasRequest;
import br.unb.cic.tuplespace.TSRequestContext;
import br.unb.cic.tuplespace.Tuple;
import br.unb.cic.tuplespace.TupleSpace;
import br.unb.cic.tuplespace.TupleSpaceImpl;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import navigators.smart.reconfiguration.views.View;
import navigators.smart.tom.ReplicaContext;
import navigators.smart.tom.ServiceProxy;

/**
 *
 * @author samuelstj
 */
public class TupleMapping implements TupleSpace {

    private TupleSpaceImpl tupleSpace = new TupleSpaceImpl();
    private int id;
    private int subspaceId;
    private int engineType;
    private ReplicaContext replicaCtx;
    private SubSpaces spaces;
    private TupleMappingEngine engine;
    private View currentView;
    private ServiceProxy clientProxy;
    private Map<Integer, InRdCtx> ctx = new Hashtable<Integer, InRdCtx>();
    private int memoriaUsada = 0;
    

    public TupleMapping(int id, ReplicaContext rc, int engineType) {
        this.id = id;
        this.replicaCtx = rc;
        this.engineType = engineType;
        spaces = new SubSpaces();
        this.currentView = getCurrentView();
        spaces.createSubSpaces(this.currentView);

        if (engineType == 0) {
            this.engine = new SimpleTupleMappingEngine();
            System.out.println("Engine: Simple");
        } else if (engineType == 1) {
            this.engine = new HilbertTupleMappingEngine(id, spaces, 120);
            System.out.println("Engine: Hilbert");
        } else if (engineType == 2) {
            this.engine = new MemoryTupleMappingEngine(id, spaces);
            System.out.println("Engine: Memory");
        } else if (engineType == 3) {
            this.engine = new TupleSizeMappingEngine(id, spaces, 60);
            System.out.println("Engine: TupleSize");
        } else if (engineType == 4) {
            //this.engine = new TupleFieldsMappingEngine(id, spaces, 3); //9 MAQ
            this.engine = new TupleFieldsMappingEngine(id, spaces, 2);
            System.out.println("Engine: TupleFields");
        }
    }

   
    private ServiceProxy getProxy() {
        if (clientProxy == null) {
            clientProxy = new ServiceProxy(id);
        }
        return clientProxy;
    }

    public void sendMsg(Tuple t, int id, int codigo) {

        InRdRequest r = new InRdRequest(this.id, t, id, codigo);

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(4);
            new ObjectOutputStream(out).writeObject(r);

            getProxy().invokeUnordered(out.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void EnviaSpacesPNovo(SubSpaces spaces, TupleSpaceImpl tupleSpace, int procUsed) {

        UpdateSubSpaceRequest r = new UpdateSubSpaceRequest(spaces, tupleSpace, procUsed);

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(4);
            new ObjectOutputStream(out).writeObject(r);

            getProxy().invokeUnordered(out.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void RedistribuiTuplas(SubSpaces spaces, TupleSpaceImpl tupleSpace) {

        RedistribuiTuplasRequest r = new RedistribuiTuplasRequest(spaces, tupleSpace);

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(4);
            new ObjectOutputStream(out).writeObject(r);

            getProxy().invokeUnordered(out.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void receiveMsg(InRdRequest r) {
        getCtx(r.getId()).receiveMsg(r);
    }

    public void receiveSubspaces(UpdateSubSpaceRequest r) {

        if (this.id == r.getProcUsed()) {

            this.spaces = r.getSpaces();
            this.tupleSpace = r.getTupleSpace();

            //System.out.println(this.tupleSpace.getTuples());

            System.out.println("Subspaces updated");
        }
    }

    public void receiveSubspaces2(RedistribuiTuplasRequest r) {

        this.spaces = r.getSpaces();
        List<Tuple> tuplas = r.getTupleSpace().getTuples();

        for (int i = 0; i < tuplas.size(); i++) {
            System.out.println("Inserindo a tupla: " + tuplas.get(i));

            if (this.engine.isToStore(tuplas.get(i), null)) {
                this.tupleSpace.out(tuplas.get(i), null);
            }
            System.out.println("--> " + this.tupleSpace.getTuples());
        }

        //this.tupleSpace = r.getTupleSpace();

        //System.out.println(this.tupleSpace.getTuples());

        //System.out.println("Subspaces updated");

    }

    public int getSubSpaceId(int pId) {
        for (int i = 0; i < this.spaces.getSpaces().size(); i++) {
            if (this.spaces.getSpaces().get(i).isMember(pId)) {
                return this.spaces.getSpaces().get(i).getId();
            }
        }
        return -1;
    }

    public View getCurrentView() {
        return this.replicaCtx.getCurrentView();
    }

    public int compareViewProcs(int[] v1, int[] v2) {

        boolean found = false;

        for (int i = 0; i < v1.length; i++) {
            for (int j = 0; j < v2.length; j++) {
                if (v1[i] == v2[j]) {
                    found = true;
                    //processo de v1 encontrado em v2
                }
            }

            if (found == false) {
                //processo de v1 nao encontrado em v2
                return v1[i];
            }

            found = false;
        }

        return -1;
    }

    private void checkViewUpdated() {
        //System.out.println("------------");
        //System.out.println("ID View antiga:: " + this.currentView.getId());
        //System.out.println("View antiga:: "+this.currentView.toString());
        //System.out.println("ID View atual:: " + getCurrentView().getId());
        //System.out.println("View atual:: "+getCurrentView().toString());
        //System.out.println("------------");

        

        if (getCurrentView().getId() != this.currentView.getId()) {
            View newView = getCurrentView();

            int n = newView.getN();
            int f = newView.getF();
            int tSubs = 2 * f + 1;
            int nStandby = n % tSubs;

            int vOld[] = this.currentView.getProcesses();
            int vNew[] = newView.getProcesses();

            /**
             * ***********************
             *** PROCESSO REMOVIDO *** ***********************
             */
            if (vOld.length > vNew.length) {

                int procRemoved = compareViewProcs(vOld, vNew);
                System.out.println("REMOVENDO PROCESSO: " + procRemoved);

                int idSubspRemoved = getSubSpaceId(procRemoved);
                System.out.println("idSubsRemoved:: " + idSubspRemoved);

                if (idSubspRemoved < 0) { //SE PR ESTA EM STANDBY

                    System.out.println("O PROCESSO [" + procRemoved + "] ESTAVA EM STANDBY, NADA A FAZER");
                    spaces.getStandby().rmProcess(procRemoved);

                    this.currentView = getCurrentView();

                } else { //SE PR NAO ESTA EM STANDBY

                    System.out.println("O PROCESSO [" + procRemoved + "] NÃO ESTAVA EM STANDBY");
                    if (spaces.hasStandby()) { //SE EXISTE UM PROCESSO EM STANDBY
                        this.currentView = getCurrentView();

                        System.out.println("EXISTE UM PROCESSO EM STANDBY");

                        int[] sbProcs = spaces.getStandby().getProcesses();
                        int procUsed = sbProcs[sbProcs.length - 1];

                        spaces.getSpaces().get(idSubspRemoved).swapProcess(procRemoved, procUsed);

                        spaces.getStandby().rmProcess(procUsed);

                        if (getSubSpaceId(this.id) == idSubspRemoved) {
                            EnviaSpacesPNovo(this.spaces, this.tupleSpace, procUsed);
                        }

                        //substituir pr pelo processo em standby - atualizar subspaces -- ok
                        //atualizar o estado do processo que estava em standby -- ok
                        //o processo pode sair -- ??

                    } else { //SE NAO EXISTE UM PROCESSO EM STANDBY
                        this.currentView = getCurrentView();

                        System.out.println("NÃO EXISTE UM PROCESSO EM STANDBY");

                        //remove o processo do subspace
                        spaces.getSpaces().get(idSubspRemoved).rmProcess(procRemoved);

                        //copia os processos para standby
                        int[] pStandby = spaces.getSpaces().get(idSubspRemoved).getProcesses();
                        spaces.addStandby(pStandby);

                        if (getSubSpaceId(this.id) == idSubspRemoved) {
                            RedistribuiTuplas(this.spaces, this.tupleSpace);
                        }

                        //remove subspace antigo
                        spaces.rmSubspace(idSubspRemoved);

                        //atualizar subspaces -- ok
                        //pegar a lista de tuplas dos processos do "subspace antigo" -- ??
                        //redistribuir as tuplas -- ??
                        //remover as tuplas dos processos antigos -- ??
                        //o processo pode sair -- ??

                    }

                }
                /**
                 * *************************
                 *** PROCESSO ADICIONADO *** *************************
                 */
            } else {

                int procAdded = compareViewProcs(vNew, vOld);

                if (procAdded != -1) {
                    System.out.println("ADICIONANDO PROCESSO: " + procAdded);
                    int sbProcs[];

                    if (spaces.hasStandby()) {
                        sbProcs = spaces.getStandby().getProcesses();
                    } else {
                        sbProcs = new int[1];
                    }

                    if (sbProcs.length < (2 * f)) { //ADICIONAR PROCESSO EM STANDBY
                        int[] pStandby = new int[nStandby];

                        for (int i = 0; i < sbProcs.length; i++) {
                            pStandby[i] = sbProcs[i];
                        }
                        pStandby[nStandby - 1] = procAdded;

                        spaces.addStandby(pStandby);

                        this.currentView = getCurrentView();

                    } else { //ADICIONAR PROCESSO E FORMAR NOVO SUBSPACE
                        int[] pSubs = new int[tSubs];

                        for (int i = 0; i < sbProcs.length; i++) {
                            pSubs[i] = sbProcs[i];
                        }
                        pSubs[tSubs - 1] = procAdded;

                        spaces.addSubspace(pSubs, f);
                        spaces.clearStandby();

                        this.currentView = getCurrentView();
                    }
                }
            }

        }

        //
        /*for (int i = 0; i < spaces.getSpaces().size(); i++) {
            System.out.println("SubSpace : " + spaces.getSpaces().get(i));
        }
        System.out.println("Standby : " + spaces.getStandby());*/
        //
    }

    private InRdCtx getCtx(int id) {
        InRdCtx ret = this.ctx.get(id);
        if (ret == null) {
            ret = new InRdCtx(id, getSubSpaceId(this.id), this);
            this.ctx.put(id, ret);
        }
        return ret;
    }

    @Override
    public List<Tuple> getTuples() {
        //checkViewUpdated();
        return this.tupleSpace.getTuples();
    }

    @Override
    public void out(Tuple tuple, TSRequestContext ctx) {
        checkViewUpdated();
        if (this.engine.isToStore(tuple, ctx)) {
            this.tupleSpace.out(tuple, ctx);
            
            //Contabilizando a utilização de memória - Início
            int tSize = tuple.getTupleSize();
            //System.out.println("Tamanho da tupla: "+tSize);
            memoriaUsada = memoriaUsada + tSize; 
            
            //System.out.println("--------------------------------------");
            //System.out.println("--------------------------------------");
            //System.out.println("Utilização de memória M: "+memoriaUsada);

            //Contabilizando a utilização de memória - Fim
        }

    }

    @Override
    public Tuple inp(Tuple template, TSRequestContext ctx) {

        checkViewUpdated();
        subspaceId = getSubSpaceId(id);

        InRdCtx reqCtx = getCtx(ctx.getId());

        if (subspaceId == 0) {
            reqCtx.liberaExecucao();
        }

        reqCtx.esperaParaExecutar();

        if (!reqCtx.isIsResultSetted()) {

            //EXECUTA LOCALMENTE A OPERACAO
            if (this.tupleSpace.rdp(template, ctx) != null) {//possui a tupla
                Tuple lida = this.tupleSpace.inp(template, ctx);
                //atualizar MemoryUsage
                reqCtx.setResult(lida);
                reqCtx.setSubspaceIdU(subspaceId);
                sendMsg(lida, ctx.getId(), 88);
                //return lida;

            } else if (this.getSpaces().getSubSpacesNum() <= 1 || this.getSubSpaceId(this.id) == (this.spaces.getSubSpacesNum() - 1)) {
                reqCtx.setResult(null);
                sendMsg(null, ctx.getId(), 88);
                reqCtx.setSubspaceIdU(-1);
            } else {//nao possui a tupla
                sendMsg(template, ctx.getId(), 98);
            }
        }

        reqCtx.esperaPeloResultado();

        Tuple result = reqCtx.getResult();

        //System.out.println("memoryUsage[0] Antes: " + engine.getMemoryUsage(0));
        //System.out.println("memoryUsage[1] Antes: " + engine.getMemoryUsage(1));

        if (engineType == 2 && result != null) {
            engine.UpdateMemoryUsage(reqCtx.getSubspaceU(), result);
        }
        
        
        // Inserido apenas para testes de utilização de memória
        /*if (engineType == 1 && result != null) {
            engine.UpdateMemoryUsage(reqCtx.getSubspaceU(), result);
        }*/

        //System.out.println("memoryUsage[0] Depois: " + engine.getMemoryUsage(0));
        //System.out.println("memoryUsage[1] Depois: " + engine.getMemoryUsage(1));

        return result;

    }

    @Override
    public Tuple in(Tuple template, TSRequestContext ctx) {
        return inp(template, ctx);
    }

    @Override
    public Tuple rdp(Tuple template, TSRequestContext ctx) {

        checkViewUpdated();
        subspaceId = getSubSpaceId(id);

        InRdCtx reqCtx = getCtx(ctx.getId());

        if (subspaceId == 0) {
            reqCtx.liberaExecucao();
        }

        reqCtx.esperaParaExecutar();

        if (!reqCtx.isIsResultSetted()) {

            //EXECUTA LOCALMENTE A OPERACAO
            Tuple lida = this.tupleSpace.rdp(template, ctx);
            if (lida != null) {//possui a tupla

                sendMsg(lida, ctx.getId(), 88);
                //return lida;
                reqCtx.setResult(lida);
            } else if (this.getSpaces().getSubSpacesNum() <= 1 || this.getSubSpaceId(this.id) == (this.spaces.getSubSpacesNum() - 1)) {
                reqCtx.setResult(null);
                sendMsg(null, ctx.getId(), 88);
            } else {//nao possui a tupla

                sendMsg(template, ctx.getId(), 98);
            }
        }
        reqCtx.esperaPeloResultado();

        return reqCtx.getResult();

    }

    @Override
    public Tuple rd(Tuple template, TSRequestContext ctx) {
        return rdp(template, ctx);
    }

    public SubSpaces getSpaces() {
        return spaces;
    }
}
