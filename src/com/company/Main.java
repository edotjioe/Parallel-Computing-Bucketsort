package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        Integer [] data;
        Integer [] result;

        data = readFile();
        result = new Integer[data.length];

        BucketSort bucketSort1, bucketSort2;
        Buckets buckets = new Buckets();

        FinalList finalList = new FinalList();
        finalList.setSize(data.length);

        // Determine minimum and maximum values
        Integer minValue = data[0];
        Integer maxValue = data[0];
        for (int i = 1; i < data.length; i++) {
            if (data[i] > maxValue) {
                maxValue = data[i];
            }
        }

        long startTime = System.nanoTime();

        bucketSort1 = new BucketSort(data, 0, "thread 1");

        bucketSort2 = new BucketSort(data, 1, "thread 2");

        buckets.initialise(maxValue);

        System.out.println("Before: " + Arrays.toString(data));

        Thread t1 = new Thread(bucketSort1);
        Thread t2 = new Thread(bucketSort2);

        t1.start();
        t2.start();


        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        System.out.println("Buckets: " + buckets.getBuckets().toString());
        // Sort buckets and place back into input array
        int currentIndex = 0;
        for (int i = 0; i < buckets.getBuckets().size(); i++) {
            Integer[] bucketArray = new Integer[buckets.getBuckets().get(i).size()];
            bucketArray = buckets.getBuckets().get(i).toArray(bucketArray);
            InsertionSort.sort(bucketArray);
            for (int j = 0; j < bucketArray.length; j++) {
                result[currentIndex++] = bucketArray[j];
            }
        }

        long estimatedTime = System.nanoTime() - startTime;

        System.out.println("\nTime: " + estimatedTime + "\nAfter:  " + Arrays.toString(result));

    }

    public static Integer[] readFile() throws FileNotFoundException {
        Scanner s = new Scanner(new File("src\\com\\company\\files\\input.txt"));
        ArrayList<Integer> list = new ArrayList<Integer>();
        Integer[] array;
        while (s.hasNext()){
            list.add(s.nextInt());
        }

        array = new Integer[list.size()];

        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        s.close();

        return array;
    }

}
