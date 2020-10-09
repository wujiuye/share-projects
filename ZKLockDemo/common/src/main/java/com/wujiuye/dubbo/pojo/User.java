package com.wujiuye.dubbo.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@ToString
public class User implements Serializable {

    private String username;
    private String phone;
    private String password;

}
