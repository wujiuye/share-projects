package com.wujiuye.demo.aspj;


import com.wujiuye.asmaop.aop.Aop;
import com.wujiuye.asmaop.aop.Point;

/**
 * 定义切面类
 * @author wjy 2018/12/10
 */
public class UserServiceAop2 implements Aop {


    @Override
    public Point getPoint() {
        Point point = new Point(
                com.wujiuye.demo.service.UserService.class,
                "sayHello",
                "()V",
                new Class[]{});
        return point;
    }

    @Override
    public void doBef(Object targer,Object[] args) {
        System.out.println("目标方法执行之前被拦截:[目标对象："+targer+"]");
        System.out.println("参数个数："+(args==null?0:args.length));
    }

    @Override
    public void doAft(Object targer,Object result) {
        System.out.println("目标方法执行之后被拦截:[目标对象："+targer+"]");
    }
}
