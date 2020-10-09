package com.wujiuye.asmaop.aop;

import org.objectweb.asm.*;

import static org.objectweb.asm.Opcodes.*;

/**
 * 基于子类的aop实现
 *
 * @author wjy 2018/12/10
 */
public class AopClassAdpter extends ClassAdapter {

    public final static String PROXY_CLASS_NAME = "$Proxy";
    public final static String TARGER_AOP_FIELD_NAME = "mAop";
    private Aop aop;

    public AopClassAdpter(ClassVisitor classVisitor, Aop aop) {
        super(classVisitor);
        this.aop = aop;
    }

    /**
     * 通过修改获取一个新的类，新类继续该类，即新的类是该类的子类
     *
     * @param version    java版本号
     * @param access     访问修饰符
     * @param name       类名
     * @param signature  签名
     * @param superName  父类名
     * @param interfaces 实现的接口列表
     */
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        //新的类即子类的名称
        String chiledClassName = name + PROXY_CLASS_NAME;
//        System.out.println("p:" + name + ",c:" + chiledClassName);
        /**
         * 第一个参数：jdk主版本号
         * 第二个参数：访问修饰符
         * 第三个参数：类名
         * 第四个参数：父类名
         */
        super.visit(version, access, chiledClassName, signature, name, interfaces);
    }

    /**
     * 修改方法
     *
     * @param access
     * @param name       方法名
     * @param desc       方法签名
     * @param signature
     * @param exceptions 需要抛出的异常
     * @return
     */
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
//        System.out.println("访问级别：" + access + ",方法名：" + name + ",方法签名：" + desc + "," + signature);
        //修改方法
        MethodVisitor methodVisitor = super.cv.visitMethod(access, name, desc, signature, exceptions);
        if (methodVisitor != null) {
            //类的构造方法
            if ("<init>".equals(name)) {
                insertAopObject(methodVisitor);
                return null;
            } else if (Opcodes.ACC_PUBLIC == access
                    && name.equals(this.aop.getPoint().getFuncName())) {
                /**
                 * String var10001 = "hello";
                 * byte var10002 = 18;
                 * float var10003 = 15.7F;
                 * double var10004 = 55.0D;
                 * byte var10005 = 99;
                 * byte var10006 = 1;
                 */
//                methodVisitor.visitLdcInsn("hello");
//                methodVisitor.visitLdcInsn(18);
//                methodVisitor.visitLdcInsn(15.7f);
//                methodVisitor.visitLdcInsn(55.0d);
//                methodVisitor.visitLdcInsn('c');
//                methodVisitor.visitLdcInsn(true);

                insertProxy(methodVisitor);
                return null;//重写了就不需要返回了
            }
        }
        return methodVisitor;
    }

    private void insertAopObject(MethodVisitor mv) {
        Point point = this.aop.getPoint();
        mv.visitCode();

        //super();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, point.getTargerClass().getName().replace(".", "/"), "<init>", "()V");

        //this.mAop = new xxx();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitTypeInsn(NEW, Type.getDescriptor(this.aop.getClass()));
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, Type.getDescriptor(this.aop.getClass()), "<init>", "()V");
        mv.visitFieldInsn(PUTFIELD, point.getTargerClass().getName().replace(".", "/") + "$Proxy", AopClassAdpter.TARGER_AOP_FIELD_NAME, Type.getDescriptor(aop.getClass()));

        mv.visitInsn(RETURN);
        mv.visitMaxs(2, 2);

        mv.visitEnd();
    }


    private void insertProxy(MethodVisitor mv) {
        Point point = this.aop.getPoint();
        //切入目标方法，目标方法执行之前，目标方法执行之后
        mv.visitCode();

        //数组参数
        if(point.getFuncParamClasses().length>0){
            //创建数组
            if (point.getFuncParamClasses().length >= 4) {
                mv.visitVarInsn(BIPUSH, point.getFuncParamClasses().length);//数组大小
            } else {
                switch (point.getFuncParamClasses().length) {
                    case 1:
                        mv.visitInsn(ICONST_1);
                        break;
                    case 2:
                        mv.visitInsn(ICONST_2);
                        break;
                    case 3:
                        mv.visitInsn(ICONST_3);
                        break;
                    default:
                        mv.visitInsn(ICONST_0);
                }
            }
            mv.visitTypeInsn(ANEWARRAY, Type.getDescriptor(Object.class));
            mv.visitVarInsn(ASTORE, point.getFuncParamClasses().length + 1);//存储到局部变量表，第二个参数指定在变量表中的下标
            //为数组赋值
            for (int i = 0; i < point.getFuncParamClasses().length; i++) {
                //给数组赋值
                mv.visitVarInsn(ALOAD, point.getFuncParamClasses().length + 1);//数组的引用，此时是第（this+参数个数+1）个局部变量
                if(i>3){
                    mv.visitVarInsn(BIPUSH, i);//设置下标
                }else {
                    switch (i){
                        case 0:
                            mv.visitInsn(ICONST_0);
                            break;
                        case 1:
                            mv.visitInsn(ICONST_1);
                            break;
                        case 2:
                            mv.visitInsn(ICONST_2);
                            break;
                        case 3:
                            mv.visitInsn(ICONST_3);
                            break;
                    }
                }
                mv.visitVarInsn(ALOAD, i + 1);//获取对应的参数
                mv.visitInsn(AASTORE);//为数组下标索引处赋值
            }
        }

        //调用之前的方法
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, point.getTargerClass().getName().replace(".", "/") + "$Proxy", AopClassAdpter.TARGER_AOP_FIELD_NAME, Type.getDescriptor(aop.getClass()));
        //设置参数
        mv.visitVarInsn(ALOAD, 0);
        if(point.getFuncParamClasses().length==0){
            mv.visitInsn(ACONST_NULL);//null
        }else {
            mv.visitVarInsn(ALOAD, point.getFuncParamClasses().length + 1);
        }
        mv.visitMethodInsn(INVOKEINTERFACE, Type.getDescriptor(Aop.class), "doBef", "(Ljava/lang/Object;[Ljava/lang/Object;)V");


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
        mv.visitFieldInsn(GETFIELD, point.getTargerClass().getName().replace(".", "/") + "$Proxy", AopClassAdpter.TARGER_AOP_FIELD_NAME, Type.getDescriptor(aop.getClass()));
        //设置参数
        mv.visitVarInsn(ALOAD, 0);
        if(point.getFuncDescribe().indexOf(")V")>0){
            mv.visitInsn(ACONST_NULL);//null
        }else{
            mv.visitVarInsn(ALOAD, point.getFuncParamClasses().length + 1);
        }
        mv.visitMethodInsn(INVOKEINTERFACE, Type.getDescriptor(Aop.class), "doAft", "(Ljava/lang/Object;Ljava/lang/Object;)V");

        if(point.getFuncDescribe().indexOf(")V")>0) {
            mv.visitInsn(RETURN);
        }else {
            mv.visitVarInsn(ALOAD, point.getFuncParamClasses().length + 1);
            mv.visitInsn(ARETURN);
        }

        //设置访问该方法的最大堆栈大小和最大本地变量数量。
        mv.visitMaxs(point.getFuncParamClasses().length + 1, point.getFuncParamClasses().length + 1);
        mv.visitEnd();
    }


    @Override
    public void visitEnd() {
        //为类添加一个属性
        //参数1；外部访问权限，公有还是私有
        //参数2：属性名
        //参数3：参数类型
        FieldVisitor fieldVisitor = super.cv.visitField(Opcodes.ACC_PRIVATE, TARGER_AOP_FIELD_NAME, Type.getDescriptor(this.aop.getClass()), null, null);
        fieldVisitor.visitEnd();
    }

}
