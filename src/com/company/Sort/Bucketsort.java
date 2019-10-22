package com.company.Sort;


import java.util.ArrayList;
import java.util.List;

/**
 * The code for the bucketsort is taken from the link below.
 * http://www.growingwiththeweb.com/2015/06/bucket-sort.html
 */
public class Bucketsort {

    protected Integer[] list;

    public Bucketsort(Integer[] list) {
        this.list = list;
    }

    private Integer[] sort(){
        try {
            sort(list, 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void sort(Integer[] list, int bucketSize) throws InterruptedException {
        if (list.length == 0) {
            return;
        }

        List<List<Integer>> buckets = initializeBuckets(bucketSize);

        buckets = distributeListIntoBuckets(buckets);

        sortBuckets(buckets);

        placeBucketsIntoList(buckets);
    }

    public void testSortSpeed(){
        System.out.println("Starting bucketsort");

        long startTime = System.nanoTime();

        sort();

        double estimatedTime = (System.nanoTime() - startTime) / 1000000000.0;

        System.out.println("Ending bucketsort, time: " + estimatedTime + " s.");
    }

    private List<List<Integer>> initializeBuckets(int bucketSize){
        int minValue = list[0];
        int maxValue = list[0];
        for (int i = 1; i < list.length; i++) {
            minValue = returnSmallestValue(list[i], minValue);
            maxValue = returnLargestValue(list[i], maxValue);
        }

        int bucketCount = (maxValue - minValue) / bucketSize + 1;
        List<List<Integer>> buckets = new ArrayList<>(bucketCount);
        for (int i = 0; i < bucketCount; i++) {
            buckets.add(new ArrayList<>());
        }

        return buckets;
    }

    private List<List<Integer>> distributeListIntoBuckets(List<List<Integer>> buckets) {
        for (int i = 0; i < list.length; i++) {
            buckets.get((int) Math.sqrt(list[i]) - 1).add(list[i]);
        }

        return buckets;
    }

    private void sortBuckets(List<List<Integer>> buckets) throws InterruptedException {
        for (int i = 0; i < buckets.size(); i++) {
            Integer[] bucketArray = new Integer[buckets.get(i).size()];
            bucketArray = buckets.get(i).toArray(bucketArray);
            InsertionSort.sort(bucketArray);
        }
    }

    private void placeBucketsIntoList(List<List<Integer>> buckets) {
        int index = 0;
        for (List<Integer> bucket:buckets) {
            index = placeBucketIntoList(bucket, index);
        }
    }

    private int placeBucketIntoList(List<Integer> bucket, int index) {
        for (Integer value:bucket) {
            list[index++] = value;
        }

        return index;
    }

    private int returnSmallestValue(int valueX, int valueY) {
        return valueY < valueX ? valueY : valueX;
    }

    private int returnLargestValue(int valueX, int valueY) {
        return valueY > valueX ? valueY : valueX;
    }
}
