package com.wujiuye.nio;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * 广播接收者
 *
 * @author wujiuye
 */
public class MulticastSocketReviceMain {

    public void startRevice() throws IOException {
        // 组播地址
        InetAddress boradcastAddrss = InetAddress.getByName("224.0.0.1");

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

    public static void main(String[] args) throws IOException {
        new MulticastSocketReviceMain().startRevice();
    }

}
