/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.hilbert;

import br.unb.cic.tuplespace.Tuple;
import com.google.uzaygezen.core.BitVector;
import com.google.uzaygezen.core.BitVectorFactories;
import com.google.uzaygezen.core.CompactHilbertCurve;
import java.util.BitSet;
import java.util.List;

/**
 *
 * @author eduardo
 */
public class Mapping {
    
    private CompactHilbertCurve chc;
    private int maxSize;
    
    
    public Mapping(int D, int tam) {
        int[] v = new int[D];
        for(int i = 0; i < v.length; i++){
            v[i] = tam;
        }
         chc = new CompactHilbertCurve(v);
         this.maxSize = tam;
    }

    public int getKey(Tuple t){
        BitVector chi = BitVectorFactories.OPTIMAL.apply(chc.getSpec().sumBitsPerDimension());
        
        BitVector[] p = getBitVector(t);
        
        chc.index(p, 0, chi);
        return chi.cardinality();
    }
    
    
    private BitVector[] getBitVector(Tuple t){
        
        List<Integer> bitsPerDimension = chc.getSpec().getBitsPerDimension();
        BitVector[] p = new BitVector[bitsPerDimension.size()];
        for (int i = p.length; --i >= 0;) {
            p[i] = BitVectorFactories.OPTIMAL.apply(bitsPerDimension.get(i));
        }
        
        Object[] fields = t.getFields();
        int i;
        for(i=0; i<fields.length && i < p.length;i++){
            BitSet b = BitSet.valueOf(fields[i].toString().getBytes());
            
             if(b.length() > maxSize){
                
                byte[] nB = new byte[maxSize/8];
                byte[] dataB = fields[i].toString().getBytes();
                
                for(int j = 0; j < nB.length; j++){
                    nB[j] = dataB[j];
                }
                 //System.out.println("Tamanho muito grande! "+b.length());
                b = BitSet.valueOf(nB);
                
                //System.out.println("Tamanho muito grande! "+b.length());
            }
            p[i].copyFrom(b);
           
        }
        
        while(i < p.length){
            BitSet b = BitSet.valueOf("*".getBytes());
            p[i].copyFrom(b);
            //System.out.println("i do resto = "+i);
            i++;
        }

        return p;
    }
}
