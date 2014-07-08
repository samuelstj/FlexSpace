/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.mapping;

import br.unb.cic.tuplespace.Tuple;

/**
 *
 * @author samuelstj
 */
public class TupleFieldsMappingEngine extends TupleSizeMappingEngine {

    public TupleFieldsMappingEngine(int id, SubSpaces spaces, int maxRange) {
        super(id, spaces, maxRange);
    }
    
    @Override
     protected int getKey(Tuple t) {
        return t.getFields().length;
    }
    
}
