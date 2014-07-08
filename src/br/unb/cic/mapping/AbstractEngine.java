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
public abstract class AbstractEngine implements TupleMappingEngine {

    protected int id;
    protected SubSpaces spaces;

    public AbstractEngine(int id, SubSpaces spaces) {
        this.id = id;
        this.spaces = spaces;
    }

    protected abstract SubSpaceView getSubSpaceView(Tuple t, TSRequestContext ctx);

    @Override
    public boolean isToStore(Tuple t, TSRequestContext ctx) {

        if (spaces.getStandby().isMember(this.id)) {
            return false;
        } else {
            SubSpaceView se = getSubSpaceView(t, ctx);

            if (se.isMember(this.id)) {
                //System.out.println("Retorna true");
                return true;
            } else {
                //System.out.println("Retorna false");
                return false;
            }
        }

    }
}
