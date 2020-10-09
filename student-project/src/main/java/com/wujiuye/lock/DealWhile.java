package com.wujiuye.lock;

/**
 * @author wujiuye
 * @version 1.0 on 2019/7/16 {描述：}
 */
public class DealWhile {


    private static final Runnable runnable = () -> {
        while (true) {

        }
    };

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            new Thread(runnable).start();
        }
    }

}
