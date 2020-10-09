package com.wujiuye.sck.common.util.jvm;

import oshi.SystemInfo;

/**
 * @author wujiuye
 * @version 1.0 on 2019/7/30
 */
public class ServiceMonitorHelper {

    /**
     * 获取系统设备信息
     *
     * @return
     */
    public static MachineInfo getSystemDeviceInfo(int options) {
        MachineInfo machineInfo = new MachineInfo();
        SystemInfo systemInfo = new SystemInfo();
        if ((options & Options.CPU) == Options.CPU) {
            machineInfo.setCpuInfo(OsInfoUtils.printCpuInfo(systemInfo));
        }
        if ((options & Options.MEMORY) == Options.MEMORY) {
            machineInfo.setMemoryInfo(OsInfoUtils.printMemoryInfo(systemInfo));
        }
        if ((options & Options.DISK) == Options.DISK) {
            machineInfo.setDiskInfos(OsInfoUtils.printDiskUsage(systemInfo));
        }
        if ((options & Options.JVM_HEAP) == Options.JVM_HEAP) {
            machineInfo.setJvmInfo(JvmInfoUtils.printJvmInfo());
        }
        if ((options & Options.JVM_STACK) == Options.JVM_STACK) {
            machineInfo.setThreadStack(JvmInfoUtils.printThreadInfo());
        }
        if ((options & Options.JVM_GCUTIL) == Options.JVM_GCUTIL) {
            machineInfo.setGcInfo(JvmInfoUtils.printGcUtilInfo());
        }
        return machineInfo;
    }

}
