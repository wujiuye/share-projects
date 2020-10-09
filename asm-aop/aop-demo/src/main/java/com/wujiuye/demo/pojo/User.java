package com.wujiuye.demo.pojo;

/**
 * @author wjy 2018/12/10
 */
public class User {

    private String username;
    private String passowrd;
    private boolean sex;
    private int age;
    private String qq;
    private String phone;
    private String email;

    public void copy(){
        Object[] args = new Object[10];
        args[1] = this;
        args[7] = this;
        args[8] = this;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassowrd() {
        return passowrd;
    }

    public void setPassowrd(String passowrd) {
        this.passowrd = passowrd;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
