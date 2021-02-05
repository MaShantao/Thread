package com.thread.readwritelock;

public class ReadWriteLock {

    private int readingReaders = 0;
    private int waitingReaders = 0;
    private int writingWriter = 0;
    private int waitingWriter = 0;

    private boolean preferWriter = true; ///偏向写线程。


    ///这里是读优先，如果有源源不断的读线程进来，会造成写线程饥饿。
//    public synchronized void readLock() throws InterruptedException {
//        this.waitingReaders++;
//        while (writingWriter > 0) {
//            this.wait();
//        }
//        this.waitingReaders--;
//        this.readingReaders++;
//    }

    //读写公平
    public synchronized void readLock() throws InterruptedException {
        this.waitingReaders++;
        while (writingWriter > 0 || (preferWriter && waitingWriter > 0)) { //偏向写线程。
            this.wait();
        }
        this.waitingReaders--;
        this.readingReaders++;
    }

    public synchronized void readUnLock() {
        this.readingReaders--;
        this.notifyAll();
    }

    public synchronized void writeLock() throws InterruptedException {
        this.waitingWriter++;
        while (writingWriter > 0 || readingReaders > 0) {
            this.wait();
        }
        this.waitingWriter--;
        this.writingWriter++;
    }

    public synchronized void writeUnLock() {
        this.writingWriter--;
        this.notifyAll();
    }

    public int getReadingReaders() {
        return readingReaders;
    }

    public int getWaitingReaders() {
        return waitingReaders;
    }

    public int getWritingWriter() {
        return writingWriter;
    }

    public int getWaitingWriter() {
        return waitingWriter;
    }
}