package com.thread.future;

import java.util.concurrent.atomic.AtomicInteger;

public class FutureServiceImpl<IN, OUT> implements FutureService<IN, OUT> {

    private final static String FUTURE_THREAD_PREFIX = "FUTURE-";
    private final AtomicInteger nextCounter = new AtomicInteger(0);

    private String getNextName() {
        return FUTURE_THREAD_PREFIX + nextCounter.getAndIncrement();
    }

    //没有返回值的提交方法，执行一个run单元即可。
    @Override
    public Future<?> submit(Runnable runnable) {
        //创建一个票据
        final FutureTask<Void> futureTask = new FutureTask<>();
        new Thread(() -> {
            // 执行逻辑单元
            runnable.run();
            // 执行结束之后，将结果返回给票据,因为是没有返回值的所以返回值为null
            futureTask.finish(null);
        }).start();
        return futureTask;
    }

    //有返回值的提交方法，使用自定义的Task任务，执行get方法，并返回计算结果
    @Override
    public Future<OUT> submit(Task<IN, OUT> task, IN in) {
        //创建一个票据
        final FutureTask<OUT> futureTask = new FutureTask<>();
        new Thread(() -> {
            // 执行Task逻辑单元,输入一个值，进行计算，然后输出值
            OUT out = task.get(in);
            // 执行结束之后，将结果返回给票据,因为是没有返回值的所以返回值为null
            futureTask.finish(out);
        }).start();
        return futureTask;
    }

    //含有回调函数的提交方法
    @Override
    public Future<OUT> submit(Task<IN, OUT> task, IN in, CallBack<OUT> callBack) {
        //创建一个票据
        final FutureTask<OUT> futureTask = new FutureTask<>();
        new Thread(() -> {
            // 执行Task逻辑单元,输入一个值，进行计算，然后输出值
            OUT out = task.get(in);
            // 执行结束之后，将结果返回给票据,因为是没有返回值的所以返回值为null
            futureTask.finish(out);
            // 执行结束之后，执行回调函数
            if (callBack != null) {
                callBack.call(out);
            }
        }).start();
        return futureTask;
    }

}
