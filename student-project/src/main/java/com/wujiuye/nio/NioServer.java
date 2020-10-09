package com.wujiuye.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
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
public class NioServer implements Runnable {

    private volatile boolean isStop = false;

    private int port;
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;//多路复用器

    public NioServer(int port) {
        this.port = port;
        try {
            this.serverSocketChannel = ServerSocketChannel.open();
            this.selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startServer() {
        try {
            /**
             * 配置是否阻塞式I/O,false非阻塞
             */
            this.serverSocketChannel.configureBlocking(false);
            /**
             * 绑定监听端口
             * 参数1：绑定的地址，端口号
             * 参数2：请求的传入连接队列的最大长度。
             */
            this.serverSocketChannel.socket()
                    .bind(new InetSocketAddress(this.port), 1024);
            /**
             * 给Selector注册Channel，监听accept、和connect
             */
            this.serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        this.startServer();
        while (!isStop) {
            try {
                // 只有调用select才会去获取准备就绪的selectionKey，
                // 获取到之后返回总数，否则线程将处于阻塞状态
                int readyCount = this.selector.select();
                if (readyCount == 0)
                    continue;
                //获取当前所有准备就绪的SelectionKey（引用执行this.selector的属性）
                Set<SelectionKey> readyKeys = this.selector.selectedKeys();
                for (SelectionKey selectionKey : readyKeys) {
                    handleEvent(selectionKey);
                    //处理完要移除，否则下次selectedKeys还是能拿到
                    readyKeys.remove(selectionKey);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 处理操作
     * //是否已完成其套接字连接操作
     * if (key.isConnectable())
     * //此key的通道是否已准备好写入
     * if (key.isWritable())
     *
     * @param key
     */
    private void handleEvent(SelectionKey key) {
        //是否有效
        if (key.isValid()) {
            //是否准备好接受新的套接字连接
            if (key.isAcceptable()) {
                try {
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                    //开始监听客户端连接
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    //配置非阻塞
                    socketChannel.configureBlocking(false);
                    EventLoopGroup.nextEventLoopGroup().bindClient(socketChannel);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 停止服务端
     */
    public void stopServer() {
        this.isStop = true;
        if (this.selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (this.serverSocketChannel != null) {
            try {
                this.serverSocketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
