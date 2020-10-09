package com.wujiuye.lock;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
 * <p>
 * 微信公众号id：code_skill
 * QQ邮箱：419611821@qq.com
 * 微信号：www_wujiuye_com
 * <p>
 * ======================^^^^^^^==============^^^^^^^============
 *
 * @ 作者       |   吴就业 www.wujiuye.com
 * ======================^^^^^^^==============^^^^^^^============
 * @ 创建日期      |   Created in 2018年12月21日
 * ======================^^^^^^^==============^^^^^^^============
 * @ 所属项目   |   lock
 * ======================^^^^^^^==============^^^^^^^============
 * @ 类功能描述    |
 * ======================^^^^^^^==============^^^^^^^============
 * @ 版本      |   ${version}
 * ======================^^^^^^^==============^^^^^^^============
 */
public class UnsafeMain {


    /**
     * 通过反射获取Unsafe
     *
     * @return
     */
    private static Unsafe getUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Unsafe unsafe = (Unsafe) field.get(null);
            return unsafe;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }


    private static void testGetArrayOffset() {
        Unsafe unsafe = getUnsafe();
        System.out.println(unsafe);

        int[] array = new int[10];
        System.out.println(Arrays.stream(array).mapToObj(s -> String.valueOf(s)).collect(Collectors.joining(",")));
        int arrayBaseOffset = unsafe.arrayBaseOffset(byte[].class);

        System.out.println(arrayBaseOffset);
        //在数组下标0处设置值
        unsafe.putInt(array, arrayBaseOffset, 1);
        //在数组下标5处设置值,因为一个int型数值占用4个字节
        unsafe.putInt(array, arrayBaseOffset + (4 * 5), 5);
        System.out.println(Arrays.stream(array).mapToObj(s -> String.valueOf(s)).collect(Collectors.joining(",")));
    }


    private int state = 0;
    private long stateLong;

    private static void testUpdateObjFieldInt() {
        try {
            Unsafe unsafe = getUnsafe();
            Field field = UnsafeMain.class.getDeclaredField("state");
            //获取字段在对象实例化之后的对象内偏移地址（从对象头+偏移地址=字段地址）
            long fieldOffsetAddress = unsafe.objectFieldOffset(field);
            System.out.println("字段的偏移地址是：" + fieldOffsetAddress);
            //通过unsafe修改对象的state字段的值
            UnsafeMain unsafeMain = new UnsafeMain();
            /**
             * 参数1：对象实例
             * 参数2：字段偏移地址
             * 参数3：要修改的值
             */
            unsafe.putInt(unsafeMain, fieldOffsetAddress, 100);
            System.out.println(unsafeMain.state);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private static void testUpdateObjFieldLong() {
        try {
            Unsafe unsafe = getUnsafe();
            Field field = UnsafeMain.class.getDeclaredField("stateLong");
            //获取字段在对象实例化之后的对象内偏移地址（从对象头+偏移地址=字段地址）
            long fieldOffsetAddress = unsafe.objectFieldOffset(field);
            System.out.println("字段的偏移地址是：" + fieldOffsetAddress);
            //通过unsafe修改对象的state字段的值
            UnsafeMain unsafeMain = new UnsafeMain();
            /**
             * 参数1：对象实例
             * 参数2：字段偏移地址
             * 参数3：要修改的值
             */
            unsafe.putLong(unsafeMain, fieldOffsetAddress, 100);
            System.out.println(unsafeMain.stateLong);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }


    static class MyLock {

        private volatile int state;
        private static Unsafe unsafe = getUnsafe();
        private Thread currentThread = null;
        private long fieldOffset;

        public MyLock() {
            //静态字段偏移
            //fieldOffset = unsafe.staticFieldOffset()
            try {
                Field field = this.getClass().getDeclaredField("state");
                field.setAccessible(true);
                fieldOffset = unsafe.objectFieldOffset(field);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        public void lock() {
            if (currentThread == Thread.currentThread()) {
                state = state + 1;
                return;
            }
            //获取不到锁就自旋
            do {
                //参数1：对象
                //参数2：字段偏移地址
                //参数3：预估值、期望值
                //参数4：新值
            } while (!unsafe.compareAndSwapInt(this, fieldOffset, 0, 1));
            //获取到锁
            currentThread = Thread.currentThread();
        }

        public void unlock() throws Exception {
            if (currentThread != Thread.currentThread()) {
                throw new Exception("释放锁异常，当前不是获取锁的线程！");
            }
            if (state == 1) {
                //同一个线程lock方法和unlock方法不会同时执行，所以不会导致lock方法currentThread == Thread.currentThread()失效。
                //但是当state=0的时候其它线程可能获取到了锁，所以先将其设置为null
                currentThread = null;
                state = 0;
                return;
            }
            state--;
        }
    }

    static volatile int count = 0;

    private static void testCompareAndSwapInt() {
        MyLock myLock = new MyLock();

        ExecutorService service = Executors.newCachedThreadPool();
        for (int i = 0; i < 100; i++) {
            service.submit(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 10; i++) {
                        myLock.lock();
                        count++;
                        System.out.println(count);
                        try {
                            myLock.unlock();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        service.shutdown();
    }

    public static void main(String[] args) {
//        testGetArrayOffset();
//        testUpdateObjField();
//        testUpdateObjFieldLong();
        testCompareAndSwapInt();

    }
}
