package com.xiumei.c_02_classloader;

import java.io.*;

/**
 * @Author: yue_zhou
 * @Email: yue_zhou@xinyan.com
 * @Date: 13:53 2020/5/25
 * @Version: 1.0
 * @Description: 自定义类加载器
 **/
public class T06_CustomizeClassLoader extends ClassLoader {

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        File f = new File("D:\\zhouyue\\Data\\Workspace\\own\\doc\\SeriesBasicCapability\\projects\\JavaVirtualMachine\\", name.replace(".", "/").concat(".class"));
        try {
            FileInputStream fis = new FileInputStream(f);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int b = 0;
            while((b=fis.read()) != 0) {
                baos.write(b);
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

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String clazzStr = "com.xiumei.c_02_classloader.HelloWorld";
        T06_CustomizeClassLoader loader = new T06_CustomizeClassLoader();
        Class<?> clazz = loader.loadClass(clazzStr);

        HelloWorld o = (HelloWorld)clazz.newInstance();
        o.m();

        System.out.println(loader.getClass().getClassLoader());
        System.out.println(loader.getParent());

    }

}
