package com.wujiuye.asmuse.use0;

import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 将父类的构造函数改造成子类的构造函数
 * @author wjy 2017
 */
public class AopChangeToChildConstructorMethodAdapter extends MethodAdapter {


    private String superClassName;

    public AopChangeToChildConstructorMethodAdapter(MethodVisitor methodVisitor,String superClassName) {
        super(methodVisitor);
        this.superClassName = superClassName;
    }


    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        System.out.println("AopChangeToChildConstructorMethodAdapter===>"+opcode+","+owner+","+name+","+desc);
        //初始化方法
        if(opcode == Opcodes.INVOKESPECIAL && name.equals("<init>")){
            //修改类名
            owner = superClassName;
        }
        super.visitMethodInsn(opcode, owner, name, desc);
    }
}
