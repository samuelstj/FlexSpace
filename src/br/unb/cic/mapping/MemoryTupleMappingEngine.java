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
public class MemoryTupleMappingEngine extends AbstractEngine{

    private int[] memoryUsage;
    
    public MemoryTupleMappingEngine(int id, SubSpaces spaces) {
        super(id,spaces);
        memoryUsage = new int[spaces.getSpaces().size()];
    }

    @Override
    protected SubSpaceView getSubSpaceView(Tuple t, TSRequestContext ctx) {
        
        int nSpaces = spaces.getSpaces().size();
        //int tSize = ctx.getTupleSize();
        int tSize = t.getTupleSize();
        
        //System.out.println("tSize: "+tSize);
        //System.out.println("nSpaces: "+nSpaces);
        int menor = 0;
        
        //System.out.println("---------------------------------------");
        //System.out.println("memoryUsage[0] Inicial: "+memoryUsage[0]);
        //System.out.println("memoryUsage[1] Inicial: "+memoryUsage[1]);
        
        for(int i = 0; i < nSpaces; i++){
            if(memoryUsage[menor] > memoryUsage[i]){
                menor = i;
            }
        }
        
        memoryUsage[menor] += tSize;
        //System.out.println("menor: "+menor);
        //System.out.println("memoryUsage[0]: "+memoryUsage[0]);
        //System.out.println("memoryUsage[1]: "+memoryUsage[1]);
        //System.out.println("memoryUsage[2]: "+memoryUsage[2]);
        
        return spaces.getSpaces().get(menor);
        
    }

    @Override
    public void UpdateMemoryUsage(int subspaceID, Tuple t) {
        if(subspaceID >= 0){
            int tSize = t.getTupleSize();
            memoryUsage[subspaceID] -= tSize;
        }
    }
    
    @Override
    public int getMemoryUsage(int indice){
        return memoryUsage[indice];
    }
        
}
