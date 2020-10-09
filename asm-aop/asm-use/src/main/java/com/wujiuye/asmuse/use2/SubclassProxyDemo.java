package com.wujiuye.asmuse.use2;

import com.wujiuye.asmuse.use2.aop.*;
import com.wujiuye.asmuse.use2.service.UserService;

/**
 * @author wjy 2018/12/10
 */
public class SubclassProxyDemo {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, ClassNotFoundException {

//        Thread.currentThread().getContextClassLoader().loadClass(
//                "com.wujiuye.asmuse.use2.service.UserService$Proxy"
//        );

        UserService userService = (UserService) AopManager.newProxy(new UserServiceAop());
        System.out.println(userService.queryUser("wujiuye"));
    }
}
