package com.wujiuye.demo;


import com.wujiuye.asmaop.aop.AopManager;
import com.wujiuye.demo.aspj.UserServiceAop;
import com.wujiuye.demo.aspj.UserServiceAop2;
import com.wujiuye.demo.pojo.User;
import com.wujiuye.demo.service.UserService;

import java.io.IOException;

/**
 * 基于子类的aop实现demo
 * @author wjy 2018/12/10
 */
public class SubclassAopDemo {

    public static void testVoid() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        Object obj = AopManager.newAopInstance(new UserServiceAop2());
        System.out.println(obj);
        ((UserService) obj).sayHello();
    }

    public static void testObject() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        Object obj = AopManager.newAopInstance(new UserServiceAop());
        System.out.println("代理对象实例："+obj);
        User user = ((UserService) obj).queryUser("wujiuye");
        System.out.println("执行代理方法返回："+user.getUsername());
    }

    public static void main(String[] args) {
        try {
            testVoid();
//            testObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


}
