package com.xiumei.multithreadinghighconcurrency.basicconcept.synchronized_pkg;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 15:22 2020/10/29
 * @Version: 1.0
 * @Description: Synchronized 关键字使用及剖析
 *
 **/
public class T01_HowUseSynchronized {

    private int count1 = 10;
    private static int count2 = 10;
    private Object o = new Object();

    public void m1() {
        synchronized (o) { // 任何线程要执行下面代码块的代码，需要拿到 o 的锁
            count1--;
            System.out.println(Thread.currentThread().getName() + " count = " + count1);
        }
    }

    public void m2() {
        synchronized (this) { // 任何线程要执行下面代码块的代码，需要先拿到 this 对象的锁
            count1--;
            System.out.println(Thread.currentThread().getName() + " count = " + count1);
        }
    }

    public synchronized void m3() { // m3 方法和 m2 的效果是相同，在方法中添加 synchronized 关键字等价于给 this 对象添加 synchronized 关键字
        count1--;
        System.out.println(Thread.currentThread().getName() + " count = " + count1);
    }

    public synchronized static void m4() { // 针对静态方法添加 synchronized 关键字，其锁对象为类对象，即 T01_HowUseSynchronized.class
        count2--;
        System.out.println(Thread.currentThread().getName() + " count = " + count2);
    }

}
