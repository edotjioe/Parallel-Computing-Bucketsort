package com.company;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;


public class Main {
    static List<Integer[]> sortedList = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, InterruptedException {

        System.out.println("Reading input file");

        Integer [] input = readFile("src/com/company/files/input.txt");

        testProducerConsumer(1, input);

        testProducerConsumer(2, input);

        testProducerConsumer(3, input);

        testProducerConsumer(4, input);

        testProducerConsumer(5, input);
    }

    public static void testProducerConsumer(int threads, Integer [] input) throws FileNotFoundException, UnsupportedEncodingException, InterruptedException {
        BlockingQueue<Item> queue = new ArrayBlockingQueue<>(100);
        Integer [] result = new Integer[input.length];

        Producer producer = new Producer(queue, input);
        Consumer consumer = new Consumer(queue, 10000, input.length);

        Thread threadProducer = new Thread(producer);
        Thread threadConsumer =  new Thread(consumer);

        List<InsertionSort> insertionSortList = new ArrayList<>();

        ExecutorService threadPool = Executors.newFixedThreadPool(threads);

        for (int i = 0; i < threads ; i++) {
            insertionSortList.add(new InsertionSort("Thread_" + i));
        }

        threadProducer.start();
        threadConsumer.start();

        //waiting for buckets to be filled
        try {
            threadProducer.join();
            threadConsumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long startTime = System.nanoTime();

        for (int i = 0; i < consumer.getBuckets().size(); i = i + threads) {
            for (int j = 0; j < threads; j++) {
                try {
                    Integer[] bucketArray = new Integer[consumer.getBuckets().get(i + j).size()];
                    bucketArray = consumer.getBuckets().get(i + j).toArray(bucketArray);
                    sorter(bucketArray, insertionSortList.get(j), threadPool);
                    sortedList.add(i + j, insertionSortList.get(j).getNum());
                } catch (NullPointerException nullErr){
                    i = consumer.getBuckets().size();
                }
            }

            threadPool.awaitTermination(1, TimeUnit.NANOSECONDS);

//            for (int j = 0; j < threads ; j++) {
//                if (!insertionSortList.get(j).getNum().equals(null))
//                sortedList.add(insertionSortList.get(j).getNum());
//            }
        }

        double estimatedTime = (System.nanoTime() - startTime) / 1000000000.0;

        //inserting sorted buckets into result array
        int currentindex = 0;
        for (int j = 0; j < sortedList.size(); j++) {
            for (int k = 0; k < sortedList.get(j).length && currentindex < result.length; k++) {
                result[currentindex++] = sortedList.get(j)[k];
            }

        }

        //double estimatedTime = (System.nanoTime() - startTime) / 1000000000.0;

        PrintWriter writer = new PrintWriter("src/com/company/files/output.txt", "UTF-8");

        for (int i = 0; i < result.length; i++) {
            writer.println(result[i]);
        }
        writer.close();

        System.out.println("\nTime: " + estimatedTime + " seconds, with " + threads + " thread(s).");

        //System.out.println("\nAfter:  " + Arrays.toString(result));

        threadPool.shutdown();
    }

    public static void sorter(Integer[] bucket, InsertionSort insertionSort, ExecutorService threadPool){
        insertionSort.setNum(bucket);
        threadPool.submit(insertionSort);
    }

    public static Integer[] readFile(String path) throws FileNotFoundException {
        Scanner s = new Scanner(new File(path));
        ArrayList<Integer> list = new ArrayList<>();
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
