package com.wujiuye.r2dbc;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;

/**
 * r2dbc动态数据源切面
 *
 * @author wujiuye 2020/10/19
 */
@Component
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DynamicDataSourceAop {

    /**
     * 定义redis动态db切换切点
     */
    @Pointcut(value = "@annotation(com.wujiuye.r2dbc.DataSource)")
    public void point() {
    }

    /**
     * 实现动态切换DB
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around(value = "point()")
    public Object aroudAop(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        DataSource dataSource = method.getAnnotation(DataSource.class);
        if (method.getReturnType() == Mono.class) {
            return RoutingConnectionFactory.warpDataSource((Mono<?>) pjp.proceed(), dataSource.value());
        } else {
            return RoutingConnectionFactory.warpDataSource((Flux<?>) pjp.proceed(), dataSource.value());
        }
    }

}
