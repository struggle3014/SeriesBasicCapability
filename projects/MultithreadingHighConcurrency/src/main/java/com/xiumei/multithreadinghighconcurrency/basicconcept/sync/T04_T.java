/**
 * synchronized 关键字
 * 对某个对象加锁
 */
package com.xiumei.multithreadinghighconcurrency.basicconcept.sync;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 6:47 2020/5/19
 * @Version: 1.0
 * @Description:
 **/
public class T04_T {

    private int count = 10;

    public void m() {
        synchronized (this) { // 任何线程要执行下面的代码，必须要拿到 this的锁
            count--;
            System.out.println(Thread.currentThread().getName() + " count = " + count);
        }
    }

}
