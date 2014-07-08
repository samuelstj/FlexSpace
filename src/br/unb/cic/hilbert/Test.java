/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.hilbert;

import br.unb.cic.tuplespace.Tuple;

/**
 *
 * @author eduardo
 */
public class Test {

    public static void main(String[] args) {

        Tuple t1 = Tuple.createTuple("Eu", "Tu", "Ele");

        Tuple template = Tuple.createTuple("Eu", "*", "*");

        Tuple t2 = Tuple.createTuple("Euuuuuuu", "Tuuuuuuu", "El123456789123456789123455555");

        Tuple t3 = Tuple.createTuple("Euuuuuuu", "Tuuuuuuu");


        Mapping map = new Mapping(3, 200);


        System.out.println("Key t1 : " + map.getKey(t1));

        System.out.println("Key t2 : " + map.getKey(t2));

        System.out.println("Key t3 : " + map.getKey(t3));

        System.out.println("Key t1 : " + map.getKey(t1));

        System.out.println("Key template : " + map.getKey(template));

        System.out.println("FIM!");


    }
}
