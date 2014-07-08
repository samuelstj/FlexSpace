/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.mapping;

import java.io.Serializable;
import org.apache.commons.lang.ArrayUtils;

/**
 *
 * @author samuelstj
 */
public class SubSpaceView implements Serializable{

    private int id;
    private int f;
    private int[] processes;

    public SubSpaceView() {
    }

    public SubSpaceView(int id, int[] processes, int f) {
        this.id = id;
        this.processes = processes;
        this.f = f;
    }

    public void clear() {
        this.processes = null;
    }

    public boolean isMember(int id) {
        if (this.processes != null) {
            for (int i = 0; i < this.processes.length; i++) {
                if (this.processes[i] == id) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getId() {
        return id;
    }

    public int getF() {
        return f;
    }

    public int getN() {
        return this.processes.length;
    }

    public int[] getProcesses() {
        return processes;
    }

    public void addProcess(int pId) {
        //this.processes  = Arrays.copyOf(this.processes, this.processes.length + 1);
        //this.processes[this.processes.length - 1] = pId;
        processes = ArrayUtils.add(processes, pId);
    }

    public void rmProcess(int pId) {
        processes = ArrayUtils.removeElement(processes, pId);
    }

    public void swapProcess(int pOld, int pNew) {
        processes = ArrayUtils.removeElement(processes, pOld);
        processes = ArrayUtils.add(processes, pNew);
        /*for(int i = 0; i < this.processes.length;i++){
         if(this.processes[i] == pOld){
         this.processes[i] = pNew;
         }
         }*/
    }

    @Override
    public String toString() {
        String ret = "ID:" + id + "; F:" + f + "; Processes:";
        if (processes == null) {
            ret = ret + " empty";
        } else {
            for (int i = 0; i < processes.length; i++) {
                ret = ret + processes[i] + ",";
            }
        }
        return ret;
    }
}
