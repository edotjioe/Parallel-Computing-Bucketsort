package com.company.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by EdoTyran on 6/25/2017.
 */
public class FileUtil {
    private static String pathOutput;

    public static Integer[] readFile() throws FileNotFoundException {
        System.out.println("Files inside Files folder:");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please paste the path of input.txt below: ");
        String pathInput = "";
        pathInput = scanner.nextLine();

        System.out.println("Please paste the path of output.txt below: ");
        pathOutput = scanner.nextLine();

        if(pathInput == "")
            pathInput = "D:\\Files\\Dropbox\\Dropbox (Personal)\\Informatica HvA\\Jaar 3\\Parallel Computing\\Parallel-Computing-Bucketsort\\src\\com\\company\\files\\input.txt";

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

    public static void writeFile(List<Integer []> result) throws FileNotFoundException, UnsupportedEncodingException {

        PrintWriter writer = new PrintWriter("D:\\Files\\Dropbox\\Dropbox (Personal)\\Informatica HvA\\Jaar 3\\Parallel Computing\\Parallel-Computing-Bucketsort\\src\\com\\company\\files\\output.txt", "UTF-8");

        for (int i = 0; i < result.size(); i++) {
            Integer[] bucket = result.get(i);
            for (int j = 0; j < bucket.length; j++) {
                writer.println(bucket[j]);
            }

        }
        writer.close();
    }
}
