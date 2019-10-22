package com.company.ProducerConsumer;

import java.util.*;
import java.util.concurrent.BlockingQueue;

/**
 * Created by EdoTyran on 5/18/2017.
 */
public class Producer implements Runnable {

    protected BlockingQueue<Pair> queue;

    protected ArrayList<ArrayList<Integer>> data;

    protected String id;

    public Producer(BlockingQueue<Pair> queue, ArrayList<ArrayList<Integer>> data, String id) {
        this.queue = queue;
        this.data = data;
        this.id = id;
    }

    @Override
    public void run() {
        for (int i = 0; i < data.size(); i++) {
            try {
                Pair pair = new Pair(i, data.get(i));

                queue.put(pair);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Stopping consumers: " + Consumer.stop());
    }
}

