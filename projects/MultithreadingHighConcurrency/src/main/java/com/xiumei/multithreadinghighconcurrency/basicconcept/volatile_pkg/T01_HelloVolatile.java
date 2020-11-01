package com.xiumei.multithreadinghighconcurrency.basicconcept.volatile_pkg;

import java.util.concurrent.TimeUnit;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 17:08 2020/5/19
 * @Version: 1.0
 * @Description: volatile 关键字
 * 1，保证线程可见性
 * 2，防止指令重排序
 * 注意：volatile 并不能保证操作的原子性
 **/
public class T01_HelloVolatile {

    volatile boolean running = true; // 对比下有无 volatile 的情况，整个程序的区别

    void m() {
        System.out.println("m start...");
        while(running) {

        }
        System.out.println("m end...");
    }

    public static void main(String[] args) {
        T01_HelloVolatile t = new T01_HelloVolatile();
        new Thread(t::m, "t1").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t.running = false;

    }

}
