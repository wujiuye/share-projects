package com.wujiuye.springaop2;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
 * <p>
 * 微信公众号id：code_skill
 * QQ邮箱：419611821@qq.com
 * 微信号：www_wujiuye_com
 * <p>
 * ======================^^^^^^^==============^^^^^^^============
 *
 * @ 作者       |   吴就业 www.wujiuye.com
 * ======================^^^^^^^==============^^^^^^^============
 * @ 创建日期      |   Created in 2018年12月20日
 * ======================^^^^^^^==============^^^^^^^============
 * @ 所属项目   |   lock
 * ======================^^^^^^^==============^^^^^^^============
 * @ 类功能描述    |
 * ======================^^^^^^^==============^^^^^^^============
 * @ 版本      |   ${version}
 * ======================^^^^^^^==============^^^^^^^============
 */

@Component
@Aspect
public class LogAspectj {


    @Before("execution(public java.util.Map<String, Object> com.wujiuye.springaop2.UserServiceImpl.getUser(..))")
    public void funcStartLog(JoinPoint joinpoint){
        System.out.println("方法执行之前=======");
    }

    @After("execution(public java.util.Map<String, Object> com.wujiuye.springaop2.UserServiceImpl.getUser(..))")
    public void funcEndLog(JoinPoint joinpoint){
        System.out.println("方法执行之后=======");
    }
}
