package com.company.ProducerConsumer;

/**
 * Created by EdoTyran on 6/24/2017.
 */
public class Pair<T, U> {
    public final T t;
    public final U u;


    public Pair(T t, U u) {
        this.t = t;
        this.u = u;
    }

    public T getT() {
        return t;
    }

    public U getU() {
        return u;
    }
}
