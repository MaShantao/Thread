package com.thread.threadpool;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class BasicThreadPool extends Thread implements ThreadPool {

    // 初始化线程数量
    private final int initSize;

    // 线程池的最大线程数量
    private final int maxSize;

    // 线程池核心线程数量
    private final int coreSize;

    // 当前活跃的线程数量
    private int activeCount;

    // 创建线程所需的工厂
    private final ThreadFactory threadFactory;

    // 任务队列
    private final RunableQueue runableQueue;

    // 线程池是否已经被shutdown
    private volatile boolean isShutdown = false;

    // 工作线程队列
    private final Queue<ThreadTask> threadTaskQueue = new ArrayDeque<>();
    // 默认的拒绝策略是丢弃的策略
    private final static DenyPolicy DEFAULT_DENY_POLICY = new DiscardDenyPolicy();

    // 默认的线程工厂实现
    private final static ThreadFactory DEFAULT_THREAD_FACTORY = new DefaultThreadFactory();

    // 默认的存活时间
    private final long keepAliveTime;

    private final TimeUnit timeUnit;

    private static class ThreadTask {
        Thread thread;
        WorkThread workThread;

        public ThreadTask(Thread thread, WorkThread workThread) {
            this.thread = thread;
            this.workThread = workThread;
        }
    }

    private static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger GROUP_COUNT = new AtomicInteger(1);
        private static final ThreadGroup group = new ThreadGroup("MyThreadPool-" + GROUP_COUNT.getAndDecrement());
        private static AtomicInteger THREAD_COUNTER = new AtomicInteger(0);

        @Override
        public Thread createThread(Runnable runnable) {
            return new Thread(group, runnable, "thread-pool-" + THREAD_COUNTER.getAndIncrement());
        }
    }

    public BasicThreadPool(int initSize, int maxSize, int coreSize, ThreadFactory threadFactory,
                           int queueSize, DenyPolicy denyPolicy, long keepAliveTime, TimeUnit timeUnit) {
        this.initSize = initSize;
        this.maxSize = maxSize;
        this.coreSize = coreSize;
        this.activeCount = activeCount;
        this.threadFactory = threadFactory;
        this.runableQueue = new LinkedRunableQueue(queueSize, denyPolicy, this);
        this.keepAliveTime = keepAliveTime;
        this.timeUnit = timeUnit;
        init();
    }

    public BasicThreadPool(int initSize, int maxSize, int coreSize, int queueSize) {
        this(initSize, maxSize, coreSize, DEFAULT_THREAD_FACTORY, queueSize, DEFAULT_DENY_POLICY, 10, TimeUnit.SECONDS);
    }

    private void newThread() {
        WorkThread workThread = new WorkThread(runableQueue);
        Thread thread = this.threadFactory.createThread(workThread);
        ThreadTask threadTask = new ThreadTask(thread, workThread);
        threadTaskQueue.offer(threadTask);
        this.activeCount++;
        thread.start();
    }

    void init() {
        start();
        for (int i = 0; i < initSize; i++) {
            newThread();
        }
    }


    @Override
    public void execute(Runnable runnable) {
        if (this.isShutdown)
            throw new IllegalStateException("The ThreadPool is destory");
        this.runableQueue.offer(runnable);
    }

    @Override
    public void shutdown() {
        synchronized (this) {
            if (isShutdown) return;
            isShutdown = true;
            threadTaskQueue.forEach(threadTask -> {
                threadTask.workThread.stop();
                threadTask.thread.interrupt();
            });
        }
    }

    // 从线程池中移除某个线程
    private void removeThread() {
        ThreadTask threadTask = threadTaskQueue.remove();
        threadTask.workThread.stop();
        this.activeCount--;
    }

    @Override
    public void run() {
        while (!isShutdown && !interrupted()) {
            try {
                timeUnit.sleep(keepAliveTime);
            } catch (InterruptedException e) {
                isShutdown = true;
                break;
            }
            synchronized (this) {
                if (isShutdown) {
                    break;
                }
                // 第一次扩容：当前队列中有任务尚未处理，并且activeCount < coreSize
                if (runableQueue.size() > 0 && activeCount < coreSize) {
                    // 因为是首次扩容，所以起点就是初试大小
                    for (int i = initSize; i < coreSize; i++) {
                        newThread();
                    }
                    continue;//先扩容到coreSize大小
                }
                //第二次扩容：当前的队列中有任务尚未处理，并且activeCount < maxSize则继续扩容
                if (runableQueue.size() > 0 && activeCount < maxSize) {
                    // 扩容到coreSize之后，发现队列中还有任务没有得到处理，则继续扩容到maxSize。
                    for (int i = coreSize; i < maxSize; i++) {
                        newThread();
                    }
                }
                // 扩容结束：如果任务队列中没有任务，则需要回收部分线程，如果线程当前正在执行着任务，就等任务执行完之后回收。
                if (runableQueue.size() == 0 && activeCount > coreSize) {
                    removeThread();
                }
            }
        }
    }

    @Override
    public int getInitSize() {
        if (this.isShutdown)
            throw new IllegalStateException("The ThreadPool is destory");
        return initSize;
    }

    @Override
    public int getMaxSize() {
        if (this.isShutdown)
            throw new IllegalStateException("The ThreadPool is destory");
        return maxSize;
    }

    @Override
    public int getCoreSize() {
        if (this.isShutdown)
            throw new IllegalStateException("The ThreadPool is destory");
        return coreSize;
    }

    @Override
    public int getQueueSize() {
        if (this.isShutdown)
            throw new IllegalStateException("The ThreadPool is destory");
        return runableQueue.size();
    }

    @Override
    public int getActiveCount() {
        if (this.isShutdown)
            throw new IllegalStateException("The ThreadPool is destory");
        return activeCount;
    }

    @Override
    public boolean isShutdown() {
        return this.isShutdown;
    }

}


