package com.gome.redline.utils.common;

import com.gome.redline.utils.Constant;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.apache.logging.log4j.util.Strings.isEmpty;

/**
 * Created by Administrator on 2016/11/30.
 * 字符串处理的公共类
 */
public class StringUtils {
    private static Logger logger = LogManager.getLogger(StringUtils.class.getName());
    /**
     * 正则表达式中的特殊字符
     */
    public static final String[] SPECIAL_REGULAR_CHARS = {
            "*", ".", "?", "+", "$",
            "^", "[", "]", "(", ")",
            "{", "}", "|", "\\", "/"
    };
    /**
     * 默认的分隔符
     */
    public static final String DEFAULT_SPLIT_CHAR = ",";
    /**
     * 默认的日期格式：yyyy-MM-dd
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 字符串首字母转换成大写
     * @param str
     * @return
     */
    public static String upperFirstLetter(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return (new StringBuilder(strLen)).append(Character.toTitleCase(str.charAt(0))).append(str.substring(1)).toString();
    }

    /**
     * 字符串首字母转换成小写
     *
     * @param str
     * @return
     */
    public static String lowerFirstLetter(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return (new StringBuilder(strLen)).append(Character.toLowerCase(str.charAt(0))).append(str.substring(1)).toString();
    }

    /**
     * 补位
     * @param str 原始字符串
     * @param length 需要的字符串长度
     * @return 补位后的字符串
     */
    public static String  fill(String str,int length,char fill){
        if(length<=0){
            return null;
        }
        if(isEmpty(str)){
            for(int i=0;i<length;i++){
                str+=fill;
            }
        }else{
            if(length<=str.length()){
                str = str.substring(0,length);
            }else{
                for(int i=str.length();i<length;i++){
                    str+=fill;
                }
            }
        }
        return str;
    }

    /**
     * 截取
     * @param str 原始字符串
     * @param length 需要的字符串长度
     * @return 截取后的字符串
     */
    public static String  cut(String str,int length){
        if(length <= 0|| isEmpty(str)){
            return null;
        }
        if(length<=str.length()){
            str = str.substring(0, length);
        }
        return str;
    }

    /**
     * 判断是否为中文字符
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS ||
                ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS ||
                ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A ||
                ub == Character.UnicodeBlock.GENERAL_PUNCTUATION ||
                ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION ||
                ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 全角字符转换成半角字符
     *
     * @param source
     * @return
     */
    public static String convertStringToNarrow(String source) {
        StringBuffer result = new StringBuffer();
        char[] ch = source.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            if (ch[i] == 12288) {
                result.append(' ');
            } else if (ch[i] == 12290) {
                result.append('.');
            } else if (ch[i] >= 65281 && ch[i] <= 65374) {
                result.append((char) (ch[i] - 65248));
            } else {
                result.append(ch[i]);
            }
        }
        return result.toString();
    }

    /**
     * 半角字符转换全角字符
     *
     * @param source
     * @return
     */
    public static String convertStringToWide(String source) {
        StringBuffer result = new StringBuffer();
        char[] ch = source.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            if (ch[i] == 32) {
                result.append('　');
            } else if (ch[i] == 46) {
                result.append('。');
            } else if (ch[i] >= 33 && ch[i] <= 126) {
                result.append((char) (ch[i] + 65248));
            } else {
                result.append(ch[i]);
            }
        }
        return result.toString();
    }

    /**
     * 判断传入字符串是否包含数字
     *
     * @param text
     * @return
     */
    public static boolean containsNum(String text) {
        boolean isNumber = false;
        for (int i = 0; i < text.length(); i++) {
            if (Character.isDigit(text.charAt(i))) {
                isNumber = true;
                break;
            }
        }
        return isNumber;
    }

    /**
     * 判断字符串是否全部由大写字母组成
     *
     * @param str
     * @return
     */
    public static boolean isAllUpperCase(String str) {
        if (null == str || str.length() == 0) {
            return false;
        }
        String temp = str.replaceAll("[A-Z]", "");
        return temp.length() == 0;
    }

    /**
     * 判断字符串是否全部由小写字母组成
     *
     * @param str
     * @return
     */
    public static boolean isAllLowerCase(String str) {
        if (null == str || str.length() == 0) {
            return false;
        }
        String temp = str.replaceAll("[a-z]", "");
        return temp.length() == 0;
    }

    /**
     * 判断字符串是否全部是数字(不含小数点)
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (null == str || str.length() == 0 || (str.length() > 1 && str.startsWith("0"))) {
            return false;
        }
        //JDK自带函数实现
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
        /*//判断ASC码实现
	    for(int i=str.length();--i >= 0;){
	        int chr = str.charAt(i);
	        if(chr < 48 || chr > 57) {
	           return false;
	        }
	    }
	    return true;*/
    }

    /**
     * 判断字符串是否可以转换成小数
     *
     * @param str
     * @return
     */
    public static boolean isDecimal(String str) {
        if (null == str || str.length() == 0 || str.indexOf(".") == -1 ||
                str.startsWith(".") || str.endsWith(".") ||
                (str.indexOf(".") != str.lastIndexOf("."))) {
            return false;
        }
        //判断是否含有非数字字符
        String temp = str.replaceAll("[\\d|.]*", "");
        if (temp.length() != 0) {
            return false;
        }
        //排除类似000.12形式
        if (str.indexOf(".") > 1) {
            String firstLetter = str.substring(0, 1);
            if (firstLetter.equals("0")) {
                return false;
            }
        }
        return true;
    }

    /**
     * 从字符串中提取数字(含小数),如：Was $365.00--365.00
     *
     * @param str
     * @return
     */
    public static String getNumberFromString(String str) {
        if (null == str || str.length() == 0) {
            throw new RuntimeException("Unable to convert " + str + " to a number.");
        }
        return str.replaceAll("[^\\d|.]*", "");
    }

    /**
     * 字符串剔除重复项，如：aaaaabbbbbccccc-->abc
     * 注意：暂不支持交叉重复，如：aaaaabbbaaaccc-->bac而不是abac
     *
     * @param str
     * @return
     */
    public static String excludeRepeat(String str) {
        if (null == str || str.length() == 0) {
            return str;
        }
        return str.replaceAll("(?s)(.)(?=.*\\1)", "");
    }

    /**
     * @param @param  str
     * @param @return
     * @return String
     * @throws
     * @Title: string2Unicode
     * @Description: 字符串转换成Unicode编码形式
     */
    public static String string2Unicode(String str, String encoding) {
        try {
            str = new String(str.getBytes("ISO-8859-1"), encoding);
        } catch (UnsupportedEncodingException e) {
            logger.error("Close [FileInputStream] Occur IOException,please check and fix it.", e);
        }
        char[] utfBytes = str.toCharArray();
        String unicodeBytes = "";
        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
            String hexB = Integer.toHexString(utfBytes[byteIndex]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        return unicodeBytes;
    }

    /**
     * @param @param  unicodeStr
     * @param @return
     * @return String
     * @throws
     * @Title: unicode2String
     * @Description: Unicode编码字符串还原成String
     */
    public static String unicode2String(String str) {
        str = (str == null ? "" : str);
        if (str.indexOf("\\u") == -1) {
            return str;
        }
        StringBuffer buffer = new StringBuffer(1000);
        for (int i = 0; i < str.length() - 6; ) {
            String strTemp = str.substring(i, i + 6);
            String value = strTemp.substring(2);
            int c = 0;
            for (int j = 0; j < value.length(); j++) {
                char tempChar = value.charAt(j);
                int t = 0;
                switch (tempChar) {
                    case 'a':
                        t = 10;
                        break;
                    case 'b':
                        t = 11;
                        break;
                    case 'c':
                        t = 12;
                        break;
                    case 'd':
                        t = 13;
                        break;
                    case 'e':
                        t = 14;
                        break;
                    case 'f':
                        t = 15;
                        break;
                    default:
                        t = tempChar - 48;
                        break;
                }
                c += t * ((int) Math.pow(16, (value.length() - j - 1)));
            }
            buffer.append((char) c);
            i = i + 6;
        }
        return buffer.toString();
    }

    /**
     * 判断某字符串是否包含中文字符
     *
     * @param text
     * @return
     */
    public static boolean containsChinese(String text) {
        Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]+");
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

    /**
     * 判断字符串是否为日期格式
     *
     * @param dateString
     * @return
     */
    public static boolean isDate(String dateString) {
        if (isEmpty(dateString)) {
            return false;
        }
        DateFormat df = null;
        for (String pattern : Constant.PATTERNS) {
            df = new SimpleDateFormat(pattern);
            df.setLenient(false);
            Date date = null;
            try {
                date = df.parse(dateString);
                if (null != date) {
                    return true;
                }
            } catch (ParseException e) {
                continue;
            }
        }
        return false;
    }

    /**
     * 剔除结尾字符(如最后一个换行符、最后一个逗号等等)
     *
     * @param str     待处理字符串
     * @param regular 正则表达式
     * @return 返回剔除后的字符
     */
    public static String replaceEndsWith(String str, String regular) {
        if (isEmpty(str)) {
            return null;
        }
        if (isEmpty(regular)) {
            return str;
        }
        return str.replaceAll(regular, "");
    }

    /**
     * 从get/set方法名中提取出属性名称
     *
     * @return
     */
    public static String getPropertyNameFromGetSet(String methodName) {
        if (null == methodName) {
            return null;
        }
        if (methodName.length() <= 3 || (!methodName.startsWith("get") &&
                !methodName.startsWith("set") && !methodName.startsWith("is"))) {
            return methodName;
        }
        //考虑boolean类型的get方法是以is前缀开头
        if (methodName.startsWith("is")) {
            return StringUtils.lowerFirstLetter(methodName.substring(2));
        }
        return StringUtils.lowerFirstLetter(methodName.substring(3));
    }

    /**
     * 骆驼命名法转换成数据库字段命名法，如studentName-->student_name
     *
     * @param propertyName 属性名称
     * @param prefix       添加前缀
     * @param stuffix      添加后缀
     * @return
     */
    public static String splitCamelName(String propertyName, String prefix, String stuffix) {
        if (isEmpty(propertyName)) {
            return propertyName;
        }
        char[] dest = new char[propertyName.length()];
        propertyName.getChars(0, propertyName.length(), dest, 0);
        StringBuilder builder = new StringBuilder();
        if (isEmpty(prefix)) {
            builder.append(prefix).append("_");
        }
        for (int i = 0; i < dest.length; i++) {
            if (i == 0) {
                builder.append(Character.toLowerCase(dest[i]));
                continue;
            }
            if (Character.isUpperCase(dest[i])) {
                builder.append("_").append(Character.toLowerCase(dest[i]));
            } else {
                builder.append(dest[i]);
            }
        }
        if (isEmpty(stuffix)) {
            builder.append("_").append(stuffix);
        }
        return builder.toString();
    }

    /**
     * 骆驼命名法转换成数据库字段命名法(重载)，如studentName-->student_name
     *
     * @param propertyName 属性名称
     * @return
     */
    public static String splitCamelName(String propertyName) {
        return splitCamelName(propertyName, null, null);
    }

    /**
     * 数据库字段名称转换成类属性名，如stu_name-->stuName
     *
     * @param fieldName 数据库字段名称
     * @param prefix    前缀
     * @param stuffix   后缀
     * @return
     */
    public static String splitDBFieldName(String fieldName, String prefix, String stuffix) {
        if (isEmpty(fieldName)) {
            return fieldName;
        }
        if (isEmpty(prefix)) {
            if (prefix.endsWith("_")) {
                fieldName = fieldName.replaceAll("^" + prefix + "(.*)", "$1");
            } else {
                fieldName = fieldName.replaceAll("^" + prefix + "_(.*)", "$1");
            }
        } else {
            fieldName = fieldName.replaceAll("^_(.*)", "$1");
        }
        if (isEmpty(stuffix)) {
            if (stuffix.startsWith("_")) {
                fieldName = fieldName.replaceAll("(.*)" + stuffix + "$", "$1");
            } else {
                fieldName = fieldName.replaceAll("(.*)_" + stuffix + "$", "$1");
            }
        } else {
            fieldName = fieldName.replaceAll("(.*)_$", "$1");
        }
        if (fieldName.indexOf("_") == -1) {
            return fieldName;
        }
        String[] array = fieldName.split("_");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i == 0) {
                builder.append(array[i].toLowerCase());
            } else {
                builder.append(StringUtils.upperFirstLetter(array[i]));
            }
        }
        return builder.toString();
    }

    /**
     * 数据库字段名称转换成类属性名(重载)，如stu_name-->stuName
     *
     * @param fieldName 数据库字段名称
     * @return
     */
    public static String splitDBFieldName(String fieldName) {
        return splitDBFieldName(fieldName, null, null);
    }

    /******************String与其他基本数据类型之间的转换 Begin****************/
    public static int string2Int(String str) {
        if (null == str || str.length() == 0) {
            throw new IllegalArgumentException("str can't be null.");
        }
        return Integer.valueOf(str);
    }

    public static short string2Short(String str) {
        if (null == str || str.length() == 0) {
            throw new IllegalArgumentException("str can't be null.");
        }
        return Short.valueOf(str);
    }

    public static long string2Long(String str) {
        if (null == str || str.length() == 0) {
            throw new IllegalArgumentException("str can't be null.");
        }
        return Long.valueOf(str);
    }

    public static float string2Float(String str) {
        if (null == str || str.length() == 0) {
            throw new IllegalArgumentException("str can't be null.");
        }
        return Float.valueOf(str);
    }

    public static double string2Double(String str) {
        if (null == str || str.length() == 0) {
            throw new IllegalArgumentException("str can't be null.");
        }
        return Double.valueOf(str);
    }

    public static boolean string2Boolean(String str) {
        if (null == str || str.length() == 0) {
            throw new IllegalArgumentException("str can't be null.");
        }
        if("1".equals(str)) {
            return Boolean.TRUE;
        } else if("0".equals(str)) {
            return Boolean.FALSE;
        }
        return Boolean.valueOf(str);
    }

    public static char string2Char(String str) {
        if (null == str || str.length() == 0) {
            throw new IllegalArgumentException("str can't be null.");
        }
        return str.toCharArray()[0];
    }

    public static boolean isEmpty(String str) {
        return null == str || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    //判断字符串是否是空白字符，是空白字符返回true,不是返回false
    public static boolean isBlank(String sourceString) {
        int stringLength;
        if (sourceString == null || (stringLength = sourceString.length()) == 0) {
            return true;
        }

        for (int i = 0; i < stringLength; i++) {
            if ((Character.isWhitespace(sourceString.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }
    /******************String与其他基本数据类型之间的转换  End****************/
}
