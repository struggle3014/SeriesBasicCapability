package com.xiumei.c_02_classloader;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 15:07 2020/5/25
 * @Version: 1.0
 * @Description: 懒加载，严格讲应称之为 lazy initializing，
 * 因为 java 虚拟机规范并没有规定什么时候必须要 loading，但严格规定了什么时候 initializing。
 **/
public class T08_LazyLoading {

    public static void main(String[] args) throws ClassNotFoundException {
//        P p;
//        X x = new X();
//        System.out.println(P.i);
//        System.out.println(P.j);
        Class.forName("com.xiumei.T08_LazyLoading$P");
    }

    public static class P {
        final static int i = 0;
        static int j = 9;
        static {
            System.out.println("P");
        }
    }

    public static class X extends P {
        static {
            System.out.println("X");
        }
    }

}
