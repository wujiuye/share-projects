package com.wujiuye.jvm;

import java.io.IOException;

public class UncaughtExceptionHandlerTest {

    private static class TaskThread extends Thread {

        @Override
        public void run() {
            throw new NullPointerException("thread-" + Thread.currentThread().getId() + " Exception");
        }
    }

    public static void main(String[] args) throws IOException {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("这里是全局异常处理 ====> " + t.getId() + "==> " + e.getLocalizedMessage());
            }
        });

        for (int i = 0; i < 10; i++) {
            Thread thread = new TaskThread();
            if (i == 0) {
                thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread t, Throwable e) {
                        System.out.println("这是为当前线程设置的异步处理器。===> " + t.getId());
                    }
                });
            }
            thread.start();
        }

        System.in.read();
    }

}
