package com.hazelcast.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Hello world!
 *
 */
public class Sorter
{
    private static final int LIST_SIZE = 200000;
    private static final int LIST_COUNT = 2048;
    private static Random rand = new Random(26);

    private static ArrayList<int[]> arrays = new ArrayList<>(LIST_COUNT);

    public static void main( String[] args )
    {

        generateTestData();

        int threadCount = 1;
        while(threadCount <= 512){
            testWithThreads(threadCount);

            generateTestData();
            threadCount *=2;
        }

        threadCount /= 2;
        while(threadCount >= 1){
            testWithThreads(threadCount);

            generateTestData();
            threadCount /=2;
        }
    }

    private static void generateTestData(){
        System.out.println("Generating test data.");
        arrays.clear();

        for (int i = 0; i < LIST_COUNT; ++i) {
            arrays.add(new int[LIST_SIZE]);
            for(int j=0;j<LIST_SIZE; ++j){
                arrays.get(i)[j] = rand.nextInt();
            }
        }

    }

    private static void testWithThreads(int threadCount){
        ArrayList<SortThread> threads = new ArrayList<SortThread>(threadCount);
        System.out.println(String.format("Starting %d thread test ...", threadCount));

        for(int i=0; i < threadCount; ++i){
            threads.add(new SortThread(i, threadCount));
        }

        long start = System.currentTimeMillis();
        for(SortThread t: threads){
            t.start();
        }

        for (SortThread t : threads) {
            try {
                t.join();
            } catch(InterruptedException x){
                System.err.println("An unexpected exception occurred while waiting for a test thread to stop.");
            }
        }

        long elapsed = System.currentTimeMillis() - start;
        System.out.println(String.format("Time to sort %d lists of %d elements each using %d threads: %dms", LIST_COUNT, LIST_SIZE, threadCount, elapsed));
    }

    private static class SortThread extends Thread {

        public SortThread(int slice, int mod){
            this.slice = slice;
            this.mod = mod;
        }

        private int slice;
        private int mod;

        public void run(){
            int i = slice;
            while(i < LIST_COUNT){
                int[] a = arrays.get(i);
                Arrays.sort(a);
                i += mod;
            }
        }
    }
}
