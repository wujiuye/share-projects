package com.wujiuye.sjms;

import java.util.ArrayList;
import java.util.List;

// 组合(Composite)
public class Composite implements Component {

    private List<Component> components;

    public Composite() {
        components = new ArrayList<>();
    }

    public void addComponent(Component component) {
        this.components.add(component);
    }

    @Override
    public void doSomething() {
        for (Component component : components) {
            component.doSomething();
        }
    }

}
