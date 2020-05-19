/**
 * 线程的概念
 * 线程是执行调度的基本单位，通俗地理解就是一个程序中不同的执行路径
 */
package com.xiumei.multithreadinghighconcurrency.basicconcept;

import java.util.concurrent.TimeUnit;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 15:12 2020/5/15
 * @Version: 1.0
 * @Description:
 **/
public class T01_WhatIsThread {

    private static class T1 extends Thread {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName());
            for(int i=0; i<10; i++) {
                try {
                    TimeUnit.MICROSECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("T1");
            }
        }
    }

    public static void main(String[] args) {
        // 直接调用 run() 方法，相当于直接在 main 线程中执行 run() 方法
//        new T1().run();
        // 调用 start() 方法，相当于启动线程并执行
        new T1().start();
        for(int i=0; i<10; i++) {
            try {
                TimeUnit.MICROSECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("main");
        }
    }

}
