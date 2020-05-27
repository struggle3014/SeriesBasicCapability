![name_code](https://gitee.com/struggle3014/picBed/raw/master/name_code.png)

# 导读



# 目录

[TOC]

# 正文

## 1 Java 内存模型如何支持高并发

### 1.1 硬件层的并发优化

#### 1.1.1 存储器层次结构

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



#### 1.1.2 缓存行

目前 CPU 为了提高效率，读取缓存以缓存行（cache line）为基本单位，目前多数为 64 字节。

##### 1.1.2.1 什么是缓存行数据不一致性问题



![缓存行](https://gitee.com/struggle3014/picBed/raw/master/高速缓存层次结构.png)

<div align="center"><font size="2">缓存行数据不一致性问题</font></div>

如果多个 Core 对内存中的缓存行数据进行修改操作，此时会造成数据不一致问题。



##### 1.1.2.2 如何解决缓存行数据不一致问题

* 总线锁方案

  优点：简单，有效

  缺点：总线锁会锁住总线，使得其他 CPU 甚至不能访问内存中其他地址，效率低。

![总线锁](https://gitee.com/struggle3014/picBed/raw/master/总线锁.png)

<div align="center"><font size="2">总线锁解决数据不一致性问题</font></div>



* 缓存一致性协议[<sup>[3]</sup>]()

  * `MSI`
  * `MOESI`
  * `MESI`（Intel 处理器）

  优点：效率高。

  缺点：使用场景有限。有些**无法被缓存**的数据或**跨多个缓存行**的数据必须使用锁总线。

![MESI](https://gitee.com/struggle3014/picBed/raw/master/MESI.png)

<div align="center"><font size="2">MESI 缓存一致性协议</font></div>



#### 1.1.3 伪共享

##### 1.1.3.1 伪共享概念

位于同一缓存行的两个不同数据，被两个不同的 CPU 锁定，产生互相影响的现象。

例如，上述“缓存行数据不一致性问题”中，若 x 和 y 位于同一缓存行，`CPU1` 对 x 进行修改，会使得 `CPU2` 中的缓存行数据失效，而 `CPU2` 对 y 进行修改，会使得 `CPU1` 中的缓存行数据失效。逻辑上，x 和 y 并没有直接联系，但实际上，两者产生了互相影响。



##### 1.1.3.2 案例（`Disruptor`）

`Disruptor` 中使用到缓存行填充来提高效率，核心组件 Sequence 会被频繁访问（每次入队，value 加 1），使用 `LhsPadding` 和 `RhsPadding`，保证 value 不会于其他数据归属同一缓存行，即可以避免伪共享。PS：更多 Disruptor 问题参考[多线程与高并发之 Distruptor](../多线程与高并发/Disruptor)。

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



### 1.2 乱序问题

CPU 为了提高指令执行效率，会在一条指令执行过程中（**读取内存（L4 Cache）**），可同时（读取 `L4 Cache` 需几十到几百个 CPU 时钟周期，而读取 `L0 Cache`，`L1 Cache`，`L2 Cache` 需 10 个 CPU 时钟周期）去执行后续与当前指令无依赖关系的指令。

![指令重排](https://gitee.com/struggle3014/picBed/raw/master/指令重排.png)

<div align="center"><font size="2">指令重排</font></div>

## 2 对象内存布局





高并发情况下，Java 的内存模型是如何提供支持的。

对象 new 出来后，在内存中是如何布局的。



硬件层的并发优化基础知识



基于硬件的 Java 内存模型的实现

指令重排，Happens-Before 原则，八大原子指令



# 总结



# 参考文献

[1] [深入理解计算机系统，第三版](https://www.jb51.net/books/541118.html)

[2] [多处理系统 MESI 缓存一致性协议，计算机体系结构，第五版]()

[3] [MESI 协议，百度百科](https://baike.baidu.com/item/MESI协议)