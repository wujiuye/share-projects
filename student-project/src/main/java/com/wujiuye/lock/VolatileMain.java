package com.wujiuye.lock;

/**
 * @author wujiuye
 * @version 1.0 on 2020/1/3 {描述：}
 */
public class VolatileMain {

    private int cnt = 0;
    private volatile boolean state = false;

    /**
     * 使用jit必要要确保代码被多次调用
     */
    public void doTest() {
        while (!state && cnt < 100000) {
            cnt++;
        }
        state = true;
    }

    // 将链接库下载放到$JAVA_HOME/jre/libs目录下
    // https://github.com/evolvedmicrobe/benchmarks/blob/master/hsdis-amd64.dylib?spm=a2c4e.10696291.0.0.13ce19a46a5XRa&file=hsdis-amd64.dylib
    public static void main(String[] args) throws InterruptedException {
        VolatileMain volatileMain = new VolatileMain();
        Thread thread = new Thread() {
            @Override
            public void run() {
                volatileMain.doTest();
            }
        };
        thread.start();
        System.out.println(volatileMain.cnt);
        thread.join();
    }

}
