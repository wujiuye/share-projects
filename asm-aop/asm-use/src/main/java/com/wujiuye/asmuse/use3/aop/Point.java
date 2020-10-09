package com.wujiuye.asmuse.use3.aop;

/**
 * @author wjy 2018/12/10
 */
public class Point {

    private Class targerClass;
    private String funcName;
    private String funcDescribe;
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
