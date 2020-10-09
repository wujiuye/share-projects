package com.wujiuye.nio;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Set;

/**
 * @author wujiuye
 * @version 1.0 on 2020/1/7 {描述：}
 */
public class EventLoopGroup extends Thread {

    private final static EventLoopGroup[] eventLoopGroups = new EventLoopGroup[Runtime.getRuntime().availableProcessors()];
    private final static AtomicInteger index = new AtomicInteger(0);

    static {
        for (int i = 0; i < eventLoopGroups.length; i++) {
            try {
                eventLoopGroups[i] = new EventLoopGroup();
                eventLoopGroups[i].start();
            } catch (Exception e) {
                throw new Error(e.getMessage());
            }
        }
    }

    public static EventLoopGroup nextEventLoopGroup() {
        return eventLoopGroups[index.getAndIncrement() % eventLoopGroups.length];
    }

    private volatile int clientCnt = 0;
    private Selector readWriteSelector;
    private ReentrantLock lock = new ReentrantLock();
    private Condition notNull = lock.newCondition();

    public EventLoopGroup() throws Exception {
        this.readWriteSelector = Selector.open();
    }

    public void bindClient(SocketChannel socketChannel) throws ClosedChannelException {
        /**
         * 给客户端channel注册多路复用器，监听read、write操作
         */
        socketChannel.register(this.readWriteSelector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
        lock.lock();
        clientCnt = clientCnt + 1;
        notNull.signal();
        lock.unlock();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            lock.lock();
            if (clientCnt == 0) {
                try {
                    System.out.println("wait.......");
                    notNull.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            lock.unlock();
            try {
                int evnetCnt = readWriteSelector.select();
                if (evnetCnt > 0) {
                    Set<SelectionKey> keySet = readWriteSelector.selectedKeys();
                    Iterator<SelectionKey> keys = keySet.iterator();
                    while (keys.hasNext()) {
                        SelectionKey key = keys.next();
                        // 此key的通道是否已准备好进行读取
                        if (key.isReadable()) {
                            SocketChannel socketChannel = (SocketChannel) key.channel();
                            ByteBuffer buffer = (ByteBuffer) key.attachment();
                            int leng = socketChannel.read(buffer);
                            if (leng == -1) {
                                // nio的客户端如果关闭了，服务端还是会收到该channel的读事件，但是数目为0，而且会读到-1，
                                // 其实-1在网络io中就是socket关闭的含义，在文件时末尾的含义，所以为了避免客户端关闭服务端一直收到读事件，
                                // 必须检测上一次的读是不是-1，如果是-1，就关闭这个channel。
                                socketChannel.close();
                                lock.lock();
                                clientCnt = clientCnt - 1;
                                lock.unlock();
                                System.out.println("客户端断开连接.......");
                            } else {
                                try {
                                    System.out.println("read length size " + leng);
                                    System.out.println(new String(buffer.array(), 0, leng));
                                } finally {
                                    buffer.flip();
                                }
                            }
                        }
                        keys.remove();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
