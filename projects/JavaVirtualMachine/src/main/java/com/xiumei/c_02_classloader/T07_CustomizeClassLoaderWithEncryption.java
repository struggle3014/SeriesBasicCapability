package com.xiumei.c_02_classloader;

import java.io.*;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 14:34 2020/5/25
 * @Version: 1.0
 * @Description: 自定义加载器，对 class 文件进行加密
 **/
public class T07_CustomizeClassLoaderWithEncryption extends ClassLoader {

    final private static int seed = 0B10110110;

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        File f = new File("D:\\zhouyue\\Data\\Workspace\\own\\doc\\SeriesBasicCapability\\projects\\JavaVirtualMachine\\target\\classes\\", name.replace(".", "/").concat(".encryptClass"));

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int b = 0;
            while((b=fis.read()) != 0) {
                baos.write(b ^ seed);
            }
            byte[] bytes = baos.toByteArray();
            baos.close();
            fis.close();

            return defineClass(name, bytes, 0, bytes.length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.findClass(name);
    }

    public static void encFile(String name) throws IOException {
        File f = new File("D:\\zhouyue\\Data\\Workspace\\own\\doc\\SeriesBasicCapability\\projects\\JavaVirtualMachine\\target\\classes\\", name.replace(".", "/").concat(".class"));
        FileInputStream fis = new FileInputStream(f);
        FileOutputStream fos = new FileOutputStream(new File("D:\\zhouyue\\Data\\Workspace\\own\\doc\\SeriesBasicCapability\\projects\\JavaVirtualMachine\\target\\classes\\", name.replace(".", "/").concat(".encryptClass")));
        int b = 0;

        while((b = fis.read()) != -1) {
            fos.write(b ^ seed); // 二进制两次异或同一个值，可得到初始的值
        }

        fis.close();
        fos.close();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
//        encFile("com.xiumei.c_02_classloader.HelloWorld");
        T07_CustomizeClassLoaderWithEncryption loader = new T07_CustomizeClassLoaderWithEncryption();
        Class<?> clazz = loader.loadClass("com.xiumei.c_02_classloader.HelloWorld");

        HelloWorld o = (HelloWorld)clazz.newInstance();
        o.m();
    }

}
