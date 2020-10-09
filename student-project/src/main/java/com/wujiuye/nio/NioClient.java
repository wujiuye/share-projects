package com.wujiuye.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
 * <p>
 * 微信公众号id：code_skill
 * QQ邮箱：419611821@qq.com
 * 微信号：www_wujiuye_com
 * <p>
 * ======================^^^^^^^==============^^^^^^^============
 *
 * @ 作者       |   吴就业 www.wujiuye.com
 * ======================^^^^^^^==============^^^^^^^============
 * @ 创建日期      |   Created in 2018年12月22日
 * ======================^^^^^^^==============^^^^^^^============
 * @ 所属项目   |   lock
 * ======================^^^^^^^==============^^^^^^^============
 * @ 类功能描述    |
 * ======================^^^^^^^==============^^^^^^^============
 * @ 版本      |   ${version}
 * ======================^^^^^^^==============^^^^^^^============
 */
public class NioClient implements Runnable {

    private int port;
    private String ipAddress;

    private SocketChannel socketChannel;
    private Selector selector;
    private volatile boolean isDropConnection = false;

    public NioClient(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    private void doConnect() {
        try {
            this.socketChannel = SocketChannel.open();
            this.socketChannel.configureBlocking(false);
            this.selector = Selector.open();
            // 连接到服务端
            this.socketChannel.connect(new InetSocketAddress(this.ipAddress, this.port));
            // 注册连接事件，将channel注册到selector上，并且设置该channel只监听连接事件
            this.socketChannel.register(this.selector, SelectionKey.OP_CONNECT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        this.doConnect();
        while (!isDropConnection) {
            try {
                this.selector.select();
                Set<SelectionKey> selectionKeys = this.selector.selectedKeys();
                Iterator<SelectionKey> iterable = selectionKeys.iterator();
                while (iterable.hasNext()) {
                    SelectionKey selectionKey = iterable.next();
                    handleEvent(selectionKey);
                    iterable.remove();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void send(String msg) {
        new Thread() {
            @Override
            public void run() {
                while (!isDropConnection && !Thread.interrupted()) {
                    try {
                        socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private void handleEvent(SelectionKey selectionKey) {
        if (selectionKey.isValid()) {
            if (selectionKey.isConnectable()) {
                try {
                    // 如果正在连接，则完成连接
                    if (socketChannel.isConnectionPending()) {
                        socketChannel.finishConnect();
                    }
                    // 连接成功，改为注册读操作
                    this.socketChannel.register(this.selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    send("hello");
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
            if (selectionKey.isReadable()) {
                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                try {
                    int leng = socketChannel.read(buffer);
                    if (leng == -1) {
                        // nio的客户端如果关闭了，服务端还是会收到该channel的读事件，但是数目为0，而且会读到-1，
                        // 其实-1在网络io中就是socket关闭的含义，在文件时末尾的含义，所以为了避免客户端关闭服务端一直收到读事件，
                        // 必须检测上一次的读是不是-1，如果是-1，就关闭这个channel。
                        closeConnect();
                        System.out.println("服务端断开连接.......");
                    } else {
                        try {
                            System.out.println("read length size " + leng);
                            System.out.println(new String(buffer.array(), 0, leng));
                        } finally {
                            buffer.flip();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void closeConnect() {
        try {
            isDropConnection = true;
            this.selector.close();
            this.socketChannel.socket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
