/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.test;

import br.unb.cic.replication.client.TupleSpaceAccess;
import br.unb.cic.tuplespace.Tuple;
import java.util.Properties;
import java.util.Scanner;

/**
 *
 * @author eduardo
 */
public class ClientTest {
    
    public static void main(String[] args){
         if (args.length < 1) {
            System.out.println("Usage: java ...ClientTest <process id>");
            System.exit(-1);
        }
         
       Properties prop = new Properties();
       prop.setProperty(TupleSpaceAccess.ID_KEY, args[0]);
       TupleSpaceAccess ts = new TupleSpaceAccess(prop); 
       
       ///////////////////////////////////////
       Scanner sc = new Scanner(System.in);
       
       while(true) {
           System.out.println("Select an option:");
           System.out.println("O. EXECUTE AN OUT OPERATION");
           System.out.println("1. EXECUTE A RD OPERATION");
           System.out.println("2. EXECUTE AN IN OPERATION");
           System.out.println("3. EXECUTE A RDP OPERATION");
           System.out.println("4. EXECUTE AN INP OPERATION");
                        
           int op = sc.nextInt();
                        
           switch(op) {
           case 0:
               System.out.println("Testando o OUT!");
               ts.out("1","2","3");
               ts.out("2","2","3");
               ts.out("1","2","3");

               break;
           case 1:
               Tuple ret = ts.rd("1","*","*");
               System.out.println("A tupla lida foi: "+ret);
               
               break;
           case 2:
               Tuple ret1 = ts.in("1","*","*");
               System.out.println("A tupla removida foi: "+ret1);

               
               break;
           case 3:
               Tuple ret2 = ts.rdp("1","*","*");
               System.out.println("A tupla lida foi: "+ret2);
               
               break;
           case 4:            
               Tuple ret3 = ts.inp("1","*","*");
               System.out.println("A tupla removida foi: "+ret3);
               
               break;
           }
       }
         
         
    }
    
    
    
}
