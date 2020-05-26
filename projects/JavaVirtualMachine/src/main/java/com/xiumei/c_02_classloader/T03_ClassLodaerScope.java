package com.xiumei.c_02_classloader;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 16:16 2020/5/24
 * @Version: 1.0
 * @Description: 类加载器的范围
 **/
public class T03_ClassLodaerScope {

    public static void main(String[] args) {
        String pathRoot = System.getProperty("sun.boot.class.path");
        System.out.println(pathRoot.replaceAll(";", System.lineSeparator()));

        System.out.println("--------------------");

        String pathExt = System.getProperty("java.ext.dirs");
        System.out.println(pathExt.replaceAll(";", System.lineSeparator()));

        System.out.println("--------------------");

        String pathApp = System.getProperty("java.class.path");
        System.out.println(pathApp.replaceAll(";", System.lineSeparator()));
    }

}
