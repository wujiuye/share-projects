package com.wujiuye.store;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import sun.misc.Cleaner;
import sun.nio.ch.DirectBuffer;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MmapFile {

    static File encodeFile;
    static FileInputStream fileInputStream;
    static FileChannel fileChannel;
    static MappedByteBuffer mappedByteBuffer;

    static {
        encodeFile = new File("/tmp/2020053113_00000000.encode");
        if (!encodeFile.exists()) {
            try {
                encodeFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(encodeFile, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        fileChannel = randomAccessFile.getChannel();
        try {
            mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 4096 * 8);
            mappedByteBuffer.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    Pointer pointer = new Pointer(((DirectBuffer) (mappedByteBuffer)).address());
                    Libc.INSTANCE.munlock(pointer, new NativeLong(4096 * 8));
                    fileChannel.close();
                    fileInputStream.close();
                } catch (Exception e) {

                }
            }
        });
    }

    private static String to4Str(int i) {
        int cnt0 = 0;
        if (i < 10) {
            cnt0 = 3;
        } else if (i < 100) {
            cnt0 = 2;
        } else if (i < 1000) {
            cnt0 = 1;
        }
        StringBuilder v = new StringBuilder("" + i);
        for (int c = 0; c < cnt0; c++) {
            v.insert(0, "0");
        }
        return v.toString();
    }

    /**
     * 由于FileChannel调用了map方法做内存映射，但是没提供对应的unmap方法释放内存，导致内存一直占用该文件。实际unmap方法在FileChannelImpl中私有方法中，在finalize时，unmap无法调用导致内存没释放。
     * 解决方案：
     * 1、手动执行unmap方法
     * 2、让MappedByteBuffer自己释放本身持有的内存
     */
    private static void munmap() {
        // 在关闭资源时执行以下代码释放内存
//        Method m = FileChannelImpl.class.getDeclaredMethod("unmap", MappedByteBuffer.class);
//        m.setAccessible(true);
//        m.invoke(FileChannelImpl.class, mappedByteBuffer);
        AccessController.doPrivileged((PrivilegedAction) () -> {
            try {
                Method getCleanerMethod = mappedByteBuffer.getClass().getMethod("cleaner", new Class[0]);
                getCleanerMethod.setAccessible(true);
                Cleaner cleaner = (Cleaner)
                        getCleanerMethod.invoke(mappedByteBuffer, new Object[0]);
                cleaner.clean();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public static void main(String[] args) throws Exception {
        preWrite();
        write();
        testRead();
        munmap();
    }

    private static void write() throws Exception {
        for (int i = 0; i < 4096; i++) {
            long start = System.currentTimeMillis();
            mappedByteBuffer.position(i * 8);
            mappedByteBuffer.put((to4Str(i) + "0000").getBytes());
            System.out.println("write cnt " + (System.currentTimeMillis() - start) + " ms");
        }
        mappedByteBuffer.force();
    }

    private static void preWrite() throws Exception {
        int pageSize = 4096;
        long fileSize = 4096 * 8;
        for (int i = 0; i < fileSize; i += pageSize) {
            mappedByteBuffer.position(i);
            mappedByteBuffer.put((byte) '0');
        }
        mappedByteBuffer.force();
        Pointer pointer = new Pointer(((DirectBuffer) (mappedByteBuffer)).address());
        Libc.INSTANCE.mlock(pointer, new NativeLong(fileSize));
        Libc.INSTANCE.madvise(pointer, new NativeLong(fileSize), Libc.MADV_WILLNEED);
    }

    private static void testRead() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(1000);
        CountDownLatch latch = new CountDownLatch(4096);
        for (int i = 0; i < 4096; i++) {
            int postion = i;
            executor.submit(() -> {
                try {
                    Thread.sleep(100);
                    read(postion);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executor.shutdownNow();
    }

    private static void read(int postion) throws Exception {
        long start = System.currentTimeMillis();
        byte[] buf = new byte[8];
        mappedByteBuffer.position(postion * 8);
        mappedByteBuffer.get(buf, 0, 8);
        System.out.println("cnt " + (System.currentTimeMillis() - start) + " ms");
        System.out.println(new String(buf));
    }

}
