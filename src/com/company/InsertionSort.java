package com.company;

public class InsertionSort {
    public static void sort(Integer[] num) {
        int i, j, key;
        for (j = 1; j < num.length; j++) {
            key = num[j];
            for(i = j-1; (i>=0)&&(num[i]< key); i--){
                num[i+1] = num[i];
            }
            num[i+1] = key;
        }
    }
}
