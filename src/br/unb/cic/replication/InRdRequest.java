/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.replication;

import br.unb.cic.tuplespace.Tuple;
import java.io.Serializable;

/**
 *
 * @author samuelstj
 */
public class InRdRequest implements CommMessage,Serializable {
    
    private Tuple tupla;
    private int codigo;
    private int id;
    private int processId;
    
    public InRdRequest(int pId, Tuple tupla, int id, int codigo) {
        this.processId = pId;
        this.tupla = tupla;
        this.id = id;
        this.codigo = codigo;
    }

    public int getId() {
        return id;
    }

    public int getProcessId() {
        return processId;
    }

    @Override
    public int getCodigo() {
        return codigo;
    }
    
    public Tuple getTupla() {
        return tupla;
    }
    
}
