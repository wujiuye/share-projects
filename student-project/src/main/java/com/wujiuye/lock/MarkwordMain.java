package com.wujiuye.lock;

import org.openjdk.jol.info.ClassLayout;

public class MarkwordMain {

    private static final String SPLITE_STR = "===========================================";
    private static User USER = new User();

    private static void printf() {
        String currentThread = Thread.currentThread().getName();
        System.out.println(SPLITE_STR);
        System.out.println("current thread name: " + currentThread + "\t current thread id: " + Long.toBinaryString(Thread.currentThread().getId()));
        System.out.println(ClassLayout.parseInstance(USER).toPrintable());
        System.out.println(SPLITE_STR);
    }

    private static Runnable RUNNABLE = () -> {
        while (!Thread.interrupted()) {
            synchronized (USER) {
                printf();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    public static void main(String[] args) throws InterruptedException {
        // System.out.println(System.getProperties());
        // System.out.println("obj size (byte) :" + ClassLayout.parseInstance(USER).instanceSize());
        // System.out.println(Integer.toHexString(USER.hashCode()));
        // printf();
        for (int i = 0; i < 3; i++) {
            new Thread(RUNNABLE).start();
            Thread.sleep(1000);
        }
        Thread.sleep(Integer.MAX_VALUE);
    }

}
