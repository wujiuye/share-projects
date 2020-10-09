package com.wujiuye.asmuse.use0;

import com.wujiuye.asmuse.use0.bean.User;
import com.wujiuye.asmuse.use0.bean.UserLoginDao;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * asm.jar包下载地址：http://maven.outofmemory.cn/asm/asm/3.3.1/
 * @author wjy 2017
 */
public class Main {


    public static Object newAopInstance(String className) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassAdapter classAdapter = new AopUtilsClassAdpter(classWriter);
        /**
         * public ClassReader(String var1) throws IOException {
         *         this(ClassLoader.getSystemResourceAsStream(var1.replace('.', '/') + ".class"));
         *     }
         *
         */
        ClassReader classReader = new ClassReader(className);
        classReader.accept(classAdapter,ClassReader.SKIP_DEBUG);

        AopClassLoader aopClassLoader = new AopClassLoader();
        byte[] data = classWriter.toByteArray();


        String fileName = Thread.currentThread().getContextClassLoader().getResource("").getPath()
                    + className.replace(".","/")+"$Proxy.class";
        //将新的类字节码保存到文件
        File file = new File( fileName);
        if(!file.exists()){
            FileOutputStream fout = new FileOutputStream(file);
            fout.write(data);
            fout.close();
        }


         Class proxyClass =  Thread.currentThread().getContextClassLoader().loadClass(className+"$Proxy");
        return proxyClass.newInstance();

        //返回一个修改后的Class的实例
//        return aopClassLoader.defineClassFromClassFile(
//                className+"$Proxy",data).newInstance();
    }

    public static void main(String[] args) {
        //AopHandler aopHandler = new LogAopHndler();
        //14: invokeinterface #22,  4 #InterfaceMethod wjy/asm/ic/AopHandler.onMethodInvoke:(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
        //aopHandler.onMethodInvoke("","",args);
        try {
            Object obj = newAopInstance("com.wujiuye.asmuse.use0.bean.UserLoginDao");
            System.out.println(obj);
            ((UserLoginDao)obj).checkUser(new User());
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


