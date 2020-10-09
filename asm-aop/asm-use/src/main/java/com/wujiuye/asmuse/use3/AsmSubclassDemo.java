package com.wujiuye.asmuse.use3;


import com.wujiuye.asmuse.use3.aop.AopManager;
import com.wujiuye.asmuse.use3.aoppoint.UserServiceAop;
import com.wujiuye.asmuse.use3.service.UserService;
import java.io.IOException;

/**
 * @author wjy 2018/12/10
 */
public class AsmSubclassDemo {

    public static void main(String[] args) {
        try {
            Object obj = AopManager.newAopInstance(new UserServiceAop());
            System.out.println(obj);
            ((UserService)obj).queryUser("wujiuye");
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
