package org.tmjug.java8;

import org.tmjug.java8.service.Fibonacci;

/**
 * Computing the Fibonacci numbers in imperative and functional programming
 */
public class FibonacciDemo {

    public static void main(String[] args) {
        // every number after the first two is the sum of the two preceding ones

        Fibonacci.imperative(20);

        System.out.println();

        Fibonacci.functional(20);
    }
}
