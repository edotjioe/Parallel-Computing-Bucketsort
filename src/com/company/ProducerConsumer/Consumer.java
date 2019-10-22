package com.company.ProducerConsumer;

import com.company.Sort.InsertionSort;

import java.util.*;
import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {

    protected ArrayList<Pair> buckets;
    protected BlockingQueue<Pair> queue;
    protected String threadName;

    private static Boolean running;

    private final Pair DEFAULT_BUCKET = new Pair(null, null);


    public Consumer(BlockingQueue<Pair> queue, String threadName) {
        this.queue = queue;
        this.threadName = threadName;

        buckets = new ArrayList<>();
    }

    public static Boolean stop(){
        running = false;

        if(running){
            return false;
        } else {
            return true;
        }

    }

    @Override
    public void run() {
        running = true;

        System.out.println("Starting" + threadName);

        InsertionSort insertionSort = new InsertionSort("Thread" + threadName);

        while (running || !queue.isEmpty()) {
            Pair nextEntry;

            if(!queue.isEmpty()){
                try {
                    nextEntry = queue.take();

                    Integer[] bucketArray = convertIntegers((ArrayList<Integer>) nextEntry.getU());

                    insertionSort.setNum(bucketArray);

                    insertionSort.setNum(bucketArray);
                    insertionSort.run();

                    nextEntry = new Pair(nextEntry.getT(), bucketArray);

                    buckets.add(nextEntry);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println(threadName + " finished.");
    }

    protected static Integer[] convertIntegers(ArrayList<Integer> integers) {
        Integer[] ret = new Integer[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }

    public ArrayList<Pair> getBuckets() {
        return buckets;
    }
}