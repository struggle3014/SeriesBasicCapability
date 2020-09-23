package com.xiumei.c_03_jmm;

import com.xiumei.jvm.agent.ObjectSizeAgent;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 21:08 2020/6/2
 * @Version: 1.0
 * @Description: 获取对象大小
 **/
public class T03_SizeOfAnObject {

    public static void main(String[] args) {
        System.out.println(ObjectSizeAgent.sizeOf(new Object()));
        System.out.println(ObjectSizeAgent.sizeOf(new int[] {}));
        System.out.println(ObjectSizeAgent.sizeOf(new P()));
    }

    /**
     * 一个 Object 对象占多少字节
     * -XX:+UseCompressedClassPointers -XX:+UseCompressedOops
     * Oops = ordinary object pointers
     */
    private static class P {
                            // 8    _markword
                            // 4    _class pointer
        int id;             // 4
        String name;        // 4
        int age;            // 4

        byte b1;            // 1
        byte b2;            // 1

        Object o;           // 4
        byte b3;            // 1
    }

}
