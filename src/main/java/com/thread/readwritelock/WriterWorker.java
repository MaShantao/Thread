package com.thread.readwritelock;

import java.util.Random;

public class WriterWorker extends Thread {

    private final SharedData sharedData;
    private char writeData;

    public WriterWorker(String name, SharedData sharedData, char writeData) {
        super(name);
        this.sharedData = sharedData;
        this.writeData = writeData;
    }

    @Override
    public void run() {
        try {
            while (true) {
                sharedData.writeData(writeData);
                System.out.println(Thread.currentThread().getName() + " is writing " + writeData + " to sharedData.....................");
                Thread.sleep(new Random().nextInt(1000));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
