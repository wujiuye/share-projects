package com.wujiuye.asmuse.use1;

/**
 * @author wjy 2018/12/10
 */
public interface UserService {

    User getUser(String username);

    User newUser(String username,String password,int age,boolean sex,String qq,String phone,String email);

}
