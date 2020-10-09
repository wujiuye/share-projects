package com.wujiuye.store;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import sun.misc.Cleaner;
import sun.nio.ch.DirectBuffer;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MappedFile extends ReferenceResource {

    /**
     * 操作系统的页大小
     */
    public static final int OS_PAGE_SIZE = 1024 * 4;

    /**
     * 总的虚拟映射内存大小（每个文件虚拟映射文件的大小的总和）
     */
    private static final AtomicLong TOTAL_MAPPED_VIRTUAL_MEMORY = new AtomicLong(0);
    /**
     * 总的虚拟映射文件数
     */
    private static final AtomicInteger TOTAL_MAPPED_FILES = new AtomicInteger(0);

    /**
     * 当前写偏移量
     */
    protected final AtomicInteger wrotePosition = new AtomicInteger(0);
    /**
     * 当前提交偏移量
     */
    protected final AtomicInteger committedPosition = new AtomicInteger(0);
    /**
     * 当前刷盘偏移量
     */
    private final AtomicInteger flushedPosition = new AtomicInteger(0);
    /**
     * 文件大小
     */
    protected int fileSize;
    /**
     * NIO文件读写通道
     */
    protected FileChannel fileChannel;

    /**
     * Message will put to here first, and then reput to FileChannel if writeBuffer is not null.
     */
    protected ByteBuffer writeBuffer = null;
    private String fileName;
    private long fileFromOffset;
    private File file;
    private MappedByteBuffer mappedByteBuffer;

    public static int getTotalMappedFiles() {
        return TOTAL_MAPPED_FILES.get();
    }

    public static long getTotalMappedVirtualMemory() {
        return TOTAL_MAPPED_VIRTUAL_MEMORY.get();
    }

    public MappedFile(final String fileName, final int fileSize) throws IOException {
        init(fileName, fileSize);
    }

    private void init(final String fileName, final int fileSize) throws IOException {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.file = new File(fileName);
        // 根据文件名获取文件的偏移量
        this.fileFromOffset = Long.parseLong(this.file.getName());
        boolean ok = false;
        ensureDirOK(this.file.getParent());
        try {
            this.fileChannel = new RandomAccessFile(this.file, "rw").getChannel();
            this.mappedByteBuffer = this.fileChannel.map(MapMode.READ_WRITE, 0, fileSize);
            TOTAL_MAPPED_VIRTUAL_MEMORY.addAndGet(fileSize);
            TOTAL_MAPPED_FILES.incrementAndGet();
            ok = true;
        } catch (IOException e) {
            throw e;
        } finally {
            if (!ok && this.fileChannel != null) {
                this.fileChannel.close();
            }
        }
    }

    /**
     * 确保文件所在目录存在
     *
     * @param dirName 目录
     */
    public static void ensureDirOK(final String dirName) {
        if (dirName != null) {
            File f = new File(dirName);
            if (!f.exists()) {
                f.mkdirs();
            }
        }
    }

    public String getFileName() {
        return fileName;
    }

    public long getLastModifiedTimestamp() {
        return this.file.lastModified();
    }

    public int getFileSize() {
        return fileSize;
    }

    public File getFile() {
        return file;
    }

    public long getFileFromOffset() {
        return this.fileFromOffset;
    }

    public boolean appendMessage(final byte[] data) {
        int currentPos = this.wrotePosition.get();
        if ((currentPos + data.length) <= this.fileSize) {
            try {
                this.fileChannel.position(currentPos);
                this.fileChannel.write(ByteBuffer.wrap(data));
            } catch (Throwable ignored) {
            }
            this.wrotePosition.addAndGet(data.length);
            return true;
        }
        return false;
    }

    public boolean appendMessage(final byte[] data, final int offset, final int length) {
        int currentPos = this.wrotePosition.get();
        if ((currentPos + length) <= this.fileSize) {
            try {
                this.fileChannel.position(currentPos);
                this.fileChannel.write(ByteBuffer.wrap(data, offset, length));
            } catch (Throwable ignored) {
            }
            this.wrotePosition.addAndGet(length);
            return true;
        }
        return false;
    }

    public int flush(final int flushLeastPages) {
        if (this.isAbleToFlush(flushLeastPages)) {
            // 加锁（添加引用计数）
            if (this.hold()) {
                int value = getReadPosition();
                try {
                    // 我们只将数据附加到fileChannel或mappedByteBuffer，而不是两者。
                    if (writeBuffer != null || this.fileChannel.position() != 0) {
                        this.fileChannel.force(false);
                    } else {
                        this.mappedByteBuffer.force();
                    }
                } catch (Throwable ignored) {
                }
                this.flushedPosition.set(value);
                // 解锁（释放引用计数）
                this.release();
            } else {
                this.flushedPosition.set(getReadPosition());
            }
        }
        return this.getFlushedPosition();
    }

    public int commit(final int commitLeastPages) {
        // 没有需要提交的数据
        if (writeBuffer == null) {
            return this.wrotePosition.get();
        }
        // 是否能够提交commitLeastPages页（当前待刷盘的页）
        if (this.isAbleToCommit(commitLeastPages)) {
            // 加锁（添加引用计数）
            if (this.hold()) {
                commit0();
                // 解锁（释放引用计数）
                this.release();
            }
        }
        // 文件已写满（且已刷盘）则清空写缓存
        if (writeBuffer != null && this.fileSize == this.committedPosition.get()) {
            this.writeBuffer = null;
        }
        return this.committedPosition.get();
    }

    /**
     * 将写入缓存（writeBuffer）的数据刷盘到磁盘上
     */
    protected void commit0() {
        int writePos = this.wrotePosition.get();
        int lastCommittedPosition = this.committedPosition.get();
        if (writePos - this.committedPosition.get() > 0) {
            try {
                ByteBuffer byteBuffer = writeBuffer.slice();
                byteBuffer.position(lastCommittedPosition);
                byteBuffer.limit(writePos);
                this.fileChannel.position(lastCommittedPosition);
                this.fileChannel.write(byteBuffer);
                this.committedPosition.set(writePos);
            } catch (Throwable ignored) {
            }
        }
    }

    private boolean isAbleToFlush(final int flushLeastPages) {
        int flush = this.flushedPosition.get();
        int write = getReadPosition();
        // 文件已写满
        if (this.isFull()) {
            return true;
        }
        // 待刷盘页数是否大于等于flushLeastPages
        if (flushLeastPages > 0) {
            return ((write / OS_PAGE_SIZE) - (flush / OS_PAGE_SIZE)) >= flushLeastPages;
        }
        return write > flush;
    }

    protected boolean isAbleToCommit(final int commitLeastPages) {
        if (this.isFull()) {
            return true;
        }
        int flush = this.committedPosition.get();
        int write = this.wrotePosition.get();
        if (commitLeastPages > 0) {
            // 已写页 - 已刷盘页 >= 需要刷盘的页
            return ((write / OS_PAGE_SIZE) - (flush / OS_PAGE_SIZE)) >= commitLeastPages;
        }
        return write > flush;
    }

    public int getWrotePosition() {
        return wrotePosition.get();
    }

    /**
     * 获取当前可读偏移量
     * （读偏移量不能大于文件的大小，即已写入文件的大小，否则MappedByteBuffer内存映射失败）
     *
     * @return
     */
    public int getReadPosition() {
        return this.writeBuffer == null ? this.wrotePosition.get() : this.committedPosition.get();
    }

    public int getFlushedPosition() {
        return flushedPosition.get();
    }

    public boolean isFull() {
        return this.fileSize == this.wrotePosition.get();
    }

    public MappedByteBuffer getMappedByteBuffer() {
        return mappedByteBuffer;
    }

    public SelectMappedBufferResult selectMappedBuffer(int pos, int size) {
        int readPosition = getReadPosition();
        if ((pos + size) <= readPosition) {
            // 加锁
            if (this.hold()) {
                try{
                    ByteBuffer byteBuffer = this.mappedByteBuffer.slice();
                    byteBuffer.position(pos);
                    ByteBuffer byteBufferNew = byteBuffer.slice();
                    byteBufferNew.limit(size);
                    return new SelectMappedBufferResult(this.fileFromOffset + pos, byteBufferNew, size, this);
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public SelectMappedBufferResult selectMappedBuffer(int pos) {
        int readPosition = getReadPosition();
        if (pos < readPosition && pos >= 0) {
            // 加锁
            if (this.hold()) {
                ByteBuffer byteBuffer = this.mappedByteBuffer.slice();
                byteBuffer.position(pos);
                int size = readPosition - pos;
                ByteBuffer byteBufferNew = byteBuffer.slice();
                byteBufferNew.limit(size);
                return new SelectMappedBufferResult(this.fileFromOffset + pos, byteBufferNew, size, this);
            }
        }
        return null;
    }

    @Override
    public boolean cleanup() {
        if (this.isAvailable()) {
            return false;
        }
        if (this.isCleanupOver()) {
            return true;
        }
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
        TOTAL_MAPPED_VIRTUAL_MEMORY.addAndGet(-this.fileSize);
        TOTAL_MAPPED_FILES.decrementAndGet();
        return true;
    }

    public boolean destroy() {
        this.shutdown(1000 * 3);
        if (this.isCleanupOver()) {
            try {
                this.fileChannel.close();
            } catch (Exception ignored) {
            }
            return true;
        }
        return false;
    }

    /**
     * 预热
     *
     * @param type  刷盘类型，如果是异步刷盘，则由操作系统决定何时刷盘，如果同步刷盘，则调用force强制刷盘
     * @param pages 多少页刷一次盘
     */
    public void warmMappedFile(FlushDiskType type, int pages) {
        ByteBuffer byteBuffer = this.mappedByteBuffer.slice();
        int flush = 0;
        // 初始化每页写入一个字节的数据，内容为0x00000000
        for (int i = 0, j = 0; i < this.fileSize; i += MappedFile.OS_PAGE_SIZE, j++) {
            byteBuffer.put(i, (byte) 0);
            // 同步刷新磁盘类型时强制刷新
            if (type == FlushDiskType.SYNC_FLUSH) {
                // 当前页 - 已经刷盘的页 >= pages
                // 假设pages=5 当前页为5 已经刷新页为0 则刷盘
                // 可简化为：i - flush >= pages
                if ((i / OS_PAGE_SIZE) - (flush / OS_PAGE_SIZE) >= pages) {
                    flush = i;
                    mappedByteBuffer.force();
                }
            }
        }
        // 准备加载完成时再强制刷一次盘
        if (type == FlushDiskType.SYNC_FLUSH) {
            mappedByteBuffer.force();
        }
        // 锁住内存，避免磁盘交换
        this.mlock();
    }

    /**
     * 锁住内存
     */
    public void mlock() {
        final long address = ((DirectBuffer) (this.mappedByteBuffer)).address();
        Pointer pointer = new Pointer(address);
        Libc.INSTANCE.mlock(pointer, new NativeLong(this.fileSize));
        Libc.INSTANCE.madvise(pointer, new NativeLong(this.fileSize), Libc.MADV_WILLNEED);
    }

    /**
     * 释放锁住的内存
     */
    public void munlock() {
        final long address = ((DirectBuffer) (this.mappedByteBuffer)).address();
        Pointer pointer = new Pointer(address);
        Libc.INSTANCE.munlock(pointer, new NativeLong(this.fileSize));
    }

}
