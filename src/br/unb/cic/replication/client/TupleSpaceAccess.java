/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.replication.client;

import br.unb.cic.tuplespace.Tuple;
import java.util.Properties;

/**
 *
 * @author eduardo
 */
public class TupleSpaceAccess {

    public static String ID_KEY = "id_key";
    
    
    private TupleSpaceClient client;
    
    public TupleSpaceAccess(Properties props) {
        int id = Integer.parseInt(props.getProperty(ID_KEY));
        client = new TupleSpaceClient(id);
    }
  
     public void out(Object... fields) {
         out(Tuple.createTuple(fields));
     }
     
     public Tuple rd(Object... fields) {
         return rd(Tuple.createTuple(fields));
     }
    
     public Tuple rdp(Object... fields) {
         return rdp(Tuple.createTuple(fields));
     }
     
     public Tuple in(Object... fields) {
         return in(Tuple.createTuple(fields));
     }
     
     public Tuple inp(Object... fields) {
        return inp(Tuple.createTuple(fields));
     }
     
    public void printt(int i) {
        client.printt(i);
     }
    
     /**Execute a OUT operation*/
    public void out(Tuple tuple) {
       
       client.out(tuple); 
   }
    
    /**Execute a RD operation*/
    public Tuple rd(Tuple template) {
       return client.rd(template);
        
    }
    
    /**Execute a IN operation*/
    public Tuple in(Tuple template)  {
        return client.in(template);
       
    }
    
    /**Execute a RDP operation*/
    public Tuple rdp(Tuple template){
        
        return client.rdp(template);
        
    }
    
    /**Execute a INP operation*/
    public Tuple inp(Tuple template){
        return client.inp(template);
    }
    
    
    
}
