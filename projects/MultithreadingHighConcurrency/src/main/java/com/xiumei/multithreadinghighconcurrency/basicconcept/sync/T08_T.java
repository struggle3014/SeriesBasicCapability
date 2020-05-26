/**
 * 分析下程序的输出
 */
package com.xiumei.multithreadinghighconcurrency.basicconcept.sync;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 13:44 2020/5/19
 * @Version: 1.0
 * @Description:
 **/
public class T08_T implements Runnable {

    private volatile/*volatile*/ int count = 100; // 保证可见性

    public synchronized/*synchronized*/ void run() { // 同样能保证可见性
        count--;
        System.out.println(Thread.currentThread().getName() + " count = " + count);
    }

    public static void main(String[] args) {
        T08_T t = new T08_T();
        for (int i = 0; i < 100; i++) {
            new Thread(t, "Thread" + i).start();
        }
    }
}
