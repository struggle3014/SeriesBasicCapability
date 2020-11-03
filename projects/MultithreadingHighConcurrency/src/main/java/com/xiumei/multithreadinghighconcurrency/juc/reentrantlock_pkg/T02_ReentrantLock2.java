package com.xiumei.multithreadinghighconcurrency.juc.reentrantlock_pkg;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 10:37 2020/11/3
 * @Version: 1.0
 * @Description: ReentrantLock 的 tryLock 尝试锁定
 * 若无法锁定，或者在指定时间内无法锁定，线程可以决定是否继续等待
 **/
public class T02_ReentrantLock2 {

    Lock lock = new ReentrantLock();

    void m1() {
        for (int i = 0; i < 10; i++) {
            try {
                lock.lock();
                TimeUnit.SECONDS.sleep(1);
                System.out.println(i);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    void m2() {
        boolean isLocked = false;
        try {
            isLocked = lock.tryLock(5, TimeUnit.SECONDS); // 通过 tryLock 尝试去定时（轮询）获取锁
            System.out.println("m2..." + isLocked);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if(isLocked) lock.unlock();
        }
    }

    public static void main(String[] args) {
        T02_ReentrantLock2 t = new T02_ReentrantLock2();
        new Thread(t::m1).start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(t::m2).start();
    }

}
