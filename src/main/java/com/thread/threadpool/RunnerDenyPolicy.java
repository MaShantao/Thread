package com.thread.threadpool;

// 该拒绝策略会使任务提交者在自己所在的线程中执行任务
public class RunnerDenyPolicy implements DenyPolicy {
    @Override
    public void reject(Runnable runnable, ThreadPool threadPool) {
        if (!threadPool.isShutdown()) {
            runnable.run();
        }
    }
}
