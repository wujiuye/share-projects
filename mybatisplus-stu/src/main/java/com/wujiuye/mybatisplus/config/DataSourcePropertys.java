package com.wujiuye.mybatisplus.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.database")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class DataSourcePropertys {

    private JdbcConnConfig db0;

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class JdbcConnConfig {
        private String driverClassName;
        private String url;
        private String username;
        private String password;
    }

}
