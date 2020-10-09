package com.wujiuye.asmaop.aop;

/**
 * 切入点定义
 * @author wjy 2018/12/10
 */
public class Point {

    //方法的类型对象
    private Class targerClass;
    //方法名
    private String funcName;
    //方法签名，如"()Ljava.lang.String;"
    private String funcDescribe;
    //方法参数类型列表，必须顺序
    private Class[] funcParamClasses;

    public Point(Class targerClass, String funcName, String funcDescribe, Class[] funcParamClasses) {
        this.targerClass = targerClass;
        this.funcName = funcName;
        this.funcDescribe = funcDescribe;
        this.funcParamClasses = funcParamClasses;
    }

    public Class getTargerClass() {
        return targerClass;
    }

    public String getFuncName() {
        return funcName;
    }

    public String getFuncDescribe() {
        return funcDescribe;
    }

    public Class[] getFuncParamClasses(){
        return funcParamClasses;
    }
}
