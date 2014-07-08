/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.tuplespace;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;


/**
 *
 * @author eduardo
 */
public class TupleSpaceImpl implements TupleSpace,Serializable{
    
    protected List<Tuple> tuplesBag = new LinkedList<Tuple>();
    public static final String WILDCARD = "*";
        
    public static final int OUT = 0;
    public static final int RD = 1;
    public static final int IN = 2;
    public static final int RDP = 3;
    public static final int INP = 4;
    public static final int PRINT1 = 8;
    public static final int PRINT2 = 9;
    
    
    public TupleSpaceImpl(){
        
    }
    
    @Override
    public List<Tuple> getTuples(){
       //Alterar para retornar as tuplas de um determinado subspaço
       return this.tuplesBag;
    }
    
    @Override
    public void out(Tuple tuple, TSRequestContext ctx){
        tuplesBag.add(tuple);      
    }
    
    @Override
    public Tuple rd(Tuple template, TSRequestContext ctx){
        //throw new UnsupportedOperationException("Not implemented at this level");
        return findMatching(template,false);
    }
    
    @Override
    public Tuple in(Tuple template, TSRequestContext ctx){
        //throw new UnsupportedOperationException("Not implemented at this level");
        return findMatching(template,true);
    }
   
    @Override
    public Tuple rdp(Tuple template, TSRequestContext ctx) {
        return findMatching(template,false);
    }
    
    @Override
    public Tuple inp(Tuple template, TSRequestContext ctx) {
        return findMatching(template,true);
    }
    
    private Tuple findMatching(Tuple template, boolean remove) {
        
        for(ListIterator<Tuple> i = tuplesBag.listIterator(); i.hasNext(); ) {
            Tuple tuple = i.next();
            
            
            if(match(tuple,template)) {
                if(remove) {
                    i.remove();
                    
                }
                return tuple;
            }
        }
        
        return null;
    }

    
    private boolean match(Tuple tuple, Tuple template) {
        Object[] tupleFields = tuple.getFields();
        Object[] templateFields = template.getFields();
        
        int n = tupleFields.length;
        
        if(n != templateFields.length) {
            return false;
        }
        
        for(int i = 0; i<n; i++) {
            if(!templateFields[i].equals(WILDCARD) && !templateFields[i].equals(tupleFields[i])) {
                return false;
            }
        }
        return true;
    }
   
}
