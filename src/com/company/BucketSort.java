package com.company;

public class BucketSort implements Runnable{
    private static int DEFAULT_BUCKET_SIZE = 5;

    String threadName;

    private Integer[] array;
    private Integer minValue;

    private int start;

    private Buckets buckets = new Buckets();
    private FinalList finalList = new FinalList();

    public BucketSort(Integer[] array, int start, Integer minValue, String threadName) {
        this.array = array;
        this.start = start;
        this.minValue = minValue;
        this.threadName = threadName;

        DEFAULT_BUCKET_SIZE = array.length;
    }

    public void sort() {
        sort(this.array, DEFAULT_BUCKET_SIZE);
    }

    private void sort(Integer[] array, int bucketSize) {
        if (array.length == 0) {
            return;
        }

        // Distribute input array values into buckets
        for (int i = start; i < array.length; i = i + 2) {
            buckets.add(array[i], (array[i] - minValue) / bucketSize);
        }
    }

    @Override
    public void run() {
        System.out.println("Starting " + threadName);
        sort(array, DEFAULT_BUCKET_SIZE);
    }

}
