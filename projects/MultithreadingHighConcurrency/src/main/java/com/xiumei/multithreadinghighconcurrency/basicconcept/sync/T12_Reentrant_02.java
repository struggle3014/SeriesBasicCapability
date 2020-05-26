/**
 * 一个同步方法可以调用另外一个同步方法，一个线程已经拥有对象的锁，再次申请时仍然会得到该对象的锁。
 * 也就是说 synchronized 获得的锁是可重入的。
 * 这里是继承中有可能发生的情形，子类调用父类的同步方法。
 */
package com.xiumei.multithreadinghighconcurrency.basicconcept.sync;

import java.util.concurrent.TimeUnit;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 15:06 2020/5/19
 * @Version: 1.0
 * @Description:
 **/
public class T12_Reentrant_02 {

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
        new TT().m();
    }

}

class TT extends T12_Reentrant_02 {
    @Override
    synchronized void m() {
        System.out.println("child m start");
        super.m();
        System.out.println("child m end");
    }
}
