package com.xiumei.c_02_classloader;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 14:56 2020/5/24
 * @Version: 1.0
 * @Description: 类加载器层级
 **/
public class T02_ClassLoaderLevel {

    public static void main(String[] args) {
        // 查看类的加载器，null 为 bootstrap 加载器
        System.out.println(String.class.getClassLoader());
        System.out.println(sun.awt.HKSCS.class.getClassLoader());
        System.out.println(sun.net.spi.nameservice.dns.DNSNameService.class.getClassLoader());
        System.out.println(T02_ClassLoaderLevel.class.getClassLoader());

        System.out.println(sun.net.spi.nameservice.dns.DNSNameService.class.getClassLoader().getClass().getClassLoader());

        System.out.println(T02_ClassLoaderLevel.class.getClassLoader().getParent());

        System.out.println(ClassLoader.getSystemClassLoader());



    }

}
