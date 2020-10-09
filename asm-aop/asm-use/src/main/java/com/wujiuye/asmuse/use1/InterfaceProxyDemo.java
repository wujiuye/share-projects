package com.wujiuye.asmuse.use1;

import com.wujiuye.asmuse.use2.aop.Aop;
import org.objectweb.asm.*;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.*;

import static jdk.internal.org.objectweb.asm.Opcodes.V1_8;
import static org.objectweb.asm.Opcodes.*;

/**
 * @author wjy 2018/12/10
 */
public class InterfaceProxyDemo {

    public static void main(String[] args) throws IOException {

        ClassWriter classWriter = new ClassWriter(0);
        TraceClassVisitor traceClassVisitor = new TraceClassVisitor(classWriter, new PrintWriter(System.out));

        traceClassVisitor.visit(V1_8, ACC_PUBLIC, "com/wujiuye/asmuse/use1/UserService$proxy", null, "java/lang/Object", new String[]{"com/wujiuye/asmuse/use1/UserService"});

        //添加构造方法
        MethodVisitor mv = traceClassVisitor.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);//访问该方法的最大堆栈大小和最大本地变量数量。
        mv.visitEnd();

        // 添加add方法
        mv = traceClassVisitor.visitMethod(ACC_PUBLIC, "getUser", "(Ljava/lang/String;)Lcom/wujiuye/asmuse/use1/User;", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 1);


        // new一个指定类型的实例
        mv.visitTypeInsn(NEW, "Lcom/wujiuye/asmuse/use1/User;");
        mv.visitInsn(DUP);
        //调用实例的无参数构造方法
        mv.visitMethodInsn(INVOKESPECIAL, "Lcom/wujiuye/asmuse/use1/User;", "<init>", "()V");
        //将结果保存到第二个参数，astore_2
        mv.visitVarInsn(ASTORE, 2);

        //调用对象是aload_2
        mv.visitVarInsn(ALOAD,2);
        //参数1是aload_1
        mv.visitVarInsn(ALOAD,1);
        //调用aload_2引用的对象的方法
        mv.visitMethodInsn(INVOKEVIRTUAL,"Lcom/wujiuye/asmuse/use1/User;","setUsername","(Ljava/lang/String;)V");

        //将aload_2返回
        mv.visitVarInsn(ALOAD,2);
        mv.visitInsn(ARETURN);

        mv.visitMaxs(2, 3);
        mv.visitEnd();

        traceClassVisitor.visitEnd();

        FileOutputStream fos = new FileOutputStream(new File("/Users/wjy/MyProjects/asm-aop/asm-use/src/main/java/com/wujiuye/asmuse/use1/UserService$proxy.class"));
        fos.write(classWriter.toByteArray());
        fos.close();
    }

}
