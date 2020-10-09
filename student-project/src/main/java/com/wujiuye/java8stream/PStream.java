package com.wujiuye.java8stream;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wujiuye
 * @version 1.0 on 2019/9/25 {描述：}
 */
public class PStream {

    public static void main(String[] args) throws InterruptedException {
        final List<Integer> list = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            list.add(1);
        }
        for (int i = 1; i <= 50; i++) {
            new Thread("test-" + i) {
                String currentThreaName = this.getName();

                @Override
                public void run() {
                    list.parallelStream()
                            .forEach(numbser -> {
                                Thread c = Thread.currentThread();
                                System.out.println(currentThreaName + "===> "
                                        + c.getClass().getName() + ":" + c.getName() + ":" + c.getId());
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            });
                }
            }.start();
        }
        Thread.sleep(Integer.MAX_VALUE);
    }

}
