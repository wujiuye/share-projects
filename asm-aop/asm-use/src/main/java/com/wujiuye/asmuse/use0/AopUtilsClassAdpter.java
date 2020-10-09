package com.wujiuye.asmuse.use0;

import com.wujiuye.asmuse.use0.ic.AopHandler;
import com.wujiuye.asmuse.use0.ic.LogAopHndler;
import org.objectweb.asm.*;

import static org.objectweb.asm.Opcodes.*;

/**
 * @author wjy 2017
 */
public class AopUtilsClassAdpter extends ClassAdapter {

    public AopUtilsClassAdpter(ClassVisitor classVisitor) {
        super(classVisitor);
    }


    private String superClassName;

    /**
     * 通过修改获取一个新的类，新类继续该类，即新的类是该类的子类
     * @param version
     * @param access
     * @param name  类名
     * @param signature
     * @param superName 父类名
     * @param interfaces 实现的接口列表
     */
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        System.out.println(version+","+access+","+name+","+signature+","+superName+","+interfaces.length);
        String chiledClassName =  name+"$Proxy";//新的类即子类的名称
        System.out.println("chiledClassName===>"+chiledClassName);
        this.superClassName = name;
        super.visit(version,access,chiledClassName,signature,this.superClassName,interfaces);
    }

    /**
     * 修改方法
     * @param access
     * @param name  方法名
     * @param desc  方法签名
     * @param signature
     * @param exceptions 需要抛出的异常
     * @return
     */
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        if(access == Opcodes.ACC_PRIVATE){
            System.out.println("Opcodes.ACC_PRIVATE===>"+"这是一个私有方法");
        }else if(access == Opcodes.ACC_PUBLIC) {
            System.out.println("Opcodes.ACC_PUBLIC====>"+"这是一个公有方法");
        }
        System.out.println("访问级别："+access+",方法名："+name+",方法签名："+desc+","+signature);
        //修改方法
        MethodVisitor methodVisitor = cv.visitMethod(access, name , desc, signature, exceptions);
        if(methodVisitor!=null){
            //类的构造方法
            if("<init>".equals(name)){
                return  new AopChangeToChildConstructorMethodAdapter(methodVisitor,this.superClassName);
            }else if(Opcodes.ACC_PUBLIC==access){
                //在所有的public方法体内植入代码

                //开始植入
                methodVisitor.visitCode();

                //初始化invokeHandler的实例赋值给this.invokeHandler
                methodVisitor.visitVarInsn(ALOAD, 0);
                //new出来的值在栈顶
                methodVisitor.visitTypeInsn(NEW, Type.getDescriptor(LogAopHndler.class));
                methodVisitor.visitInsn(DUP);
                methodVisitor.visitMethodInsn(INVOKESPECIAL,Type.getDescriptor(LogAopHndler.class), "<init>", "()V");

                //this.invokeHandler = new LogAopHndler();
                methodVisitor.visitFieldInsn(PUTFIELD, this.superClassName + "$Proxy", "invokeHandler", Type.getDescriptor(AopHandler.class));


                //将 this 压入操作数栈, Opcodes.GETFIELD值需要一个参数，就是调用谁的
                methodVisitor.visitVarInsn(Opcodes.ALOAD,0);
                //如果是获取父类的字段，第二个参数传递父类的类名
                //获取名称为invokeHandler的字段的值
                methodVisitor.visitFieldInsn(Opcodes.GETFIELD, this.superClassName+"$Proxy","invokeHandler",Type.getDescriptor(AopHandler.class));
                methodVisitor.visitLdcInsn(superClassName);
                methodVisitor.visitLdcInsn(name);


                /**
                 * 插入一条指令
                 * 参数2：类名
                 * 参数3：方法名
                 * 参数4：方法签名，可用javap命令查看类中的方法签名
                 */
                methodVisitor.visitMethodInsn(Opcodes.INVOKEINTERFACE,Type.getDescriptor(AopHandler.class),"onMethodInvoke","(Ljava/lang/String;Ljava/lang/String;)V");
                //结束植入
                methodVisitor.visitEnd();
            }
        }
        return methodVisitor;//返回methodVisitor，asm会在当前基础上添加父类方法的代码，相当与叠加的效果
    }


    @Override
    public void visitEnd() {
        //为类添加一个属性
        //参数1；外部访问权限，公有还是私有
        //参数2：属性名
        //参数3：参数类型
        cv.visitField(Opcodes.ACC_PRIVATE,"invokeHandler",Type.getDescriptor(AopHandler.class),null,null);
    }

    @Override
    public FieldVisitor visitField(int i, String s, String s1, String s2, Object o) {

        return super.visitField(i, s, s1, s2, o);
    }
}
