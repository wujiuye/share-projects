# asm-aop
使用asm自己实现基于子类的动态代理，使用需要定义切入点和切面，在运行时在classpath下生成一个类名+$Proxy的代理类字节码文件，在父类方法执行之前和之后植入字节码实现动态代理。必须要对字节码理解才能看懂源码哦。


>微信公众号：**[code_skill](#jump_10)**
关注我，一起进步;


###使用步骤

第一步：定义一个业务类，不需要实现接口

```
/**
 * @author wjy 2018/12/10
 */
public class UserService {

    public User queryUser(String useranme){
        User user = new User();
        user.setUsername(useranme);
        return user;
    }

    public void sayHello(){
        System.out.println("hello!>..");
    }
}
```

第二步：定义一个切面类，需要实现Aop接口，并且实现接口的方法。getPoint方法定义一个切入点返回，doBef是前置通知，doAft是后置通知
```
/**
 * 定义切面类
 * @author wjy 2018/12/10
 */
public class UserServiceAop implements Aop {


    @Override
    public Point getPoint() {
        Point point = new Point(
                com.wujiuye.demo.service.UserService.class,
                "queryUser",
                "(Ljava/lang/String;)Lcom/wujiuye/demo/pojo/User;",
                new Class[]{
                        String.class
                });
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

``` 

###来看下实现的效果
```
    public static void testObject() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        Object obj = AopManager.newAopInstance(new UserServiceAop());
        System.out.println("代理对象实例："+obj);
        User user = ((UserService) obj).queryUser("wujiuye");
        System.out.println("执行代理方法返回："+user.getUsername());
    }

    public static void main(String[] args) {
        try {
            testObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
```

控制台输出
```
代理对象实例：com.wujiuye.demo.service.UserService$Proxy@5674cd4d
目标方法执行之前被拦截:[目标对象：com.wujiuye.demo.service.UserService$Proxy@5674cd4d]
参数个数：1
目标方法执行之后被拦截:[目标对象：com.wujiuye.demo.service.UserService$Proxy@5674cd4d]
执行代理方法返回：wujiuye
```

###对切入点方法可以无返回值，也可以无参数
这次要切入的是一个无返回值且无参数的方法
```
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
```
main方法创建一个UserService的代理对象，并调用sayHello方法。
```
/**
 * 基于子类的aop实现demo
 * @author wjy 2018/12/10
 */
public class SubclassAopDemo {

    public static void testVoid() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        Object obj = AopManager.newAopInstance(new UserServiceAop2());
        System.out.println(obj);
        ((UserService) obj).sayHello();
    }

    public static void main(String[] args) {
        try {
            testVoid();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


}
```

查看输出：
```
com.wujiuye.demo.service.UserService$Proxy@5674cd4d
目标方法执行之前被拦截:[目标对象：com.wujiuye.demo.service.UserService$Proxy@5674cd4d]
参数个数：0
hello!>..
目标方法执行之后被拦截:[目标对象：com.wujiuye.demo.service.UserService$Proxy@5674cd4d]

```

---
欢迎关注个人微信公众号，一起学习，一起进步。。。。






 
