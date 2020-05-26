package com.xiumei.c_02_classloader;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 16:22 2020/5/24
 * @Version: 1.0
 * @Description: ClassLoader 源码解析
 **/
public class T05_LoadClassByHand {

    public static void main(String[] args) throws ClassNotFoundException {
        ClassLoader classLoader = T05_LoadClassByHand.class.getClassLoader();
        Class<?> clazz = classLoader.loadClass("com.xiumei.c_02_classloader.T02_ClassLoaderLevel");
        System.out.println(clazz.getName());
    }

}
