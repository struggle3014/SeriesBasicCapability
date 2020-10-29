package com.xiumei.multithreadinghighconcurrency.basicconcept;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 15:12 2020/5/15
 * @Version: 1.0
 * @Description: 如何创建线程
 * 继承 Thread 类，实现 Runnable 接口
 **/
public class T02_HowToCreateThread {

    static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("Hello MyThread");
        }
    }

    static class MyRun implements Runnable {
        @Override
        public void run() {
            System.out.println("Hello MyRun");
        }
    }

    public static void main(String[] args) {
        // 创建线程的两种方式：继承 Thread 类，实现 Runnable 接口
        // 1，继承 Thread 类
        new MyThread().start();
        // 2，实现 Runnable 接口
        new Thread(new MyRun()).start();
        // 3，JDK1.8 Lambda 表达式的写法
        new Thread(() -> {
            System.out.println("Hello Lambda!");
        }).start();
    }

}
