/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.unb.cic.replication.server;

import java.lang.management.ManagementFactory;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author samuelstj
 */
public class Funcoes {
    
    public Funcoes(){
        
    }
      
    public StringBuilder getMemoryInfo(){
        long totalMemory = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize();
        long freeMemory  = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getFreePhysicalMemorySize();
        long allocatedMemory = totalMemory - freeMemory;
        
        StringBuilder st = new StringBuilder();    
	st.append("\nTotal Memory: ").append(totalMemory/1024).append("\n");
        st.append("Free Memory: ").append(freeMemory/1024).append("\n");
        st.append("Allocated Memory: ").append(allocatedMemory/1024).append("\n");
        return st;
    }
    
    public Float mediaLatencia(LinkedList<Float> latencias){
        int j = (5*latencias.size())/100;
        
        for (int i = 0; i < j; i++){
            latencias.removeFirst();
            latencias.removeLast();
        }
            
        Iterator<Float>pos = latencias.iterator();  
        float soma = 0;  
        float qtd = 0;
        while(pos.hasNext()){  
            soma += pos.next();  
            qtd++;
        }
        return soma/qtd;
    }
    
}
