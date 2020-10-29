package com.xiumei.multithreadinghighconcurrency.basicconcept.synchronized_pkg;

import java.util.concurrent.TimeUnit;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 22:03 2020/10/29
 * @Version: 1.0
 * @Description: 可重入
 * synchronized 获得的锁是可重入的
 * 一个同步方法调用另外一个同步方法，两个方法加的是同一把锁，一个线程已经拥有该锁对象，
 * 再次申请的时候仍然会得到该对象的锁。
 * 如下述代码所示，若某线程获取了 this 锁对象调用 m1 方法，在 m1 方法中调用
 * m2 方法，又 m2 也是加了 this 锁对象的。
 * 若 synchronized 修饰的 this 锁无法重入，则 m1 方法进入死锁，因此为了设计合理性考虑，
 * 应该将 synchronized 修饰的锁设计为可重入锁。
 **/
public class T05_Reentranct {

    synchronized void m1() {
        System.out.println("m1 start...");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        m2();
        System.out.println("m1 end...");
    }

    synchronized void m2() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("m2");
    }

    public static void main(String[] args) {
        new T05_Reentranct().m1();
    }

}
