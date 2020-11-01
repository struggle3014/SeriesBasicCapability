package com.xiumei.multithreadinghighconcurrency.basicconcept.volatile_pkg;

import java.util.concurrent.TimeUnit;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 21:05 2020/5/19
 * @Version: 1.0
 * @Description: volatile 引用类型（包括数组）只能保证引用本身的可见性，不能保证内部字段的可见性
 **/
public class T02_VolatileReference {

    boolean running = true;

    volatile static T02_VolatileReference t = new T02_VolatileReference();

    void m() {
        System.out.println("m start");
        while (running) {
        }
        System.out.println("m end");
    }

    public static void main(String[] args) {
        // lambda 表达式 new Thread(new Runnable(run() {m()})
        new Thread(t::m, "t1").start();

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t.running = false;

    }

}
