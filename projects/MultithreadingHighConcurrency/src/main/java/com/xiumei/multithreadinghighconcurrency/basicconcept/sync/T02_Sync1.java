/**
 * synchronized 底层实现
 */
package com.xiumei.multithreadinghighconcurrency.basicconcept.sync;

import org.openjdk.jol.info.ClassLayout;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 8:38 2020/5/17
 * @Version: 1.0
 * @Description:
 **/
public class T02_Sync1 {

    public static void main(String[] args) {
        Object o = new Object();
        System.out.println(ClassLayout.parseInstance(o).toPrintable());
//        synchronized (o) {
//            System.out.println(ClassLayout.parseInstance(o).toPrintable());
//        }
    }

}
