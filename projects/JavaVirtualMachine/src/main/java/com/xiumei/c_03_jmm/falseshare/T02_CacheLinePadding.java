package com.xiumei.c_03_jmm.falseshare;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 14:52 2020/5/26
 * @Version: 1.0
 * @Description: 缓存行伪共享问题，缓存行大小为 64 字节
 **/
public class T02_CacheLinePadding {

    private static class Padding {
        public volatile long p1, p2, p3, p4, p5, p6, p7; // long 类型占 8 个字节，共 7*8=56 字节
    }

    private static class T extends Padding {
        public volatile long x = 0L;
    }

    public static T[] arr = new T[2];

    static {
        arr[0] = new T();
        arr[1] = new T();
    }

    static T02_CacheLinePadding t02_cacheLinePadding = new T02_CacheLinePadding();
    static T02_CacheLinePadding t02_cacheLinePadding2 = new T02_CacheLinePadding();


    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i <1000_0000L; i++) {
                arr[0].x = i;
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i <1000_0000L; i++) {
                arr[1].x = i;
            }
        });

        final long start = System.nanoTime();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println((System.nanoTime() - start)/100_0000);

    }

}
