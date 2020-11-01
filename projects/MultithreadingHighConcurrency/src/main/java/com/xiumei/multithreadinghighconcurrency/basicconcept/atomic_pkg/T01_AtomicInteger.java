package com.xiumei.multithreadinghighconcurrency.basicconcept.atomic_pkg;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 15:25 2020/11/1
 * @Version: 1.0
 * @Description: AtomicXXX 类提供的方法是原子的
 * 但是不能保证多个方法连续调用具有原子性
 **/
public class T01_AtomicInteger {

    // /*volatile*/ int count = 0;
    AtomicInteger count = new AtomicInteger(0);

    /*synchronized*/ void m() {
        for (int i = 0; i < 1000; i++) {
            // if count.get() < 1000
            count.incrementAndGet(); // 等价于原子性操作的 count++
        }
    }

    public static void main(String[] args) {
        T01_AtomicInteger t = new T01_AtomicInteger();
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(t::m, "thread-" + i));
        }
        threads.forEach((o) -> o.start());
        threads.forEach((o) -> {
            try {
                o.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(t.count);
    }

}
