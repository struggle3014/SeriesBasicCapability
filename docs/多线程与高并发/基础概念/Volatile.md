<div align="center"><img src="https://gitee.com/struggle3014/picBed/raw/master/name_code.png"></div>

# 导读

本文介绍 Volatile 关键字。

***持续更新中~***



# 目录

<nav>
<a href='#导读' style='text-decoration:none;font-weight:bolder'>导读</a><br/>
<a href='#目录' style='text-decoration:none;font-weight:bolder'>目录</a><br/>
<a href='#正文' style='text-decoration:none;font-weight:bolder'>正文</a><br/>
<a href='#总结' style='text-decoration:none;font-weight:bolder'>总结</a><br/>
<a href='#参考文献' style='text-decoration:none;font-weight:bolder'>参考文献</a><br/>
</nav>

# 正文

volatile 实现细节

* 字节码层面

ACC_VOLATILE 访问修饰符

* JVM 层面

StoreStoreBarrier

volatile 写操作

StoreLoadBarrier



LoadLoadBarrier

volatile 读操作

LoadStoreBarrier

* OS 和硬件层面

可通过 hsdis<sup>[[1](https://www.cnblogs.com/xrq730/p/7048693.html)]</sup>（HotSpot Dis Assembler，HotSpot 反汇编） 观察。



保证线程可见性

- MESI（CPU 缓存一致性协议）

禁止指令重排序

- DCL 单例
- Double Check Lock
- Mgr06.java



# 总结



# 参考文献

[1] [大话处理器，缓存一致性协议之 MESI](https://blog.csdn.net/muxiqingyang/article/details/6615199)

[2] [Java 并发编程实战- Java Concurrency in Practice]()

[3] [就是要你懂 Java 的 volatile 关键字实现原理](https://www.cnblogs.com/xrq730/p/7048693.html)