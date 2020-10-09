package com.wujiuye.lock;

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
 * @ 创建日期      |   Created in 2018年12月24日
 * ======================^^^^^^^==============^^^^^^^============
 * @ 所属项目   |   lock
 * ======================^^^^^^^==============^^^^^^^============
 * @ 类功能描述    |
 * ======================^^^^^^^==============^^^^^^^============
 * @ 版本      |   ${version}
 * ======================^^^^^^^==============^^^^^^^============
 */
public class ThreadLocalMain {

    /**
     * Thread{
     *     //ThreadLocalMap实际存在Thread实例对象中
     *     ThreadLocal.ThreadLocalMap threadLocals = null;
     *
     * }
     *
     *
     * ThreadLocal{
     *
     * ThreadLocal并不持有ThreadLocalMap实例，只是在ThreadLocal定义了ThreadLocalMap类
     *
     *      static class ThreadLocalMap {
     *            static class Entry extends WeakReference<ThreadLocal<?>> {
     *                  Object value;
     *                  //ThreadLocal实例作为key，v为要存储的值
     *                  //所以一个Thread的ThreadLocalMap可以存储不同的 ThreadLocal线程私有变量
     *                  Entry(ThreadLocal<?> k, Object v) {
     *                      super(k);
     *                      value = v;
     *                  }
     *              }
     *      }
     * }
     *
     * threadLocal.get()调用的是getMap方法，而getMap方法则返回Thread对象的threadLocals成员变量
     *      ThreadLocalMap getMap(Thread t) {
     *         return t.threadLocals;
     *     }
     *
     */

    private static ThreadLocal<String> threadLocal = new ThreadLocal<String>(){
        @Override
        protected String initialValue() {
            return "default value......"+Thread.currentThread().getName();
        }
    };

    public static void main(String[] args) {
        for(int i=0;i<10;i++){
            new Thread("thread-local-"+i){
                @Override
                public void run() {

                    System.out.println(threadLocal.get());

                    threadLocal.remove();//用完要移除，避免内存泄漏
                }
            }.start();
        }
    }

}
