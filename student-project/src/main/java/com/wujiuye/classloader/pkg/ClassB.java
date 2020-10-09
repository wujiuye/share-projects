package com.wujiuye.classloader.pkg;

public class ClassB {

    public static void say() {
        System.out.println("my name is classB " + Thread.currentThread().getContextClassLoader());
    }

}
