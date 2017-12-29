package com.hongfans.common1.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 */
public class RegexUtil{

    /**
     * 通过正则表达式判断是否为手机号
     *
     * @return
     */
    public static boolean isPhoneNumber(String phoneString){
        String format = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
        return isMatch(format, phoneString);
    }

    /**
     * 字符串正则校验
     */
    public static boolean isMatch(String regex, String string){

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    /**
     * 邮箱验证
     */
    public static boolean isEmail(String email){
        String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 身份证验证
     */
    public static boolean isIdCard(String id){
        // 15 位  ^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$
        // 18 位  ^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X|x)$
        Pattern p = Pattern.compile(
                "^([1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3})|([1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X|x))$");
        Matcher m = p.matcher(id);
        return m.matches();
    }

    /**
     * 手机号验证
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobile(String mobiles){
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }


    public static boolean checkPhone(String phone){
        String regex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
        return Pattern.matches(regex, phone);
    }


    public static boolean checkDigit(String digit){
        String regex = "\\-?[1-9]\\d+";
        return Pattern.matches(regex, digit);
    }


    public static boolean checkDecimals(String decimals){
        String regex = "\\-?[1-9]\\d+(\\.\\d+)?";
        return Pattern.matches(regex, decimals);
    }


    public static boolean checkBlankSpace(String blankSpace){
        String regex = "\\s+";
        return Pattern.matches(regex, blankSpace);
    }


    public static boolean checkChinese(String chinese){
        String regex = "^[\u4E00-\u9FA5]+$";
        return Pattern.matches(regex, chinese);
    }


    public static boolean checkBirthday(String birthday){
        String regex = "[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}";
        return Pattern.matches(regex, birthday);
    }


    public static boolean checkURL(String url){
//        String regex = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";
        String regex = "(http|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&:/~\\+#]*[\\w\\-\\@?^=%&/~\\+#])?";
        return Pattern.matches(regex, url);
    }

    public static String getDomain(String url){
        Pattern p = Pattern.compile("(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);
        // 获取完整的域名
        // Pattern p=Pattern.compile("[^//]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(url);
        matcher.find();
        return matcher.group();
    }

    public static boolean checkPostcode(String postcode){
        String regex = "[1-9]\\d{5}";
        return Pattern.matches(regex, postcode);
    }


    public static boolean checkIpAddress(String ipAddress){
        String regex = "[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))";
        return Pattern.matches(regex, ipAddress);
    }
}