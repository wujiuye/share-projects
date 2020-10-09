package com.wujiuye.store;

import com.sun.jna.*;

public interface Libc extends Library {

    Libc INSTANCE = (Libc) Native.loadLibrary(Platform.isWindows() ? "msvcrt" : "c", Libc.class);

    /**
     * 预计在不久的将来访问(因此,最好预加载)
     */
    int MADV_WILLNEED = 3;

    /**
     * 系统调用 mlock 家族允许程序在物理内存上锁住它的部分或全部地址空间。
     * 这将阻止Linux 将这个内存页调度到交换空间（swap space），即使该程序已有一段时间没有访问这段空间。
     *
     * @param var1 内存地址
     * @param var2 连续内存长度
     * @return
     */
    int mlock(Pointer var1, NativeLong var2);

    /**
     * 释放内存锁
     *
     * @param var1
     * @param var2
     * @return
     */
    int munlock(Pointer var1, NativeLong var2);

    /**
     * 这个函数会传入一个地址指针，以及一个区间长度，
     * madvise会向内核提供一个针对于于地址区间的I/O的建议，内核可能会采纳这个建议，会做一些预读的操作。
     * 例如：MADV_SEQUENTIAL这个就表明顺序预读。
     *
     * @param var1 内存地址
     * @param var2 长度
     * @param var3 建议
     * @return
     */
    int madvise(Pointer var1, NativeLong var2, int var3);

}
