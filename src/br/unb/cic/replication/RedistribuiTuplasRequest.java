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
public class RedistribuiTuplasRequest implements CommMessage, Serializable {

    private int codigo;
    private SubSpaces spaces;
    private TupleSpaceImpl tupleSpace;

    public RedistribuiTuplasRequest(SubSpaces spaces, TupleSpaceImpl tupleSpace) {

        this.codigo = 66;
        this.spaces = spaces;
        this.tupleSpace = tupleSpace;
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
}
