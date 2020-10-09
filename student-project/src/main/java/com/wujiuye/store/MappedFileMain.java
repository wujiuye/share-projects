package com.wujiuye.store;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MappedFileMain {

    private static void write() throws IOException {
        IndexMappedFile indexMappedFile = new IndexMappedFile("/tmp/4096", 4096 * 8);
        indexMappedFile.initIndex();
        System.out.println(indexMappedFile.getFileFromOffset());

        Index index = new Index(4323, 1025);
        indexMappedFile.appendIndex(68, index);

        index = new Index(7878, 5656);
        indexMappedFile.appendIndex(1026, index);
        indexMappedFile.commit(0);

        indexMappedFile.flush(0);
        indexMappedFile.destroy();
    }

    private static void read() throws IOException {
        IndexMappedFile indexMappedFile = new IndexMappedFile("/tmp/4096", 4096 * 8);
        indexMappedFile.wrotePosition.set(indexMappedFile.fileSize);

        SelectMappedBufferResult selectMappedBufferResult = indexMappedFile.selectMappedBuffer((1024 - 1) * 8, 8);
        byte[] buf = new byte[8];
        selectMappedBufferResult.getByteBuffer().get(buf);
        selectMappedBufferResult.release();
        System.out.println(Index.parseByteArray(buf));

        selectMappedBufferResult = indexMappedFile.selectMappedBuffer((68 - 1) * 8, 8);
        buf = new byte[8];
        selectMappedBufferResult.getByteBuffer().get(buf);
        selectMappedBufferResult.release();
        System.out.println(Index.parseByteArray(buf));
    }

    public static void main(String[] args) throws IOException {
//        write();
//        read();

//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    mwrite();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();

        new Thread() {
            @Override
            public void run() {
                try {
                    mread();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
        }
    }

    private static final ExecutorService executorService = Executors.newFixedThreadPool(100);

    private static void mwrite() throws IOException {
        MappedFile mappedFile = new MappedFile("tmp/0000", 1024 * 1024 * 1024);
        CountDownLatch latch = new CountDownLatch(10000);
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        for (int i = 0; i < 10000; i++) {
            String msg = "" + i;
            if (i < 10) {
                msg = "0000000" + msg;
            } else if (i < 100) {
                msg = "000000" + msg;
            } else if (i < 1000) {
                msg = "00000" + msg;
            } else if (i < 10000) {
                msg = "0000" + msg;
            }
            String tmp = msg;
            executorService.execute(() -> {
                readWriteLock.writeLock().lock();
                try {
                    long start = System.currentTimeMillis();
                    mappedFile.appendMessage(tmp.getBytes());
                    System.out.println("write cnt " + (System.currentTimeMillis() - start) + " ms.");
                } finally {
                    readWriteLock.writeLock().unlock();
                    latch.countDown();
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
        }
        mappedFile.getMappedByteBuffer().position(0);
        mappedFile.getMappedByteBuffer().put("11111111".getBytes());
        mappedFile.getMappedByteBuffer().force();
        mappedFile.destroy();
    }

    private static void mread() throws IOException {
        MappedFile mappedFile = new MappedFile("tmp/0000", 1024 * 1024 * 1024);
        mappedFile.wrotePosition.set(mappedFile.fileSize);
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        CountDownLatch latch = new CountDownLatch(10000);
        for (int i = 0; i < 10000; i++) {
            int tmp = i;
            executorService.execute(() -> {
                readWriteLock.readLock().lock();
                try {
                    long start = System.currentTimeMillis();
                    SelectMappedBufferResult selectMappedBufferResult = mappedFile.selectMappedBuffer(tmp * 8, 8);
                    if (selectMappedBufferResult == null) {
                        return;
                    }
                    byte[] buf = new byte[8];
                    selectMappedBufferResult.getByteBuffer().get(buf);
                    System.out.println("write cnt " + (System.currentTimeMillis() - start) + " ms.");
                    System.out.println(new String(buf));
                } finally {
                    latch.countDown();
                    readWriteLock.readLock().unlock();
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
        }
        mappedFile.destroy();
    }

}
