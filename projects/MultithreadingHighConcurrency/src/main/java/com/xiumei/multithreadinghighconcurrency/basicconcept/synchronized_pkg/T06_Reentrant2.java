package com.xiumei.multithreadinghighconcurrency.basicconcept.synchronized_pkg;

import java.util.concurrent.TimeUnit;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 22:35 2020/10/29
 * @Version: 1.0
 * @Description: synchronized 修饰的锁具有可重入性
 *
 **/
public class T06_Reentrant2 {

    synchronized void m() {
        System.out.println("m start");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("m end");
    }

    public static void main(String[] args) {
        new T06_Reentrant2().m();
    }

}

class T06_Reentrant2Child extends T06_Reentrant2 {
    @Override
    synchronized void m() {
        System.out.println("child m start");
        super.m();
        System.out.println("child m end");
    }
}
