package com.wujiuye.mybatisplus.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Resource
    private DataSourcePropertys propertys;

    @Bean
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(propertys.getDb0().getUrl());
        datasource.setUsername(propertys.getDb0().getUsername());
        datasource.setPassword(propertys.getDb0().getPassword());
        datasource.setDriverClassName(propertys.getDb0().getDriverClassName());
        return datasource;
    }

}
