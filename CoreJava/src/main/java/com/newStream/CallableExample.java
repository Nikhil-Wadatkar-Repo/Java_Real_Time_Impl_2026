package com.newStream;

import java.util.concurrent.*;

public class CallableExample {
    // public static void main(String[] args) throws Exception {
    // ExecutorService executor = Executors.newSingleThreadExecutor();

    // Callable<Integer> task = () -> {
    // int sum = 0;
    // for (int i = 1; i <= 5; i++)
    // sum += i;
    // return sum; // returns result
    // };

    // Future<Integer> future = executor.submit(task);

    // System.out.println("Result: " + future.get());
    // executor.shutdown();
    // }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<Integer> future = executor.submit(() -> 10 + 20);

        try {
            Integer result = future.get(); // waits but returns instantly
            System.out.println("Result: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}