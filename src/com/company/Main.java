package com.company;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;


public class Main {
    private static List<List<Integer>> bucketlist;

    private static List<Integer[]> sortedList = new ArrayList<>();

    private static List<InsertionSort> insertionSortList;

    public static void main(String[] args) throws UnsupportedEncodingException, InterruptedException {

        System.out.println("Reading input file\n");

        Integer [] input = new Integer[0];
        try {
            input = readFile();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return;
        }

        testparallel(1, input);

        testparallel(2, input);

        testparallel(3, input);

        testparallel(4, input);

        testparallel(5, input);
    }

    public static void testparallel(int threads, Integer [] data) throws
            UnsupportedEncodingException, InterruptedException {
        long startTime = System.nanoTime();

        //Creating empty bucketList
        bucketlist = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            bucketlist.add(new ArrayList<>());
        }

        //Creating different insertionSorters for the threads
        insertionSortList = new ArrayList<>();

        for (int i = 0; i < threads; i++) {
            insertionSortList.add(new InsertionSort("thread" + i + 1));
        }

        //Inserting data into bucketList unsorted
        for (int i = 0; i < data.length; i++) {
            int value = data[i];
            int key = (int) Math.sqrt(value) - 1;

            bucketlist.get(key).add(value);
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
            writeFile(sortedList, "src/com/company/files/output.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.print("\nTime: " + estimatedTime + " seconds, with " + threads + " thread(s).");


    }

    public static void sorter(Integer[] bucket, InsertionSort insertionSort, ExecutorService thread){
        insertionSort.setNum(bucket);
        thread.submit(insertionSort);
    }

    public static Integer[] readFile() throws FileNotFoundException {
        System.out.println("Files inside Files folder:");
        File file = new File("src/com/company/files");
        for(String fileNames : file.list()) System.out.println(fileNames);

        Scanner scanner = new Scanner(System.in);

        System.out.println("Please paste the path of input.txt below: ");
        String path = "";
        path = scanner.nextLine();

        if(path == "")
            path = "src/com/company/Files/input.txt";

        scanner.close();

        scanner = new Scanner(new File(path));
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
            for (int j = 0; j < result.get(i).length; j++) {
                writer.println(result.get(i)[j]);
            }

        }
        writer.close();
    }
}
