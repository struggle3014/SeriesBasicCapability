

<div align="center"><img src="https://gitee.com/struggle3014/picBed/raw/master/name_code.png"></div>

# 导读

本文简述 **Synchronized** 关键字。

***持续更新中~***



# 目录

<nav>
<a href='#导读' style='text-decoration:none;font-weight:bolder'>导读</a><br/>
<a href='#目录' style='text-decoration:none;font-weight:bolder'>目录</a><br/>
<a href='#正文' style='text-decoration:none;font-weight:bolder'>正文</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#1 前置知识' style='text-decoration:none;${border-style}'>1 前置知识</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#1.1 用户态和内核态' style='text-decoration:none;${border-style}'>1.1 用户态和内核态</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#1.2 CAS' style='text-decoration:none;${border-style}'>1.2 CAS</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#1.3 Unsafe' style='text-decoration:none;${border-style}'>1.3 Unsafe</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#1.4 Markword' style='text-decoration:none;${border-style}'>1.4 Markword</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#1.5 JOL 工具(Java Object Layout)' style='text-decoration:none;${border-style}'>1.5 JOL 工具(Java Object Layout)</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#2 Synchronzied 橫切面讲解' style='text-decoration:none;${border-style}'>2 Synchronzied 橫切面讲解</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#2.1 Java 源码层级' style='text-decoration:none;${border-style}'>2.1 Java 源码层级</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#2.2 Java 字节码层级' style='text-decoration:none;${border-style}'>2.2 Java 字节码层级</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#2.3 JVM 层级（Hotspot）' style='text-decoration:none;${border-style}'>2.3 JVM 层级（Hotspot）</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#2.4 操作系统和硬件层级' style='text-decoration:none;${border-style}'>2.4 操作系统和硬件层级</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#3 锁升级过程' style='text-decoration:none;${border-style}'>3 锁升级过程</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#3.1 JDK8 markword 实现表' style='text-decoration:none;${border-style}'>3.1 JDK8 markword 实现表</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#3.2 锁升级' style='text-decoration:none;${border-style}'>3.2 锁升级</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#3.3 锁重入' style='text-decoration:none;${border-style}'>3.3 锁重入</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#3.4 Synchronized VS Lock(CAS)' style='text-decoration:none;${border-style}'>3.4 Synchronized VS Lock(CAS)</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#4 锁消除(lock eliminate)' style='text-decoration:none;${border-style}'>4 锁消除(lock eliminate)</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#5 锁粗化(lock coarsening)' style='text-decoration:none;${border-style}'>5 锁粗化(lock coarsening)</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#6 锁降级' style='text-decoration:none;${border-style}'>6 锁降级</a><br/>
<a href='#总结' style='text-decoration:none;font-weight:bolder'>总结</a><br/>
<a href='#参考文献' style='text-decoration:none;font-weight:bolder'>参考文献</a><br/>
</nav>

# 正文

## 1 前置知识

### 1.1 用户态和内核态

JDK 早起，synchronized 叫做重量级锁，因为申请锁资源必须通过 kernel，系统调用。

```asm
;hello.asm
;write(int fd, const void *buffer, size_t nbytes)

section data
	msg db "Hello", 0xA
	len equ $ - msg

section .text
global _start
_start:
	
	mov edx, len
	mov ecs, msg
	mov ebx, 1 ;文件描述符1 std_out
	mov eax, 4 ;write 函数系统调用号 4
	int 0x80
	
	mov ebx, 0
	mov eax, 1 ;exit 函数系统调用号
	int 0x80
```



### 1.2 CAS

![cas](https://gitee.com/struggle3014/picBed/raw/master/cas.png)

<div align="center"><font size="2">CAS 内部实现</font></div>

CAS 即 Compare And Swap（Compare And Exchange / Compare And Set） / 自旋 / 自旋锁 / 无锁（无重量锁）。因为经常配合循环操作，直到完成为止，所以泛指一类操作。

cas(v, a, b)  变量 v，期待值 a，修改值 b。



#### 1.2.1 自旋

自旋就是一直在空转等待，一直等到 CAS 操作成功为止。



#### 1.2.2 ABA 问题

形象化解释：你的女朋友离开你的时间中经历了别人。

如何解决：版本号 AtomicStampedReference，基础类型简单值不需要版本号。



### 1.3 Unsafe

跟踪 CAS 调用链，我们以 JDK 1.8 的 AtomicInteger 的 incrementAndGet 方法为例，进行跟踪：

Step1：java.util.concurrent.atomic.AtomicInteger 的 incrementAndGet 方法

```java
/**
 * Atomically increments by one the current value.
 *
 * @return the updated value
 */
public final int incrementAndGet() {
    return unsafe.getAndAddInt(this, valueOffset, 1) + 1;
}
```

Step2：sun.misc.Unsafe 的 getAndAddInt 方法

```java
public final int getAndAddInt(Object var1, long var2, int var4) {
    int var5;
    do {
        var5 = this.getIntVolatile(var1, var2);
    } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));

    return var5;
}
```

Step3：sun.misc.Unsafe 的 compareAndSwapInt 方法

```java
public final native boolean compareAndSwapInt(Object var1, long var2, int var4, int var5);
```

Step4：unsafe.cpp（jdk8u）中的 cmpxchg，即 compare and exchange。

```c++
UNSAFE_ENTRY(jboolean, Unsafe_CompareAndSwapInt(JNIEnv *env, jobject unsafe, jobject obj, jlong offset, jint e, jint x))
  UnsafeWrapper("Unsafe_CompareAndSwapInt");
  oop p = JNIHandles::resolve(obj);
  jint* addr = (jint *) index_oop_from_field_offset_long(p, offset);
  return (jint)(Atomic::cmpxchg(x, addr, e)) == e;
UNSAFE_END
```

Step5：atomic_linux_x86.inline.hpp（jdk8u）is_MP = Multi Processor

```c++
inline jint     Atomic::cmpxchg    (jint     exchange_value, volatile jint*     dest, jint     compare_value) {
  int mp = os::is_MP();
  __asm__ volatile (LOCK_IF_MP(%4) "cmpxchgl %1,(%3)"
                    : "=a" (exchange_value)
                    : "r" (exchange_value), "a" (compare_value), "r" (dest), "r" (mp)
                    : "cc", "memory");
  return exchange_value;
}
```

Step6：os.hpp（jdk8u）is_MP()

```c++
static inline bool is_MP() {
    // During bootstrap if _processor_count is not yet initialized
    // we claim to be MP as that is safest. If any platform has a
    // stub generator that might be triggered in this phase and for
    // which being declared MP when in fact not, is a problem - then
    // the bootstrap routine for the stub generator needs to check
    // the processor count directly and leave the bootstrap routine
    // in place until called after initialization has ocurred.
    return (_processor_count != 1) || AssumeMP;
}
```

Step7：atomic_linux_x86.inline.hpp

```c++
#define LOCK_IF_MP(mp) "cmp $0, " #mp "; je 1f; lock; 1: "
```

Step8：最终实现： cmpxchg = cas 修改变量值

```assembly
lock cmpxchg 指令
```

Step9：lock 指令在执行后面指令的时候锁定一个北桥信号，不采用锁总线的方式。



### 1.4 Markword

详见 JVM 中的 Java 对象内存布局。



### 1.5 JOL 工具(Java Object Layout)

JOL 可以查看 Java 对象内存布局。

```xml
<dependencies>
    <!-- https://mvnrepository.com/artifact/org.openjdk.jol/jol-core -->
    <dependency>
        <groupId>org.openjdk.jol</groupId>
        <artifactId>jol-core</artifactId>
        <version>0.9</version>
    </dependency>
</dependencies>
```

markOop.hpp（jdk8u）

```c++
// Bit-format of an object header (most significant first, big endian layout below):
//
//  32 bits:
//  --------
//             hash:25 ------------>| age:4    biased_lock:1 lock:2 (normal object)
//             JavaThread*:23 epoch:2 age:4    biased_lock:1 lock:2 (biased object)
//             size:32 ------------------------------------------>| (CMS free block)
//             PromotedObject*:29 ---------->| promo_bits:3 ----->| (CMS promoted object)
//
//  64 bits:
//  --------
//  unused:25 hash:31 -->| unused:1   age:4    biased_lock:1 lock:2 (normal object)
//  JavaThread*:54 epoch:2 unused:1   age:4    biased_lock:1 lock:2 (biased object)
//  PromotedObject*:61 --------------------->| promo_bits:3 ----->| (CMS promoted object)
//  size:64 ----------------------------------------------------->| (CMS free block)
//
//  unused:25 hash:31 -->| cms_free:1 age:4    biased_lock:1 lock:2 (COOPs && normal object)
//  JavaThread*:54 epoch:2 cms_free:1 age:4    biased_lock:1 lock:2 (COOPs && biased object)
//  narrowOop:32 unused:24 cms_free:1 unused:4 promo_bits:3 ----->| (COOPs && CMS promoted object)
//  unused:21 size:35 -->| cms_free:1 unused:7 ------------------>| (COOPs && CMS free block)
```



## 2 Synchronzied 橫切面讲解

### 2.1 Java 源码层级

```java
/**
 * synchronized 底层实现
 */
package com.xiumei.multithreadinghighconcurrency.basicconcept.sync;

import org.openjdk.jol.info.ClassLayout;

public class T02_Sync1 {

    public static void main(String[] args) {
        Object o = new Object();
        synchronized (o) {
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
        }
    }

}
```



### 2.2 Java 字节码层级

* synchronized 修饰方法：ACC_SYNCHRONIZED 访问修饰符

* synchronized 同步语句块：monitorenter monitorexit

```asm
 0 new #2 <java/lang/Object>
 3 dup
 4 invokespecial #1 <java/lang/Object.<init>>
 7 astore_1
 8 getstatic #3 <java/lang/System.out>
11 aload_1
12 invokestatic #4 <org/openjdk/jol/info/ClassLayout.parseInstance>
15 invokevirtual #5 <org/openjdk/jol/info/ClassLayout.toPrintable>
18 invokevirtual #6 <java/io/PrintStream.println>
21 aload_1
22 dup
23 astore_2
24 monitorenter
25 getstatic #3 <java/lang/System.out>
28 aload_1
29 invokestatic #4 <org/openjdk/jol/info/ClassLayout.parseInstance>
32 invokevirtual #5 <org/openjdk/jol/info/ClassLayout.toPrintable>
35 invokevirtual #6 <java/io/PrintStream.println>
38 aload_2
39 monitorexit
40 goto 48 (+8)
43 astore_3
44 aload_2
45 monitorexit
46 aload_3
47 athrow
48 return
```



### 2.3 JVM 层级（Hotspot）

C++ 调用了操作系统提供的同步机制。

Java 对象内存布局：

```java
java.lang.Object object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           c8 f7 70 02 (11001000 11110111 01110000 00000010) (40957896)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           e5 01 00 20 (11100101 00000001 00000000 00100000) (536871397)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
```



interpreterRuntime.cpp 的 InterpreterRuntime:: monitorenter 方法

```c++
IRT_ENTRY_NO_ASYNC(void, InterpreterRuntime::monitorenter(JavaThread* thread, BasicObjectLock* elem))
#ifdef ASSERT
  thread->last_frame().interpreter_frame_verify_monitor(elem);
#endif
  if (PrintBiasedLockingStatistics) {
    Atomic::inc(BiasedLocking::slow_path_entry_count_addr());
  }
  Handle h_obj(thread, elem->obj());
  assert(Universe::heap()->is_in_reserved_or_null(h_obj()),
         "must be NULL or an object");
  if (UseBiasedLocking) {
    // Retry fast entry if bias is revoked to avoid unnecessary inflation
    ObjectSynchronizer::fast_enter(h_obj, elem->lock(), true, CHECK);
  } else {
    ObjectSynchronizer::slow_enter(h_obj, elem->lock(), CHECK);
  }
  assert(Universe::heap()->is_in_reserved_or_null(elem->obj()),
         "must be NULL or an object");
#ifdef ASSERT
  thread->last_frame().interpreter_frame_verify_monitor(elem);
#endif
IRT_END
```



synchronizer.cpp 的 revoke_and_rebias fast_enter slow_enter 方法

```c++
void ObjectSynchronizer::fast_enter(Handle obj, BasicLock* lock, bool attempt_rebias, TRAPS) {
 if (UseBiasedLocking) {
    if (!SafepointSynchronize::is_at_safepoint()) {
      BiasedLocking::Condition cond = BiasedLocking::revoke_and_rebias(obj, attempt_rebias, THREAD);
      if (cond == BiasedLocking::BIAS_REVOKED_AND_REBIASED) {
        return;
      }
    } else {
      assert(!attempt_rebias, "can not rebias toward VM thread");
      BiasedLocking::revoke_at_safepoint(obj);
    }
    assert(!obj->mark()->has_bias_pattern(), "biases should be revoked by now");
 }

 slow_enter (obj, lock, THREAD) ;
}
```

```c++
void ObjectSynchronizer::slow_enter(Handle obj, BasicLock* lock, TRAPS) {
  markOop mark = obj->mark();
  assert(!mark->has_bias_pattern(), "should not see bias pattern here");

  if (mark->is_neutral()) {
    // Anticipate successful CAS -- the ST of the displaced mark must
    // be visible <= the ST performed by the CAS.
    lock->set_displaced_header(mark);
    if (mark == (markOop) Atomic::cmpxchg_ptr(lock, obj()->mark_addr(), mark)) {
      TEVENT (slow_enter: release stacklock) ;
      return ;
    }
    // Fall through to inflate() ...
  } else
  if (mark->has_locker() && THREAD->is_lock_owned((address)mark->locker())) {
    assert(lock != mark->locker(), "must not re-lock the same lock");
    assert(lock != (BasicLock*)obj->mark(), "don't relock with same BasicLock");
    lock->set_displaced_header(NULL);
    return;
  }

#if 0
  // The following optimization isn't particularly useful.
  if (mark->has_monitor() && mark->monitor()->is_entered(THREAD)) {
    lock->set_displaced_header (NULL) ;
    return ;
  }
#endif

  // The object header will never be displaced to this lock,
  // so it does not matter what the value is, except that it
  // must be non-zero to avoid looking like a re-entrant lock,
  // and must not look locked either.
  lock->set_displaced_header(markOopDesc::unused_mark());
  ObjectSynchronizer::inflate(THREAD, obj())->enter(THREAD);
}
```

inflate 方法：膨胀为重量级锁



### 2.4 操作系统和硬件层级

X86：lock cmpxchg xxx<sup>[[1](https://blog.csdn.net/21aspnet/article/details/88571740)]</sup>



## 3 锁升级过程

### 3.1 JDK8 markword 实现表

64 markword 实现表：

![markword_64](https://gitee.com/struggle3014/picBed/raw/master/markword_64.png)

<div align="center"><font size="2">64位 markword 实现表</font></div>

### 3.2 锁升级

![synchronized 锁升级](https://gitee.com/struggle3014/picBed/raw/master/synchronized 锁升级.png)

<div align="center"><font size="2">锁升级</font></div>

synchronized 优化与 markword 相关，使用 markword 中最低三位表示锁状态，其中一位是偏向锁，剩余两位是普通锁（具体见 markword 实现表）。

* 偏向锁和自旋锁属于**用户空间锁**，都在用户空间完成的。

* **重量级锁**是需要向内核申请。



#### 3.2.1 更多细节

1.  Object o = new Object()

   锁 = 001 无锁态

   注意：如果偏向锁打开，默认是匿名偏向状态。

2. o.hashCode()

   001 + hashcode

   ```java
   00000001 10101101 00110100 00110110
   01011001 00000000 00000000 00000000
   ```

   little endian big endian

3. 默认 synchronized(o)

   00 -> 轻量级锁

   默认情况下，偏向锁有时延，默认是 4 秒（可通过 -XX:BaisedLockingStartupDelay 参数进行设置）。因为，JVM 虚拟机有自己一些默认启动的线程，有很多 sync 代码，这些 sync 代码启动时，肯定会有竞争。如果使用偏向锁，就会造成偏向锁不断地进行锁撤销和锁升级的操作，效率较低。

   ```shell
   -XX:BaisedLockingStartupDelay=0
   ```

   若设置了上述参数，则标识立即启动偏向锁，则上述 new 出的对象，会上为 101 的匿名偏向锁。

4. 若线程上锁

   上偏向锁，指的是，把 markword 的线程 ID 改为自己的线程 ID。

   偏向锁不可重偏向，批量偏向，批量撤销。

5. 如果线程有竞争

   撤销偏向锁，升级为轻量级锁

   线程在自己的线程栈生成 LockRecord，用 CAS 操作将 markword 设置为指向自己的线程的 LockRecord 的指针，设置成功者得锁。

6. 如果竞争加剧

   * 1.6 之前有线程超过 10 次自旋（可通过 -XX:PreBlockSpin 参数控制），或自旋线程超过 CPU 核数的一半，升级为重量级锁。

   * 1.6 及之后加入自适应自旋 Adapative Self Spinning，JVM 自己控制，升级为重量级锁。

   **升级为重量级锁的过程：**向操作系统申请资源，linux mutex，CPU 从 3级-0级系统调用，线程挂起，进入等待队列，等操作系统的调度，然后再映射回用户空间。

   

#### 3.2.2 常见问题

为什么有自旋锁还需要重量级锁？

> 自旋消耗 CPU 资源，如果锁的时间长，或者自旋线程多，CPU 会被大量消耗。
>
> 重量级锁有等待队列（参考 Hotspot 的 objectMonitor.hpp 实现），所有拿不到锁的线程进入等待队列，不需要消耗 CPU 资源。

偏向锁是否一定比自旋效率高？

> 不一定，在明确知道会有多线程竞争的情况下，偏向锁肯定会涉及锁撤销，这时直接使用自旋锁。
>
> 例如，JVM 启动过程，会有很多线程竞争（明确知道），所以默认情况下不打开偏向锁，过段时间再打开。

轻量级锁重量级锁的 hashCode 存在于什么地方？

> 线程栈中，轻量级锁的LR中，或是代表重量级锁的 ObjectMonitor 的成员中。

关于epoch: (不重要)

> **批量重偏向与批量撤销**渊源：从偏向锁的加锁解锁过程中可看出，当只有一个线程反复进入同步块时，偏向锁带来的性能开销基本可以忽略，但是当有其他线程尝试获得锁时，就需要等到 safe point 时，再将偏向锁撤销为无锁状态或升级为轻量级，会消耗一定的性能，所以在多线程竞争频繁的情况下，偏向锁不仅不能提高性能，还会导致性能下降。于是，就有了批量重偏向与批量撤销的机制。
>
> **原理**以 class 为单位，为每个 class 维护**解决场景**批量重偏向（bulk rebias）机制是为了解决：一个线程创建了大量对象并执行了初始的同步操作，后来另一个线程也来将这些对象作为锁对象进行操作，这样会导致大量的偏向锁撤销操作。批量撤销（bulk revoke）机制是为了解决：在明显多线程竞争剧烈的场景下使用偏向锁是不合适的。
>
> 一个偏向锁撤销计数器，每一次该 class 的对象发生偏向撤销操作时，该计数器+1，当这个值达到重偏向阈值（默认20）时，JVM 就认为该 class 的偏向锁有问题，因此会进行批量重偏向。每个 class 对象会有一个对应的epoch字段，每个处于偏向锁状态对象的 Mark Word 中也有该字段，其初始值为创建该对象时 class 中的epoch的值。每次发生批量重偏向时，就将该值+1，同时遍历 JVM 中所有线程的栈，找到该 class 所有正处于加锁状态的偏向锁，将其epoch字段改为新值。下次获得锁时，发现当前对象的 epoch 值和 class 的 epoch 不相等，那就算当前已经偏向了其他线程，也不会执行撤销操作，而是直接通过 CAS 操作将其 Mark Word 的 Thread Id 改成当前线程 Id。当达到重偏向阈值后，假设该class计数器继续增长，当其达到批量撤销的阈值后（默认40），JVM 就认为该 class 的使用场景存在多线程竞争，会标记该 class 为不可偏向，之后，对于该 class 的锁，直接走轻量级锁的逻辑。



### 3.3 锁重入

synchronized 是可重入锁

重入次数必须记录，因为要解锁几次必须得对应

偏向锁 自旋锁 -> 线程栈 -> LR + 1



### 3.4 Synchronized VS Lock(CAS)

在高争用、高耗时的环境下 synchronized 效率更高
 在低争用 低耗时的环境下 CAS 效率更高

* synchronized 到重量级之后是等待队列（不消耗 CPU）
* CAS（等待期间消耗 CPU）



## 4 锁消除(lock eliminate)

```java
public void add(String str1,String str2){
    StringBuffer sb = new StringBuffer();
    sb.append(str1).append(str2);
}
```

StringBuffer 是线程安全的，因为它的关键方法都是被 synchronized 修饰过的，但我们看上面这段代码，我们会发现，sb 这个引用只会在 append 方法中使用，不可能被其它线程引用（因为是局部变量，栈私有），因此 sb 是不可能共享的资源，JVM 会自动消除 StringBuffer 对象内部的锁。



## 5 锁粗化(lock coarsening)

```java
public String test(String str){
    int i = 0;
    StringBuffer sb = new StringBuffer():
    while(i < 100){
        sb.append(str);
        i++;
    }
    return sb.toString():
}
```

JVM 会检测到这样一连串的操作都对同一个对象加锁（while 循环内 100 次执行 append，没有锁粗化的就要进行 100  次加锁/解锁），此时 JVM 就会将加锁的范围粗化到这一连串的操作的外部（比如 while 虚幻体外），使得这一连串操作只需要加一次锁即可。



## 6 锁降级

锁降级参考：https://www.zhihu.com/question/63859501



## 7 Synchronized 关键字使用



# 总结

文章从多方面介绍了 synchronized 关键字，包括底层原理层面破解，横切面剖析，以及锁升级，锁消除，锁粗化，锁降级，帮助大家更好地理解 synchronized 关键字。



# 参考文献

[1] [Hotspot 术语汇编](http://openjdk.java.net/groups/hotspot/docs/HotSpotGlossary.html)

[2] [锁降级](https://www.zhihu.com/question/63859501)

[3] [Java 使用字节码和汇编语言同步分析 volatile，synchronized 的底层实现](https://blog.csdn.net/21aspnet/article/details/88571740)