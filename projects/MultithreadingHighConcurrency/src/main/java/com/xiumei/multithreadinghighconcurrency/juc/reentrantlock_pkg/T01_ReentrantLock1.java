package com.xiumei.multithreadinghighconcurrency.juc.reentrantlock_pkg;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 10:20 2020/11/3
 * @Version: 1.0
 * @Description: ReentrantLock 加锁与释放锁
 * 使用 ReentrantLock 替代 Synchronized
 * 下述代码中，由于 m1 方法所在线程锁定 this，所以只有当 m1 方法所在线程执行完毕，m2 方法所在线程才能执行
 * 使用 ReentrantLock 需要在 finally 代码块中手动释放锁~！！！如果使用 Synchronized 发生异常，JVM 会自动释放锁
 **/
public class T01_ReentrantLock1 {

    Lock lock = new ReentrantLock();

    void m1() {
        try {
            lock.lock(); // 加锁操作，类似于 synchronized(this)
            for (int i = 0; i < 10; i++) {
                TimeUnit.SECONDS.sleep(1);
                System.out.println(i);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock(); // 释放锁操作，需要放在 finally 代码块中
        }
    }

    void m2() {
        try {
            lock.lock();
            System.out.println("m2...");
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        T01_ReentrantLock1 t = new T01_ReentrantLock1();
        new Thread(t::m1).start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(t::m2).start();
    }

}
