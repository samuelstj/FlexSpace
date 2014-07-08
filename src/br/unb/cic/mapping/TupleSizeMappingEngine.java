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
public class TupleSizeMappingEngine extends AbstractEngine {

    protected int maxRange;

    public TupleSizeMappingEngine(int id, SubSpaces spaces, int maxRange) {
        super(id, spaces);
        this.maxRange = maxRange;
    }

    
    protected int getKey(Tuple t) {
        return t.getTupleSize();
    }

    @Override
    protected SubSpaceView getSubSpaceView(Tuple t, TSRequestContext ctx) {
        int key = getKey(t);
        
        int sp = key % maxRange;
        int subRange = maxRange / spaces.getSpaces().size();

        for (int i = 0; i < spaces.getSpaces().size(); i++) {
            if ((sp >= (i * subRange)) && (sp < ((i + 1) * subRange))) {
                return spaces.getSpaces().get(i);
            }
        }
        return null;
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
