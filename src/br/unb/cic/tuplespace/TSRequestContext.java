/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.tuplespace;

/**
 *
 * @author samuelstj
 */
public class TSRequestContext {
    
    
    private int tupleSize;

    private int consensusId;
    
    private int sender;
    
    public TSRequestContext(int tupleSize, int cId, int sender) {
        this.tupleSize = tupleSize;
        this.sender = sender;
        this.consensusId = cId;
    }

    public int getSender() {
        return sender;
    }

    public int getId(){
        return sender+consensusId;
    }
    
    public int getTupleSize() {
        return tupleSize;
    }

    public int getConsensusId() {
        return consensusId;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TSRequestContext){
            TSRequestContext outro = (TSRequestContext) obj;
            return outro.getId() == this.getId();
        }
        return false;
    }
    
    
    
    
    
}
