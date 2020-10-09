package com.wujiuye.lock;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wujiuye
 * @version 1.0 on 2019/3/30 {描述：}
 */
public class SyncWRTest {

    private /*volatile*/ Map<String, String> map = new HashMap<>();

    public void run(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    synchronized (map) {
                        try {
                            System.out.println(map.size());
                        }catch (Exception e){

                        }
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Map<String, String> tmpmap = new HashMap<>();
                    tmpmap.put("xxx", "yyyy");
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (map) {
                        map = tmpmap;//一条赋值字节码，但字节码并非机器码。线程安全，但其它线程还在使用副本。只能保证不会因为
                        //赋值导致map在一瞬间为空的出现
                    }
                }
            }
        }).start();

        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        System.out.println(map.get("xxx"));
                        map = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public static void main(String[] args) {
        SyncWRTest wrTest = new SyncWRTest();
        //wrTest.run();
        wrTest.run2();
    }


    private void run2(){
        map.put("xxx","xxxxxx");
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    Map<String, String> tmpmap = new HashMap<>();
                    tmpmap.put("xxx", "yyyy");
                    map = tmpmap;
                    System.out.println(System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        for (int i=0;i<1000;i++){
            new Thread("thread"+i){
                @Override
                public void run() {
                    while (true){
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(map.get("xxx")+System.currentTimeMillis());
                    }
                }
            }.start();
        }
    }
}
