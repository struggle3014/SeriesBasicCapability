

<div align="center"><img src="https://gitee.com/struggle3014/picBed/raw/master/name_code.png"></div>

# 导读

本文简介了 Java 对象的内存布局。主要从对象的创建过程，对象内存布局，使用 JavaAgent 测试 Object 大小，对象访问定位及对象分配。

***持续更新中~***



# 目录

<nav>
<a href='#导读' style='text-decoration:none;font-weight:bolder'>导读</a><br/>
<a href='#目录' style='text-decoration:none;font-weight:bolder'>目录</a><br/>
<a href='#正文' style='text-decoration:none;font-weight:bolder'>正文</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#1 对象的创建过程' style='text-decoration:none;${border-style}'>1 对象的创建过程</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#2 Java 对象内存布局' style='text-decoration:none;${border-style}'>2 Java 对象内存布局</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#2.1 普通对象' style='text-decoration:none;${border-style}'>2.1 普通对象</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#2.2 数组对象' style='text-decoration:none;${border-style}'>2.2 数组对象</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#3 使用 JavaAgent 测试 Object 的大小' style='text-decoration:none;${border-style}'>3 使用 JavaAgent 测试 Object 的大小</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#4 对象访问定位' style='text-decoration:none;${border-style}'>4 对象访问定位</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#4.1 句柄方式' style='text-decoration:none;${border-style}'>4.1 句柄方式</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#4.2 直接指针（HotSpot 虚拟机实现）' style='text-decoration:none;${border-style}'>4.2 直接指针（HotSpot 虚拟机实现）</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#5 对象分配' style='text-decoration:none;${border-style}'>5 对象分配</a><br/>
<a href='#总结' style='text-decoration:none;font-weight:bolder'>总结</a><br/>
<a href='#参考文献' style='text-decoration:none;font-weight:bolder'>参考文献</a><br/>
</nav>

# 正文

## 1 [对象的创建过程](./类加载，链接，初始化.md)

1. class loading

   将 class 装载到内存中

2. class linking(verfication, preparation, resouling)

   1. verfication 校验
   2. preparation 类的静态变量赋默认值
   3. resolution 解析

3. class initializing

   类的静态变量设为初始值，执行静态语句块

4. 申请对象内存

5. 成员对象赋默认值

6. 调用构造方法 <init>

   1. 成员变量赋为初始值
   2. 执行构造方法语句



## 2 Java 对象内存布局

观察虚拟机配置：java -XX:+PrintCommandLineFlags

```java
-XX:InitialHeapSize=132524480 -XX:MaxHeapSize=2120391680 -XX:+PrintCommandLineFlags -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:-UseLargePagesIndividualAllocation -XX:+UseParallelGC
```



HotSpot 开启内存压缩规则（64 位虚拟机）

* 4G 以下，直接去掉高 32 位
* 4G-32G，默认开启内存压缩，即 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops
* 32G，压缩无效



### 2.1 普通对象

* 对象头（`markword`，更多细节查看 markOop.hpp）

  8 bytes

  ![markword_64](https://gitee.com/struggle3014/picBed/raw/master/markword_64.png)

  <div align="center"><font size="2">64位 markword 实现表</font></div>

  * 当某个对象计算过 identityHashCode 值后，无法进入偏向锁状态。如果对象未重写 hashCode()，默认调用 os::random 产生 hashcode，可通过调用 System.identityHashCode 获取。os::random 产生 hashcode 的规则为：next_rand = (16807 seed) mod (2*31 - 1)，因此可使用 31 位存储。
  * 调用未重写的 hashcode() 以及 System.identityHashCode 时，会产生 hashcode，并由 JVM 记录在 markwod 中。
  * 为什么 GC 年龄默认是 15（最大为 15）？因为分代年龄由4bit表示，最大值为 2^4 -1 = 15。

* ClassPointer 指针

  --XX:+UseCompressedClassPointers 开启时为4 bytes，不开启时为8 bytes。

* 实例数据

  引用类型：-XX:+UseCompressedOops 开启时为4 bytes，不开启时为8 bytes。

* Padding 对齐

  8 的整数倍



### 2.2 数组对象

* 对象头（`markword`，更多细节查看 markOop.hpp）

  8 bytes

* ClassPointer 指针

  --XX:+UseCompressedClassPointers 开启时为4 bytes，不开启时为8 bytes。

* 数组长度

  4 bytes

* 数组数据

* Padding 对齐

  8 的整数倍



## 3 使用 JavaAgent 测试 Object 的大小

1. 新建项目 ObjectSize

2. 创建 ObjectSizeAgent

   ```java
   package com.xiumei.jvm.agent;
   
   import java.lang.instrument.Instrumentation;
   
   /**
    * @Author: yue_zhou
    * @Email: yue_zhou@xinyan.com
    * @Date: 20:43 2020/6/2
    * @Version: 1.0
    * @Description: 对象大小 agent
    **/
   public class ObjectSizeAgent {
   
       private static Instrumentation inst;
   
       public static void premain(String agentArgs, Instrumentation _inst) {
           inst = _inst;
       }
   
       public static long sizeOf(Object o) {
           return inst.getObjectSize(o);
       }
   
   }
   ```

3. src 目录下创建 META-INFO/MANIFEST.MF

   ```java
   Manifest-Version: 1.0
   Created-By: xiumei.com
   Premain-Class: com.xiumei.jvm.agent.ObjectSizeAgent
   
   ```

4. 打包 jar 文件

5. 需要使用该 Agent Jar 的项目引入该 Jar 包。

   File -> Project Structure -> Libraries 添加 Agent Jar

6. 指定运行时参数

   ```java
   -javaagent:D:\zhouyue\Data\Workspace\own\doc\SeriesBasicCapability\projects\ObjectSize\out\artifacts\ObjectSize_jar\ObjectSize.jar -XX:-UseCompressedClassPointers
   ```

7. 使用该类

   ```java
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
   ```

   

## 4 对象访问定位

Java 程序通过栈上的 reference 数据来操作对上的具体对象。由于 reference 类型在 Java 虚拟机规范中只固定了一个纸箱对象的引用，并没有定义这个引用应该通过何种方式去定位、访问对堆中的对象的具体地址，所以对象访问方式也取决于虚拟机实现而定的。目前主流的访问方式有句柄和直接指针两种。

### 4.1 句柄方式

![通过句柄访问对象](https://gitee.com/struggle3014/picBed/raw/master/通过句柄访问对象.png)

<div align="center"><font size="2">通过句柄访问对象</font></div>

如果使用句柄访问，那么 Java 堆中将会划分出一块内存来作为句柄池，referecne 中存储了对象的句柄地址。

* 优势：reference 中存储的是稳定的句柄地址，在对象被移动（垃圾收集时移动对象是非常普遍的行为）时智慧改变句柄中的示例数据指针，而 reference 本身不需要改变。



### 4.2 直接指针（HotSpot 虚拟机实现）

![通过直接指针访问对象](https://gitee.com/struggle3014/picBed/raw/master/通过直接指针访问对象.png)

如果使用直接指针访问，那么 Java 堆对象的内存布局中就必须考虑如何防止访问类型数据的相关信息，而 reference 中存储对象地址。

* 优势：速度快，节省了一次指针定位的时间开销，由于对象的访问在 Java 中非常频繁，因此这类开销也是一项非常可观的执行成本。



## 5 对象分配





# 总结

本文简介了 Java 对象的内存布局。主要从对象的创建过程，对象内存布局，使用 JavaAgent 测试 Object 大小，对象访问定位及对象分配。



# 参考文献

[1] [Java Agent](./Java Agent.md)

[2] [The class File Format, Java Virtual Machine Specifications](https://docs.oracle.com/javase/specs/jvms/se14/html/jvms-4.html)

[3] [OpenJDK 源码](http://hg.openjdk.java.net/)

[4] [对象的访问定位，深入理解 Java 虚拟机，第2版](https://www.jb51.net/books/163531.html)