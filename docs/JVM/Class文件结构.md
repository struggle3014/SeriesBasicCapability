<div align="center"><img src="https://gitee.com/struggle3014/picBed/raw/master/name_code.png"></div>

# 导读

class 的本质是二进制字节流，本文带大家了解下 class 文件格式以及如何使用工具查看文件格式。

***持续更新中~***



# 目录

<nav>
<a href='#导读' style='text-decoration:none;font-weight:bolder'>导读</a><br/>
<a href='#目录' style='text-decoration:none;font-weight:bolder'>目录</a><br/>
<a href='#正文' style='text-decoration:none;font-weight:bolder'>正文</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#1 ClassFile 格式' style='text-decoration:none;${border-style}'>1 ClassFile 格式</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#1.1 数据类型' style='text-decoration:none;${border-style}'>1.1 数据类型</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#1.2 ClassFile 结构' style='text-decoration:none;${border-style}'>1.2 ClassFile 结构</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;<a href='#2 常用工具' style='text-decoration:none;${border-style}'>2 常用工具</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#2.1 Class 文件分析工具（16 进制）' style='text-decoration:none;${border-style}'>2.1 Class 文件分析工具（16 进制）</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='#2.2 反解析工具（查看 ByteCode）' style='text-decoration:none;${border-style}'>2.2 反解析工具（查看 ByteCode）</a><br/>
<a href='#总结' style='text-decoration:none;font-weight:bolder'>总结</a><br/>
<a href='#参考文献' style='text-decoration:none;font-weight:bolder'>参考文献</a><br/>
</nav>

# 正文

## 1 ClassFile 格式

| 思维导图                                                   |
| ---------------------------------------------------------- |
| [Class 文件结构](../docs/MindMapping/Class 文件结构.xmind) |

![Class 文件结构](https://gitee.com/struggle3014/picBed/raw/master/Class 文件结构.png)

### 1.1 数据类型

数据类型：u1 u2 u4 u8 和 _info（表类型）

* u1，u2，u4，u8 表示无符号的一字节，二字节，四字节，八字节量，其中 u 是 unsinged 的缩写。

* _info 的来源是 Hotspot 源码中的写法



### 1.2 ClassFile 结构

![class_file_format](https://gitee.com/struggle3014/picBed/raw/master/class_file_format.png)

<div align="center"><font size="2">Class 文件结构</font></div>

```java
ClassFile {
    u4             magic;
    u2             minor_version;
    u2             major_version;
    u2             constant_pool_count;
    cp_info        constant_pool[constant_pool_count-1];
    u2             access_flags;
    u2             this_class;
    u2             super_class;
    u2             interfaces_count;
    u2             interfaces[interfaces_count];
    u2             fields_count;
    field_info     fields[fields_count];
    u2             methods_count;
    method_info    methods[methods_count];
    u2             attributes_count;
    attribute_info attributes[attributes_count];
}
```



## 2 常用工具

### 2.1 Class 文件分析工具（16 进制）

* sublime / notepad
* IDEA 插件 - BinEd



### 2.2 反解析工具（查看 ByteCode）

* javap
  * Java 自带，界面不友好
* JBE
  * 可以直接修改
* JClassLib
  * IDEA 插件，界面友好



# 总结

本文介绍了 class 文件格式，以及如何使用工具查看 class 原始文件（二进制）及字节码文件。



# 参考文献

[1]  [Java Language and Virutual Machine Specifications](https://docs.oracle.com/javase/specs/index.html