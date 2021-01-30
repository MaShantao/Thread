package com.thread.threadpool;

public interface RunableQueue {
    // 当有新的任务进来时首先会offer到队列
    void offer(Runnable runnable);

    // 工作线程通过take方法获取Runnable。线程获取过程中可能会抛出异常。
    Runnable take() throws InterruptedException;

    // 获取任务队列中任务的数量
    int size();
}
