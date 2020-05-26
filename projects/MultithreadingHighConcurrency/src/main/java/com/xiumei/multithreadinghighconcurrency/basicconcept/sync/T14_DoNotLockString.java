/**
 * 不要以字符串常量作为锁对象
 * 在下面的例子中，m1 和 m2 其实锁定的是同一个对象
 * 这种情况还会发生比较诡异的现象，比如你用到了一个类库，在该类库中代码锁定了字符串“Hello”，
 * 但是你读不到源码，所以你在自己的代码中也锁定了“Hello”，这时候及有可能发生非常诡异的死锁阻塞，
 * 因为你的程序和你用到的类库不经意间使用了同一把锁
 */
package com.xiumei.multithreadinghighconcurrency.basicconcept.sync;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 21:57 2020/5/19
 * @Version: 1.0
 * @Description:
 **/
public class T14_DoNotLockString {

    String s1 = "Hello";
    String s2 = "Hello";

    void m1() {
        synchronized (s1) {

        }
    }

    void m2() {
        synchronized (s2) {

        }
    }

    public static void main(String[] args) {
        T14_DoNotLockString t = new T14_DoNotLockString();
        new Thread(() -> {
            t.m1();
        }).start();
        new Thread(() -> {
            t.m2();
        }).start();
    }

}
