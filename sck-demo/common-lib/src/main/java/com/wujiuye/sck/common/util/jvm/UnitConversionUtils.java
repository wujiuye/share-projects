package com.wujiuye.sck.common.util.jvm;

import java.text.DecimalFormat;

/**
 * 单位转换工具类
 *
 * @author wujiuye
 * @version 1.0 on 2019/9/8
 */
public class UnitConversionUtils {

    /**
     * 将字节数转kb、mb、gb、tb等可识别的单位
     *
     * @param byteNumber
     * @return
     */
    public static String formatByte(long byteNumber) {
        double FORMAT = 1024.0;
        double kbNumber = byteNumber / FORMAT;
        if (kbNumber < FORMAT) {
            return new DecimalFormat("#.##KB").format(kbNumber);
        }
        double mbNumber = kbNumber / FORMAT;
        if (mbNumber < FORMAT) {
            return new DecimalFormat("#.##MB").format(mbNumber);
        }
        double gbNumber = mbNumber / FORMAT;
        if (gbNumber < FORMAT) {
            return new DecimalFormat("#.##GB").format(gbNumber);
        }
        double tbNumber = gbNumber / FORMAT;
        return new DecimalFormat("#.##TB").format(tbNumber);
    }

    /**
     * 将耗时时间戳格式化输出
     *
     * @param timesmp 耗时时间戳，单位毫秒
     * @return
     */
    public static String formatTime(long timesmp) {
        long second = timesmp / 1000;
        long minute = second / 60;
        long hour = minute / 60;
        StringBuilder builder = new StringBuilder();
        if (hour > 0) {
            builder.append(hour).append(" hour ");
        }
        if (minute > 0) {
            builder.append(minute).append(" minute ");
        }
        if (second > 0) {
            builder.append(second).append(" second ");
        }
        builder.append(timesmp % 1000).append(" millisecond");
        return builder.toString();
    }

}
