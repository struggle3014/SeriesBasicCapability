<div align="center"><img src="https://gitee.com/struggle3014/picBed/raw/master/name_code.png"></div>

#  导读 

本文介绍 Atomic 系列。

原子变量可以用做一种“更好的 volatile 类型变量”，提供了与 volatile 类型变量相同的内存语义，此外还支持原子的更新操作，从而使它们更加适用于实现计数器、序列发生器和统计数据收集等，同时还能比基于锁的方法提供更高的可伸缩性。

***持续更新中~***



# 目录



# 正文

原子变量比锁的粒度更细，量级更轻，并且对于在多处理器系统上实现高性能的并发代码来说是非常关键的。原子变量将发生竞争的范围缩小到单个变量上，这是你获得的粒度最细的情况。更新原子变量的快速（非竞争）路径不会比获取锁的快速路径慢，并且通常会更快，而它的慢速路径肯定比锁的慢速路径快，因为它不需要挂起或重新调度线程。在使用基于原子变量而非锁的算法中，线程在执行时更不易出现延迟，并且如果遇到竞争，也更容易恢复过来。



## 原子变量类的实现

原子变量类相当于一种泛化的 volatile 变量，能够支持原子的和有条件的读-改-写操作。AtomicInteger 表示一个 int 类型的值，并提供了 get 和 set 方法，这些 Volatile 类型的 int 变量在读取和写入上有着相同的内存语义。它还提供了一个原子的 compareAndSet 方法（如果该方法成功执行，那么将实现与读取/写入一个 volatile 变量相同的内存效果），以及原子的添加、递增或递减等方法。AtomicInteger 表面上非常像一个扩展的 Counter 类，但在发生竞争的情况下能提供更高的可剩多少星，因为它直接利用硬件对并发的支持。



## 原子变量类型

JUC 提供12个原子变量类，分为四组：

* 标量类（Scalar）
  * AtomicInteger
  * AtomicLong
  * AtomicBoolean
  * AtomicReference
* 更新器类
* 数组类
  * 原子数组类（只支持 Integer、Long 和 Reference）中的元素可以实现原子更新。
* 复合变量类



## 常见问题

原子变量可提供原子方法，但无法保证多个方法调用的原子性

[AtomicInteger](../../../projects/MultithreadingHighConcurrency/src/main/java/com/xiumei/multithreadinghighconcurrency/basicconcept/atomic_pkg/T01_AtomicInteger.java)



Atomic 变量，Synchronized 变量和 LongAdder 变量耗时比对

LongAdder 是基于分段锁，每段进行累加，并将分段结果做汇总。底层同样是基于 CAS 实现。

[AtomicVsSyncVsLongAdder](../../../projects/MultithreadingHighConcurrency/src/main/java/com/xiumei/multithreadinghighconcurrency/basicconcept/atomic_pkg/T02_AtomicVsSyncVsLongAdder.java)



# 总结



# 参考文献

[1] [Java 并发编程实战](https://www.99baiduyun.com/baidu/Java并发编程实战)