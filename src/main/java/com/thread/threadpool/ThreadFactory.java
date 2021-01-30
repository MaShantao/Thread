package com.thread.threadpool;

// 创建线程的工厂
@FunctionalInterface
public interface ThreadFactory {
    // 创建线程
    Thread createThread(Runnable runnable);
}
