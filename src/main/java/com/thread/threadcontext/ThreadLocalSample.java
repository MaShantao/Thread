package com.thread.threadcontext;

import java.util.HashMap;

public class ThreadLocalSample<T> {

    //核心存储容器，ThreadLocal需要考虑到多个线程并发，所以要考虑线程安全问题。
    final private HashMap<Thread, T> threadLocalMap = new HashMap<Thread, T>();

    public void set(T t) {
        synchronized (threadLocalMap) {
            Thread key = Thread.currentThread();
            threadLocalMap.put(key, t);
        }
    }

    public T get() {
        synchronized (threadLocalMap) {
            Thread currentThread = Thread.currentThread();
            return threadLocalMap.get(currentThread);
        }
    }
}