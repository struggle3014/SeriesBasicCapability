/**
 * synchronized 关键字
 */
package com.xiumei.multithreadinghighconcurrency.basicconcept.sync;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 6:44 2020/5/19
 * @Version: 1.0
 * @Description:
 **/
public class T03_T {

    private int count = 10;
    private Object o = new Object();

    public void m() {
        synchronized (o) { // 任何线程要执行下面的代码，必须先拿到 o 的锁
            count--;
            System.out.println(Thread.currentThread().getName() + " count = " + count);
        }
    }

}
