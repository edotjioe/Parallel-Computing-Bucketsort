package com.company;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;


public class Main {
    static List<Integer[]> sortedList = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, InterruptedException {

        System.out.println("Reading input file\n");

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
        List<Thread> threadList = new ArrayList<>();

        for (int i = 0; i < threads ; i++) {
            insertionSortList.add(new InsertionSort("Thread_" + i));
            threadList.add(new Thread(insertionSortList.get(i)));
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

        System.out.println("Starting sort");

        System.out.print("|                                                                                                    |\n|");

        for (int i = 0; i < consumer.getBuckets().size(); i = i + threads) {

            ExecutorService threadPool = Executors.newFixedThreadPool(threads);

            for (int j = 0; j < threads; j++) {
                try {
                    Integer[] bucketArray = new Integer[consumer.getBuckets().get(i + j).size()];
                    bucketArray = consumer.getBuckets().get(i + j).toArray(bucketArray);

                    insertionSortList.get(j).setNum(bucketArray);

                    sorter(bucketArray, insertionSortList.get(j), threadPool);

                    sortedList.add(i + j, insertionSortList.get(j).getNum());
                } catch (NullPointerException nullErr) {
                    i = consumer.getBuckets().size();
                }

                System.out.print("=");
            }

            threadPool.shutdown();
            threadPool.awaitTermination(1000, TimeUnit.MILLISECONDS);
        }


        System.out.print("|");

        double estimatedTime = (System.nanoTime() - startTime) / 1000000000.0;

        writeFile(sortedList, "src/com/company/files/output.txt");

        System.out.print("\nTime: " + estimatedTime + " seconds, with " + threads + " thread(s).");


    }

    public static void sorter(Integer[] bucket, InsertionSort insertionSort, ExecutorService thread){
        insertionSort.setNum(bucket);
        thread.submit(insertionSort);
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

    public static void writeFile(List<Integer []> result, String path) throws FileNotFoundException, UnsupportedEncodingException {

        PrintWriter writer = new PrintWriter(path, "UTF-8");

        for (int i = 0; i < result.size(); i++) {
            for (int j = 0; j < result.get(i).length; j++) {
                writer.println(result.get(i)[j]);
            }

        }
        writer.close();
    }

    public static void progressBar(int percentage){
        System.out.print("|");
        for (int i = 0; i < percentage; i++) {
            System.out.print("=");
        }

        for (int i = 0; i < 100 - percentage; i++) {
            System.out.print(" ");
        }

        System.out.print("|\r");
    }

}
