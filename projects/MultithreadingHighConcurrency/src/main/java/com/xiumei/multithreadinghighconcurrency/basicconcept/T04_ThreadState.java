/**
 * 线程状态：
 * NEW
 * RUNNABLE
 * TERMINATED
 * TIMEWAITING
 * WAITING
 * BLOCKED
 * 线程状态迁移具体见笔记中的线程状态迁移图
 */
package com.xiumei.multithreadinghighconcurrency.basicconcept;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 15:13 2020/5/15
 * @Version: 1.0
 * @Description:
 **/
public class T04_ThreadState {

    static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println(this.getState());
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(i);
            }
        }
    }

    public static void main(String[] args) {
        Thread t = new MyThread();
        System.out.println(t.getState());
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(t.getState());
    }

}
