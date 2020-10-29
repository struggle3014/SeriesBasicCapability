package com.xiumei.multithreadinghighconcurrency.basicconcept.synchronized_pkg;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 15:48 2020/10/29
 * @Version: 1.0
 * @Description: 多线程并发修改同一对象
 * 下述小程序存在的问题：
 *  1，多个线程在没有控制的情况下同时对 count 进行修改，会发生脏读
 *  2，某个线程修改了 count 数值，其他线程无法及时获知
 * 如何修改？
 *  针对 run 方法添加 synchronized 关键字，可以保证多线程操作的原子性和可读性
 *  而 volatile 只能保证可见性
 **/
public class T02_DataConfusion implements Runnable {

    private /*volatile*/ int count = 10;

    public /*synchronized*/ void run() {
        count--;
        System.out.println(Thread.currentThread().getName() + "count = " + count);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            T02_DataConfusion o = new T02_DataConfusion();
            new Thread(o, "THREAD" + i).start();
        }
    }

}
