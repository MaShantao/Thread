package com.thread.threadpool;

public class AbortDenyPolicy implements DenyPolicy {
    @Override
    public void reject(Runnable runnable, ThreadPool threadPool) {
        throw new RunnableDenyException("The RunnableQueue is Full, and" + runnable + "will be abort");
    }
}
