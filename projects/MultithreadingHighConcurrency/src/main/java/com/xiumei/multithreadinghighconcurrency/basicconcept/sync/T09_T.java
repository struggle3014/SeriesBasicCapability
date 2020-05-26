/**
 * 同步和非同步方法是否可以同时运行？可以的
 */
package com.xiumei.multithreadinghighconcurrency.basicconcept.sync;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 13:50 2020/5/19
 * @Version: 1.0
 * @Description:
 **/
public class T09_T {

    public synchronized void m1() {
        System.out.println(Thread.currentThread().getName() + " m1 start...");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " m1 end...");
    }

    public void m2() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " m2");
    }

    public static void main(String[] args) {
        T09_T t = new T09_T();
        new Thread(t::m1, "t1").start();
        new Thread(t::m2, "t2").start();

        // 1.8 之前的写法
        new Thread(new Runnable() {
            @Override
            public void run() {
                t.m1();
            }
        }).start();
    }

}
