package com.thread.threadpool;

public class WorkThread implements Runnable {

    private final RunableQueue runableQueue;

    private volatile boolean running = true;

    public WorkThread(RunableQueue runableQueue) {
        this.runableQueue = runableQueue;
    }

    @Override
    public void run() {
        ///如果当前任务为runing并且没有被中断，则会不断地从queue中获取runnable，然后执行run方法
        while (running && !Thread.currentThread().isInterrupted()) {
            Runnable task = null;
            try {
                task = runableQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            task.run();
        }
    }

    public void stop() {
        this.running = false;
    }
}
