package com.wujiuye.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ClassLoaderMain {

    private static class MyClassLoader extends ClassLoader {

        public MyClassLoader(ClassLoader p) {
            super(p);
        }

        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            synchronized (getClassLoadingLock(name)) {
                Class<?> c = findLoadedClass(name);
                if (c == null) {
                    if (name.startsWith("java")) {
                        return super.loadClass(name);
                    }
                    c = findClass(name);
                }
                return c;
            }
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            // 获取该class文件字节码数组
            byte[] classData = getData(name.replace(".", "/"));
            if (classData != null) {
                // 将class的字节码数组转换成Class类的实例
                return defineClass(name, classData, 0, classData.length);
            }
            throw new ClassNotFoundException(name);
        }

        /**
         * 将class文件转化为字节码数组
         *
         * @return
         */
        private byte[] getData(String classPath) {
            File file = new File(Thread.currentThread().getContextClassLoader().getResource("")
                    .getFile() + classPath + ".class");
            if (file.exists()) {
                FileInputStream in = null;
                ByteArrayOutputStream out = null;
                try {
                    in = new FileInputStream(file);
                    out = new ByteArrayOutputStream();

                    byte[] buffer = new byte[1024];
                    int size = 0;
                    while ((size = in.read(buffer)) != -1) {
                        out.write(buffer, 0, size);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return out.toByteArray();
            } else {
                return null;
            }

        }

    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        MyClassLoader loader = new MyClassLoader(ClassLoaderMain.class.getClassLoader());
        Class classB = loader.loadClass("com.wujiuye.classloader.pkg.ClassA");
        Class classA = loader.getParent().loadClass("com.wujiuye.classloader.pkg.ClassA");
        System.out.println(classA==classB);
    }

}
