package com.xiumei.multithreadinghighconcurrency.basicconcept.synchronized_pkg;

import com.sun.org.apache.xml.internal.utils.ThreadControllerWrapper;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 17:47 2020/10/29
 * @Version: 1.0
 * @Description: 同步方法和非同步方法是否可以同时调用
 * 可以同时调用，下述代码就是验证了该结果
 **/
public class T03_IsSyncNonSyncCallTogether {

    public synchronized void m1() {
        System.out.println(Thread.currentThread().getName() + " m1 start....");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " m1 end....");
    }

    public void m2() {
        System.out.println(Thread.currentThread().getName() + " m2 start...");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " m2 end...");
    }

    public static void main(String[] args) {
        T03_IsSyncNonSyncCallTogether t = new T03_IsSyncNonSyncCallTogether();
        // :: lambda 表达式写法
        new Thread(t::m2, "t2").start();
        new Thread(t::m1, "t1").start();
    }

}
