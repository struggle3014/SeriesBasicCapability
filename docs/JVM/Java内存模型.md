<div align="center"><img src="https://gitee.com/struggle3014/picBed/raw/master/name_code.png"></div>

# 导读

本文主要介绍 Java 内存模型相关的内容，包括硬件层优化，CPU 层面的优化，保证不乱序的解决方法，并发内存模型的实质以及 Happens-before 原则。

***持续更新中~***



# 目录

<nav>
<a href='#导读' style='text-decoration:none;font-weight:bolder'>导读</a><br/>
<a href='#目录' style='text-decoration:none;font-weight:bolder'>目录</a><br/>
<a href='#正文' style='text-decoration:none;font-weight:bolder'>正文</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#1 Java 内存模型' style='text-decoration:none;${border-style}'>1 Java 内存模型</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#2 硬件层并发优化' style='text-decoration:none;${border-style}'>2 硬件层并发优化</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#2.1 存储器层次结构' style='text-decoration:none;${border-style}'>2.1 存储器层次结构</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#2.2 缓存行' style='text-decoration:none;${border-style}'>2.2 缓存行</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#2.3 伪共享' style='text-decoration:none;${border-style}'>2.3 伪共享</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#3 CPU 性能优化' style='text-decoration:none;${border-style}'>3 CPU 性能优化</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#3.1 乱序执行（重排序）' style='text-decoration:none;${border-style}'>3.1 乱序执行（重排序）</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#3.2 合并写' style='text-decoration:none;${border-style}'>3.2 合并写</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#4 如何保证不乱序' style='text-decoration:none;${border-style}'>4 如何保证不乱序</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#4.1 硬件级别保证' style='text-decoration:none;${border-style}'>4.1 硬件级别保证</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#4.2 JVM 级别规范' style='text-decoration:none;${border-style}'>4.2 JVM 级别规范</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#4.3 具体实现' style='text-decoration:none;${border-style}'>4.3 具体实现</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#5 并发内存模型的实质' style='text-decoration:none;${border-style}'>5 并发内存模型的实质</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#5.1 原子性' style='text-decoration:none;${border-style}'>5.1 原子性</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#5.2 可见性' style='text-decoration:none;${border-style}'>5.2 可见性</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#5.3 有序性' style='text-decoration:none;${border-style}'>5.3 有序性</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#6 Happens-before 原则（JVM 规定重排序必须遵循的规则）' style='text-decoration:none;${border-style}'>6 Happens-before 原则（JVM 规定重排序必须遵循的规则）</a><br/>
<a href='#总结' style='text-decoration:none;font-weight:bolder'>总结</a><br/>
<a href='#参考文献' style='text-decoration:none;font-weight:bolder'>参考文献</a><br/>
</nav>

# 正文

## 1 Java 内存模型

Java 内存模型（Java Memory Model，JMM）是 Java 虚拟机规范定义的，用来屏蔽 Java 程序在各种不同的硬件和操作系统对内存的访问差异，使得 Java 程序在各平台都能达到内存访问的一致性。

可避免如 C++ 等直接采用物理硬件和操作系统的内存模型在不同操作系统和硬件平台下的不同。



## 2 硬件层并发优化

### 2.1 存储器层次结构

计算机存储器层次结构很像一座金字塔，越往上越小，访问速度越快，成本越高；越往下，越大，访问速度越慢，成本越低。

![存储器层次结构](https://gitee.com/struggle3014/picBed/raw/master/存储器层次结构.png)

<div align="center"><font size="2">存储器层次结构</font></div>

**存储器指标统计**

| 类型                          | CPU 时钟周期（cycle） | 执行时间     |            |
| ----------------------------- | --------------------- | ------------ | ---------- |
| L0 Cache 寄存器               | 1 cycle               | 约 0.25ns    |            |
| L1 Cache 高速缓存             | 约 3~4 cycle          | 约 1ns       | 32K        |
| L2 Cache 高速缓存             | 约 10 cycle           | 约 3ns       | 256K       |
| L3 Cache 高速缓存             | 约 40~50 cycle        | 约 15ns      | 2M         |
| L4 Cache 主存                 | 几十到几百个 cycle    | 约 60ns~80ns | 数 G       |
| L5 本地磁盘                   |                       |              | 百 G，上 T |
| L6 分布式文件系统，Web 服务器 |                       |              | PB 以上    |



### 2.2 缓存行

CPU 缓存是一种高效的非链式结构的 Hash Map，每个桶（bucket）通常是 64 字节，即一个“缓存行（cache line）”。缓存行是内存交换的实际单位。



#### 2.2.1 什么是缓存行数据不一致性问题

![缓存行](https://gitee.com/struggle3014/picBed/raw/master/高速缓存层次结构.png)

<div align="center"><font size="2">缓存行数据不一致性问题</font></div>

如果多个 Core 对内存中的缓存行数据进行修改操作，此时会造成数据不一致问题。



#### 2.2.2 如何解决缓存行数据不一致问题

* 总线锁方案

  优点：简单，有效

  缺点：总线锁会锁住总线，使得其他 CPU 甚至不能访问内存中其他地址，效率低。

![总线锁](https://gitee.com/struggle3014/picBed/raw/master/总线锁.png)

<div align="center"><font size="2">总线锁解决数据不一致性问题</font></div>



* 缓存一致性协议[<sup>[3]</sup>]()

  * MSI
  * MOESI
  * MESI（Intel 处理器）

  优点：效率高。

  缺点：使用场景有限。有些**无法被缓存**的数据或**跨多个缓存行**的数据必须使用锁总线。

![MESI](https://gitee.com/struggle3014/picBed/raw/master/MESI.png)

<div align="center"><font size="2">MESI 缓存一致性协议</font></div>



### 2.3 伪共享

#### 2.3.1 伪共享概念

位于同一缓存行的两个不同数据，被两个不同的 CPU 锁定，产生互相影响的现象。

例如，上述“缓存行数据不一致性问题”中，若 x 和 y 位于同一缓存行，CPU1 对 x 进行修改，会使得 CPU2 中的缓存行数据失效，而 CPU2 对 y 进行修改，会使得 CPU1 中的缓存行数据失效。逻辑上，x 和 y 并没有直接联系，但实际上，两者产生了互相影响。



#### 2.3.2 案例（Disruptor）

Disruptor 中使用到缓存行填充来提高效率，核心组件 Sequence 会被频繁访问（每次入队，value 加 1），使用 LhsPadding 和 RhsPadding，保证 value 不会于其他数据归属同一缓存行，即可以避免伪共享。PS：更多 Disruptor 问题参考[多线程与高并发之 Distruptor](../多线程与高并发/Disruptor)。

```java
/**
 * 代码有删减
 */

class LhsPadding // 左端填充，7*8=56 字节
{
    protected long p1, p2, p3, p4, p5, p6, p7;
}

class Value extends LhsPadding
{
    protected volatile long value;
}

class RhsPadding extends Value // 右端填充，7*8=56 字节
{
    protected long p9, p10, p11, p12, p13, p14, p15;
}

public class Sequence extends RhsPadding  {
 	// ....
}
```



## 3 CPU 性能优化

对于现代 CPU，性能瓶颈是对于内存的访问，CPU 的速度往往比主存高两个数量级。因此 CPU 引入了 L1 Cache，L2 Cache，L3 Cache，但同时也带来了一个问题，即“若 CPU 在执行时需要访问的数据不在 cache 中，则 CPU 需要通过数据总线到内存中取数据，那么在该时间段中该做些什么？”。

针对上述问题，针对读写指令分别做了不同的优化，即读指令的乱序执行操作，写指令的合并写操作。



### 3.1 乱序执行（重排序）

关于乱序执行，先看下泡茶的问题。起初是顺序执行洗水壶，烧开水，洗水壶，洗茶杯，拿茶叶，泡茶。但是我们发现烧开水的时间会比较长，而洗茶壶和洗茶杯与烧开水没有直接的依赖关系，这段时间可以洗茶壶和洗茶杯来减少时间。

![泡茶](https://gitee.com/struggle3014/picBed/raw/master/泡茶.png)



同样，CPU 为了提高指令执行效率，会在一条指令执行过程中（**比如读取主存数据**），可同时执行后续与当前指令无依赖关系的“独立指令”（读取 L4 Cache 需几十到几百个 CPU 时钟周期，而读取 L1 Cache，L2 Cache，L3 Cache  只需几十个 CPU 时钟周期以内）。CPU 一般通过指令间的内存引用关系来判断指令间的“独立关系”（具体参见各 CPU 的文档），这是导致 CPU 乱序执行指令的根源之一。

![指令重排](https://gitee.com/struggle3014/picBed/raw/master/指令重排.png)

<div align="center"><font size="2">指令重排</font></div>



### 3.2 合并写

CPU 执行存储指令（store）时，会尝试将数据写到离 CPU 最近的 L1 缓存，若出现缓存未命中情况，CPU 会访问下一级缓存。此时，因特尔或其他厂商的 CPU 会使用“合并写（write combing）”技术。

在请求 L2 缓存行的所有权尚未完成时，待存储的数据被写到与处理器自身众多跟缓存行一样大小的存储缓冲区之一。该缓冲区允许 CPU 在缓存子系统准备好接收和处理数据时继续执行指令。

当后续的写操作需要修改相同的缓存行时，在这些写操作提交到 L2 缓存之前，可以进行缓冲区写合并。这些 64 字节的缓冲区维护了一个 64 位的字段，每更新一个字节就会设置对应的位，来表示将缓冲区交换到外部缓存时哪些数据是有效的。如果需要读取被写入缓冲区的数据时，在读取缓存之前会先读取缓冲区。这意味着，如果能在缓冲区输出到外部缓存之前将其填满，会极大地提高传输总线的效率。

该缓冲区的数量是有限的，随着 CPU 模型而异。在 Intel CPU 中，同一时刻只能拿到4个。



## 4 如何保证不乱序

### 4.1 硬件级别保证

* CPU 内存屏障/内存栅栏（X86 处理器）

  sfence：在 sfence 指令前的写操作当必须在 sfence 指令后的写操作前完成。

  lfence：在 lfence 指令前的读操作当必须在 lefence 指令后的读操作前完成。

  mfence：在 mfence 指令前的读写操作当必须在 mfence 指令后的的读写操作前完成。

* lock 汇编指令

  原子指令，如 x86 处理器 上的 “lock ...”指令是一个 Full Barrier，执行时会锁住内存子系统来确保执行顺序，甚至跨越多个 CPU。SoftWare Locks 通常使用了内存屏障或原子指令来实现变量可见性和保证顺序。



### 4.2 JVM 级别规范

| 屏障类型           | 语句                     | 作用                                                         |
| ------------------ | ------------------------ | ------------------------------------------------------------ |
| LoadLoad Barrier   | Load1;LoadLoad;Load2     | 在 Load2 及后续读取操作要读写的数据被访问前，保证 Load1 要读取的数据被读取完毕。 |
| StoreStore Barrier | Store1;StoreStore;Store2 | 在 Store2 及后续写入操作执行前，保证 Store1 的写入操作对其他处理器可见。 |
| LoadStore Barrier  | Load1;LoadStore;Store2   | 在 Store2 及后续写入操作被刷出前，保证 Load1 要读取的数据被读取完毕。 |
| StoreLoad Barrier  | Store1;StoreLoad;Load2   | 在 Load2 及后续所有读取操作执行前，保证 Store1 的写入对所有处理器可见。该屏障的开销是四种屏障中最大的。在多数处理器的实现中，该屏障是万能屏障，兼容其他三种内存屏障的功能。 |



### 4.3 具体实现

* [Volatile 实现原理](../多线程与高并发/基础概念/Volatile.md)

* [Synchronized 实现原理](../多线程与高并发/基础概念/Synchronized.md)



## 5 并发内存模型的实质

Java 并发内存模型围绕并发过程中如何处理原子性，可见性和顺序性三个特征来设计。

![Java 并发内存模型](https://gitee.com/struggle3014/picBed/raw/master/Java 并发内存模型.png)

<div align="center"><font size="2">Java 并发内存模型</font></div>



### 5.1 原子性

由 Java 内存模型来直接来保证原子性的变量操作（long 和 double 存在特例），即 Java 八大原子操作（最新 JSR 已放弃该描述）。

八大原子操作（JVM 规范）：

| 原子操作 | 描述                                        |
| -------- | ------------------------------------------- |
| lock     | 主内存，标识变量为线程独占。                |
| unlock   | 主内存，解锁线程独占变量。                  |
| read     | 主内存，读取内存到工作内存。                |
| load     | 工作内存，read 后的值放入线程本地变量副本。 |
| use      | 工作内存，传值给执行引擎。                  |
| assign   | 工作内存，执行引擎结果赋值给线程本地变量。  |
| store    | 工作内存，存值到主内存给 write 备用。       |
| write    | 主内存，写变量值。                          |



### 5.2 可见性

可见性是指当一个线程修改了一个变量的值后，其他线程立刻可以感知到这个值的修改。

如：volatile 类型的变量在修改后会立即同步给主内存，在使用时从主内存重新读取，是依赖主内存为中介保证多线程下变量对其他线程可见（底层实现：在写操作之后会插入一个 store 屏障，在读操作之会插入一个 load 屏障）；synchronized 关键字是通过 unlock 之前必须把变量同步会主内存来实现；final 是在初始化后就不会修改（底层实现：final 字段会在初始化后插入一个 store 屏障，来确保 final 字段在构造函数初始化完成并可被使用时可见）。



### 5.3 有序性

有序型从不同的角度来看是不同的。如果在本线程内部观察，所有操作都是有序的；如果在一个线程中观察另一个线程，所有的操作都是无序的。前半句描述的是线程**表现为串行的语义**，即 <font color="red">as-if-serial</font>，不管怎么重排序（编译器和处理器为了提高并行度），（单线程）程序的执行结果不会改变。后半句描述的是**指令重排序**，即主内存与工作内存之间同步存在延迟。



## 6 Happens-before 原则（JVM 规定重排序必须遵循的规则）

* 程序顺序规则：如果程序中操作 A 在操作 B 之前，那么在线程中 A 操作将在 B 操作之前执行。

* 监视器锁规则：在监视器锁上的解锁操作必须在同一个监视器锁上的加锁操作之前执行。
* volatile 变量规则：对一个 volatile 变量的写操作必须在堆该变量额度操作之前执行。
* 线程启动规则：在线程上对 Thread.start() 的调用必须在该线程中执行任何操作之前执行。
* 线程终止规则：线程中任何操作都必须在其他线程检测到该线程已经结束之前执行，或从 Thread.join() 中成功返回，或在调用 Thread.isAlive() 时返回 false。
* 线程中断规则：当一个线程在另一个线程上调用 interrupt 时，必须在被中断线程检测到 interrupt 调用之前（通过抛出 InterruptedException 或者调用 isInterrupted() 和 interrupted()）。
* 终结器规则：对象的构造函数必须在启动该对象的 finalize() 方法之前完成。
* 传递性：如果操作 A 先于操作 B，操作 B 先于操作 C，那么操作 A 先于操作 C。



# 总结

本文简介了 Java 内存模型，从硬件层面的并发优化到 CPU 层面性能优化，接着又详细描述了如何保证不乱序（指令重排），并发内存模型的实质以及 Happens-before 原则等方面的内容。



# 参考文献

[1] [深入理解计算机系统，第三版](https://www.jb51.net/books/541118.html)

[2] [多处理系统 MESI 缓存一致性协议，计算机体系结构，第五版]()

[3] [MESI 协议，百度百科](https://baike.baidu.com/item/MESI协议)

[4] [CPU 合并写缓冲区简介](https://segmentfault.com/a/1190000000382798)

[5] [深入理解 Java 虚拟机，第三版](https://www.jb51.net/books/66334.html)

[6] [Java Language Specification, Happens-before Order](https://docs.oracle.com/javase/specs/jls/se14/html/jls-17.html#jls-17.4.5)

[7] [Java 并发编程实战](https://www.jb51.net/books/377613.html)