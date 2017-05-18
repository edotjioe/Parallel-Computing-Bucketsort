package com.company;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class InsertionSort {
    public static void sort(Integer[] num) {
        int i, key;
        for (int j = 1; j < num.length; j++) {
            key = num[j];
            for(i = j-1; (i>=0)&&(num[i]< key); i--){
                num[i+1] = num[i];
            }
            num[i+1] = key;
        }
    }

    public static void sort(ArrayList<Integer> num) {
        int i, key;
        for (int j = 1; j < num.size(); j++) {
            key = num.get(j);
            for(i = j-1; (i>=0)&&(num.get(j)< key); i--){
                num.set(num.get(i+1),num.get(i));
            }
            num.set(num.get(i+1), key);
        }
    }


    public static ArrayList<Integer> sortAndReturn(ArrayList<Integer> num) {
        int i, key;
        for (int j = 1; j < num.size(); j++) {
            key = num.get(j);
            for(i = j-1; (i>=0)&&(num.get(j)< key); i--){
                num.set(num.get(i+1),num.get(i));
            }
            num.set(num.get(i+1), key);
        }
        return num;
    }

}
