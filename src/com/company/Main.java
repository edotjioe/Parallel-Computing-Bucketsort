package com.company;

import java.io.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class Main {
    static List<Integer[]> test = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {

        testProducerConsumer(2);

    }

    public static void testProducerConsumer(int threads) throws FileNotFoundException, UnsupportedEncodingException {
        BlockingQueue<Item> queue = new ArrayBlockingQueue<>(100);
        Integer [] result = new Integer[100000];

        Producer producer = new Producer(queue, readFile("src\\com\\company\\files\\input.txt"));
        Consumer consumer = new Consumer(queue, 10000, 100000);
//        Consumer consumer2 = new Consumer(queue, 10000, 50000);

        Thread threadProducer = new Thread(producer);
        Thread threadConsumer =  new Thread(consumer);
//        Thread threadConsumer2 =  new Thread(consumer2);

        List<InsertionSort> insertionSortList = new ArrayList<>();
        List<Thread> threadList = new ArrayList<>();


        for (int i = 0; i < threads ; i++) {
            insertionSortList.add(new InsertionSort("Thread_" + i));
            threadList.add(new Thread(insertionSortList.get(i)));
        }


        long startTime = System.nanoTime();

        threadProducer.start();
        threadConsumer.start();


        //waiting for buckets to be filled
        try {
            threadProducer.join();
            threadConsumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < consumer.getBuckets().size(); i = i + threads) {
            for (int j = 0; j < threads; j++) {
                Integer[] bucketArray = new Integer[consumer.getBuckets().get(i + j).size()];
                bucketArray = consumer.getBuckets().get(i + j).toArray(bucketArray);
                sorter(bucketArray ,insertionSortList.get(j), threadList.get(j));
            }

            try {
                for (int j = 0; j < threads; j++) {
                    threadList.get(j).join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int j = 0; j < threads ; j++) {
                test.add(insertionSortList.get(j).getNum());
            }
        }



        //System.out.println(consumer.toString());


        //inserting sorted buckets into result array
        int currentindex = 0;
        for (int j = 0; j < test.size(); j++) {
            for (int k = 0; k < test.get(j).length; k++) {
                result[currentindex++] = test.get(j)[k];
            }
        }
        double estimatedTime = (System.nanoTime() - startTime) / 1000000000.0;
        PrintWriter writer = new PrintWriter("src\\com\\company\\files\\output.txt", "UTF-8");


        for (int i = 0; i < result.length; i++) {
            writer.println(result[i]);
        }
        writer.close();

        System.out.println("\nTime: " + estimatedTime + " seconds");

        //System.out.println("\nAfter:  " + Arrays.toString(result));
    }

    public static void sorter(Integer[] bucket, InsertionSort insertionSort, Thread threadInsertionSort){
            insertionSort.setNum(bucket);
            threadInsertionSort = new Thread(insertionSort);
            threadInsertionSort.start();
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
//            InsertionSort.sort(bucketArray);
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
