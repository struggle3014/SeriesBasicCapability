/**
 * volatile 关键字，使一个变量在多个线程间可见
 * A B 线程都用到一个变量，java 默认是 A 线程中保存一份 copy，这样 B 线程修改了该变量，则 A 线程未必知道
 * 使用 volatile 关键字，会让所有线程都会独到变量的修改值
 *
 * 下面的代码中，running 是存在于堆内存的 t 对象中
 * 当线程 t1 开始运行时，会把 running 值从内存读到 t1 线程的工作区，在运行过程中直接使用这个 copy，并不会每次都去
 * 读取堆内存，这样，当主线程修改 running 的值之后，t1 线程感知不到，所以不会停止运行。
 *
 * 使用 volatile，将会强制所有线程都去堆内存中读取 running 的值
 *
 * 可以阅读这篇文章进行更深入的理解
 * http://www.cnblogs.com/nexiyi/p/java_memory_model_and_thread.html
 *
 * volatile 并不能保证多个线程共同修改 running 变量时所带来的不一致问题，也就是说 volatile 不能替代 synchronized
 */
package com.xiumei.multithreadinghighconcurrency.basicconcept.volatile_pkg;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 17:08 2020/5/19
 * @Version: 1.0
 * @Description:
 **/
public class T01_HelloVolatile {

    volatile boolean running = true; // 对比下有无 volatile 的情况，整个程序的区别

    public T01_HelloVolatile(boolean running) {
        this.running = running;
    }

    public static void main(String[] args) {
        new T01_HelloVolatile(false);
    }

}
