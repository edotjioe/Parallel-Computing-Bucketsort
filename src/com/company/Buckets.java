package com.company;

import java.util.ArrayList;
import java.util.List;


public class Buckets {
    private static List<List<Integer>> buckets;

    public Buckets() {
    }

    public synchronized static void add(Integer value, int location){
        if(!value.equals(null))
            buckets.get(location).add(value);
    }

    public static void initialise(int maxValue){
        double bucketCount = Math.sqrt(maxValue);
        buckets = new ArrayList<>((int) bucketCount);
        for (int i = 0; i < bucketCount; i++) {
            buckets.add(new ArrayList<>());
        }
    }

    public static void reset(){
        buckets.clear();
    }

    public static void setBuckets(List<List<Integer>> buckets) {
        Buckets.buckets = buckets;
    }

    public static List<List<Integer>> getBuckets() {
        return buckets;
    }
}
