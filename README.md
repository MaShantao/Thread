# Thread

​		鄙人小渣硕的一个线程仓库，该仓库会逐渐实现Java多线程里面的一些经典的代码，如线程池、AQS、原子类等一些多线程的经典代码，并加入适当注释，方便交流学习。欢迎大家的star，您的star就是我的动力。

## 1、ThreadPool 线程池

​	手写线程池的代码已经实现了，线程池代码所在的包为**com.thread.threadpool**。关于线程池原理以及介绍，看博客[Java动手撸代码之手写线程池](https://blog.csdn.net/qq_34037358/article/details/113412745)

## 2、ThreadLocal

手写实现ThreadLocal，简易版的已经写完了，顺便把Jdk8的源码翻了翻，分析了一下，具体去看博客[一文全解ThreadLocal](https://blog.csdn.net/qq_34037358/article/details/113504875)

## 3、读写锁

手写实现ReadWriteLock，具体去看博客[读者写者问题](https://blog.csdn.net/qq_34037358/article/details/113696495)

## 4、Future设计模型

假设有个任务需要执行较长的时间，通常需要等待任务执行结束或者出错才能返回结果，在此期间调用者只能陷入阻塞苦苦等待，对此，Future设计模式提供了一种凭据式的解决方案。具体看博客[手写实现Future设计模式](https://blog.csdn.net/qq_34037358/article/details/114286268)