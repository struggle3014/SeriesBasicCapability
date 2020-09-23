package com.xiumei.methodinvoke;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 21:48 2020/6/4
 * @Version: 1.0
 * @Description:
 **/
public class MethodInvokeProblem {

    static class GrandFather {
        void thinking() {
            System.out.println("i am grandfather");
        }
    }

    static class Father extends GrandFather {
        @Override
        void thinking() {
            System.out.println("i am father");
        }

    }

    static class Son extends Father {
        @Override
        void thinking() {
            // 在这里加上适当代码（不能修改其他地方的代码）
            // 实现调用租父类的 thinking() 方法，打印 "i am grandfather"
        }
    }

    public static void main(String[] args) {
        Son son = new Son();
        son.thinking();
    }

}
