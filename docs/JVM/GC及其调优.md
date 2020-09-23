<div align="center"><img src="https://gitee.com/struggle3014/picBed/raw/master/name_code.png"></div>

# 导读

本文介绍 GC 及其调优相关的内容。

***持续更新中~***



# 目录

<nav>
<a href='#导读' style='text-decoration:none;font-weight:bolder'>导读</a><br/>
<a href='#目录' style='text-decoration:none;font-weight:bolder'>目录</a><br/>
<a href='#正文' style='text-decoration:none;font-weight:bolder'>正文</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#GC 基础知识' style='text-decoration:none;${border-style}'>GC 基础知识</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#1 什么是垃圾' style='text-decoration:none;${border-style}'>1 什么是垃圾</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#2 如何定位垃圾' style='text-decoration:none;${border-style}'>2 如何定位垃圾</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#3 常见垃圾回收算法' style='text-decoration:none;${border-style}'>3 常见垃圾回收算法</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#4 JVM 内存分代模型（用于分代垃圾回收算法）' style='text-decoration:none;${border-style}'>4 JVM 内存分代模型（用于分代垃圾回收算法）</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#5 常见垃圾回收器' style='text-decoration:none;${border-style}'>5 常见垃圾回收器</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#常见垃圾回收器组合参数设定' style='text-decoration:none;${border-style}'>常见垃圾回收器组合参数设定</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#JVM 常用命令行参数' style='text-decoration:none;${border-style}'>JVM 常用命令行参数</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#PS GC 日志详情' style='text-decoration:none;${border-style}'>PS GC 日志详情</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#调优前的基础概念' style='text-decoration:none;${border-style}'>调优前的基础概念</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#什么是调优' style='text-decoration:none;${border-style}'>什么是调优</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#调优从规划开始' style='text-decoration:none;${border-style}'>调优从规划开始</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#优化环境' style='text-decoration:none;${border-style}'>优化环境</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#解决 JVM 运行中的问题' style='text-decoration:none;${border-style}'>解决 JVM 运行中的问题</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#GC算法基础概念' style='text-decoration:none;${border-style}'>GC算法基础概念</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#CMS' style='text-decoration:none;${border-style}'>CMS</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#CMS 的问题' style='text-decoration:none;${border-style}'>CMS 的问题</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#CMS 日志分析' style='text-decoration:none;${border-style}'>CMS 日志分析</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#G1' style='text-decoration:none;${border-style}'>G1</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#G1 日志详解' style='text-decoration:none;${border-style}'>G1 日志详解</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#案例汇总' style='text-decoration:none;${border-style}'>案例汇总</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#GC 常用参数' style='text-decoration:none;${border-style}'>GC 常用参数</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#Parallel 常用参数' style='text-decoration:none;${border-style}'>Parallel 常用参数</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#CMS 常用参数' style='text-decoration:none;${border-style}'>CMS 常用参数</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#G1 常用超参数' style='text-decoration:none;${border-style}'>G1 常用超参数</a><br/>
<a href='#总结' style='text-decoration:none;font-weight:bolder'>总结</a><br/>
<a href='#参考文献' style='text-decoration:none;font-weight:bolder'>参考文献</a><br/>
</nav>

# 正文

## GC 基础知识

### 1 什么是垃圾

> * C 及 C++ 需要手动管理内存：
>
>   * C 申请及释放内存：malloc free
>   * C++ 申请及释放内存：new delete
>
> * Java 由垃圾回收器自动管理内存
>
>   编程上简单，系统不易出错。而手动释放内存，易出两类问题：
>
>   * 忘记回收
>   * 多次回收

没有任何引用指向的一个对象或对个对象（循环引用）

### 2 如何定位垃圾

* 引用计数（Reference Count）
* 根可达算法（Root Searching）

### 3 常见垃圾回收算法

* 标记清除（mark sweep）

  优点：简单

  缺点：位置不连续，产生碎片，效率偏低（两边扫描）

* 拷贝算法（copying）

  优点：没有碎片，速度快，效率高

  缺点：浪费空间

* 标记压缩（mark compact）

  优点：没有碎片

  缺点：效率偏低（两边扫描，指针需要调整）

### 4 JVM 内存分代模型（用于分代垃圾回收算法）

* 部分垃圾回收器使用的模型

  * 除 Epsilon，ZGC，Shenandoah 之外的 GC 都是使用逻辑分代模型
  * G1 是逻辑分代，物理部分带
  * 除此之外的垃圾回收器，不仅逻辑分代，而且物理分代

* 新生代+老年代+永久代（Perm Generation）【1.7】/元数据区（Metaspace）【1.8】

  * 永久代存放 Class 的元数据
  * 永久代必须制定大小限制；元数据区可设置，也可不设置，无上限（受限于物理内存）
  * 字符串常量，1.7 时位于永久代，1.8 时位于堆
  * 方法区（Method Area）是逻辑概念，而具体对应永久代（Perm Generation）和元数据区（Metaspace）

* 新生代 = Eden + 2 个 Survivor 区域

  * YGC 回收后，大多数对象会被回收，活着的对象进入 S0
  * 再次 YGC，活着的对象 Eden + S0 -> S1
  * 再次 YCC，活着的对象 Eden + S1 -> S0
  * 年龄足够 -> 老年代（15）
  * Survivor 区装不下 -> 老年代

* 老年代

  * 顽固分子
  * 老年代满了，触发 FGC（Full GC）

* GC Tuning（Generation）

  * 尽量减少 FCG
  * MinorGC = YGC（Yong GC）
  * MajorGC = FGC（Full GC）

* 对象分配过程

* 动态年龄（不重要）：https://www.jianshu.com/p/989d3b06a49d

* 分配担保（不重要）：

  YGC 期间 Survivor 区空间不够，空间担保直接进入老年代

  https://cloud.tencent.com/developer/article/1082730

### 5 常见垃圾回收器

![垃圾回收器](https://gitee.com/struggle3014/picBed/raw/master/垃圾回收器.png)

<div align="center"><font size="2">垃圾回收器</font></div>

* JDK 诞生，Serial 追随。为了提高效率，诞生了 PS，为了配合 CMS，诞生了 PN，CMS 是 1.4 版本后期引入，CMS 是里程碑式的 GC，开启了并发回收的过程，但是 CMS 毛病较多，因此目前任何一个 JDK 默认是 CMS。并发垃圾回收是因为无法忍受 STW。

* Serial 年轻代，串行回收

* PS 年轻代，并行回收

* ParNew 年轻代，配合 CMS 并行回收

* SerialOld，ParallelOld

* ConcurrentMarkSweep 即 CMS 老年代并发的，垃圾回收和应用程序同时运行，降低 STW 的时间（200ms），CMS 问题比较多，所以目前没有一个版本默认是 CMS，只能手工指定。CMS 既然是 MarkSweep，就一定会有碎片化的问题，碎片到达一定程度，CMS 的老年代分配对象分配不下时，使用 SerialOld 进行老年代回收。

  想象下：PS + PO -> 加内存，换垃圾回收器 -> PN + CMS + SerialOld（几个小时-几天的 STW）。

  几十个G的内存，单线程回收 -> G1 + FGC  几十个G ->  上T内存的服务器 ZGC

* G1（10ms）

  算法：三色标记 + SATB

* ZGC（1ms）

  算法：Colored Pointers + LoadBarrier

* Shenadoah

  算法：Colored Pointers + WriteBarrier

* Eplison

* [PS 和 PN 区别的衍生阅读](https://docs.oracle.com/en/java/javase/13/gctuning/ergonomics.html)

* 垃圾收集器和内存大小的关系

  * Serial	几十M
  * PS	上百M - 几个G
  * CMS	20G
  * G1	上百G
  * ZGC	4T-16T

* JDK1.8 默认的垃圾回收：PS + PO

## 常见垃圾回收器组合参数设定

* -XX:+UseSerialGC = Serial New(DefNew) + Serial Old
* -XX:+UseParNewGC = ParNew + SerialOld
  * 该组合很少使用（在某些版本中已经废弃）
  * https://stackoverflow.com/questions/34962257/why-remove-support-for-parnewserialold-anddefnewcms-in-the-future
* -XX:+UseCon<font color="red">(urrent)</font>MarkSweepGC = ParNew + CMS + Serial Old
* -XX:+UseParallelGC = Parallel Scavenge + Parallel Old（1.8 默认）
* -XX:+UseParallelOldGC = Parallel Scavenge + Parallel Old
* -XX:+UseG1GC = G1
* Linux 中没有找到默认 GC 的查看方法，而 Windows 中会打印 UseParallelGC
  * java -XX:+PrintCommandLineFlags -version
  * 通过 GC 日志来分辨
* Linux 下 1.8 默认版本的垃圾回收器到底是什么？
  * 1.8.0_181 默认（看不出来）Copy MarkCompact
  * 1.8.0_222 默认 PS+PO

## JVM 常用命令行参数

* [Oracle JVM 命令行参数](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/java.html)

* HotSpt 参数分类

  * 标准： - 开头，所有的 HotSpot 都支持
  * 非标准：-X 开头，特定版本的 HotSpot 支持特定命令
  * 不稳定：-XX 开头，下个版本可能取消

  试验程序：

  ```java
  import java.util.List;
  import java.util.LinkedList;
  
  public class HelloGC {
      public static void main(String[] args) {
          System.out.println("HelloGC!");
          List list = new LinkedList();
          for(;;) {
              byte[] b = new byte[1024*1024];
              list.add(b);
          }
      }
  }
  ```

  * 区分概念：内存泄漏 memory leak，内存溢出 out of memory
  * java -XX:+PrintCommandLineFlags HelloGC
  * java -Xmn 10M -Xms40M -Xmx 60M -XX:+PrintCommandLineFlags -XX:+PrintGC HelloGC
  * java -XX:+UseConcMarkSweepGC -XX:+PrintCommandLineFlags HelloGC
  * java -XX:+PrintFlagsInitial 默认参数值
  * java -XX:+PrintFlagsFinal 最终参数值
  * java -XX:+PrintFlagsFinal | grep xxx 找到对应的参数
  * java -XX:+PrintFlagsFinal -version | grep GC

## PS GC 日志详情

每种垃圾回收器的日志格式是不同的~！

PS 日志格式

![GC日志详解](https://gitee.com/struggle3014/picBed/raw/master/GC日志详解.png)

heap dump 部分：

```java
eden space 5632K, 94% used [0x00000000ff980000,0x00000000ffeb3e28,0x00000000fff00000]
内存地址：起始地址，使用空间结束地址，整体空间结束地址
```

![GCHeapDump](https://gitee.com/struggle3014/picBed/raw/master/GCHeapDump.png)

total = eden + 1 个 survivor

## 调优前的基础概念

* 吞吐量：用户代码时间 / （用户代码执行时间 + 垃圾回收时间）
* 响应时间：STW 越短越好，响应时间越好

所谓调优，首先确定，追求啥？吞吐量优先，还是响应时间优先？还是在满足一定的响应时间的情况下，要求达到多大的吞吐量...

* 吞吐量优先
  * 场景
    * 科学计算
    * 数据挖掘
  * 垃圾收集器选择
    * PS + PO
* 响应时间优先
  * 场景
    * 网站
    * GUI
    * API
  * 垃圾收集器选择
    * G1（1.8）

## 什么是调优

* 根据需求进行 JVM 规划和预调优
* 优化运行 JVM 运行环境（慢，卡顿）
* 解决 JVM 运行过程中出现的各种问题（OOM）

### 调优从规划开始

#### 调优准则

调优，从业务场景开始，没有业务场景的调优都是耍流氓。

无监控（压力测试，能看到结果），不调优。

#### 调优步骤

1，熟悉业务场景（没有最好的垃圾回收器，只有最合适的垃圾回收器）

* 响应时间、停顿时间（需要给用户作响应）
* 吞吐量 = 用户时间 / （用户时间 + GC 时间）

2，选择回收器组合

3，计算内存需求（经验值 1.5G 16G）

4，选定 CPU（越高越好）

5，设定年龄大小，升级年龄

6，设定日志参数

* -Xloggc:/opt/xxx/logs/xxx-xxx-gc-%t.og -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=20M -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause
* 或每天产生一个日志文件

7，观察日志情况

#### 调优案例

* 垂直电商，最高每日百万订单，处理订单系统需要什么样的服务器配置？

  > 这个问题比较业余，因为很多不同的服务器配置都能支撑（1.5G 16G）
  >
  > 1小时3600000集中时间段，100订单/秒，（找到一个小时内的高峰期，1000订单/秒）
  >
  > 经验值
  >
  > 非要计算：一个订单产生需要多少内存？512K*1000=5000M内存
  >
  > 专业问法：要求响应时间 100ms？压测

* 12306 遭遇春节大规模抢票应该如何支撑？

  > 12306 应该是中国并发量最大的秒杀网站：号称并发量最高 100W
  >
  > CDN -> LVS -> NGINX -> 业务系统 -> 每台机器 1W 并发，100台机器
  >
  > 普通电商订单：下单 -> 订单系统（IO）减库存 -> 等待用户付款
  >
  > 12306 可能的模式：下单 -> 减库存和订单同时异步进行 -> 等待付款
  >
  > 上述还是会存现如下问题，即减库存最后还是把压力压到一台服务器
  >
  > 可以做分布式本地库存 + 单独服务器做库存负载均衡
  >
  > 综上：大流量的处理方法为<font color="red">分而治之</font>

* 怎么得到一个事务会消耗多少内存？

  > * 弄台机器，看看能承受多少 TPS？是不是达到目标？扩容或调优，让其达到
  > * 用压测来确定

### 优化环境

* 有一个50万PV的资料类网站（从磁盘提取文档到内存），原服务器32位，1.5G的堆，用户反馈网站比较缓慢，因此公司决定升级，新的服务器为64位，16G堆内存，结果用户反馈卡顿十分严重，反而比以前的效率更低了！~

  * 为什么原网站慢？

    很多用户浏览数据，很多数据 load 到内存，内存不足，频繁 GC，STW 长，响应时间变慢

  * 内存扩容后，为什么会更卡顿？

    内存越大，FGC 时间越长

  * 如何解决？

    PS -> PN + CMS 或者 G1

* **系统 CPU 经常 100%，如何调优？（面试高频）**

  CPU 100% 那么一定有线程在占用系统资源

  1，找出那个线程 CPU 高（top）

  2，该进程中的哪个线程 CPU 高（top -Hp）

  3，导出该线程的堆栈（jstack）

  4，查找哪个方法（栈帧）消耗时间（jstack）

  5，工作线程占比高 or 垃圾回收线程占比高

* 系统内存飙高，如何查找问题？（面试高频）

  1，导出堆内存（jmap）

  2，分析（jhat jvisualvm mat jprofiler ...）

* 如何监控 JVM

  jstat jvisual jprofiler arthas top

### 解决 JVM 运行中的问题

#### 通过案例理解常用工具

1，测试代码：

```java
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FULLGC_Problem01 {
    private static class CardInfo {
        BigDecimal price = new BigDecimal(0.0);
        String name = "张三";
        int age = 5;
        Date birthdate = new Date();
        
        public void m() {}
    }
    
    private static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(50, new ThreadPoolExecutor.DiscardOldestPolicy());

    public static void main(String[] args) throws Exception {
        executor.setMaximumPoolSize(50);
        for(;;) {
            modelFit();
            Thread.sleep(100);
        }
    }
    
    private static void modelFit() {
        List<CardInfo> taskList = getAllCardInfo();
        taskList.forEach(info -> {
            // do something
            executor.scheduleWithFixedDelay(() -> {
                // do sth with info
                info.m();
            }, 2, 3, TimeUnit.SECONDS);
        });
    }
    
    private static List<CardInfo> getAllCardInfo() {
        List<CardInfo> taskList = new ArrayList<>();
        for(int i=0; i<100; i++) {
            CardInfo ci = new CardInfo();
            taskList.add(ci);
        }
        return taskList;
    }
}
```

2，运行上述脚本：java -Xms20M -Xmx20M -XX:+PrintGC com.xiumei.jvm.gc.FullGC_Problem01

3，运维图案段首先收到报警信息（CPU Memory）

4，top 命令观察到问题：内存不断增长，CPU 占用率居高不下

5，top -Hp 观察进程中的线程，哪个线程 CPU 和内存占比高

6，jsp 定位具体 java 进程

​	jstack 定位线程状态，重点关注 WAITING BLOCKED 等。假设一个进程中100个线程，很多线程都在 waiting on <xx>，一定要找到是哪个线程持有这把锁。

​	如何查找？搜索 jstack dump 的信息，找 <xx>，看看哪个线程持有这把锁 RUNNABLE

7，为什么阿里规范里定义线程的名称（尤其是线程池）都要有意义的名称

​	怎么样自定义线程池里线程名称？自定义 ThreadFactory

8，jinfo pid

9，jstack -gc 动态观察 gc 情况；阅读 GC 日志发现频繁 GC；arthas 观察； jconsole； jvisualVM； jprofiler

​	jstat -gc 4655 500：每隔500毫秒打印 GC 的情况。

​	若面试官问你是如何定位 OOM 问题的？若回答用图形界面（错误）

​	1）已经上线的系统不用图形界面用什么？CmdLine arthas

​	2）图形界面到底用在什么地方？测试，测试时进行监控~！压测观察

10，jmap -histo 4655 | head -20，查找有多少对象产生

11，jmap -dump:format=b,file=xxx pid

​	线上系统，内存特别大，jmap 执行期间会对进程产生很大影响，设置卡顿（电商不合适）

​	1）设定参数 HeapDump，OOM 时会自动产生堆转储文件

​	2）<font color="red">很多服务器是高可用，停掉某台服务器对其他服务器不影响</font>

​	3）在线定位（一般小公司用不到）

12，java -Xms20M -Xmx20M -XX:+UseParallelGC -XX:+HeadpDumpOnOutOfMemoryError com.xiumei.jvm.gc.FullGC_Problem01

13，使用 MAT，jhat，jvisualvm 进行 dump 文件分析

​	https://www.cnblogs.com/baihuitestsoftware/articles/6406271.html

​	jhat -J-mx512M xxx.dump

​	http://192.168.17.11:7000

​	可使用 OQL 查找特定问题对象

14，找到代码问题

#### jconsole 远程连接

1，程序启动加入参数

```shell
java -Djava.rmi.server.hostname=192.168.17.11 -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=1111 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false XXX
```

2，如果遭遇 Local host name unkown：XXX 的错误，修改 /etc/hosts 文件，把 XXX 加入进去

```shell
192.168.17.11 basic localhost
localhost.localdomain localhost4
localhost4.localdomain4
::1 localhost
localhost.localdomain.localhost6
localhost6.localdomain6
```

3，关闭 linux 防火墙（实战中应该打开对应端口）

```shell
service iptables stop
chkconfig iptables off # 永久关闭
```

4，windows 上打开 jconsole 远程连接，即 192.168.17.11:1111

#### jvisualvm 远程连接

简单做法： https://www.cnblogs.com/liugh/p/7620336.html

#### jprofiler（收费）

#### arthas 在线排查工具

* 为什么需要在线排查？

* jvm 观察 jvm 信息

* thread 定位线程问题

* dashborad 观察系统情况

* heapdump + jhat 分析

* jad 反编译

  * 动态代理生成类的问题定位
  * 第三方的类（观察源码）
  * 版本问题（确定自己最新提交的版本是不是被使用）

* redefine 热替换

  目前有些限制：只能改方法实现（方法），不能该方法名称，不能该改属性。

* sc -> search class

* watch -> watch method

* 没有包含的功能：jmap

## GC算法基础概念

Card Table

由于做 YGC 时，如果扫描整个 OLD 区，效率非常低，所以 JVM 设计了 CardTable，如果一个 Old 区 CardTable 中有对象指向 Y 区，就将它设为 Dirty，下次扫描时，只需要扫描 Dirty Card

在结构上，Card Table 用 BitMap 实现

## CMS

### CMS 的问题

1，Memory Fragmentation（内存碎片）

> -XX:+UseCMSCompactAtFullCollection
>
> -XX:+CMSFullGCsBeeforeCompaction 默认为0，指经过多少次 FGC 才进行压缩

2，Floating Garbage（浮动垃圾）

> Concurrent Mode Failure
>
> 产生：if the concurrent collector is unable to finish reclaiming the unreachable objects before the tenured generation fills up, or if an allocation cannot be satisfied with the available free space blocks in the tenured generation, then the application is paused and the collection is completed with all the application threads stopped.
>
> * 老年代在用完之前不能完成对无引用对象的回收；
> * 当新空间分配请求在老年代的剩余空间中得到满足。
>
> 解决方案：降低触发 CMS 的阈值
>
> Promotion Failed
>
> 解决方案类似，保持老年代有足够的空间
>
> -XX:+CMSInitialingOccupancyFraction 92%，可以降低这个值，让 CMS 保持老年代足够空间

CMS 问题具体分析参考：https://blog.csdn.net/weixin_43752854/article/details/104776134

### CMS 日志分析

执行命令：java -Xms20M -Xmx20M -XX:+PrintGCDetails -XX:+UseConcMarkSweepGC com.xiumei.jvm.gc.FullGC_Problem01

[GC (Allocation Failure) [ParNew: 6144K->640K(6144K), 0.0265885 secs] 6585K->2770K(19840K), 0.0268035 secs] [Times: user=0.02 sys=0.00, real=0.02 secs]

> ParNew：年轻代收集器
>
> 6144K->640K：收集前后的对比
>
> (6144K)：整个年轻代容量
>
> 6585K->2770K：整个堆的情况
>
> (19840K)：整个堆大小

```java
[GC (CMS Initial Mark) [1 CMS-initial-mark: 8511K(13696K)] 9866K(19840K), 0.0040321 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
//8511 (13696) : 老年代使用（最大）
//9866 (19840) : 整个堆使用（最大）
[CMS-concurrent-mark-start]
[CMS-concurrent-mark: 0.018/0.018 secs] [Times: user=0.01 sys=0.00, real=0.02 secs] 
//这里的时间意义不大，因为是并发执行
[CMS-concurrent-preclean-start]
[CMS-concurrent-preclean: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
//标记Card为Dirty，也称为Card Marking
[GC (CMS Final Remark) [YG occupancy: 1597 K (6144 K)][Rescan (parallel) , 0.0008396 secs][weak refs processing, 0.0000138 secs][class unloading, 0.0005404 secs][scrub symbol table, 0.0006169 secs][scrub string table, 0.0004903 secs][1 CMS-remark: 8511K(13696K)] 10108K(19840K), 0.0039567 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
//STW阶段，YG occupancy:年轻代占用及容量
//[Rescan (parallel)：STW下的存活对象标记
//weak refs processing: 弱引用处理
//class unloading: 卸载用不到的class
//scrub symbol(string) table: 
	//cleaning up symbol and string tables which hold class-level metadata and 
	//internalized string respectively
//CMS-remark: 8511K(13696K): 阶段过后的老年代占用及容量
//10108K(19840K): 阶段过后的堆占用及容量

[CMS-concurrent-sweep-start]
[CMS-concurrent-sweep: 0.005/0.005 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
//标记已经完成，进行并发清理
[CMS-concurrent-reset-start]
[CMS-concurrent-reset: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
//重置内部结构，为下次GC做准备
```

## G1

[Oracle G1 GC](https://www.oracle.com/technical-resources/articles/java/g1gc.html)

### G1 日志详解

```java
[GC pause (G1 Evacuation Pause) (young) (initial-mark), 0.0015790 secs]
//young -> 年轻代 Evacuation-> 复制存活对象 
//initial-mark 混合回收的阶段，这里是YGC混合老年代回收
   [Parallel Time: 1.5 ms, GC Workers: 1] //一个GC线程
      [GC Worker Start (ms):  92635.7]
      [Ext Root Scanning (ms):  1.1]
      [Update RS (ms):  0.0]
         [Processed Buffers:  1]
      [Scan RS (ms):  0.0]
      [Code Root Scanning (ms):  0.0]
      [Object Copy (ms):  0.1]
      [Termination (ms):  0.0]
         [Termination Attempts:  1]
      [GC Worker Other (ms):  0.0]
      [GC Worker Total (ms):  1.2]
      [GC Worker End (ms):  92636.9]
   [Code Root Fixup: 0.0 ms]
   [Code Root Purge: 0.0 ms]
   [Clear CT: 0.0 ms]
   [Other: 0.1 ms]
      [Choose CSet: 0.0 ms]
      [Ref Proc: 0.0 ms]
      [Ref Enq: 0.0 ms]
      [Redirty Cards: 0.0 ms]
      [Humongous Register: 0.0 ms]
      [Humongous Reclaim: 0.0 ms]
      [Free CSet: 0.0 ms]
   [Eden: 0.0B(1024.0K)->0.0B(1024.0K) Survivors: 0.0B->0.0B Heap: 18.8M(20.0M)->18.8M(20.0M)]
 [Times: user=0.00 sys=0.00, real=0.00 secs] 
//以下是混合回收其他阶段
[GC concurrent-root-region-scan-start]
[GC concurrent-root-region-scan-end, 0.0000078 secs]
[GC concurrent-mark-start]
//无法evacuation，进行FGC
[Full GC (Allocation Failure)  18M->18M(20M), 0.0719656 secs]
   [Eden: 0.0B(1024.0K)->0.0B(1024.0K) Survivors: 0.0B->0.0B Heap: 18.8M(20.0M)->18.8M(20.0M)], [Metaspace: 38
76K->3876K(1056768K)] [Times: user=0.07 sys=0.00, real=0.07 secs]
```



## 案例汇总

OOM 产生的原因多种多样，有些程序未必产生 OOM，不断 FGC（CPU 飙高，但内存回收特别少）（上面案例）

* 硬件升级系统反而卡顿的问题（见上）

* 线程池不当运用产生 OOM 问题（见上）

  不断地往 List 里加对象
  
* smile jira 问题

  * 现象：实际系统不断重启
  * 解决方案：扩充内存+垃圾回收器更换为 G1
  * 原因：未知

* tomcat http-header-size 过大问题（Hector）

* lambda 表达式导致方法区溢出问题

* 直接内存溢出问题（少见）

  《深入理解 Java 虚拟机》P59，使用 Unsafe 分配直接内存，或使用 NIO 问题

* 栈溢出问题

  -Xss 设定太小

* 比较两段代码的异常，分析哪个是更有的写法

  ```java
  // 片段1
  Object o = null;
  for(int i=0; i<100; i++) {
      o = new Object();
      // 业务处理
  }
  
  // 片段2
  for(int i=0; i<100; i++) {
      Object o = new Object();
      
  }
  ```

* 重写 finalize 引发频繁 GC

  小米云，HBase 同步系统，系统通过 nginx 访问超时报警，最后排查，C++ 程序员重写 finalize 引发频繁 GC 问题。

* 如果有一个系统，内存一直消耗不超过 10%，但是观察 GC 日志，发现 FGC 总是频繁发生，是什么引起的？

  System.gc()

* Distuptor 有个可以设置链的长度，如果过大，然后对象大，消费完不主动释放，会溢出。

* new 大量线程，会产生 native thread OOM，应该使用线程池方案

## GC 常用参数

* -Xmn -Xms -Xmx -Xss

  年轻代，最小堆，最大堆，栈空间

* -XX:+UseTLAB

  使用 TLAB，默认打开

* -XX:+PrintTLAB

  打印 TLAB 的使用情况

* -XX:TLABSize

  设置 TLAB 大小

* -XX:+DisableExpllictGC

  System.gc() 不管用，FGC

* -XX:+PrintGC

* -XX:+PrintGCDetails

* -XX:+PrintHeapAtGC

* -XX:+PrintGCTimeStamps

* -XX:+PrintGCApplicationConcurrentTime（低）

  打印应用程序时间

* -XX:+PrintGCApplicationStoppedTime（低）

  打印暂停时长

* -XX:+PrintReferenceGC（重要性低）

  记录回收了多少种不同引用类型的引用

* -verbose:class

  类加载详细过程

* -XX:+PrintVMOptions

* -XX:+PrintFlagsFinal -XX:+PrintFlagsInitial（重要）

* -Xloggc:opt/log/gc.log

* -XX:MaxTenuringThreadshold

  升代年龄，最大值 15

* 锁自旋次数 -XX:PreBlockSpin 热点代码检测参数 -XX:CompileThreadshold 逃逸分析 标量替换... 不建议设置

## Parallel 常用参数

* -XX:SurvivorRatio

* -XX:PreTenureSizeThreadshold

  大对象到底多大

* -XX:MaxTenuringThreadshold

* -XX:+ParallelGCThreads

  并行收集器的线程数，同样适用于 CMS，一般认为和 CPU 核数相同

* -XX:+UseAdaptiveSizePolicy

  自动选择各区大小比例

## CMS 常用参数

* -XX:+UseConcMarkSweepGC

* -XX:ParallelCMSThreads

  CMS 线程数量

* -XX:CMSInitiatingOccupancyFraction

  使用多少比例的老年代后开始 CMS 收集，默认是 68%（近似值），如果频繁发生 SerialOld 卡顿，应该调小（频繁 CMS 回收）

* -XX:+UseCMSCompactAtFullCollection

  在 FGC 时进行压缩

* -XX:CMSFullGCBeforeCompaction

  多少次 FGC 之后进行压缩

* -XX:+CMSClassUnloadingEnabled

* -XX:CMSInitiatingPermOccupancyFraction

  达到什么比例进行 Perm 回收

* GCTimeRatio

  设置 GC 时间占用程序运行时间的百分比

* -XX:MaxGCPauseMills

  停顿时间，是一个建议时间，GC 会尝试用各种手段达到这个时间，比如减少年轻代

## G1 常用超参数

* -XX:+UseG1GC

* -XX:MaxGCPauseMillis

  建议值，G1 会尝试调整 Young 区的块数来达到这个值

* -XX:GCPauseIntervalMills

  GC 的间隔时间

* -XX:+G1HeapRegionSize

  分区大小，主键增加该值，1 2 4 8 16 32

  随着 size 增加，垃圾的存活时间更长，GC 间隔更长，但每次 GC 的时间也会更长

  ZGC 做了改进（动态区块大小）

* G1NewSizePercent

  新生代最小比例，默认为 5%

* G1MaxNewSizePercent

  新生代最大比例，默认为 60%

* GCTimeRatio

  GC 时间建议比例，G1 会根据这个值调整堆空间

* ConcGCThreads

  线程数量

* InitiatingHeapOccupancyPercent

  启动 G1 的堆空间占用比例



# 总结



# 参考文献

[1] [HBase GC 的前世今生，范欣欣，个人网站](http://hbasefly.com/2016/05/21/hbase-gc-1/)

[2] [HBase 在小米的最佳实践，G1 GC](http://openinx.github.io/ppt/hbaseconasia2017_paper_18.pdf)