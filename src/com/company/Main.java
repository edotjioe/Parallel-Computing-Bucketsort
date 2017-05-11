package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        testCores1();

        testCores2();

        testCores3();

    }

    public static void testCores1() throws FileNotFoundException {
        Integer [] data;
        Integer [] result;

        data = readFile("src\\com\\company\\files\\input.txt");
        result = new Integer[data.length];

        BucketSort bucketSort1, bucketSort2;
        Buckets buckets = new Buckets();

        FinalList finalList = new FinalList();
        finalList.setSize(data.length);

        long startTime = System.nanoTime();

        bucketSort1 = new BucketSort(data, 0, "thread 1", 1);

        buckets.initialise(10000);

        //System.out.println("Before: " + Arrays.toString(data));

        Thread t1 = new Thread(bucketSort1);

        t1.start();


        try {
            t1.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        //System.out.println("Buckets: " + buckets.getBuckets().toString());
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

        double estimatedTime = (System.nanoTime() - startTime) / 1000000000.0;

        System.out.println("\nTime: " + estimatedTime);
        //System.out.println("\nAfter:  " + Arrays.toString(result));
    }

    public static void testCores2() throws FileNotFoundException {
        Integer [] data;
        Integer [] result;

        data = readFile("src\\com\\company\\files\\input.txt");
        result = new Integer[data.length];

        BucketSort bucketSort1, bucketSort2;
        Buckets buckets = new Buckets();

        FinalList finalList = new FinalList();
        finalList.setSize(data.length);

        long startTime = System.nanoTime();

        bucketSort1 = new BucketSort(data, 0, "thread 1", 2);

        bucketSort2 = new BucketSort(data, 1, "thread 2", 2);

        buckets.initialise(10000);

        //System.out.println("Before: " + Arrays.toString(data));

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

        //System.out.println("Buckets: " + buckets.getBuckets().toString());
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

        double estimatedTime = (System.nanoTime() - startTime) / 1000000000.0;

        System.out.println("\nTime: " + estimatedTime);
        //System.out.println("\nAfter:  " + Arrays.toString(result));
    }

    public static void testCores3() throws FileNotFoundException {
        Integer [] data;
        Integer [] result;

        data = readFile("src\\com\\company\\files\\input.txt");
        result = new Integer[data.length];

        BucketSort bucketSort1, bucketSort2, bucketSort3;
        Buckets buckets = new Buckets();

        FinalList finalList = new FinalList();
        finalList.setSize(data.length);

        long startTime = System.nanoTime();

        bucketSort1 = new BucketSort(data, 0, "thread 1", 3);

        bucketSort2 = new BucketSort(data, 1, "thread 2", 3);

        bucketSort3 = new BucketSort(data, 1, "thread 2", 3);

        buckets.initialise(10000);

        //System.out.println("Before: " + Arrays.toString(data));

        Thread t1 = new Thread(bucketSort1);
        Thread t2 = new Thread(bucketSort2);
        Thread t3 = new Thread(bucketSort3);

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        //System.out.println("Buckets: " + buckets.getBuckets().toString());
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

        double estimatedTime = (System.nanoTime() - startTime) / 1000000000.0;

        System.out.println("\nTime: " + estimatedTime);
        //System.out.println("\nAfter:  " + Arrays.toString(result));
    }

    public static Integer[] readFile(String path) throws FileNotFoundException {
        Scanner s = new Scanner(new File(path));
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
