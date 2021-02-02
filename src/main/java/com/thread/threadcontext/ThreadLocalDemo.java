package com.thread.threadcontext;

public class ThreadLocalDemo {
    public static void main(String[] args) {
        ThreadLocal<Object> threadLocal1 = new ThreadLocal<>();
        ThreadLocal<Object> threadLocal2 = new ThreadLocal<>();

        Thread thread = new Thread(() -> {
            Object object = new Object(); ///强引用。
            threadLocal1.set(object);
            threadLocal2.set(object);
            Thread t = Thread.currentThread();
            System.out.println("一个线程可能用过多个ThreadLocal");
        });
        thread.start();
    }
}
