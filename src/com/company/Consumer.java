package com.company;

import java.util.*;
import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable{

    protected static HashMap<Integer, LinkedList<Integer>> buckets;

    protected BlockingQueue<Item> queue;

    int counter = 0, size;
    private LinkedList<Integer> list;

    public Consumer(BlockingQueue<Item> queue, int maxValue, int size) {
        this.queue = queue;
        this.size = size;

        int bucketCount = (int) Math.sqrt(maxValue);

        this.buckets = new HashMap<>(bucketCount);

        for (int i = 0; i < bucketCount; i++) {
            this.buckets.put(i, new LinkedList<>());
        }
    }

    @Override
    public synchronized void run() {
        list = new LinkedList<>();

        while (counter < size) {
            Item nextEntry;

            try {
                nextEntry = queue.take();
                //System.out.println("Consumed " + nextEntry.toString());
                list = buckets.get(nextEntry.getKey());
                InsertionSort.sort(list);

                buckets.get(nextEntry.getKey()).add(nextEntry.getValue());


                counter++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static HashMap<Integer, LinkedList<Integer>> getBuckets() {
        return buckets;
    }

//    @Override
//    public synchronized String toString() {
//        for (int i = 0; i < buckets.size(); i++) {
//            System.out.print("Bucket list " + i + ": {");
//            for (int j = 0; j < buckets.get(i).size(); j++) {
//                System.out.print(", " + buckets.get(i).get(j));
//            }
//            System.out.println("}");
//        }
//        return null;
//    }
}

class SmartConsumer extends Consumer {
    private int id;

    public SmartConsumer(BlockingQueue<Item> queue, int maxValue, int size, int id) {
        super(queue, maxValue, size);
        this.id = id;
    }

    @Override
    public synchronized void run() {
        super.run();
    }
}