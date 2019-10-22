package com.company.Sort;

public class InsertionSort implements Runnable{

    String threadName;

    private Integer[] num;

    public InsertionSort(String threadName) {
        this.threadName = threadName;
    }

    public static void sort(Integer[] num) {
        int i, key;
        for (int j = 1; j < num.length; j++) {
            key = num[j];
            for(i = j-1; (i>=0)&&(num[i]> key); i--){
                num[i+1] = num[i];
            }
            num[i+1] = key;
        }
    }

    public Integer[] getNum() {
        return num;
    }

    public void setNum(Integer[] num) {
        this.num = num;
    }


    @Override
    public void run() {
        sort(num);
    }
}
