package com.xiumei.multithreadinghighconcurrency.basicconcept.synchronized_pkg;

import java.util.concurrent.TimeUnit;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 21:44 2020/10/29
 * @Version: 1.0
 * @Description: 银行账户读写问题，并发读写同一账户余额。
 * 会产生脏读现象，即读线程可能会读取到写线程的中间状态。
 * 因为同步方法和非同步方法可以同时运行。
 * 需要看业务是否允许这种现象，如果不允许的话，则读线程和写线程需要加同一把锁。
 **/
public class T04_BankAccountReadWriteProblem {
    // 姓名
    private String name;
    // 银行账户
    private double balance;

    public synchronized void set(String name, double balance) {
        this.name = name;

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.balance = balance;
    }

    public /*synchronized*/ double getBalance(String name) {
        return this.balance;
    }

    public static void main(String[] args) {
        T04_BankAccountReadWriteProblem t = new T04_BankAccountReadWriteProblem();
        new Thread(() -> t.set("zhangsan", 100.0)).start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(t.getBalance("zhangsan"));
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(t.getBalance("zhangsan"));
    }

}
