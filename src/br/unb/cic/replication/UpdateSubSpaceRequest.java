/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.replication;

import java.io.Serializable;
import br.unb.cic.mapping.SubSpaces;
import br.unb.cic.tuplespace.TupleSpaceImpl;

/**
 *
 * @author samuelstj
 */
public class UpdateSubSpaceRequest implements CommMessage, Serializable {

    private int codigo;
    private SubSpaces spaces;
    private int procUsed;
    private TupleSpaceImpl tupleSpace;

    public UpdateSubSpaceRequest(SubSpaces spaces, TupleSpaceImpl tupleSpace, int procUsed) {

        this.codigo = 77;
        this.spaces = spaces;
        this.tupleSpace = tupleSpace;
        this.procUsed = procUsed;

    }

    @Override
    public int getCodigo() {
        return codigo;
    }

    public SubSpaces getSpaces() {
        return spaces;
    }

    public TupleSpaceImpl getTupleSpace() {
        return tupleSpace;
    }

    public int getProcUsed() {
        return procUsed;
    }
}
