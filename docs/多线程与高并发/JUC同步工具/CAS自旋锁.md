<div align="center"><img src="https://gitee.com/struggle3014/picBed/raw/master/name_code.png"></div>

#  导读 

本文介绍 CAS 自旋锁。

***持续更新中~***



# 目录

<nav>
<a href='# 导读' style='text-decoration:none;font-weight:bolder'> 导读</a><br/>
<a href='#目录' style='text-decoration:none;font-weight:bolder'>目录</a><br/>
<a href='#正文' style='text-decoration:none;font-weight:bolder'>正文</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#CAS 概念' style='text-decoration:none;${border-style}'>CAS 概念</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#ABA 问题' style='text-decoration:none;${border-style}'>ABA 问题</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#ABA 问题影响' style='text-decoration:none;${border-style}'>ABA 问题影响</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#如何解决 ABA 问题' style='text-decoration:none;${border-style}'>如何解决 ABA 问题</a><br/>
<a href='#总结' style='text-decoration:none;font-weight:bolder'>总结</a><br/>
<a href='#参考文献' style='text-decoration:none;font-weight:bolder'>参考文献</a><br/>
</nav>

# 正文

## CAS 概念

CAS，Compare And Set / Compare And Swap，通常称为无锁优化，自旋锁，乐观锁。

CAS 是 CPU 原语支持，具有原子性。

```java
cas(V, Excepted, NewValue)
	if V == E:
		V = New;
	else:
        try again or fail
```



## ABA 问题

ABA 问题，即在并发情况下其他线程将原值 A 修改成 B，后续又将 B 修改成 A。



### ABA 问题影响

* 对于基础类型，ABA 问题是没有影响的。
* 对于引用类型，可能会造成 A 中对象属性发生改变，对后续逻辑造成影响。



### 如何解决 ABA 问题

* 每次修改值添加版本号，后续操作，连同版本号一并检查。
* AtomicStampedReference 解决 ABA 问题。



# 总结



# 参考文献

[1] [Java 并发编程实战](https://www.99baiduyun.com/baidu/Java并发编程实战)