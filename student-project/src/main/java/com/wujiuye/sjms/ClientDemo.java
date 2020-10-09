package com.wujiuye.sjms;

public class ClientDemo {


    public static void main(String[] args) {
        // 将ComponentImpl1与ComponentImpl2组合
        // ComponentImpl1与ComponentImpl2都是叶子节点
        Composite composite1 = new Composite();
        composite1.addComponent(new ComponentImpl1());
        composite1.addComponent(new ComponentImpl2());

        // 将composite1与ComponentImpl3组合
        // composite1是数的节点，但非叶子节点
        // ComponentImpl3是叶子节点
        Composite rootComposite = new Composite();
        rootComposite.addComponent(composite1);
        rootComposite.addComponent(new ComponentImpl3());

        // rootComposite作为数的根
        rootComposite.doSomething();
    }

}
