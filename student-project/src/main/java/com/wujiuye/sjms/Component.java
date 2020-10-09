package com.wujiuye.sjms;

// 组件(Component)
public interface Component {
    // 行为
    void doSomething();
}
// 叶子(Leaf)
class ComponentImpl1 implements Component {
    @Override
    public void doSomething() {
        System.out.println("impl1");
    }
}
// 叶子(Leaf)
class ComponentImpl2 implements Component {
    @Override
    public void doSomething() {
        System.out.println("impl2");
    }
}
// 叶子(Leaf)
class ComponentImpl3 implements Component{
    @Override
    public void doSomething() {
        System.out.println("impl3");
    }
}
