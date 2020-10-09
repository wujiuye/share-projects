package com.wujiuye.dubbo.interfaces.impl;

import com.wujiuye.dubbo.interfaces.UserService;
import com.wujiuye.dubbo.pojo.User;

import java.util.concurrent.ConcurrentHashMap;

public class UserServiceImpl implements UserService {

    private ConcurrentHashMap<String,User> users = new ConcurrentHashMap<>();

    @Override
    public User getUser(String username) {
        return users.get(username);
    }

    @Override
    public void savaUser(User user) {
        if(user==null||user.getUsername()==null)
            return;
        users.put(user.getUsername(),user);
    }
}
