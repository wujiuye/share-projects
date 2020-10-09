package com.wujiuye.asmuse.use0.ic;


/**
 * @author wjy 2017
 */
public class LogAopHndler implements AopHandler{

    @Override
    public void onMethodInvoke(String className, String methodName) {
        System.out.println("这是目标方法执行之前注入的方法===>"+className+","+methodName);
    }
}
