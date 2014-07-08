/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.replication.server;

import br.unb.cic.mapping.TupleMapping;
import br.unb.cic.replication.*;
import br.unb.cic.tuplespace.TSRequestContext;
import br.unb.cic.tuplespace.Tuple;
import br.unb.cic.tuplespace.TupleSpace;
import br.unb.cic.tuplespace.TupleSpaceImpl;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import navigators.smart.tom.MessageContext;
import navigators.smart.tom.ServiceReplica;
import navigators.smart.tom.server.Recoverable;
import navigators.smart.tom.server.SingleExecutable;

/**
 *
 * @author eduardo
 */
public class TupleSpaceServer implements SingleExecutable, Recoverable {

    private int id;
    private TupleSpace tupleSpace;
    private Funcoes fc = new Funcoes();
    //private StringBuilder MemoryInfo;

    private int memoriaUsada = 0;

    public TupleSpaceServer(int id) {
        this.id = id;
        ServiceReplica sr = new ServiceReplica(id, this, this);
        tupleSpace = new TupleSpaceImpl();
        //MemoryInfo = fc.getMemoryInfo();
    }

    public TupleSpaceServer(int id, int engineType) {
        this.id = id;
        ServiceReplica sr = new ServiceReplica(id, this, this);
        tupleSpace = new TupleMapping(id, sr.getReplicaContext(), engineType);
        //MemoryInfo = fc.getMemoryInfo();
    }

    @Override
    public byte[] executeUnordered(byte[] command, MessageContext msgCtx) {

        CommMessage req = bytesToCommRequest(command);

        if (req.getCodigo() == 88 || req.getCodigo() == 98) {
            ((TupleMapping) this.tupleSpace).receiveMsg((InRdRequest) req);
        } else if (req.getCodigo() == 77) {
            ((TupleMapping) this.tupleSpace).receiveSubspaces((UpdateSubSpaceRequest) req);
        } else if (req.getCodigo() == 66) {
            ((TupleMapping) this.tupleSpace).receiveSubspaces2((RedistribuiTuplasRequest) req);
        } //else if
        return null;
    }

    @Override
    public byte[] executeOrdered(byte[] command, MessageContext msgCtx) {

        //System.out.println("Ts =" + msgCtx.getConsensusId());
        //System.out.println("sender =" + msgCtx.getSender());

        Request req = bytesToRequest(command);

        Response resp = null;
        TSRequestContext ctx = new TSRequestContext(command.length, msgCtx.getConsensusId(), msgCtx.getSender());
        //TSRequestContext ctx = null;

        if (req.getOperation() == TupleSpaceImpl.OUT) {
            tupleSpace.out(req.getTupla(), ctx);

            //Contabilizando a utilização de memória - Início
            Tuple tuple = req.getTupla();
            int tSize = tuple.getTupleSize();
            memoriaUsada = memoriaUsada + tSize;
            //Contabilizando a utilização de memória - Fim

            resp = new Response(null, true);
        } else if (req.getOperation() == TupleSpaceImpl.RD) {
            Tuple ret = tupleSpace.rd(req.getTupla(), ctx);
            resp = new Response(ret, true);
        } else if (req.getOperation() == TupleSpaceImpl.RDP) {
            Tuple ret = tupleSpace.rdp(req.getTupla(), ctx);
            resp = new Response(ret, true);
        } else if (req.getOperation() == TupleSpaceImpl.IN) {
            Tuple ret = tupleSpace.in(req.getTupla(), ctx);
            resp = new Response(ret, true);
        } else if (req.getOperation() == TupleSpaceImpl.INP) {
            Tuple ret = tupleSpace.inp(req.getTupla(), ctx);
            resp = new Response(ret, true);
        } else if (req.getOperation() == TupleSpaceImpl.PRINT1) {
            /*PrintWriter writer;
            MemoryInfo.append(fc.getMemoryInfo());

            try {
                writer = new PrintWriter("MemoryInfoOUT"+id+".txt", "UTF-8");                
                writer.println(MemoryInfo);
                writer.close();
            } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                Logger.getLogger(TupleSpaceServer.class.getName()).log(Level.SEVERE, null, ex);
            }*/

            System.out.println("Utilização de memória S: " + memoriaUsada);
            resp = new Response(null, true);
        } else if (req.getOperation() == TupleSpaceImpl.PRINT2) {
            /*PrintWriter writer;
            MemoryInfo.append(fc.getMemoryInfo());
            
            try {
                writer = new PrintWriter("MemoryInfoINP-RDP"+id+".txt", "UTF-8");
                writer.println(MemoryInfo);
                writer.close();
            } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                Logger.getLogger(TupleSpaceServer.class.getName()).log(Level.SEVERE, null, ex);
            }*/
            
            resp = new Response(null, true);
        }

        // Mostra as tuplas armazenadas
        //System.out.println(tupleSpace.getTuples());
        //System.out.println(".");

        return responseToBytes(resp);
    }

    @Override
    public byte[] getState() {
        //recurperar o estado de acordo com o segmento (fazer isso na camada TupleSpaceImpl
        /*try{
         ByteArrayOutputStream out = new ByteArrayOutputStream(4);
         new ObjectOutputStream(out).writeObject(new Integer(contador));
         return out.toByteArray();
         }catch(Exception e){
         e.printStackTrace();
         return null;
         }*/
        return new byte[0];

    }

    @Override
    public void setState(byte[] state) {
        /* try{
         contador = ((Integer) new ObjectInputStream(new ByteArrayInputStream(state)).readObject()).intValue();
         }catch(Exception e){
         e.printStackTrace();
         }*/
    }

    private byte[] responseToBytes(Response r) {
        try {

            ByteArrayOutputStream out = new ByteArrayOutputStream(4);
            new ObjectOutputStream(out).writeObject(r);
            return out.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Request bytesToRequest(byte[] b) {
        try {
            return (Request) new ObjectInputStream(new ByteArrayInputStream(b)).readObject();
        } catch (Exception e) {
            e.printStackTrace();;
            return null;
        }
    }

    private CommMessage bytesToCommRequest(byte[] b) {
        try {
            return (CommMessage) new ObjectInputStream(new ByteArrayInputStream(b)).readObject();
        } catch (Exception e) {
            e.printStackTrace();;
            return null;
        }
    }
}
