package com.company;

import java.util.*;
import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable{

    protected BlockingQueue<Item> queue;

    public Consumer(BlockingQueue<Item> queue) {
        this.queue = queue;
    }

    @Override
    public synchronized void run() {
        while (true) {
            Item nextEntry;

            try {
                nextEntry = queue.take();
                System.out.println("Consumed " + nextEntry.toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
