package com.wujiuye.sync;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * @author wujiuye
 * @version 1.0 on 2019/11/12 {描述：}
 */
public class ConditionSyncMain {

    private static Map<String, Long> userAccessTimeMap = new ConcurrentSkipListMap<>();
    private static Map<String, Integer> dataCacheMap = new ConcurrentSkipListMap<>();

    public static void main(String[] args) {
        ConditionSyncMain conditionSync = new ConditionSyncMain();
        IntStream.range(0, 3).forEachOrdered(id -> new Thread() {
            @Override
            public void run() {
                while (!Thread.interrupted()) {
                    System.out.println(conditionSync.dataBy("user_" + id));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start());
    }

    private Object dataBy(String username) {
        //synchronized (this){
        synchronized (username.intern()) {
            Integer object;
            Long lastAccessTime = userAccessTimeMap.get(username);
            userAccessTimeMap.put(username, System.currentTimeMillis());
            if (lastAccessTime == null
                    || lastAccessTime + 1000 < System.currentTimeMillis()) {
                // 远程调用
                object = rpcBy(username);
                dataCacheMap.put(username, object);
            } else {
                // 缓存取
                object = dataCacheMap.get(username);
            }
            return object;
        }
        //}
    }

    private static Map<String, AtomicInteger> simulatio = new ConcurrentSkipListMap<>();

    private static Integer rpcBy(String username) {
        if (!simulatio.containsKey(username)) {
            simulatio.put(username, new AtomicInteger(0));
        }
        return simulatio.get(username).incrementAndGet();
    }

}
