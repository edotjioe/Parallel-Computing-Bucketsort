package com.company.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 */
public class FileUtil {
    private static final String PATHOUTPUT = "src\\com\\company\\files\\output.txt";
    private static final String PATHINPUT = "src\\com\\company\\files\\input.txt";

    public static Integer[] read() throws FileNotFoundException {
        Scanner file = new Scanner(new File(PATHINPUT));
        ArrayList<Integer> list = new ArrayList<>();
        Integer[] array;
        while (file.hasNext()){
            list.add(file.nextInt());
        }

        array = new Integer[list.size()];

        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        file.close();

        return array;
    }

    public static void write(List<Integer []> buckets) throws UnsupportedEncodingException {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(PATHOUTPUT, "UTF-8");
        } catch (FileNotFoundException ex) {
            System.out.println("Output file not found.");
            return;
        }

        for (int i = 0; i < buckets.size() - 100; i++) {
            writeBucketIntoOutput(writer, buckets.get(i));
        }
        writer.close();
    }

    private static void writeBucketIntoOutput(PrintWriter writer, Integer[] bucket) {
        try {
            for (int j = 0; j < bucket.length; j++) {
                writer.println(bucket[j]);
            }
        } catch (NullPointerException ex) {
            System.out.println("found nothing.");
        }
    }
}
