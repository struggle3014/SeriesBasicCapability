<div align="center"><img src="https://gitee.com/struggle3014/picBed/raw/master/name_code.png"></div>

# 导读

本文是 JVM 面试专题。

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

对象的创建过程？

对象在内存中的内存布局？

对象头具体包括什么？

对象怎么定位？

对象怎么分配？

Object o = new Object() 在内存中占用多少字节？

* -XX:MaxTenuringThreshold控制的是什么？
  A: 对象升入老年代的年龄
  B: 老年代触发FGC时的内存垃圾比例

* 生产环境中，倾向于将最大堆内存和最小堆内存设置为：（为什么？）
  A: 相同 B：不同

* JDK1.8默认的垃圾回收器是：
  A: ParNew + CMS
  B: G1
  C: PS + ParallelOld
  D: 以上都不是

* 什么是响应时间优先？

* 什么是吞吐量优先？

* ParNew和PS的区别是什么？

* ParNew和ParallelOld的区别是什么？（年代不同，算法不同）

* 长时间计算的场景应该选择：A：停顿时间 B: 吞吐量

* 大规模电商网站应该选择：A：停顿时间 B: 吞吐量

* HotSpot的垃圾收集器最常用有哪些？

* 常见的HotSpot垃圾收集器组合有哪些？

* JDK1.7 1.8 1.9的默认垃圾回收器是什么？如何查看？

* 所谓调优，到底是在调什么？

* 如果采用PS + ParrallelOld组合，怎么做才能让系统基本不产生FGC

* 如果采用ParNew + CMS组合，怎样做才能够让系统基本不产生FGC
  * 加大JVM内存
  * 加大Young的比例
  * 提高Y-O的年龄
  * 提高S区比例
  * 避免代码内存泄漏

* G1是否分代？G1垃圾回收器会产生FGC吗？

* 如果G1产生FGC，你应该做什么？
  * 扩内存
  * 提高CPU性能（回收的快，业务逻辑产生对象的速度固定，垃圾回收越快，内存空间越大）
  * 降低MixedGC触发的阈值，让MixedGC提早发生（默认是45%）

* 生产环境中能够随随便便的dump吗？
  小堆影响不大，大堆会有服务暂停或卡顿（加live可以缓解），dump前会有FGC

* 常见的OOM问题有哪些？
  栈 堆 MethodArea 直接内存



# 总结



# 参考文献

