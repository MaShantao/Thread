package com.thread.threadpool.simplethread;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SimpleThreadPool {

    private static final int DEFAULT_MAX_THREAD_SIZE = 10;

    private static final LinkedList<Runnable> TASK_QUEUE = new LinkedList<Runnable>();

    private static final String THREAD_POOL_PREFIX = "SIMPLE_THREAD_POOL-";

    private static final int DEFAULT_MAX_TASK_SIZE = 2000;

    private final List<WorkerThread> THREAD_QUEUE = new ArrayList<WorkerThread>();

    private static final DiscardPolicy DEFAULT_DISCARD_POLICY = () -> {
        throw new DiscardException("Discard this Task...");
    };

    private int seq = 0;

    private int threadSize;

    private int taskSize;

    private DiscardPolicy discardPolicy;

    private ThreadGroup threadGroup = new ThreadGroup("simpleThreadGroup");

    private volatile boolean isDestory = false;

    public SimpleThreadPool(int threadSize, int taskSize, DiscardPolicy discardPolicy) {
        this.threadSize = threadSize;
        this.taskSize = taskSize;
        this.discardPolicy = discardPolicy;
        init();
    }

    public SimpleThreadPool() {
        this(DEFAULT_MAX_THREAD_SIZE, DEFAULT_MAX_TASK_SIZE, DEFAULT_DISCARD_POLICY);
    }

    private void init() {
        for (int i = 0; i < threadSize; i++) {
            WorkerThread WorkerThread = new WorkerThread(threadGroup, THREAD_POOL_PREFIX + seq++);
            WorkerThread.start();
            THREAD_QUEUE.add(WorkerThread);
        }
    }


    public void submit(Runnable runner) throws Exception {
        if (isDestory) {
            throw new RuntimeException("The thread pool is already destoryed and not allow to submit");
        }
        synchronized (TASK_QUEUE) {
            if (TASK_QUEUE.size() > taskSize)
                discardPolicy.discard();
            TASK_QUEUE.addLast(runner);
            TASK_QUEUE.notifyAll();
        }
    }

    public void shutdown() throws InterruptedException {
        System.out.println("shutdown");
        while (!TASK_QUEUE.isEmpty()) {
            Thread.sleep(10);
        }
        int size = THREAD_QUEUE.size();
        while (size > 0) {
            for (WorkerThread task : THREAD_QUEUE) {
                if (task.TASK_STATE == TaskState.BLOCK) {
                    task.interrupt();
                    task.close();
                    size--;
                } else {
                    Thread.sleep(10);
                }
            }
        }
        this.isDestory = true;
        System.out.println("The Thread Pool shutdown...");
    }

    public int getThreadSize() {
        return threadSize;
    }

    public int getTaskSize() {
        return taskSize;
    }

    private enum TaskState {FREE, RUNNING, BLOCK, DEAD}

    private static class DiscardException extends RuntimeException {
        public DiscardException(String message) {
            super(message);
        }
    }

    private static interface DiscardPolicy {
        public void discard() throws DiscardException;
    }


    private static class WorkerThread extends Thread {
        private volatile TaskState TASK_STATE = TaskState.FREE;

        public WorkerThread(ThreadGroup threadGroup, String threadName) {
            super(threadGroup, threadName);
        }

        @Override
        public void run() {
            OUTER:
            while (TASK_STATE != TaskState.DEAD) {
                Runnable runner = null;
                synchronized (TASK_QUEUE) {
                    while (TASK_QUEUE.size() == 0) {
                        try {
                            TASK_STATE = TaskState.BLOCK;
                            TASK_QUEUE.wait();
                        } catch (InterruptedException e) {
                            //e.printStackTrace();
                            break OUTER;
                        }
                    }
                    runner = TASK_QUEUE.removeFirst();
                }
                if (runner != null) {
                    TASK_STATE = TaskState.RUNNING;
                    runner.run();
                    TASK_STATE = TaskState.FREE;
                }
            }
        }

        public void close() {
            TASK_STATE = TaskState.DEAD;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SimpleThreadPool simpleThreadPool = new SimpleThreadPool();
        for (int i = 0; i < 40; i++) {
            final int j = i;
            try {
                simpleThreadPool.submit(() -> {
                    System.out.println("The runnable " + j + "be served as " + Thread.currentThread().getName() + " start");
                    try {
                        Thread.sleep(1000);
                        System.out.println("The runnable " + j + "be served as " + Thread.currentThread().getName() + " end");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
//                e.printStackTrace();
                System.out.println(e);
            }
        }
        Thread.sleep(9000);
        simpleThreadPool.shutdown();
        try {
            simpleThreadPool.submit(() -> {
                System.out.println("尝试再次提交...");
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
