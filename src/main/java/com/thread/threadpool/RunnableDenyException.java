package com.thread.threadpool;

// RunnableDenyException，用于通知任务提交者，任务队列已经无法再接受新的任务。
public class RunnableDenyException extends RuntimeException {

    RunnableDenyException(String message) {
        super(message);
    }
}
