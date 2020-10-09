package com.wujiuye.dubbo.sub;

import com.wujiuye.dubbo.interfaces.UserService;

public class SubUserService extends BaseSubService<UserService>{

    public SubUserService() {
        super(UserService.class);
    }

}
