/**
 * JOL 工具使用
 */
package com.xiumei.multithreadinghighconcurrency.basicconcept.sync;

import org.openjdk.jol.info.ClassLayout;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 16:09 2020/5/16
 * @Version: 1.0
 * @Description: JOL 工具使用，查看 Java 对象内存布局
 **/
public class T0_HelloJOL {

    public static void main(String[] args) {

        Object o = new Object();
        System.out.println(ClassLayout.parseInstance(o).toPrintable());

        synchronized (o) {
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
        }

    }

}
