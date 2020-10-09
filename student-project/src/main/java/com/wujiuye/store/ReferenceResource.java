package com.wujiuye.store;

import java.util.concurrent.atomic.AtomicLong;

public abstract class ReferenceResource {

    protected final AtomicLong refCount = new AtomicLong(1);
    protected volatile boolean available = true;
    protected volatile boolean cleanupOver = false;
    private volatile long firstShutdownTimestamp = 0;

    /**
     * 锁住资源
     *
     * @return
     */
    public synchronized boolean hold() {
        if (this.isAvailable()) {
            if (this.refCount.getAndIncrement() > 0) {
                return true;
            } else {
                this.refCount.getAndDecrement();
            }
        }
        return false;
    }

    /**
     * 是否可以，非shutdown
     *
     * @return
     */
    public boolean isAvailable() {
        return this.available;
    }

    public void shutdown(final long intervalForcibly) {
        if (this.available) {
            this.available = false;
            this.firstShutdownTimestamp = System.currentTimeMillis();
            this.release();
        } else if (this.getRefCount() > 0) {
            // 不是第一次调用shutdown时会进入这里
            // 延迟释放资源
            if ((System.currentTimeMillis() - this.firstShutdownTimestamp) >= intervalForcibly) {
                this.refCount.set(-1000 - this.getRefCount());
                this.release();
            }
        }
    }

    /**
     * 释放锁
     */
    public void release() {
        // 引用减一
        long value = this.refCount.decrementAndGet();
        if (value > 0) {
            return;
        }
        // 引用为0时调用cleanup方法
        synchronized (this) {
            this.cleanupOver = this.cleanup();
        }
    }

    /**
     * 当前引用总数
     *
     * @return
     */
    public long getRefCount() {
        return this.refCount.get();
    }

    public abstract boolean cleanup();

    /**
     * 是否已经cleanup
     *
     * @return
     */
    public boolean isCleanupOver() {
        return this.refCount.get() <= 0 && this.cleanupOver;
    }

}
