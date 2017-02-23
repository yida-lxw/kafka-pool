package com.gome.redline.utils.common;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by Lanxiaowei at 2017/1/12 17:58
 * 数字操作工具类
 */
public class NumberUtils {
    private static Random random = new Random();
    /**
     * 保留2位小数
     */
    public static final String DEFAULT_NUMBER_FORMAT = "#########0.00";

    /**
     * 按照指定的格式格式化数字
     * 支持四舍五入
     *
     * @param d
     * @param format
     * @return
     */
    public static String format2String(double d, String format) {
        if (StringUtils.isEmpty(format)) {
            format = DEFAULT_NUMBER_FORMAT;
        }
        DecimalFormat df = new DecimalFormat(format);
        return df.format(d);
    }

    /**
     * 将数字四舍五入为2位小数的字符串
     *
     * @param d
     * @return
     */
    public static String upto2DigitsString(double d) {
        return format2String(d, null);
    }

    /**
     * 按照指定的格式格式化数字
     * 支持四舍五入
     *
     * @param d
     * @param format
     * @return
     */
    public static double format(double d, String format) {
        if (StringUtils.isEmpty(format)) {
            format = DEFAULT_NUMBER_FORMAT;
        }
        DecimalFormat df = new DecimalFormat(format);
        return Double.valueOf(df.format(d));
    }

    /**
     * 将数字四舍五入为2位小数的小数
     *
     * @param d
     * @return
     */
    public static double upto2Digits(double d) {
        String s = format2String(d, null);
        if (StringUtils.isEmpty(s)) {
            return 0.0;
        }
        return Double.valueOf(s);
    }

    /**
     * 将数字四舍五入为2位小数的小数
     *
     * @param d
     * @return
     */
    public static double doubleFormat(double d, int bit) {
        String format = "#";
        if (bit > 0) {
            format = String.format("%s%s", format, ".");
            for (int i = 0; i < bit; i++) {
                format = String.format("%s%s", format, "0");
            }
        }
        DecimalFormat df = new DecimalFormat(format);
        return Double.valueOf(df.format(d));
    }

    /**
     * 生成指定区间[min-max)之间的随机数
     *
     * @param max
     * @param min
     * @return
     */
    public static int generateRandomNumber(int max, int min) {
        return random.nextInt(max) % (max - min + 1) + min;
    }
}
