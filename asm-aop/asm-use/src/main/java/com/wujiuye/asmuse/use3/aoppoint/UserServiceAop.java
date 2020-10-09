package com.wujiuye.asmuse.use3.aoppoint;


import com.wujiuye.asmuse.use3.aop.Aop;
import com.wujiuye.asmuse.use3.aop.Point;

/**
 * 定义切面类
 * @author wjy 2018/12/10
 */
public class UserServiceAop implements Aop {


    @Override
    public Point getPoint() {
        Point point = new Point(
                com.wujiuye.asmuse.use3.service.UserService.class,
                "queryUser",
                "(Ljava/lang/String;)Lcom/wujiuye/asmuse/use1/User;",
                new Class[]{
                        String.class
                });
        return point;
    }

    @Override
    public void doBef(Object targer) {
        System.out.println("目标方法执行之前被拦截:[目标对象："+targer+"]");
    }

    @Override
    public void doAft(Object targer,Object result) {
        System.out.println("目标方法执行之后被拦截:[目标对象："+targer+"]");
    }
}
