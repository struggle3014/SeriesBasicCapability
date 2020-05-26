package com.xiumei.multithreadinghighconcurrency.basicconcept.volatile_pkg;

/**
 * volatile 引用类型（包括数组）只能保证引用本身的可见性，不能保证内部字段的可见性
 */

import java.util.concurrent.TimeUnit;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 21:05 2020/5/19
 * @Version: 1.0
 * @Description:
 **/
public class T02_VolatileReference1 {

    boolean running = true;

    volatile static T02_VolatileReference1 t = new T02_VolatileReference1();

    void m() {
        System.out.println("m start");
        while (running) {
            try {
                TimeUnit.MICROSECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("m end");
    }

    public static void main(String[] args) {
        new Thread(t::m, "t1").start();

        // lambda 表达式 new Thread(new Runnable(run() {m()})

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t.running = false;

    }

}
