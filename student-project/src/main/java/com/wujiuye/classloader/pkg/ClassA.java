package com.wujiuye.classloader.pkg;

public class ClassA {

    public static void say() {
        System.out.println("my name is classA " + Thread.currentThread().getContextClassLoader());
        ClassB.say();
    }

}
