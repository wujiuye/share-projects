package com.wujiuye.asmuse.use2.service;

import com.wujiuye.asmuse.use1.User;

/**
 * @author wjy 2018/12/10
 */
public class UserService {

    public User queryUser(String useranme){
        User user = new User();
        user.setUsername(useranme);
        return user;
    }

}
