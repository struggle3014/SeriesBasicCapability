package com.xiumei.multithreadinghighconcurrency.basicconcept.lock_pkg;

import java.util.concurrent.TimeUnit;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 21:49 2020/5/19
 * @Version: 1.0
 * @Description: 锁优化，锁细化和锁粗化
 * 同步代码块中的语句越少越好
 **/
public class FineCoarseLock {

    int count = 0;

    synchronized void m1() {
        // do something need not sync
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 业务逻辑中只有下面这句需要 sync，这时候不应该给整个方法上锁
        count++;
        // do something need not sync

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    void m2() {
        // do something need not sync
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 業務邏輯中只有下面這句话需要 sync，这时不应该给整个方法上锁
        // 采用细粒度的锁，可以使线程争用时间变短，从而提高效率
        synchronized (this) {
            count++;
        }
        // do something need not sync
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
