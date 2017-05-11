package com.company;

/**
 * Created by edo on 10-5-17.
 */
public class FinalList {
    private static Integer[] list;

    public static void setSize(int size){
        list = new Integer[size];
    }

    public static void addToList(Integer value, int location){
        list[location] = value;
    }

    public static Integer[] getList() {
        return list;
    }

    public static void setList(Integer[] list) {
        FinalList.list = list;
    }
}
