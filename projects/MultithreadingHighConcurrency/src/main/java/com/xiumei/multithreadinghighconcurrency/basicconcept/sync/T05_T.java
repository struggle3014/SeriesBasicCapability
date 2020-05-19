/**
 * synchronized 关键字
 * 对某个对象加锁
 */
package com.xiumei.multithreadinghighconcurrency.basicconcept.sync;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 6:49 2020/5/19
 * @Version: 1.0
 * @Description:
 **/
public class T05_T {

    private int count = 10;

    public synchronized void m() { // 等同于在方法的代码执行时加 synchronized(this)
        count--;
        System.out.println(Thread.currentThread().getName() + " count = " + count);
    }

}
