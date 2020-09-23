<div align="center"><img src="https://gitee.com/struggle3014/picBed/raw/master/name_code.png"></div>

# 导读

本文介绍 IO 模型。

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

netstat -natp：查看网络状态

tcpdump -nn -i etho port 9090：打开 tcp 监控，etho 为网卡

65535 个端口号，2 个字节

lsof -i {pid}：查看系统文件描述符（linux 中一切皆文件）

nc {ip} {port}：net connection

strace -ff -o out：追踪一个进程的每一个线程对内核的系统调用	 

man（linux）

route -n：查看路由表

route add -host 192.168.110.100 gw 192.168.150.1



补充知识点：

TCP/IP 协议

MTU（Maximum Transmission Unit）：一般指最大传输单位，用来通知对方所能接受数据服务单元的最大尺度，说明发送方能够接受的有效载荷大小。是包或帧的最大长度，一般以字节记。

MSS（Maximum Segment Size）：最大报文段长度，是 TCP 协议的一个选项，用于在 TCP 建立连接时，接收双方协商通信时每一个报文段所能承受的最大数据长度（不包括文段头）。

拥塞控制：

www.kegel.com/c10k.html



# 总结



# 参考文献



