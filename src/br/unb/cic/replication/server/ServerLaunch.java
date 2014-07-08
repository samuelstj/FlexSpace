/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.replication.server;

/**
 *
 * @author eduardo
 */
public class ServerLaunch {
    
    public static void main(String[] args){

        if (args.length < 1){
            System.out.println("Usage:");
            System.out.println("java ...ServerLaunch <process ID>");
            System.out.println("java ...ServerLaunch <processID> <engineType>");
            System.out.println("Engine types: Simple: 0 | Hilbert: 1 | Memory: 2 | TupleSize: 3 | TupleFields");
            System.exit(-1);
        }
        
        int id = Integer.parseInt(args[0]);
        
        if (args.length == 2){
            int engineType = Integer.parseInt(args[1]);
            new TupleSpaceServer(id,engineType);
        }else{
            new TupleSpaceServer(id);
        }
                
    }
    
    
}
