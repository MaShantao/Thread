package com.thread.future;

public class FutureTask<T> implements Future<T> {

    //计算结果
    private T result;

    //任务是否完成
    private boolean isDone = false;

    //定义对象锁
    private final Object LOCK = new Object();


    //该方法会造成阻塞，直到任务结束
    @Override
    public T get() throws InterruptedException {
        synchronized (LOCK) {
            while (!isDone) {
                LOCK.wait();
            }
            return result;
        }
    }

    //任务执行结束，将结果赋值给FutureTask
    protected void finish(T result) {
        synchronized (LOCK) {
            if (this.isDone) return;
            this.result = result;
            this.isDone = true;
            LOCK.notifyAll();
        }
    }

    @Override
    public boolean done() {
        return isDone;
    }
}
