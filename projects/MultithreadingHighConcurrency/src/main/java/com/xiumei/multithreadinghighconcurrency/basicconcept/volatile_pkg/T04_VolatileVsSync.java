/**
 * 对比上一个程序，可以用 synchronized 解决，synchronized 可以保证可见性和原子性，volatile 只能保证可见性
 */
package com.xiumei.multithreadinghighconcurrency.basicconcept.volatile_pkg;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 21:40 2020/5/19
 * @Version: 1.0
 * @Description:
 **/
public class T04_VolatileVsSync {

    volatile int count = 0;

    synchronized void m() {
        for (int i = 0; i < 10000; i++) {
            count++;
        }
    }

    public static void main(String[] args) {
        T04_VolatileVsSync t = new T04_VolatileVsSync();

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(t::m, "thread-" + i));
        }

        threads.forEach(o -> o.start());

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
