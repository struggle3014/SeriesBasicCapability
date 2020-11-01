<div align="center"><img src="https://gitee.com/struggle3014/picBed/raw/master/name_code.png"></div>

# 导读

本文介绍 Volatile 关键字。

Java 语言提供一种稍弱的同步机制，即 volatile 变量，用来确保变量的更新操作通知到其他线程。当把线程声明为 volatile 类型后，编译器与运行时都会注意到这个变量是共享的，因此不会将该变量上的操作与其他内存操作一起重排序。volatile 变量不会被缓存在寄存器或者对其他处理器不可见的地方，因此在读取 volatile 类型的变量时总会返回最新写入的值。

***持续更新中~***



# 目录



# 正文

## volatile 作用

* 保证线程可见性
  * MESI（CPU 缓存一致性协议）

* 禁止指令重排序



## volatile 注意点

### 保证线程可见性

* 基础类型

  可以保证线程间的可见性。

* 引用类型

  引用类型只能保证引用本身的可见性，不能保证内部字段的可见性。

  [VolatileReference](../../../projects/MultithreadingHighConcurrency/src/main/java/com/xiumei/multithreadinghighconcurrency/basicconcept/volatile_pkg/T02_VolatileReference.java)



### 保证线程可见性，但不能保证原子性

[VolatileNotSync](../../../projects/MultithreadingHighConcurrency/src/main/java/com/xiumei/multithreadinghighconcurrency/basicconcept/volatile_pkg/T03_VolatileNotSync.java)

[VolatileVsSync](../../../projects/MultithreadingHighConcurrency/src/main/java/com/xiumei/multithreadinghighconcurrency/basicconcept/volatile_pkg/T04_VolatileVsSync.java)



## volatile 使用场景

当且仅当满足以下所有条件时，才应该使用 volatile 变量：

* 对变量的写入操作不依赖变量的当前值，或者你能确保只有单个线程更新变量的值。
* 该变量不会与其他状态变量一起纳入不变性条件中。
* 在访问变量时不需要加锁。



# 总结



# 参考文献

[1] [Java 并发编程实战](https://www.99baiduyun.com/baidu/Java并发编程实战)https://www.cnblogs.com/xrq730/p/7048693.html)