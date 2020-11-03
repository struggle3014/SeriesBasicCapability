package com.xiumei.multithreadinghighconcurrency.juc.reentrantlock_pkg;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 15:40 2020/11/3
 * @Version: 1.0
 * @Description: ReentrantLock 可指定为公平锁
 **/
public class T04_ReentrantLock4 extends Thread {

    private static ReentrantLock lock = new ReentrantLock(true); // 公平锁

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            try {
                lock.lock();
                System.out.println(Thread.currentThread().getName() + "获得锁");
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        T04_ReentrantLock4 t = new T04_ReentrantLock4();
        Thread t1 = new Thread(t);
        Thread t2 = new Thread(t);
        t1.start();
        t2.start();
    }

}
