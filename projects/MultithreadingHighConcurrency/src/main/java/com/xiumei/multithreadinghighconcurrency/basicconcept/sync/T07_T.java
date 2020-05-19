/**
 * synchronized 关键字
 * 对某个对象进行加锁
 */
package com.xiumei.multithreadinghighconcurrency.basicconcept.sync;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 6:55 2020/5/19
 * @Version: 1.0
 * @Description:
 **/
public class T07_T {

    private static int count = 10;

    public synchronized static void m() { // 这里等同于 synchronized(T07_T.class)
        count--;
        System.out.println(Thread.currentThread().getName() + " count = " + count);
    }

    public static void n() {
        synchronized (T07_T.class) { // 考虑下这里写 synchronized(this) 是否可以？
            count--;
        }
    }

//    synchronized(this) { //考虑一下这里写synchronized(this)是否可以？ 不可以，this 不能指向静态 context
//        count --;
//    }

}
