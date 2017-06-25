package com.company;


import java.util.ArrayList;
import java.util.List;

/**
 * The code for the bucketsort is taken from the link below.
 * http://www.growingwiththeweb.com/2015/06/bucket-sort.html
 */
public class Bucketsort {

    protected Integer[] array;

    public Bucketsort(Integer[] array) {
        this.array = array;
    }

    private Integer[] sort(){

        try {
            sort(array, 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return array;
    }

    public static void sort(Integer[] array, int bucketSize) throws InterruptedException {
        if (array.length == 0) {
            return;
        }

        // Determine minimum and maximum values
        Integer minValue = array[0];
        Integer maxValue = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < minValue) {
                minValue = array[i];
            } else if (array[i] > maxValue) {
                maxValue = array[i];
            }
        }

        // Initialise buckets
        int bucketCount = (maxValue - minValue) / bucketSize + 1;
        List<List<Integer>> buckets = new ArrayList<>(bucketCount);
        for (int i = 0; i < bucketCount; i++) {
            buckets.add(new ArrayList<>());
        }

        // Distribute input array values into buckets
        for (int i = 0; i < array.length; i++) {
            buckets.get((int) Math.sqrt(array[i]) - 1).add(array[i]);
        }

        // Sort buckets and place back into input array
        int currentIndex = 0;
        for (int i = 0; i < buckets.size(); i++) {
            Integer[] bucketArray = new Integer[buckets.get(i).size()];
            bucketArray = buckets.get(i).toArray(bucketArray);
//            InsertionSort.sort(bucketArray);
            InsertionSort.sort(bucketArray);
            for (int j = 0; j < bucketArray.length; j++) {
                array[currentIndex++] = bucketArray[j];
            }
        }
    }

    public void start(){
        System.out.println("Starting bucketsort");

        long startTime = System.nanoTime();

        sort();

        double estimatedTime = (System.nanoTime() - startTime) / 1000000000.0;

        System.out.println("Ending bucketsort, time: " + estimatedTime + " s.");
    }

    public Integer[] getArray() {
        return array;
    }
}
