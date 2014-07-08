/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.mapping;

import br.unb.cic.tuplespace.TSRequestContext;
import br.unb.cic.tuplespace.Tuple;

/**
 *
 * @author samuelstj
 */
public interface TupleMappingEngine {
 
    public boolean isToStore(Tuple t, TSRequestContext ctx);
    
    public void UpdateMemoryUsage(int subspaceID, Tuple t);

    public int getMemoryUsage(int indice);
}
