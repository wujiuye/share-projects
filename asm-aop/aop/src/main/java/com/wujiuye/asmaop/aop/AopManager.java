package com.wujiuye.asmaop.aop;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 管理代理实例的创建
 * @author wjy 2018/12/10
 */
public final class AopManager {

    /**
     * 根据切入点创建一个代理对象
     * @param aop
     * @return
     * @throws IOException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public static Object newAopInstance(Aop aop) throws IOException, IllegalAccessException, InstantiationException, ClassNotFoundException {

        String fileName = Thread.currentThread().getContextClassLoader().getResource("").getPath()
                + aop.getPoint().getTargerClass().getName().replace(".","/")+"$Proxy.class";
        File file = new File( fileName);

        //如果已经生成过了就直接加载了
//        if(file.exists()){
//            Class proxyClass =  Thread.currentThread().getContextClassLoader().loadClass(aop.getPoint().getTargerClass().getName()+"$Proxy");
//            return proxyClass.newInstance();
//        }

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassAdapter classAdapter = new AopClassAdpter(classWriter,aop);

        ClassReader classReader = new ClassReader(aop.getPoint().getTargerClass().getName());
        classReader.accept(classAdapter,ClassReader.SKIP_DEBUG);
        byte[] data = classWriter.toByteArray();

        //将新的类字节码保存到文件
        FileOutputStream fout = new FileOutputStream(file);
        fout.write(data);
        fout.close();

        //使用当前线程类加载其从刚生成的文件加载类
        Class proxyClass =  Thread.currentThread().getContextClassLoader().loadClass(aop.getPoint().getTargerClass().getName()+"$Proxy");
        return proxyClass.newInstance();
    }

}
