package com.wujiuye.dubbo.open;

import com.wujiuye.dubbo.interfaces.UserService;
import com.wujiuye.dubbo.interfaces.impl.UserServiceImpl;

public class OpenUserService extends BaseOpenService<UserService>{


    public void openService() {
        super.openService(new UserServiceImpl());
    }
}
