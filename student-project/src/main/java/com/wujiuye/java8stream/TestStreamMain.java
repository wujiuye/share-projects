package com.wujiuye.java8stream;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
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
public class TestStreamMain {

    public static void main(String[] args) {
        Stream.of("wjy", "wcl", "fmy", "lyh", "zsf", "", "", "s", "a")
                .filter(str -> !str.isEmpty())//只保留lab表达式返回true的记录
                .forEach(s -> System.out.println(s));//遍历过滤后的结果


        List<String> stringList = Arrays.asList("wjy", "wcl", "fmy", "lyh", "zsf", "", "", "s", "a");
        int[] result = stringList.stream()
                .limit(5)
                .mapToInt(s -> s.length())
                .distinct()//过滤掉相同的int值记录
                .toArray();
        Arrays.stream(result).forEach(s -> System.out.print(s+"\t"));
        System.out.println();

        //将int数组过滤掉<=0的记录转为字符串，再取6条记录保存为字符串
        String toString  = Arrays.stream(result)
                .filter(number->number>0)
                .mapToObj(number->""+number)
                .limit(6)
                .collect(Collectors.joining(","));
        System.out.println(toString);

        //排序与求和
        int[] intArray = new int[]{12, 32, 45, 1, 2, 3, 67, 9, 4, 3, 6, 0, 11, 354, 78, 6, 2, 6, 8, 92, 1, 5, 7, 9, 53, 2, 1, 3, 2, 3, 2, 34, 5, 6, 7, 8, 4, 3, 2, 2, 2};

        //排序
        int[] sortArray = Arrays.stream(intArray)
                .sorted()
                .toArray();
        //并行排序
        sortArray = Arrays.stream(intArray)
                .parallel()
                .sorted()
                .toArray();

        //求和
        System.out.println(Arrays.stream(intArray)
                .sum());
        //最大值
        System.out.println(Arrays.stream(intArray).max().getAsInt());
        //最小值
        System.out.println(Arrays.stream(intArray)
                .min().getAsInt());
        //平均值
        System.out.println(Arrays.stream(intArray)
                .average().getAsDouble());
        //字符串求和
        System.out.println(stringList.stream()
                .mapToInt(s -> s.length())
                .sum());

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
