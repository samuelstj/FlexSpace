/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.mapping;

import br.unb.cic.replication.InRdRequest;
import br.unb.cic.tuplespace.Tuple;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 *
 * @author samuelstj
 */
public class InRdCtx {

    private Semaphore sm;
    private Semaphore smResult;
    private int id;
    private Tuple result;
    private List<InRdRequest> msgs = new LinkedList<InRdRequest>();
    private boolean isResultSetted = false;
    private int subspaceId;
    private int subspaceIdU;
    private TupleMapping tm;

    
    
    public InRdCtx(int id, int subspaceId, TupleMapping tm) {
        this.id = id;
        this.subspaceId = subspaceId;
        this.sm = new Semaphore(0);
        this.smResult = new Semaphore(0);
        this.tm = tm;
    }

    public Tuple getResult() {
        return result;
    }
    
    public int getSubspaceU() {
        return subspaceIdU;
    }

    public void setSubspaceIdU(int subspaceIdU) {
        this.subspaceIdU = subspaceIdU;
    }
    
    
    
    public synchronized void receiveMsg(InRdRequest r) {
        if(!isIsResultSetted() && 
                this.tm.getSubSpaceId(r.getProcessId()) != subspaceId ){
        
            msgs.add(r);

            //verificar se ja possui uma valor para retornar
            if (checkResult(r)) {
                this.setResult(r.getTupla());
                this.liberaExecucao();
            } else if (checkExecute(r)) {
                //verificar se deve executar a operacao
                //System.out.println("Vai liberar a execução!!!");
                this.liberaExecucao();
            }
        }

    }

    private boolean checkExecute(InRdRequest rec) {
        int f = this.tm.getCurrentView().getF();
        int q = f + 1;

        int c = 0;
        for (int j = 0; j < this.msgs.size(); j++) {
            InRdRequest r = this.msgs.get(j);
            
            if (r.getCodigo() == MessageType.DONTHAVETUPLE && this.tm.getSubSpaceId(r.getProcessId()) == (subspaceId - 1)) {
                c++;
            }

        }
        //System.out.println("valor de c: "+c);
        //System.out.println("sub Id: "+subspaceId);
        
        if (c >= q) {
            return true;
        }
        return false;

    }

    private boolean checkResult(InRdRequest rec) {
        int f = this.tm.getCurrentView().getF();
        int q = f + 1;
        int subNum = this.tm.getSpaces().getSubSpacesNum();

        for (int i = 0; i < subNum; i++) {
            int count = 0;
            List<InRdRequest> tmp = new LinkedList<InRdRequest>();
            //System.out.println("this.msgs.size(): "+this.msgs.size());
            for (int j = 0; j < this.msgs.size(); j++) {
                //System.out.println("J: "+j+" | msgs.size: "+this.msgs.size()+" msg: "+this.msgs);
                //System.out.println("retorno: "+this.msgs.get(j));
                InRdRequest r = this.msgs.get(j);
                if (r.getCodigo() == MessageType.HAVETUPLE && this.tm.getSubSpaceId(r.getProcessId()) == i) {
                    tmp.add(r);
                }

            }
            if (countTuples(tmp, rec) >= q) {
                this.subspaceIdU = this.tm.getSubSpaceId(rec.getProcessId());
                return true;
            }

        }
        return false;

    }

    private int countTuples(List<InRdRequest> l, InRdRequest rec) {
        int c = 0;
        for (int j = 0; j < l.size(); j++) {
            InRdRequest r = l.get(j);
            if (((r.getTupla() == null) && (rec.getTupla() == null)) || 
                    (r.getTupla() != null && r.getTupla().equals(rec.getTupla()))) {
                c++;
            }

        }
        return c;
    }

    public void setResult(Tuple t) {
        this.result = t;
        isResultSetted = true;
        this.smResult.release();
    }

    public boolean isIsResultSetted() {
        return isResultSetted;
    }

    public void esperaParaExecutar() {
        try {
            this.sm.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void liberaExecucao() {
        this.sm.release();
    }

    public void esperaPeloResultado() {
        try {
            this.smResult.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //@Override
    /*public boolean equals(Object obj) {
     if(obj instanceof TSRequestContext){
     TSRequestContext outro = (TSRequestContext) obj;
     return outro.getId() == this.getId();
     }
     return false;
     }*/

    public int getId() {
        return id;
    }
}
