package com.wujiuye.nio;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;

/**
 * 广播发送者
 * 《Elasticsearch集群发现与选举》
 *
 * 在介绍使用Docker部署ES集群的那篇文章中，docker-compose.yml配置文件中，只配置了第一个ES节点的端口，
 * 第二个节点并没有配置端口，而是使用默认端口9300。这个docker-compose.yml文件我是从github找来的，
 * 当时我就好奇，第二个ES节点是怎么加入到集群的。所以今天这篇便是解答我自己的疑惑的。
 *
 * 我们熟悉的微服务框架dubbo，消息中间件kafka都是基于一个第三方服务注册的，如zookeeper。在dubbo中，其实还提供了一个
 * 基于广播的注册中心，dubbo的使用入门例子就是用的广播注册中心。而elasticsearch的集群发现则是使用其内置的发现模块。
 *
 * elasticsearch集群发现机制有两种，分别是Zen Discovery和EC2 discovery。其中zen discovery是elasticsearch默认使用的内置发现模块，
 * 它提供了多播和单播两种发现方式。
 *
 * 使用组播（多播），我们只需要在每个节点配置好集群名称、节点名称。节点会根据es自定义的服务发现协议去按照多播的方式来寻找网络上配置在同样集群内的节点。
 * 了解一下就好了，虽然组播仍然作为插件提供，但不应该在生产环境使用。
 *
 * 单播发现需要一个主机列表充作路由列表。这些主机可以是主机名称也可以是IP地址，在每次pinging的时候主机名称会相应地被解析为对应的ip地址。
 * 在7.x之前的版本，配置参数为discovery.zen.ping.unicast.hosts，[Zen Discovery的官方文档](https://www.elastic.co/guide/en/elasticsearch/reference/2.4/modules-discovery-zen.html)
 * 【图片1】
 * hosts多个之间用逗号隔开，每一项都是host:port，如果只有host，则默认端口号为9300
 * 7.1版本的配置参数为：discovery.seed_hosts
 *
 * 单播发现是使用传输（transport）模块实现的，就不是使用UDP协议了。
 * 传输（transport）模块用于集群内节点之间的内部通信。ES集群内部，从一个节点到另一个节点的每个调用都使用传输模块。
 *
 * 最后介绍一下组播的实现，以此了解为什么elasticsearch默认禁用组播方式，以及dubbo的基于广播实现的注册中心为什么只能适用于本地测试。
 *
 * 【代码】
 *
 * 此demo要运行需要配置VM参数：-Djava.net.preferIPv4Stack=true
 * IPV4的广播地址范围是: 224.0.0.0 - 239.255.255.255，dubbo使用的是239.255.255.255
 *
 * 启动多个广播MulticastSocketReviceMain服务，在任意一个服务上发送消息其它服务都能接收到。
 *
 * 如果当前是线上环境，我突然启动一个服务，但并不想加入到集群中，但是节点启动就发广播，线上的服务接收到广播就认为你要新增节点了。
 * 这样得到的结果就是一个节点意外的加入到了生产环境，仅仅是因为一个错误的组播信号。因此Elasticsearch默认被配置为使用单播发现，以防止节点无意中加入集群。
 *
 * 最后，简单了解一下elasticsearch的集群选举原理。
 * 我学习Elasticsearch与学习Redis一样，我的目的只是去了解一些我想要了解的，并不打算深入去研究，所以我也不会去翻看源码。
 *
 * @author wujiuye
 */
public class MulticastSocketSendMain {

    public void startRevice() throws IOException {
        // 组播地址
        InetAddress boradcastAddrss = InetAddress.getByName("239.255.255.255");

        // 创建MulticastSocket对象
        MulticastSocket mSocket = new MulticastSocket(3006);
        // 将该MulticastSocket加入到组播
        mSocket.joinGroup(boradcastAddrss);
        // 设置本MulticastSocket发送的数据报会被回送到自身
        // true表示不会回送（本机测试需要设置为false）
        mSocket.setLoopbackMode(false);

        // 创建相关的DatagramPacket,用于接受数据
        byte[] inBuff = new byte[1024];
        DatagramPacket inPacket = new DatagramPacket(inBuff, inBuff.length);

        while (!Thread.interrupted()) {
            // 等待接收数据
            mSocket.receive(inPacket);
            String recevieMsgStr = new String(inBuff, 0, inPacket.getLength());
            System.out.println("接收到广播消息：" + recevieMsgStr);
        }
    }

    public void startSend() throws IOException {
        // 组播地址
        InetAddress boradcastAddrss = InetAddress.getByName("239.255.255.255");

        // 创建MulticastSocket对象
        MulticastSocket mSocket = new MulticastSocket(3006);
        // 将该MulticastSocket加入到组播地址中
        mSocket.joinGroup(boradcastAddrss);
        // 设置本MulticastSocket发送的数据报会被回送到自身
        // true表示不会回送（本机测试需要设置为false）
        mSocket.setLoopbackMode(false);

        // 创建用于发送数据的DatagramPacket
        byte[] inBuff = new byte[1024];
        DatagramPacket outPacket = new DatagramPacket(inBuff, inBuff.length, boradcastAddrss, 3006);

        // 从控制台读取一行字符串发送
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            byte[] outBuff = sc.nextLine().getBytes();
            outPacket.setData(outBuff);
            // 发送数据
            mSocket.send(outPacket);
        }
    }

    public static void main(String[] args) throws IOException {
        MulticastSocketSendMain multicastSocketSendMain = new MulticastSocketSendMain();
        new Thread() {
            @Override
            public void run() {
                try {
                    multicastSocketSendMain.startRevice();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                try {
                    multicastSocketSendMain.startSend();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
