package com.wujiuye.asmuse.use2.aop;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.*;

import static jdk.internal.org.objectweb.asm.Opcodes.V1_8;
import static org.objectweb.asm.Opcodes.*;

/**
 * 异常1：
 * Exception in thread "main" java.lang.VerifyError: Operand stack overflow
 * 方法堆栈大小没设置
 * Exception Details:
 *   Location:
 *     com/wujiuye/asmuse/use2/service/UserService$Proxy.setAop(Lcom/wujiuye/asmuse/use2/aop/UserServiceAop;)V @0: aload_0
 *   Reason:
 *     Exceeded max stack size.
 *   Current Frame:
 *     bci: @0
 *     flags: { }
 *     //本地变量
 *     locals: { 'com/wujiuye/asmuse/use2/service/UserService$Proxy', 'com/wujiuye/asmuse/use2/aop/UserServiceAop' }
 *     //堆栈
 *     stack: { }
 *   Bytecode:
 *     0x0000000: 2a2b b500 0a
 *
 * 异常2：
 *  Exception in thread "main" java.lang.ClassFormatError: Class name contains illegal character '.' in descriptor in class file com/wujiuye/asmuse/use2/service/UserService$Proxy
 *  方法签名写错导致的，将'／'写成了'.',如："(Ljava.lang.Object;Ljava.lang.Object;)V" 是错误的
 *
 * @author wjy 2018/12/10
 */
public final class AopManager {

    public static Object newProxy(Aop aop) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        TraceClassVisitor visitor = new TraceClassVisitor(classWriter, new PrintWriter(System.out));
        createProxy(visitor, aop);
        visitor.visitEnd();
        savaProxyClassFile(classWriter, Thread.currentThread().getContextClassLoader().getResource("").getPath() + aop.getPoint().getTargerClass().getName().replace(".", "/") + "$Proxy.class");
        return newProxyInstance(aop.getPoint().getTargerClass().getName() + "$Proxy");
    }

    private static void createProxy(TraceClassVisitor visitor, Aop aop) {
        Point point = aop.getPoint();
        //创建子类，基于子类的代理
        visitor.visit(V1_8, ACC_PUBLIC, point.getTargerClass().getName().replace(".", "/") + "$Proxy",
                null, point.getTargerClass().getName().replace(".", "/"), null);

        //添加一个字段
        FieldVisitor fieldVisitor = visitor.visitField(ACC_PRIVATE, "aop", Type.getDescriptor(aop.getClass())
                , null, null);
        fieldVisitor.visitEnd();

        insertAspObjGetter(visitor,aop);
        insertAspObjSetter(visitor,aop);
        insertCreateAspObj(visitor,aop);
        insertProxyFunc(visitor,aop);

    }


    private static void insertAspObjGetter(TraceClassVisitor visitor, Aop aop) {
        Point point = aop.getPoint();
        //get方法
        MethodVisitor mvGet = visitor.visitMethod(ACC_PUBLIC, "getAop", "()" + Type.getDescriptor(aop.getClass()), null, null);
        mvGet.visitCode();
        mvGet.visitVarInsn(ALOAD, 0);
        mvGet.visitFieldInsn(GETFIELD, point.getTargerClass().getName().replace(".", "/") + "$Proxy", "aop", Type.getDescriptor(aop.getClass()));
        mvGet.visitInsn(ARETURN);
        //操作数栈大小最多同时(即没有调用方法导操作数弹出栈顶，也没有xxstore指令保存)使用xxload指令一条，所以为1;局部变量表大小：this(1)=1
        mvGet.visitMaxs(1, 1);
        mvGet.visitEnd();
    }

    private static void insertAspObjSetter(TraceClassVisitor visitor, Aop aop) {
        Point point = aop.getPoint();
        //为这个字段生成get和set方法，因为不是静态的字段
        MethodVisitor mvSet = visitor.visitMethod(ACC_PUBLIC, "setAop", "(" + Type.getDescriptor(aop.getClass()) + ")V", null, null);
        mvSet.visitCode();
        mvSet.visitVarInsn(ALOAD, 0);
        mvSet.visitVarInsn(ALOAD, 1);
        mvSet.visitFieldInsn(PUTFIELD, point.getTargerClass().getName().replace(".", "/") + "$Proxy", "aop",  Type.getDescriptor(aop.getClass()));
        //return不能少，否则会报Exception in thread "main" java.lang.VerifyError: Operand stack overflow
        mvSet.visitInsn(RETURN);
        //操作数栈大小最多同时(即没有调用方法导操作数弹出栈顶，也没有xxstore指令保存)使用xxload指令两条，所以为2;局部变量表大小：this(1)+参数aop(1)=2
        mvSet.visitMaxs(2,2);
        mvSet.visitEnd();
    }

    private static void insertCreateAspObj(TraceClassVisitor visitor, Aop aop) {
        Point point = aop.getPoint();
        //添加构造方法
        MethodVisitor mv = visitor.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        //super();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, point.getTargerClass().getName().replace(".", "/"), "<init>", "()V");
        //初始化aop实例赋值给var1
        mv.visitTypeInsn(NEW, Type.getDescriptor(aop.getClass()));
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, Type.getDescriptor(aop.getClass()), "<init>", "()V");
        mv.visitVarInsn(ASTORE, 1);
        //将var1赋值给this.aop
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitFieldInsn(PUTFIELD, point.getTargerClass().getName().replace(".", "/") + "$Proxy", "aop", Type.getDescriptor(aop.getClass()));
        //结束方法
        mv.visitInsn(RETURN);
        /**
         * MAXSTACK和MAXLOCALS的值就是操作数栈和局部变量表的大小。
         *
         * 局部变量表和操作数栈中的每个槽(slot)可以保存除long和double之外的任意java值，
         * 而long和double需要两个槽，比如向局部变量表储存一个int和一个long，则表中第一个位置是int值，第二和第三个位置存的是long值。
         *
         * 还有一点需要注意，如果是非静态方法，局部变量表的第0个位置为"this"。
         */
        //操作数栈大小最多同时(即没有调用方法导操作数弹出栈顶，也没有xxstore指令保存)使用xxload指令两条，所以为2;局部变量表大小：this(1)+参数aop(1)=2
        mv.visitMaxs(2, 2);
        mv.visitEnd();
    }

    private static void insertProxyFunc(TraceClassVisitor visitor, Aop aop) {
        Point point = aop.getPoint();
        //切入目标方法，目标方法执行之前，目标方法执行之后
        MethodVisitor mv = visitor.visitMethod(ACC_PUBLIC, point.getFuncName(), point.getFuncDescribe(), null, null);
        mv.visitCode();

        //调用之前的方法
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, point.getTargerClass().getName().replace(".", "/") + "$Proxy", "aop", Type.getDescriptor(aop.getClass()));
        //设置参数
        mv.visitVarInsn(ALOAD, 0);
        //INVOKEINTERFACE指令第二个参数是设置接口的类型签名，而不是实现类，所以aop实例必须是实现Aop接口的
        mv.visitMethodInsn(INVOKEINTERFACE, Type.getDescriptor(/*aop.getClass().getInterfaces()[0]*/Aop.class), "doBef", "(Ljava/lang/Object;)V");


        //调用父类的方法
        mv.visitVarInsn(ALOAD, 0);
        mv.visitInsn(DUP);
        //设置参数,非静态方法参数索引从1开始，0为this|super
        for (int i = 0; i < point.getFuncParamClasses().length; i++) {
            if (point.getFuncParamClasses()[i] == int.class
                    || point.getFuncParamClasses()[i] == char.class) {
                mv.visitVarInsn(ILOAD, i + 1);
            } else if (point.getFuncParamClasses()[i] == float.class) {
                mv.visitVarInsn(FLOAD, i + 1);
            } else if (point.getFuncParamClasses()[i] == double.class) {
                mv.visitVarInsn(DLOAD, i + 1);
            } else if (point.getFuncParamClasses()[i] == long.class) {
                mv.visitVarInsn(LLOAD, i + 1);
            } else {
                mv.visitVarInsn(ALOAD, i + 1);
            }
        }
        //需要调用父类的方法，所以第二个参数传递父类的类名
        mv.visitMethodInsn(INVOKESPECIAL, Type.getDescriptor(point.getTargerClass()), point.getFuncName(), point.getFuncDescribe());
        //局部变量的索引从最后一个参数的索引+1算起，因为this的索引为0，所以不用加上this的偏移量
        mv.visitVarInsn(ASTORE, point.getFuncParamClasses().length + 1);


        //调用切入之后的方法,获取this
        mv.visitVarInsn(ALOAD, 0);
        /**
         * 第一个参数：字节码指令
         * 第二个参数：字段所在的类名，如果想要this就传当前类名，如果想要super就传父类的类名
         * 第三个参数：字段名
         * 第四个参数：字段的类型签名
         */
        mv.visitFieldInsn(GETFIELD, point.getTargerClass().getName().replace(".", "/") + "$Proxy", "aop", Type.getDescriptor(aop.getClass()));
        //设置参数
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, point.getFuncParamClasses().length + 1);
        mv.visitMethodInsn(INVOKEINTERFACE,Type.getDescriptor(Aop.class), "doAft", "(Ljava/lang/Object;Ljava/lang/Object;)V");


        mv.visitVarInsn(ALOAD, point.getFuncParamClasses().length + 1);
        mv.visitInsn(ARETURN);

        //设置访问该方法的最大堆栈大小和最大本地变量数量。
        mv.visitMaxs(point.getFuncParamClasses().length+1, point.getFuncParamClasses().length+1);
        mv.visitEnd();
    }

    /**
     * 保存代理类字节码到class文件
     *
     * @param classWriter
     */
    private static void savaProxyClassFile(ClassWriter classWriter, String fileName) {
        try {
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(classWriter.toByteArray());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static Object newProxyInstance(String proxyClassName) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        System.out.println("prosyClassName is " + proxyClassName);
        Class proxyClass = Thread.currentThread().getContextClassLoader().loadClass(proxyClassName);
        return proxyClass.newInstance();
    }

}
