package com.thread.threadpool;

// 拒绝策略
@FunctionalInterface
public interface DenyPolicy {
    //拒绝接口
    void reject(Runnable runnable,ThreadPool threadPool);
}
