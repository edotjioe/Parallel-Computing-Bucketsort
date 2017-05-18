package com.company;

import java.util.*;
import java.util.concurrent.BlockingQueue;

/**
 * Created by EdoTyran on 5/18/2017.
 */
public class Producer implements Runnable {

    protected BlockingQueue<Item> queue;

    protected Integer[] data;

    private int key, value;

    public Producer(BlockingQueue<Item> queue, Integer[] data) {
        this.queue = queue;
        this.data = data;
    }

    @Override
    public void run() {

        for (int i = 0; i < data.length; i++) {
            value = data[i];
            key = (int) Math.sqrt(value) - 1;

            Item item = new Item(key, value);

            try {
                queue.put(item);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(i + ": Produced BucketValue " + key + " of value " + value + ".");
        }
    }
}

