/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.replication.client;

import br.unb.cic.replication.Request;
import br.unb.cic.replication.Response;
import br.unb.cic.tuplespace.Tuple;
import br.unb.cic.tuplespace.TupleSpaceImpl;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Comparator;
import navigators.smart.tom.ServiceProxy;

/**
 *
 * @author eduardo
 */
public class TupleSpaceClient {

    private ServiceProxy proxy;

    public TupleSpaceClient(int id) {
        criarProxy(id);
    }

    public void criarProxy(int id) {
        Comparator comp = new Comparator<byte[]>() {
            @Override
            public int compare(byte[] o1, byte[] o2) {
                try {
                    Object obj1 = new ObjectInputStream(new ByteArrayInputStream(o1)).readObject();
                    Object obj2 = new ObjectInputStream(new ByteArrayInputStream(o2)).readObject();
                    if (obj1 instanceof Response && obj2 instanceof Response) {

                        Response r1 = (Response)obj1;
                        Response r2 = (Response) obj2;

                        if ((r1.isSuccess() == r2.isSuccess())
                                && ((r1.getTuple() == null && r2.getTuple() == null)
                                || ((r1.getTuple() != null && r2.getTuple() != null)
                                && r1.getTuple().equals(r2.getTuple())))) {
                            return 0;
                        } else {
                            return -1;
                        }
                    }else{
                        if(obj1.equals(obj2)){
                            return 0;
                        }else{
                            return -1;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return -1;
                }
            }
        };
        proxy = new ServiceProxy(id, null, comp, null);
    }

    /**
     * Execute a OUT operation
     */
    public void out(Tuple tuple) {
        executeOp(TupleSpaceImpl.OUT, tuple);

        //  Tuple t = Tuple.createTuple("campo1", "campo2");

        // Tuple template = Tuple.createTuple("campo1", TupleSpaceImpl.WILDCARD);

    }

    /**
     * Execute a RD operation
     */
    public Tuple rd(Tuple template) {

        //return executeOp(TupleSpaceImpl.RD, template);
        Tuple ret = executeOp(TupleSpaceImpl.RD, template);
        while (ret == null) {

            try {
                //Thread.currentThread().wait(1000);
                Thread.currentThread().sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("Reenviando requisição...");
            ret = executeOp(TupleSpaceImpl.RD, template);
        }

        return ret;
    }

    /**
     * Execute a IN operation
     */
    public Tuple in(Tuple template) {

        Tuple ret = executeOp(TupleSpaceImpl.IN, template);
        while (ret == null) {

            try {
                //Thread.currentThread().wait(1000);
                Thread.currentThread().sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("Reenviando requisição...");
            ret = executeOp(TupleSpaceImpl.IN, template);
        }

        return ret;
    }

    /**
     * Execute a RDP operation
     */
    public Tuple rdp(Tuple template) {
        return executeOp(TupleSpaceImpl.RDP, template);
    }

    /**
     * Execute a INP operation
     */
    public Tuple inp(Tuple template) {
        return executeOp(TupleSpaceImpl.INP, template);
    }
    
    /**
     * Execute a PRINT operation
     */
    public void printt(int cod) {
        if(cod == 0){
            executeOp(TupleSpaceImpl.PRINT1, null);
        } else if (cod == 1){
            executeOp(TupleSpaceImpl.PRINT2, null);
        }
    }
    

    private Tuple executeOp(int op, Tuple t) {


        //criar uma requisicao com tuple
        Request r = new Request(t, op);
        //enviar a requisicao usando o proxy
        byte[] resp = proxy.invokeOrdered(requestToBytes(r));
        //pegar a resposta (byte[])
        //transformar os byte[] para respota
        Response response = bytesToResponse(resp);
        //retornar a resposta
        if (response.isSuccess()) {
            return response.getTuple();
        } else {
            System.out.println("Operacao não executada corretamente!");
            return null;
        }

    }

    private byte[] requestToBytes(Request req) {

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(4);
            new ObjectOutputStream(out).writeObject(req);

            return out.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }

    private Response bytesToResponse(byte[] b) {
        try {
            return (Response) new ObjectInputStream(new ByteArrayInputStream(b)).readObject();
        } catch (Exception e) {
            e.printStackTrace();;
            return null;
        }
    }
}
