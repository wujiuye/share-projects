package com.wujiuye.sck.common.util.jvm;

import java.lang.management.ManagementFactory;

/**
 * 获取当前进程ID
 *
 * @author wujiuye
 * @version 1.0 on 2019/9/7
 */
public class ProcessUtils {

    /**
     * 当前进程的id
     */
    public final static int PID;

    static {
        PID = getCurrentPid();
    }

    /**
     * 获取当前的进程id
     *
     * @return
     */
    private static int getCurrentPid() {
        try {
            String name = ManagementFactory.getRuntimeMXBean().getName();
            String[] pidAndInfo = name.split("@");
            return Integer.valueOf(pidAndInfo[0]);
        } catch (Exception e) {
            return -1;
        }
    }

}
