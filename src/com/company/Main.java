package com.company;

import javax.xml.bind.annotation.XmlType;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;


public class Main {
    private static ArrayList<ArrayList<Integer>> bucketlist;
    private static List<Integer[]> sortedList = new ArrayList<>();
    private static List<InsertionSort> insertionSortList;
    private static String pathOutput;

    private static final Pair DEFAULT_BUCKET = new Pair(null, null);

    public static void main(String[] args) throws UnsupportedEncodingException, InterruptedException {

        System.out.println("Reading input file\n");

        Integer [] input = new Integer[0];
        try {
            input = readFile();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return;
        }

        initialize(input);

        testConsumerProducer(1, 4, 20);
    }

    public static void testConsumerProducer(int producers, int consumers, int queueCapacity){
        long startTime = System.nanoTime();

        System.out.println("Starting sort");

        BlockingQueue<Pair> queue = new ArrayBlockingQueue<>(queueCapacity);

        ExecutorService executorService = Executors.newWorkStealingPool();

        // the producer and consumer share a blocking queue
//        Producer producer = new Producer(queue, bucketlist, "producer" + 1);
//        executorService.submit(producer);
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
            writeFile(sortedList, pathOutput);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.print("\nTime: " + estimatedTime + " seconds, with " + consumers + " consumer(s) and " + producers + " producer(s).");

    }

    public static void testparallel(int threads) throws
            UnsupportedEncodingException, InterruptedException {

        long startTime = System.nanoTime();

        //Creating different insertionSorters for the threads
        insertionSortList = new ArrayList<>();

        for (int i = 0; i < threads; i++) {
            insertionSortList.add(new InsertionSort("thread" + i + 1));
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

                        insertionSortList.get(j).setNum(bucketArray);

                        sorter(bucketArray, insertionSortList.get(j), threadPool);

                        sortedList.add(i + j, insertionSortList.get(j).getNum());

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

        try {
            writeFile(sortedList, "output.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.print("\nTime: " + estimatedTime + " seconds, with " + threads + " thread(s).");


    }

    protected static void initialize(Integer[] data){
        System.out.println("Filling buckets");

        for (int i = 0; i < 100; i++) {
            sortedList.add(null);
        }

        //Creating empty bucketList
        bucketlist = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            bucketlist.add(new ArrayList<>());
        }

        //Inserting data into bucketList unsorted
        for (int i = 0; i < data.length; i++) {
            int value = data[i];
            int key = (int) Math.sqrt(value) - 1;

            bucketlist.get(key).add(value);
        }
    }

    public static void sorter(Integer[] bucket, InsertionSort insertionSort, ExecutorService thread){
        insertionSort.setNum(bucket);
        thread.submit(insertionSort);
    }

    public static Integer[] readFile() throws FileNotFoundException {
        System.out.println("Files inside Files folder:");
        //File file = new File("src/com/company/files");
        //for(String fileNames : file.list()) System.out.println(fileNames);

        Scanner scanner = new Scanner(System.in);

        System.out.println("Please paste the path of input.txt below: ");
        String pathInput = "";
        pathInput = scanner.nextLine();

        System.out.println("Please paste the path of output.txt below: ");
        pathOutput = scanner.nextLine();

        if(pathInput == "")
            pathInput = "src/com/company/Files/input.txt";

        scanner.close();

        scanner = new Scanner(new File(pathInput));
        ArrayList<Integer> list = new ArrayList<>();
        Integer[] array;
        while (scanner.hasNext()){
            list.add(scanner.nextInt());
        }

        array = new Integer[list.size()];

        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        scanner.close();

        return array;
    }

    public static void writeFile(List<Integer []> result, String path) throws FileNotFoundException, UnsupportedEncodingException {

        PrintWriter writer = new PrintWriter(path, "UTF-8");

        for (int i = 0; i < result.size(); i++) {
            Integer[] bucket = result.get(i);
            for (int j = 0; j < bucket.length; j++) {
                writer.println(bucket[j]);
            }

        }
        writer.close();
    }
}
