package com.thread.readwritelock;

///模拟读写锁的测试
public class ReadWriteClient {
    public static void main(String[] args) {
        SharedData sharedData = new SharedData(15);
        //创建两个读进程
        new ReaderWorker("reader1", sharedData).start();
        new ReaderWorker("reader2", sharedData).start();
        //创建两个写进程
        new WriterWorker("writer1", sharedData, 'a').start();
        new WriterWorker("writer2", sharedData, 'b').start();
    }
}
