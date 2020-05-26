/**
 * volatile 并不能保证多个线程共同修改 running 变量时带来的不一致问题，也就是说 volatile 不能替代 synchronized
 * 运行下面的程序，并分析结果
 */
package com.xiumei.multithreadinghighconcurrency.basicconcept.volatile_pkg;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 21:32 2020/5/19
 * @Version: 1.0
 * @Description:
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

        threads.forEach((o) -> o.start());

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
