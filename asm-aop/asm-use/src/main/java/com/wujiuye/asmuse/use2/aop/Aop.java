package com.wujiuye.asmuse.use2.aop;


/**
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
     * @param targer 目标对象
     */
    void doBef(Object targer);

    /**
     * 方法执行完成之后插入
     * @param targer 目标对象
     * @param result    目标方法返回的结果
     */
    void doAft(Object targer,Object result);

}
