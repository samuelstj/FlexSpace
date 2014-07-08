/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.test;

/**
 *
 * @author samuelstj
 */
public class ClientTestGUI {
    
    public static void main(String[] args){
        if (args.length < 1) {
            System.out.println("Usage: java ...ClientTest <process id>");
            System.exit(-1);
        }
         
        new GUI(args[0]).setVisible(true);
         
    }
    
    
    
}
