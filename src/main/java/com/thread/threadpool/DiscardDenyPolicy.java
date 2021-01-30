package com.thread.threadpool;

//丢弃的拒绝策略
public class DiscardDenyPolicy implements DenyPolicy {
    @Override
    public void reject(Runnable runnable, ThreadPool threadPool) {
        System.out.println("The RunnableQueue is Full, and" + runnable + "will be discard");
    }

}
