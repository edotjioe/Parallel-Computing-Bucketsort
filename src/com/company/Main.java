package com.company;

import com.company.ProducerConsumer.Consumer;
import com.company.ProducerConsumer.Pair;
import com.company.ProducerConsumer.Producer;
import com.company.Sort.Bucketsort;
import com.company.Sort.InsertionSort;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static com.company.Util.FileUtil.read;
import static com.company.Util.FileUtil.write;

/**
 *
 */
public class Main {
    private static ArrayList<ArrayList<Integer>> bucketlist;
    private static List<Integer[]> sortedList = new ArrayList<>();
    private static List<InsertionSort> insertionSortListPerThread;

    public static void main(String[] args) throws UnsupportedEncodingException, InterruptedException {

        System.out.println("Reading input file\n");

        Integer [] input;
        try {
            input = read();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return;
        }
        long startTime = System.nanoTime();
        Bucketsort bucketsort = new Bucketsort(input);
        bucketsort.testSortSpeed();
        double estimatedTime = (System.nanoTime() - startTime) / 1000000000.0;
        System.out.print("\nTime: " + estimatedTime);

       initialize(input);

       //TL test
       testParallel(4);

       //ProducerConsumer Test
       testConsumerProducer(1, 3, 20);
    }

    /**
     * Test for the concurrency pattern implementation of bucketsort
     * @param producers
     * @param consumers
     * @param queueCapacity
     */
    public static void testConsumerProducer(int producers, int consumers, int queueCapacity){
        long startTime = System.nanoTime();

        System.out.println("Starting sort");

        BlockingQueue<Pair> queue = new ArrayBlockingQueue<>(queueCapacity);

        ExecutorService executorService = Executors.newWorkStealingPool();

        Producer producer;

        for (int i = 0; i < producers; i++) {
            int lower = (int) (bucketlist.size()*((double)i/producers));
            int higher = (int) (bucketlist.size()*((double)(i+1)/producers));
            ArrayList a = new ArrayList<>(bucketlist.subList(lower, higher));
            producer = new Producer(queue, a, "");
            executorService.submit(producer);
        }

        ArrayList<Consumer> consumerList = new ArrayList<>();

        for (int i = 0; i < consumers; i++) {
            consumerList.add(new Consumer(queue, "consumer" + (i + 1)));
            executorService.submit(consumerList.get(i));
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < consumerList.size(); i++) {
            ArrayList<Pair> consumerBucket = consumerList.get(i).getBuckets();
            for (int j = 0; j < consumerBucket.size(); j++) {
                Pair bucket = consumerBucket.get(j);
                sortedList.set((int) bucket.getT(), (Integer[]) bucket.getU());
            }
        }

        double estimatedTime = (System.nanoTime() - startTime) / 1000000000.0;

        try {
            write(sortedList);
        }
//        catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.print("\nTime: " + estimatedTime + " seconds, with " + consumers + " consumer(s) and " + producers + " producer(s).");

    }

    /**
     * Test for the first parallel implementation of bucketsort
     * @param threads
     * @throws UnsupportedEncodingException
     * @throws InterruptedException
     */
    public static void testParallel(int threads) throws
            UnsupportedEncodingException, InterruptedException {

        long startTime = System.nanoTime();

        //Creating different insertionSorters for the threads
        insertionSortListPerThread = new ArrayList<>();

        for (int i = 0; i < threads; i++) {
            insertionSortListPerThread.add(new InsertionSort("thread" + i + 1));
        }

        System.out.println("Starting sort");

        System.out.print("|                                                                                                    |\n|");

        for (int i = 0; i < bucketlist.size(); i = i + threads) {
            ExecutorService threadPool = Executors.newFixedThreadPool(threads);
            for (int j = 0; j < threads; j++) {
                try {
                    if(i + j < 100){
                        Integer[] bucketArray = new Integer[bucketlist.get(i + j).size()];

                        bucketArray = bucketlist.get(i + j).toArray(bucketArray);

                        insertionSortListPerThread.get(j).setNum(bucketArray);

                        sorter(bucketArray, insertionSortListPerThread.get(j), threadPool);

                        sortedList.add(i + j, insertionSortListPerThread.get(j).getNum());

                        System.out.print("=");
                    }
                } catch (NullPointerException nullErr) {
                    i = bucketlist.size();
                }
            }

            threadPool.shutdown();
            threadPool.awaitTermination(1, TimeUnit.MINUTES);
        }


        System.out.print("|");

        double estimatedTime = (System.nanoTime() - startTime) / 1000000000.0;

//        try {
//            writeFile(sortedList);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        System.out.print("\nTime: " + estimatedTime + " seconds, with " + threads + " thread(s).");


    }

    /**
     * To initialize the different arraylists used
     * @param data
     */
    protected static void initialize(Integer[] data){
        System.out.println("Filling buckets");

        for (int i = 0; i < 100; i++) {
            sortedList.add(null);
        }

        bucketlist = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            bucketlist.add(new ArrayList<>());
        }

        for (int i = 0; i < data.length; i++) {
            int value = data[i];
            int key = (int) Math.sqrt(value) - 1;

            bucketlist.get(key).add(value);
        }
    }

    /**
     * Sorters an array of Integer
     * @param bucket
     * @param insertionSort
     * @param thread
     */
    public static void sorter(Integer[] bucket, InsertionSort insertionSort, ExecutorService thread){
        insertionSort.setNum(bucket);
        thread.submit(insertionSort);
    }
}
