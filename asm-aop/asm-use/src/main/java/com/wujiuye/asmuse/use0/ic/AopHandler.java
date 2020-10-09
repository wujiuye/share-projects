package com.wujiuye.asmuse.use0.ic;

/**
 * @author wjy 2017
 */
public interface AopHandler {

    /**
     * 在代理对象的公有方法执行之前调用
     * @param className 类名
     * @param methodName    方法名
     */
    void onMethodInvoke(String className, String methodName);
}
