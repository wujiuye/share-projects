package com.wujiuye.sck.provider.client;

import feign.RetryableException;
import feign.Retryer;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;

import java.net.ConnectException;

/**
 * 使用Ribbon时不会起作用，也不需要
 *
 * @author wujiuye 2020/07/06
 */
@AutoConfigureBefore(FeignClientsConfiguration.class)
public class DefaultFeignRetryConfig {

    @Bean
    public Retryer retryer() {
        return new YcpaySrvRetry();
    }

    private static class YcpaySrvRetry implements Retryer {
        /**
         * 最大重试次数
         */
        private final static int retryerMax = 1;
        /**
         * 当前重试次数
         */
        private int currentRetryCnt = 0;

        @Override
        public void continueOrPropagate(RetryableException e) {
            if (currentRetryCnt > retryerMax) {
                throw e;
            }
            // 连接异常时重试
            if (e.getCause() instanceof ConnectException) {
                currentRetryCnt++;
                return;
            }
            throw e;
        }

        @Override
        public Retryer clone() {
            return new YcpaySrvRetry();
        }
    }

}
