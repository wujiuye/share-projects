package com.wujiuye.sck.provider.model.props;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * @author wujiuye 2020/06/05
 */
@Component
@ConfigurationProperties(prefix = "sck-demo")
@RefreshScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DemoProps {

    private String message;

}
