package com.company;

import java.util.Arrays;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        int maxVal = 10;
        Integer [] data = {60, 25, 32, 22, 34, 40, 54, 28, 25, 34, 56, 21, 12, 11, 47, 23, 43, 38, 36, 83, 61};

        BucketSort bucketSort1, bucketSort2;
        Buckets buckets = new Buckets();

        FinalList finalList = new FinalList();
        finalList.setSize(data.length);

        // Determine minimum and maximum values
        Integer minValue = data[0];
        Integer maxValue = data[0];
        for (int i = 1; i < data.length; i++) {
            if (data[i] < minValue) {
                minValue = data[i];
            } else if (data[i] > maxValue) {
                maxValue = data[i];
            }
        }

        bucketSort1 = new BucketSort(data, 0, minValue, "thread 1");

        bucketSort2 = new BucketSort(data, 1, minValue, "thread 2");

        buckets.initialise(maxValue, minValue, data.length);

        System.out.println("Before: " + Arrays.toString(data));

        Thread t = new Thread(bucketSort1);
        Thread v = new Thread(bucketSort2);

        t.start();
        v.start();

        //bucketSort1.run();
        //bucketSort2.run();

        //System doesnt wait on threads, need to find a fix, using now a hack fix by waiting on user input
        Scanner input = new Scanner(System.in);
        System.out.println("Continue?");
        input.nextInt();

        // Sort buckets and place back into input array
        int currentIndex = 0;
        for (int i = 0; i < buckets.getBuckets().size(); i++) {
            Integer[] bucketArray = new Integer[buckets.getBuckets().get(i).size()];
            bucketArray = buckets.getBuckets().get(i).toArray(bucketArray);
            InsertionSort.sort(bucketArray);
            for (int j = 0; j < bucketArray.length; j++) {
                data[currentIndex++] = bucketArray[j];
            }
        }
        System.out.println("Buckets: " + buckets.getBuckets().toString());
        System.out.println("After:  " + Arrays.toString(data));
    }


}
