package com.wujiuye.asmuse.use1;

/**
 * @author wjy 2018/12/10
 */
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(String username) {
        com.wujiuye.asmuse.use1.User user = new com.wujiuye.asmuse.use1.User();
        user.setUsername(username);
        return user;
    }

    @Override
    public User newUser(String username, String password, int age, boolean sex, String qq, String phone, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassowrd(password);
        user.setQq(qq);
        user.setEmail(email);
        user.setSex(sex);
        user.setAge(age);
        user.setPhone(phone);
        return user;
    }
}
