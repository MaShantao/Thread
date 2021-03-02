package com.thread.future;

public interface CallBack<T> {
    //回调方法，任务执行结束之后，执行的方法
    void call(T t);
}
