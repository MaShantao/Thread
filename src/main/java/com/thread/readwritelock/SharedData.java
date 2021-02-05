package com.thread.readwritelock;

import com.thread.readwritelock.ReadWriteLock;

public class SharedData {

    private char[] buffer;

    private final ReadWriteLock LOCK = new ReadWriteLock();

    public SharedData(int size) {
        buffer = new char[size];
        for (int i = 0; i < size; i++) {
            buffer[i] = '*';
        }
    }

    public char[] readData() throws InterruptedException {
        try {
            LOCK.readLock();
            return doRead();
        } finally {
            LOCK.readUnLock();
        }
    }

    private char[] doRead() {
        char newBuffer[] = new char[buffer.length];
        for (int i = 0; i < buffer.length; i++) {
            newBuffer[i] = buffer[i];
        }
        return newBuffer;
    }

    public void writeData(char c) throws InterruptedException {
        try {
            LOCK.writeLock();
            doWrite(c);
        } finally {
            LOCK.writeUnLock();
        }

    }

    private void doWrite(char c) {
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = c;
        }
    }

}