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
public class SimpleTupleMappingEngine implements TupleMappingEngine{

    public SimpleTupleMappingEngine() {
    }

    @Override
    public boolean isToStore(Tuple t, TSRequestContext ctx) {
        return true;
    }

    @Override
    public void UpdateMemoryUsage(int subspaceID, Tuple t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getMemoryUsage(int indice) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
