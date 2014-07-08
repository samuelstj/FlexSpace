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
public class Request implements Serializable{
    
    private Tuple tupla;
    private int operation;

    public Request() {
    }

    public Request(Tuple tupla, int operation) {
        this.tupla = tupla;
        this.operation = operation;
    }

    public int getOperation() {
        return operation;
    }

    public Tuple getTupla() {
        return tupla;
    }
    
    
    
}
