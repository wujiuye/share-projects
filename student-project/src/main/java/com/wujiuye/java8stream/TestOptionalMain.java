package com.wujiuye.java8stream;

import java.util.Optional;
import java.util.function.Supplier;

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
 * @ 创建日期      |   Created in 2018年12月25日
 * ======================^^^^^^^==============^^^^^^^============
 * @ 所属项目   |   lock
 * ======================^^^^^^^==============^^^^^^^============
 * @ 类功能描述    |
 *      Optional的使用方法以及使用场景
 * ======================^^^^^^^==============^^^^^^^============
 * @ 版本      |   ${version}
 * ======================^^^^^^^==============^^^^^^^============
 */
public class TestOptionalMain {


    /**
     * Optional官方建议只用在作为返回参数，不能声明为属性或者方法参数
     * @return
     */
    private Optional<String> getUserName() {
//        return Optional.empty();//创建一个空的Optional
        //如果对象即可能是 null 也可能是非 null，就要使用ofNullable()方法
//        return Optional.ofNullable(null);
        return Optional.ofNullable("");
//        return Optional.ofNullable("value from ofNullable");
        //如果把 null 值作为参数传递进去，of() 方法会抛出 NullPointerException
//        return Optional.of("wujiuye");
    }

    public static void main(String[] args) {
        TestOptionalMain optionalMain = new TestOptionalMain();
        Optional<String> username = optionalMain.getUserName();
        //orElseThrow：如果为空抛出自定义的异常
//        String  realUsername = username.orElseThrow(()->new NullPointerException("用户名不能为空"));
        //如果为空则返回other值
//        String realUsername = username.orElse("");
        //如果为空则调用其它方法获取值
//        String realUsername = username.orElseGet(() -> {
//            for (int i = 0; i < 10; i++) {
//
//            }
//            return "default value...";
//        });
//        System.out.println(realUsername);

        //如果不想在为空的时候指定默认值，那么可以这么使用
//        if(username.isPresent()){
            //检查是否有值的另一个选择是 ifPresent() 方法
//            String realUsername = username.get();
//            System.out.println(realUsername);

            //Optional的链式调用
            int value =
                    // 过滤不了null值，因为值为null的时候还是就是Optional.empty()了，看源码就懂了
                    // 这里过滤掉""，获取非""的值，即当false时，返回Optional.empty()
                    username.filter((v)-> {
                        System.out.println("返回false表示过滤这个值，返回Optional.empty()");
                        return !v.equals("");
                    })
                            //映射，将原本的字符串类型转换为其它类型的值，比如可以将username转为User对象
                            //如果value为null则不会执行这个方法
                    .map((v)->{
                        System.out.println("value 不为null 时才会执行这个方法");
                        return v.length();
                    })
                            //获取值，如果不存在则抛出异常
                    .orElseThrow(()->new NullPointerException("不存在值"));
            System.out.println(value);

//        }else {
            //如果为null则做些什么......
//        }
    }

}
