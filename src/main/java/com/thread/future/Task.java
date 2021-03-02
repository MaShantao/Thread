package com.thread.future;

public interface Task<IN, OUT> {
    //给定一个参数，返回一个结果
    OUT get(IN in);
}
