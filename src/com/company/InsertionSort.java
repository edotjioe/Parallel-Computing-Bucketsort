package com.company;

public class InsertionSort implements Runnable{

    String threadName;


    private Integer[] num;

    private int start, cores;

    public InsertionSort(String threadName) {
        this.threadName = threadName;
    }

    public static void sort(Integer[] num) throws InterruptedException{
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
        System.out.println("\u001B[34mStarting " + threadName + "\u001B[0m");
        try {
            sort(num);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\u001B[35mEnding " + threadName + "\u001B[0m");
    }
}
