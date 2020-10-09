package com.wujiuye.asmaop.aop;

/**
 * 切面定义，切面必须要实现该接口
 * @author wjy 2018/12/10
 */
public interface Aop {

    /**
     * 获取切入点
     * @return
     */
    Point getPoint();

    /**
     * 目标方法执行之前插入
     * @param targer 目标对象（获取到的是代理对象本身，即代理类中传递的this）
     * @param args  调用目标方法的参数
     */
    void doBef(Object targer,Object[] args);

    /**
     * 方法执行完成之后插入
     * @param targer 目标对象
     * @param result    目标方法返回的结果
     */
    void doAft(Object targer, Object result);

}
