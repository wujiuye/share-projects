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
 * @ 创建日期      |   Created in 2018年12月13日
 * ======================^^^^^^^==============^^^^^^^============
 * @ 所属项目   |   lock
 * ======================^^^^^^^==============^^^^^^^============cd
 * @ 类功能描述    |
 * ======================^^^^^^^==============^^^^^^^============
 * @ 版本      |   ${version}
 * ======================^^^^^^^==============^^^^^^^============
 */
public class SyncMain {

    static {
        System.out.println("static block...");
    }

    private Object lock = new Object();


    public SyncMain(String name){

    }

    public void sayHello() {
        synchronized (lock) {
            System.out.println("hello...");
        }
    }

    public synchronized void sayHell2() {
        System.out.println("hello2...");
    }

    public String pj(String a, String b) {
        String str = a + b;
        char ch = str.charAt(-1);
        return str;
    }

    public String pj2(String a, String b) throws Exception {
        try {
            String str = a + b;//new StringBuffer().append(1).append(2);
            char ch = str.charAt(-1);
            return str;
        } catch (Exception e) {
            throw e;
        }
    }

    public String pj3(String a, String b) throws Exception {
        try {
            String str = a + b;
            char ch = str.charAt(-1);
            return str;
        } catch (Exception e) {
            throw e;
        } finally {
            System.out.println("我总数会被执行到。。。。");
        }
    }


    public int add(int d) {
        int count = 0;
        for (int i = 0; i < d; i++) {
            count++;
        }
        return count;
    }

    public int add2(int d) {
        int count = 0;
        while (count < d) {
            count++;
        }
        return count;
    }

    public int add3(int d) {
        int count = 0;
        do {
            count++;
        } while (count < d);
        return count;
    }

    public boolean comp(int a, int b) {
        if (a > b) {
            return true;
        }
        return false;
    }

    public String menu(int a) {
        switch (a) {
            case 0:
                return "is 3";
            case 2:
                return "is 2";
            case 1:
                return "is 1";
            default:
                return null;
        }
    }

    public static void main(String[] args) {
        new SyncMain("").sayHello();
    }

}
