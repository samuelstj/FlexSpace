/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.tuplespace;

import java.util.List;

/**
 *
 * @author samuelstj
 */
public interface TupleSpace {
    
    public void out(Tuple tuple, TSRequestContext ctx);
    
    public Tuple rd(Tuple template, TSRequestContext ctx);
    
    public Tuple in(Tuple template, TSRequestContext ctx);
   
    public Tuple rdp(Tuple template, TSRequestContext ctx);
    
    public Tuple inp(Tuple template, TSRequestContext ctx);
    
    public List<Tuple> getTuples();
    
}
