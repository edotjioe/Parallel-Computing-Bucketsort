package com.company;

/**
 * Created by EdoTyran on 5/18/2017.
 */
public class Item {
    Integer key;
    Integer value;

    public Item(Integer key, Integer value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Item{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
