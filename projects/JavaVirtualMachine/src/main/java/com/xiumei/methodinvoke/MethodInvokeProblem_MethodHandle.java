package com.xiumei.methodinvoke;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 21:57 2020/6/4
 * @Version: 1.0
 * @Description:
 **/
public class MethodInvokeProblem_MethodHandle {

    class GrandFather {
        void thinking() {
            System.out.println("i am grandfather");
        }
    }

    class Father extends GrandFather {
        @Override
        void thinking() {
            System.out.println("i am father");
        }

    }

    class Son extends Father {
        @Override
        void thinking() {
            MethodType mt = MethodType.methodType(void.class);
            try {
                MethodHandle mh = MethodHandles.lookup().findSpecial(GrandFather.class, "thinking", mt, getClass());
                mh.invoke(this);
            } catch (Throwable e) {
            }
        }
    }

    public static void main(String[] args) {
        // JDK 1.7 下可以实现，而 JDK 1.8 下无法实现。详见：https://blog.csdn.net/floor2011/article/details/100907096
        (new MethodInvokeProblem_MethodHandle().new Son()).thinking();
    }

}
