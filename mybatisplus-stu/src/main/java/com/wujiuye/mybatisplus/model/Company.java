package com.wujiuye.mybatisplus.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@TableName("company")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Company {

    @TableId
    private Long id;
    private String company;
    private String password;
    private String email;
    // 不开启dbColumnUnderline自动驼峰映射则需要使用@TableField指定数据库表的字段名
    // @TableField("api_token")
    private String apiToken;
    private String details;
    // @TableField("create_tm")
    private Date createTm;
    // @TableField("modified_tm")
    private Date modifiedTm;

}