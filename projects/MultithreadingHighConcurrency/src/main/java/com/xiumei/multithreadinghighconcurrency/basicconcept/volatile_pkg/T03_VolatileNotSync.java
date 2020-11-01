package com.xiumei.multithreadinghighconcurrency.basicconcept.volatile_pkg;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 21:32 2020/5/19
 * @Version: 1.0
 * @Description: volatile 不能保证多个线程同时修改同一变量所带来的不一致问题，
 * 即 volatile 不能替代 synchronized
 **/
public class T03_VolatileNotSync {

    volatile int count = 0;
    void m() {
        for (int i = 0; i < 10000; i++) {
            count++;
        }
    }

    public static void main(String[] args) {
        T03_VolatileNotSync t = new T03_VolatileNotSync();

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(t::m, "thread-" + i));
        }

        threads.forEach(o -> {o.start();});

        threads.forEach(o -> {
            try {
                o.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println(t.count);

    }

}
