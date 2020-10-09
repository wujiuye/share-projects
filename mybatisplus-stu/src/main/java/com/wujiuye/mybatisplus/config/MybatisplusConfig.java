package com.wujiuye.mybatisplus.config;

import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.plugins.parser.ISqlParser;
import com.baomidou.mybatisplus.plugins.parser.SqlInfo;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisplusConfig {

    /**
     * 要使用分页查询功能，就需要配置分页拦截器
     *
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor interceptor =  new PaginationInterceptor();
//        interceptor.setSqlParser(new ISqlParser() {
//            @Override
//            public SqlInfo optimizeSql(MetaObject metaObject, String sql) {
//                return null;
//            }
//        });
        return interceptor;
    }

}
