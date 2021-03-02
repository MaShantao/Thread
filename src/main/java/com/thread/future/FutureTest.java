package com.thread.future;

public class FutureTest {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("下面测试三种不同的回调方法");
        System.out.println("首先第一种不需要返回值的：");
        FutureService<Integer, Void> futureService1 = new FutureServiceImpl<>();
        Future<?> future1 = futureService1.submit(() -> {
            System.out.println("西服做完了。。。。。");
        });
        System.out.println("不需要返回值的返回：" + future1.get());


        System.out.println("其次第二种需要返回值的：");
        FutureService<Integer, String> futureService2 = new FutureServiceImpl<>();
        Future<?> future2 = futureService2.submit(input -> {
            System.out.println("西服根据您的身高" + input + "做完了，将该消息通知给用户");
            return "西服做完了";
        }, 175);
        System.out.println("需要返回值的返回：" + future2.get());

        System.out.println("其次第三种基于时间回调的：");
        FutureService<Integer, String> futureService3 = new FutureServiceImpl<>();
        Future<?> future3 = futureService2.submit(input -> {
            System.out.println("西服根据您的身高" + input + "做完了");
            return "西服做完了";
        }, 175, input -> {
            System.out.println("西服做完了，并根据您填写的地址，给您送到了家里面");
        });
        System.out.println("回调函数的get方法就失去意义了，因为想做的事情都在回调函数里面执行了");
    }

}
