package com.company;

import java.util.*;
import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable{

    protected static HashMap<Integer, LinkedList<Integer>> buckets;

    protected BlockingQueue<Item> queue;

    int counter = 0, size;
    private List list;

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
        list = new ArrayList<Integer>();

        while (counter < size) {
            Item nextEntry;

            try {
                nextEntry = queue.take();
                //System.out.println("Consumed " + nextEntry.toString());
                list = buckets.get(nextEntry.getKey());
                list = InsertionSort.sortAndReturn((ArrayList<Integer>) list);

                buckets.get(nextEntry.getKey()).add(nextEntry.getValue());


                counter++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        for (int i = 0; i < buckets.size(); i++) {
            System.out.print("Bucket list " + i + ": {");
            for (int j = 0; j < buckets.get(i).size(); j++) {
                System.out.print(", " + buckets.get(i).get(j));
            }
            System.out.println("}");
        }
        return null;
    }
}
