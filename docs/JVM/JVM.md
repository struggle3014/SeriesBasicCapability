

![name_code](https://gitee.com/struggle3014/picBed/raw/master/name_code.png)



# 导读

本文开始，系统梳理 `JVM` 机相关内容，使得知识更加体系化，方便日后查阅和复习。

**持续更新中~**



# 目录

[TOC]

# 正文

## 1 [虚拟机的概念](./虚拟机的概念.md)

* 什么是虚拟机
* 流行的 Java 虚拟机
* 从跨平台的语言到跨语言的平台
* 虚拟机知识体系



## 2 [Class 文件结构](./Class 文件结构.md)

| Markdown                              | 思维导图                                                   |
| ------------------------------------- | ---------------------------------------------------------- |
| [Class 文件结构](./Class 文件结构.md) | [Class 文件结构](../docs/MindMapping/Class 文件结构.xmind) |

![Class 文件结构](https://gitee.com/struggle3014/picBed/raw/master/Class 文件结构.png)

<div align="center"><font size="2">Class 文件结构</font></div>

* MagicNumber
* ConstantPool
* AccessFlags
* Fields
* Methods
* 常用工具



## 3 内存加载过程

* Loading
* Linking
  * verfication
  * preparation
  * resolution
* Initializing
* ClassLoader 的概念
* 双亲委派机制
* 自定义 ClassLoader



## 4 运行时内存结构

* Program Counter
* JVM Stack
* Native Stack
* Heap
* Method Area
* Stack Frame
  * Local Variable Table
  * Operand Stack



## 5 JVM 常用指令

* load
* store
* add
* pop
* dup
* invoke_XXX
* ldc



## 6 GC 调优

* 什么是垃圾
* 垃圾是如何产生的
* Java，C，C++ 垃圾回收对比
* 垃圾定位算法
  * RC
  * Root Seraching
* 垃圾回收算法
  * Mark Sweep
  * Copying
  * Market Compat
* 堆内存逻辑结构
  * Eden
  * Survivor
  * Tenured
* 对象的分配过程
  * 栈上分配
  * TLAB
* 常用垃圾回收器
  * Serial / Serial Old
  * PS / Parallel Old
  * ParNew
  * CMS
  * Epsilon
* 调优实战
  * 选择垃圾收集器的原则
    * 吞吐量
    * 反应时间
  * 生产环境常用组合
  * 常用参数设定
  * G1 参数设定
  * ParNew + CMS 参数设定
  * 模拟触发 YGC/FGC
  * 常见 JVM 问题分析
    * 为什么频繁 GC
    * Eden/Old 设定比例
    * 高并发系统高频 YGC
    * 在线系统频繁卡顿
    * JVM 虚拟器崩溃如何记录与处理
  * 实战 JVM 工具
    * jmap
    * jstat
    * jconsole
    * visual vm
  * 上线前 JVM 预调优
    * 预估 Eden/Old 内存需求
    * 预估机器配置
    * 设定日志文件
    * 进行 JVM 虚拟机监控
    * 实战案例



# 总结

文章介绍了虚拟机的概念，Class 文件结构，内存加载过程，运行时内存结构，JVM 常用指令，GC 调优等。注重理论和实战并行。



# 参考文献

[1] [`Java Language and Virutual Machine Specifications`](https://docs.oracle.com/javase/specs/index.html)