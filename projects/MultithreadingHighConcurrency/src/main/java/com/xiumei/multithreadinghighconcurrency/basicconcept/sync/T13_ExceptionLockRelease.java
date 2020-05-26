/**
 * 程序在执行过程中，如果出现异常，默认情况锁会被释放
 * 所以，在并发处理的过程中，有异常要多加小心，不然可能发生不一致的情况。
 * 比如，在一个 web app 处理过程中，多个 servlet 线程共同访问同一个资源，这时如果异常处理不合适，
 * 比如在第一个线程中抛出异常，其他线程就会进入同步代码区，有可能访问到异常产生时的数据。
 * 因此要非常小心地处理同步业务逻辑中的异常。
 */
package com.xiumei.multithreadinghighconcurrency.basicconcept.sync;

import java.util.concurrent.TimeUnit;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 15:11 2020/5/19
 * @Version: 1.0
 * @Description:
 **/
public class T13_ExceptionLockRelease {

    int count = 0;
    synchronized void m() {
        System.out.println(Thread.currentThread().getName() + " start");
        while (true) {
            count++;
            System.out.println(Thread.currentThread().getName() + " count = " + count);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (count == 5) {
                int i = 1/0; // 此处抛出异常，锁将被释放，想要不被释放，这里可以进行 catch，然后让循环继续
                System.out.println(i);
            }

        }
    }

    public static void main(String[] args) {
        T13_ExceptionLockRelease t = new T13_ExceptionLockRelease();

        Runnable r = new Runnable() {

            @Override
            public void run() {
                t.m();
            }

        };

        new Thread(r, "t1").start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(r, "t2").start();
    }

}
