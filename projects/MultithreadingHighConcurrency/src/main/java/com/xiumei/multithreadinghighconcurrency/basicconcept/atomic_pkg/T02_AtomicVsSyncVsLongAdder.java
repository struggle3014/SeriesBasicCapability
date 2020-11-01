package com.xiumei.multithreadinghighconcurrency.basicconcept.atomic_pkg;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 18:40 2020/11/1
 * @Version: 1.0
 * @Description: atomic synchronized longAdder 多线程同步自增耗时对比
 **/
public class T02_AtomicVsSyncVsLongAdder {

    static long count2 = 0L;
    static AtomicLong count1 = new AtomicLong(0L);
    static LongAdder count3 = new LongAdder();

    public static void main(String[] args) throws Exception {
        // AtomicInteger
        Thread[] threads = new Thread[1000];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int k = 0; k < 100000; k++) {
                    count1.incrementAndGet();
                }
            });
        }
        long start = System.currentTimeMillis();
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }
        long end = System.currentTimeMillis();
        System.out.println("Atomic:" + count1.get() + " time " + (end - start));

        // Synchronized
        final Object lock = new Object();
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new Thread(() -> {
                for (int k = 0; k < 100000; k++) {
                    synchronized (lock) {
                        count2++;
                    }
                }
            }));
        }
        start = System.currentTimeMillis();
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }
        end = System.currentTimeMillis();
        System.out.println("Synchronized:" + count2 + " time " + (end - start));

        // LongAdder
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                for (int k = 0; k < 100000; k++) {
                    count3.increment();
                }
            });
        }
        start = System.currentTimeMillis();
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }
        end = System.currentTimeMillis();
        System.out.println("LongAdder:" + count3 + " time " + (end - start));

    }

}
