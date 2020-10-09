package com.wujiuye.asmuse.use0;


/**
 * 自定义的类加载器
 * @author wjy 2017
 */
public class AopClassLoader extends ClassLoader{

    /**
     * 用来加载修改后的Class字节数组
     * @param className
     * @param classFile
     * @return
     * @throws ClassFormatError
     */
    public Class defineClassFromClassFile(String className,
                                          byte[] classFile) throws ClassFormatError {
        //将字节数组转换为Class的实例，返回值类型Class<?>
        //参数1：类的期望二进制名称（如：wjy.asm.bean.UserLoginDao,包名+类名，不需要后缀名），如果不知道传null
        return defineClass(className, classFile,0,classFile.length);
    }

}
