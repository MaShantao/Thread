package com.thread.readwritelock;

public class ReaderWorker extends Thread {

    private final SharedData sharedData;

    public ReaderWorker(String name, SharedData sharedData) {
        super(name);
        this.sharedData = sharedData;
    }

    @Override
    public void run() {
        try {
            while (true) {
                System.out.println(Thread.currentThread().getName() + " read data from sharedData is " + String.valueOf(sharedData.readData()));
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}