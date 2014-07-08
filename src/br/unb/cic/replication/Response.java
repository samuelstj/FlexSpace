/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.replication;

import br.unb.cic.tuplespace.Tuple;
import java.io.Serializable;

/**
 *
 * @author eduardo
 */
public class Response implements Serializable{
    
    private Tuple tuple;
    private boolean success;

    public Response() {
    }

    public Response(Tuple tuple, boolean success) {
        this.tuple = tuple;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public Tuple getTuple() {
        return tuple;
    }
    
    
    
}
