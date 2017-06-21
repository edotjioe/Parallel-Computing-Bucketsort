package com.company;

public class BucketSort implements Runnable{

    String threadName;

    private Integer[] array;

    private int start, cores;

    private Buckets buckets = new Buckets();

    public BucketSort(Integer[] array, int start, String threadName, int cores) {
        this.array = array;
        this.start = start;
        this.threadName = threadName;
        this.cores = cores;
    }

    private void sort(Integer[] array) throws InterruptedException {
        if (array.length == 0) {
            return;
        }

        // Distribute input array values into buckets
        for (int i = start; i < array.length; i = i + cores) {
            Buckets.add(array[i], (int) Math.sqrt(array[i]) - 1);
        }
    }

    @Override
    public void run() {
        System.out.println("\u001B[34mStarting " + threadName + "\u001B[0m");
        try {
            sort(array);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
