package com.thread.future;

public interface FutureService<IN, OUT> {

    //提交不需要返回值得任务，Future.get方法返回null
    Future<?> submit(Runnable runnable);

    // 提交需要返回值得任务
    Future<OUT> submit(Task<IN, OUT> task, IN in);

    // 提交需要返回值得任务
    Future<OUT> submit(Task<IN, OUT> taskM, IN in, CallBack<OUT> callBack);

    //使用静态方法创建一个FutureService的实现
    static <T, R> FutureService<T, R> newService() {
        return new FutureServiceImpl<>();
    }

}
