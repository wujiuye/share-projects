package com.wujiuye.demo.service;


import com.wujiuye.demo.pojo.User;

/**
 * @author wjy 2018/12/10
 */
public class UserService {

    public User queryUser(String useranme){
        User user = new User();
        user.setUsername(useranme);
        return user;
    }

    public void sayHello(){
        System.out.println("hello!>..");
    }
}
