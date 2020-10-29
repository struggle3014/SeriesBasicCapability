package com.xiumei.multithreadinghighconcurrency.basicconcept;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 15:13 2020/5/15
 * @Version: 1.0
 * @Description: 线程的常用方法：sleep， yeild， join
 **/
public class T03_Sleep_Yield_Join {

    public static void main(String[] args) {
        testSleep();
        testYield();
        testJoin();
    }

    /**
     * 线程 sleep 方法
     */
    static void testSleep() {
        new Thread(() -> {
            for(int i=0; i<100; i++) {
                System.out.println("A" + i);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 线程 yield 方法
     */
    static void testYield() {
        new Thread(() -> {
            for(int i=0; i<100; i++) {
                System.out.println("A" + i);
                if(i%10 == 0) Thread.yield();
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println("---B" + i);
                if(i%10 == 0) Thread.yield();
            }
        }).start();
    }

    /**
     * 线程 join 方法
     */
    static void testJoin() {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println("A" + i);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                t1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 100; i++) {
                System.out.println("B" + i);
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t2.start();
        t1.start();

    }

}
