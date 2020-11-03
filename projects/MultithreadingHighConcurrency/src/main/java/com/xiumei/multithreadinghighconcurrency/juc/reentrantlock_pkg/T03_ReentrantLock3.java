package com.xiumei.multithreadinghighconcurrency.juc.reentrantlock_pkg;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 11:04 2020/11/3
 * @Version: 1.0
 * @Description: ReentrantLock 可中断的锁获取方式
 * 使用 ReentrantLock 可调用 lockInterruptibly 方法，对线程 interrupt 方法做出响应
 * 即在线程等待锁的过程中，可以被打断
 *
 **/
public class T03_ReentrantLock3 {

    public static void main(String[] args) {
        Lock lock = new ReentrantLock();

        Thread t1 = new Thread(() -> {
            try {
                lock.lock();
                System.out.println("t1 start...");
                TimeUnit.SECONDS.sleep(10);
                System.out.println("t1 end...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                lock.lockInterruptibly(); // 可对 interrupt() 方法做出响应，若调用线程的 interrupt 方法，会发生 InterruptedException
                System.out.println("t2 start...");
                TimeUnit.SECONDS.sleep(5);
                System.out.println("t2 end...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                try {
                    lock.unlock();
                } catch(Exception e) {
                    System.out.println("异常发生啦~~~");
                }
            }
        });

        t1.start();
        t2.start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        t2.interrupt(); // 打断 Thread2 的等待


    }

}
