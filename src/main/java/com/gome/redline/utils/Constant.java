package com.gome.redline.utils;

/**
 * Created by Lanxiaowei at 2017/1/20 13:39
 * 系统公共常量类
 */
public class Constant {
    /**
     * 拉丁字符编码
     */
    public static final String DEFAULT_CHARSET_ISO = "ISO-8859-1";
    /**
     * 默认读写缓冲区大小
     */
    public static final int BUFFER_SIZE = 8192;
    /**
     * 允许上传文件的最大字节数
     */
    public static final int MAX_FILE_SIZE = 20 * 1024 * 1024;
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:18.0) Gecko/20100101 Firefox/18.0";
    /**
     * 默认编码
     */
    public static final String DEFAULT_CHARSET = "UTF-8";
    /**
     * 支持识别的日期格式，可以逐步追加
     */
    public static final String[] PATTERNS = new String[]{
            "yyyy-MM-dd",
            "yyyy-MM-dd HH:mm",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy/MM/dd",
            "yyyy/MM/dd HH:mm",
            "yyyy/MM/dd HH:mm:ss",
            "M/dd/yyyy",
            "M/d/yyyy",
            "MM/d/yyyy",
            "MM/dd/yyyy",
            "MM/dd/yyyy HH:mm",
            "MM/dd/yyyy HH:mm:ss",
            "HH:mm:ss",
            "HH:mm",
    };
}
