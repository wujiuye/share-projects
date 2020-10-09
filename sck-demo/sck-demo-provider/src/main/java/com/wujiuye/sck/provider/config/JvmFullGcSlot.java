package com.wujiuye.sck.provider.config;

import com.alibaba.csp.sentinel.context.Context;
import com.alibaba.csp.sentinel.node.DefaultNode;
import com.alibaba.csp.sentinel.slotchain.AbstractLinkedProcessorSlot;
import com.alibaba.csp.sentinel.slotchain.ResourceWrapper;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.alibaba.csp.sentinel.util.TimeUtil;
import com.wujiuye.sck.common.util.jvm.MachineInfo;
import com.wujiuye.sck.common.util.jvm.Options;
import com.wujiuye.sck.common.util.jvm.ServiceMonitorHelper;

import java.util.concurrent.atomic.AtomicReference;

public class JvmFullGcSlot extends AbstractLinkedProcessorSlot<DefaultNode> {

    private AtomicReference<Long> lastUpdateTime = new AtomicReference<>(TimeUtil.currentTimeMillis());
    private volatile long lastFgcCnt = Integer.MAX_VALUE;

    @Override
    public void entry(Context context, ResourceWrapper resourceWrapper, DefaultNode param, int count, boolean prioritized, Object... args) throws Throwable {
        long currentTime = TimeUtil.currentTimeMillis();
        if (currentTime - lastUpdateTime.get() > 1000) {
            if (lastUpdateTime.compareAndSet(lastUpdateTime.get(), currentTime)) {
                MachineInfo machineInfo = ServiceMonitorHelper.getSystemDeviceInfo(Options.JVM_GCUTIL);
                MachineInfo.GcInfo gcInfo = machineInfo.getGcInfo();
                int curGcCnt = gcInfo.getFGC().intValue();
                if (curGcCnt < lastFgcCnt) {
                    lastFgcCnt = curGcCnt;
                } else if (curGcCnt - lastFgcCnt > 1) {
                    lastFgcCnt = curGcCnt;
                    throw new SystemBlockException(resourceWrapper.getName(), "jvm full gc");
                }
            }
        }
        fireEntry(context, resourceWrapper, param, count, prioritized, args);
    }

    @Override
    public void exit(Context context, ResourceWrapper resourceWrapper, int count, Object... args) {
        fireExit(context, resourceWrapper, count, args);
    }

}
