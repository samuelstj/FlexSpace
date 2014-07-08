/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.mapping;

import br.unb.cic.hilbert.Mapping;
import br.unb.cic.tuplespace.Tuple;

/**
 *
 * @author samuelstj
 */
public class HilbertTupleMappingEngine extends TupleSizeMappingEngine {
 
    private Mapping map = new Mapping(4, 100);
    //private Mapping map = new Mapping(3, 100); //Original
    
    
    public HilbertTupleMappingEngine(int id, SubSpaces spaces, int maxRange) {
        super(id, spaces, maxRange);
    }

    @Override
    protected int getKey(Tuple t) {
        //System.out.println("Key: "+map.getKey(t));
        return map.getKey(t);
    }

}
