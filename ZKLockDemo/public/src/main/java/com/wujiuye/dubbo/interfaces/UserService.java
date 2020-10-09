package com.wujiuye.dubbo.interfaces;

import com.wujiuye.dubbo.pojo.User;

public interface UserService {

    User getUser(String username);


    void savaUser(User user);
}
