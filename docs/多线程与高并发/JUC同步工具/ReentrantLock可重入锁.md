<div align="center"><img src="https://gitee.com/struggle3014/picBed/raw/master/name_code.png"></div>

#  导读 

本文介绍 ReentrantLock 锁和 ReentrantReadWriteLock。

***持续更新中~***



# 目录

<nav>
<a href='# 导读' style='text-decoration:none;font-weight:bolder'> 导读</a><br/>
<a href='#目录' style='text-decoration:none;font-weight:bolder'>目录</a><br/>
<a href='#正文' style='text-decoration:none;font-weight:bolder'>正文</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#Lock 与 ReentrantLock' style='text-decoration:none;${border-style}'>Lock 与 ReentrantLock</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#轮询锁与定时锁' style='text-decoration:none;${border-style}'>轮询锁与定时锁</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#可中断的锁获取操作' style='text-decoration:none;${border-style}'>可中断的锁获取操作</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#非块结构的加锁' style='text-decoration:none;${border-style}'>非块结构的加锁</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#性能考虑因素' style='text-decoration:none;${border-style}'>性能考虑因素</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#公平性' style='text-decoration:none;${border-style}'>公平性</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#在 Synchronized 和 ReentrantLock 之间进行选择' style='text-decoration:none;${border-style}'>在 Synchronized 和 ReentrantLock 之间进行选择</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#读-写锁' style='text-decoration:none;${border-style}'>读-写锁</a><br/>
<a href='#总结' style='text-decoration:none;font-weight:bolder'>总结</a><br/>
<a href='#参考文献' style='text-decoration:none;font-weight:bolder'>参考文献</a><br/>
</nav>

# 正文

## Lock 与 ReentrantLock

Lock 接口中定义了一组抽象的加锁操作。与内置加锁机制不同的是，Lock 提供了一种**无条件的、可轮询的、定时的以及可中断的**锁获取操作，所有加锁和解锁的方法都是现实的。在 Lock 的实现中必须提供与内部锁相同的内存可见性语义，但在加锁语义、调度算法、顺序保证以及性能特性等方面可以有所不同。

**为什么要创建一种与内置锁如此相似的新加锁机制？**在大多数情况下，内置锁都能够很好地工作，但在功能上存在一些局限性。

* 无法中断一个正在等待获取锁的线程，
* 无法在请求获取一个锁时无限地等待下去。
* 内置锁（synchronized）必须在获取该锁的代码块中释放，简化了编码工作，并且与异常处理操作实现了很好的交互，但却无法实现非阻塞结构的加锁机制。在某些情况下，一种更灵活的加锁机制通常能带来更好的活跃性和性能。

Lock 在使用方面比内置锁复杂，需要在 **finally 块**中**释放锁**。否则，如果在被保护的代码抛出异常，那么该锁永远无法释放。若未使用 finally 来释放锁，相当于启动一个定时炸弹。当“炸弹爆炸”时，将很难追踪到最初始发生错误的位置，因为没有记录释放锁的位置和时间，这是 ReentrantLock 不能完全替代 synchronized 的原因。

[ReentrantLock1](../../../projects/MultithreadingHighConcurrency/src/main/java/com/xiumei/multithreadinghighconcurrency/juc/reentrantlock_pkg/T01_ReentrantLock1.java)



### 轮询锁与定时锁

**可定时的与可轮询的锁**的获取模式是由 **tryLock** 方法实现，与无条件的锁获取模式相比，具有更完善的错误恢复机制。在内置锁中，**死锁**是一个严重的问题，恢复程序的唯一方法是重启应用程序，而防止死锁的唯一方法就是在构造程序时避免出现不一致的锁顺序。可定时的与可轮询的锁提供了另一种选择：避免死锁的发生。

如果不能获得所有需要的锁，那么可以使用可定时的或可轮询的锁获取方式，从而使得可重新获得控制权，它会释放已经获得的锁，然后尝试获取所有锁（或者至少会将这个失败记录到日志，并采取其他措施）。

在实现具体有时间限制的操作时，定时锁同样非常有用。当在带有时间限制的操作中调用了一个阻塞方法时，它能根据剩余时间来提供一个时限。如果操作不能在指定的时间内给出结果，那么就会使程序提前结束。当使用内置锁时，在开始请求锁后，这个操作将无法取消，因此内置锁很难时限带有时间限制的操作。

[ReentrantLock2](../../../projects/MultithreadingHighConcurrency/src/main/java/com/xiumei/multithreadinghighconcurrency/juc/reentrantlock_pkg/T02_ReentrantLock2.java)



### 可中断的锁获取操作

正如定时的锁获取操作能在带有时间限制的操作中使用独占锁，可中断的锁获取操作同样能再可取消的操作中使用加锁。lockInterruptibly 方法能够在获得锁的同时保持对中断的响应，并且由于它包含在 Lock 中，因此无须创建其他类型的不可中断阻塞机制。

[ReentrantLock3](../../../projects/MultithreadingHighConcurrency/src/main/java/com/xiumei/multithreadinghighconcurrency/juc/reentrantlock_pkg/T03_ReentrantLock3.java)



### 非块结构的加锁

在内置锁中，锁的获取与释放等操作都是基于代码块的—释放锁的操作总是与获得锁的操作处于同一个代码块，而不考虑控制权如何退出该代码块。自动的锁释放操作简化了对程序的分析，避免了可能的编码错误，但有时需要更灵活的加锁规则。

通过降低锁的粒度可以提高代码的可伸缩性。**锁分段技术**在基于散列的容器中实现了不同散列链，以便使用不同的锁。我们可以通过采用类似的原则**降低链表中锁的粒度**，即为每个链表节点使用一个独立的锁，使不同的线程能够独立地对链表中的不同部分进行操作。每个节点的锁将保护链接指针以及在该指针中存储的数据，因此当遍历或修改链表时，我们必须持有该节点上的这个锁，直到获得下一个节点的锁，只有这样，才能释放前一个节点上的锁。



## 性能考虑因素

Java 5.0 中，ReentrantLock 提供比内置锁更好的竞争性能。对于同步源于来说，竞争性能是可伸缩的关键要素：如果有越多的资源被消耗在管理和调度上，那么应用程序得到的资源就越少。锁的实现方式越好，将需要越少的系统调用和上下文切换，并且在共享内存总线上的内存同步通信量也越少，而一些耗时的操作将占用应用程序的计算资源。

Java 6 使用了改进的算法管理内置锁，与 ReentrantLock 中使用的算法类似，该算法有效地提高了可伸缩性。



## 公平性

ReentrantLock 提供两种公平性选择：

* 非公平锁（默认）

  * 如果在发出请求的同时该锁的状态变成可用，那么该线程将**跳过**队列中所有线程并获得锁。非公平的 ReentrantLock 并不提倡“插队”行为，但无法防止某个线程在合适的时候进行**“插队”**。

  * 在非公平锁中，只有当锁被某个线程持有时，新发出请求的线程才会被放入队列中。

* 公平锁

  * 线程将按照它们发出请求的顺序来获得锁。

  * 在公平锁中，如果有另外一个线程持有这个锁或者有其他线程在度列中等待这个锁，那么新发出请求的线程将被放入队列中。

我们为什么不希望所有的锁都是公平的？毕竟，公平是一种好的行为，而不公平则是一种不好的行为，对不对？当执行加锁操作时，公平性将由于在挂起线程和恢复线程时存在的开销而极大地降低性能。在实际情况中，统计上的公平性保证—确保被阻塞的线程能最终获得锁，通常已经够用了，并且实际开销也小得多。**在大多数情况下，非公平锁的性能要高于公平锁的性能**。

在**激烈竞争**的情况下，**非公平锁**的性能高于公平锁的性能的一个原因：在恢复一个被挂起的线程与该线程真正开始运行之间存在严重的延迟。假设线程 A 持有一把锁，并且线程 B 请求这把锁。由于该锁已被线程 A 持有，因此 B 将被挂起。当 A 释放锁时，B 将被唤醒，因此会再次尝试获取锁。与此同时，如果 C 也请求这把锁，那么 C 很可能在 B 被完全唤醒之前获得、使用以及释放该锁。这种情况是一种双赢的局面：B 获得锁的时刻并没有推迟，C 更早地获得了锁，并且吞吐量也获得了提高。

当**持有锁的时间相对较长**，或者**请求锁的平均时间间隔较长**，应该使用**公平锁**。在这种情况下，“插队”带来的吞吐量提升（当锁处于可用状态时，线程还处于被唤醒的过程中）则可能不会出现。

[ReentrantLock4](../../../projects/MultithreadingHighConcurrency/src/main/java/com/xiumei/multithreadinghighconcurrency/juc/reentrantlock_pkg/T04_ReentrantLock4.java)



## 在 Synchronized 和 ReentrantLock 之间进行选择

ReentrantLock 在加锁和内存上提供的语义与内置锁相同，此外它还提供了一些其他功能，包括定时的锁等待、可中断的锁等待、公平锁以及实现非结构的加锁。

为什么不放弃 synchronized？

* 与显式锁相比，内置锁具有优势。为开发人员所熟悉，很多程序中使用了内置锁。
* ReentrantLock 的危险性比同步机制高，如果忘记在 finally 块中调用 unlock，会埋下定时炸弹。
* 内置锁的性能在提高。若确实需要 ReentLock 包含的伸缩性，否则就性能方面来讲，应该考虑内置锁。



## 读-写锁

ReentrantLock 实现了一种标准的互斥锁：每次最多只有一个线程持有 ReentrantLock。但对于维护数据的完整性来说，**互斥**通常是一种过于**强硬的加锁规则**，因此会不必要地限制并发性。

互斥是一种保守的加锁策略，可以各类读写冲突：

* “写/写”冲突
* “写/读”冲突
* “读/读”冲突

在许多情况下，数据结构上的操作都是“读操作”，虽然它们也是可变的并且在某些情况下被修改，但其中大多数访问操作都是读操作。若能够放宽加锁需求，允许多个执行读操作的线程同时访问数据结构，将提高程序性能。

ReentrantReadWriteLock 暴露了两个 Lock 对象，分别用于读操作和写操作。若要读取由 ReentrantReadWriteLock 保护的数据，需要获得读锁，若需要读取由 ReentrantReadWriteLock 保护的数据，需要获得写锁。尽管两个锁看上是彼此独立的，但是读锁和写锁只是读-写锁对象的不同视图。

[ReentrantReadWriteLock](../../../projects/MultithreadingHighConcurrency/src/main/java/com/xiumei/multithreadinghighconcurrency/juc/reentrantlock_pkg/T05_ReentrantReadWriteLock.java)



# 总结



# 参考文献

[1] [Java 并发编程实战](https://www.99baiduyun.com/baidu/Java并发编程实战)