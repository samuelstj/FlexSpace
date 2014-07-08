/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.tuplespace;

import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
 *
 * @author eduardo
 */
public class Tuple implements Externalizable {
    
    private Object[] fields;
    private int tSize;
    
     public Tuple() {

        this.fields = null;
        tSize = getTupleSize1();

    }
    
    /** private constructor, called by 'create' methods */
    private Tuple(Object[] fields) {

        this.fields = fields;
         tSize = getTupleSize1();
    }
    
   //create methods for several kinds of tuples
    public static final Tuple createTuple(Object... fields) {
        return new Tuple(fields);
    }
    
    public boolean isTemplate(){
        int tam = fields.length;
        for(int i = 0; i < tam; i++){
                   if(fields[i].equals(TupleSpaceImpl.WILDCARD)){ //trocar !!!
                       return true;
                   }
                 
        }
        return false;
    
    }
    
    
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException{
          
       
        int tam = in.readInt();
        if(tam < 0){
            fields = null;
        }else{
            fields = new Object[tam];
            for(int i = 0; i < tam; i++){
                fields[i] = in.readObject();
            }
            tSize = in.readInt();
        }
      }
    
    

    
    public void writeExternal(ObjectOutput out) throws IOException{
        
        
        if(fields != null){
            //out.writeInt(1);
            int tam = fields.length;
            out.writeInt(tam);
            for(int i = 0; i < tam; i++){
                out.writeObject(fields[i]);
            }
            out.writeInt(tSize);
        }else{
            out.writeInt(-1);
        }
    }
    
    
    //acessor methods
    
    public final Object[] getFields() {
        return fields;
    }
    
    
    public boolean equals(Object obj) {
        if(!(obj instanceof Tuple)) {
            return false;
        }
        
        Tuple tuple = (Tuple)obj;
        
        
        return equalFields(tuple);
    }
    public int getTupleSize(){
        return tSize;
    }
    
    private int getTupleSize1(){
        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream(4);
            new ObjectOutputStream(out).writeObject(this.getFields());
            return out.toByteArray().length;
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }
    
    public boolean equalFields(Tuple tuple) {
        Object[] tfields = tuple.getFields();
        
        if(tfields == this.fields){
            return true;
        }
        
        if(tfields == null || this.fields == null){
            return false;
        }
        
        
        int m = this.fields.length;
        
        if(tfields.length != m) {
            return false;
        }
        
        for(int i=0; i<m; i++) {
            if(!tfields[i].equals(this.fields[i])) {
                return false;
            }
        }
        
        return true;
    }
    
    public String toString() {
        StringBuffer buff = new StringBuffer(1024);
        buff.append("[<");
        if(fields != null && fields.length > 0){
            buff.append(fields[0]);
            for(int i=1; i<fields.length; i++) {
                buff.append(",").append(fields[i]);
            }
        }
        buff.append(">]");
        
        return buff.toString();
    }
    
    
}
