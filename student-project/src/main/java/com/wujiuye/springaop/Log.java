package com.wujiuye.springaop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.aop.framework.adapter.MethodBeforeAdviceInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

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
public class Log implements MethodInterceptor,
        BeanNameAware,
        BeanFactoryAware,
        BeanClassLoaderAware,
        InitializingBean {

    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    private String thisBeanName;

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();
        System.out.println(thisBeanName+":"+"目标方法执行之前被调用====》"+method.getDeclaringClass().getName()+"."+method.getName());
        /**
         * MethodInvocation实现了Joinpoint接口
         * 同时org.springframework.aop.framework.ReflectiveMethodInvocation也实现了MethodInvocation接口
         * 所以调用methodInvocation的proceed方法就是调用ReflectiveMethodInvocation的proceed方法，
         * 这就是链式调用
         *
         * ReflectiveMethodInvocation的proceed方法的代码：
         * ((MethodInterceptor)interceptorOrInterceptionAdvice).invoke(this);
         *
         * 切入点
         * public interface Joinpoint {
         *     Object proceed() throws Throwable;
         *
         *      //代理的目标类
         *     Object getThis();
         *
         *     AccessibleObject getStaticPart();
         * }
         */
        try {
            /**
             * public Object proceed() throws Throwable {
             *         if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
             *             //当所有的切入通知都被调用完成之后，调用invokeJoinpoint方法执行最终的被代码对象的目标方法
             *             return this.invokeJoinpoint();
             *         } else {
             *             Object interceptorOrInterceptionAdvice = this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
             *             if (interceptorOrInterceptionAdvice instanceof InterceptorAndDynamicMethodMatcher) {
             *                 InterceptorAndDynamicMethodMatcher dm = (InterceptorAndDynamicMethodMatcher)interceptorOrInterceptionAdvice;
             *                 Class<?> targetClass = this.targetClass != null ? this.targetClass : this.method.getDeclaringClass();
             *                 return dm.methodMatcher.matches(this.method, targetClass, this.arguments) ? dm.interceptor.invoke(this) : this.proceed();
             *             } else {
             *                  //这句话就是执行到这里的原因
             *                 return ((MethodInterceptor)interceptorOrInterceptionAdvice).invoke(this);
             *             }
             *         }
             *     }
             */
            //又回调了org.springframework.aop.framework.ReflectiveMethodInvocation的proceed方法
            //这也是可以为同一个方法切入多个通知的原因
            Object result = methodInvocation.proceed();
            return result;
        }catch (Exception e){
            System.out.println(thisBeanName+":"+"目标方法执行异常被调用====》"+method.getDeclaringClass().getName()+"."+method.getName());
            throw e;
        }finally {
            System.out.println(thisBeanName+":"+"目标方法执行之后被调用====》" + method.getDeclaringClass().getName() + "." + method.getName());
        }
    }

    @Override
    public void setBeanName(String s) {
        this.thisBeanName = s;
        System.out.println("获取到当前bean的id："+thisBeanName+"[步骤："+atomicInteger.getAndIncrement()+"]");
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        System.out.println(thisBeanName+"的类加载为："+classLoader.getClass().getName()+"[步骤："+atomicInteger.getAndIncrement()+"]");
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println(thisBeanName+"的bean工厂为："+beanFactory.getClass().getName()+"[步骤："+atomicInteger.getAndIncrement()+"]");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("在对象new出来之后且注入完成属性（依赖）之后被调用：afterPropertiesSet"+"[步骤："+atomicInteger.getAndIncrement()+"]");
    }
}
