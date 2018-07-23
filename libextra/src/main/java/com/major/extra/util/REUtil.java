package com.major.extra.util;

import android.text.TextUtils;

import com.major.base.util.CommonUtil;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 输入信息验证
 */
public class REUtil{

    /**
     * 正则：手机号（简单）
     */
    public static final String REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$";
    /**
     * 正则：手机号（精确）
     * <p>移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188</p>
     * <p>联通：130、131、132、145、155、156、175、176、185、186</p>
     * <p>电信：133、153、173、177、180、181、189</p>
     * <p>全球星：1349</p>
     * <p>虚拟运营商：170</p>
     */
    public static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|(147))\\d{8}$";
    /**
     * 正则：电话号码
     */
    public static final String REGEX_TEL = "^0\\d{2,3}[- ]?\\d{7,8}";

    /******************** 正则相关常量 ********************/
    /**
     * 正则：身份证号码15位
     */
    public static final String REGEX_ID_CARD15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
    /**
     * 正则：身份证号码18位
     */
    public static final String REGEX_ID_CARD18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";
    /**
     * 正则：邮箱
     */
    public static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    /**
     * 正则：URL
     */
    public static final String REGEX_URL = "[a-zA-z]+://[^\\s]*";
    /**
     * 正则：汉字
     */
    public static final String REGEX_ZH = "^[\\u4e00-\\u9fa5]+$";
    /**
     * 正则：用户名，取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位
     */
    public static final String REGEX_USERNAME = "^[\\w\\u4e00-\\u9fa5\\s]{1,100}(?<!_)$";
    /**
     * 正则：yyyy-MM-dd格式的日期校验，已考虑平闰年
     */
    public static final String REGEX_DATE = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$";
    /**
     * 正则：IP地址
     */
    public static final String REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
    /**
     * 正则：双字节字符(包括汉字在内)
     */
    public static final String REGEX_DOUBLE_BYTE_CHAR = "[^\\x00-\\xff]";
    /**
     * 正则：空白行
     */
    public static final String REGEX_BLANK_LINE = "\\n\\s*\\r";
    /**
     * 正则：QQ号
     */
    public static final String REGEX_TENCENT_NUM = "[1-9][0-9]{4,}";

    /************** 以下摘自http://tool.oschina.net/regex **************/
    /**
     * 正则：中国邮政编码
     */
    public static final String REGEX_ZIP_CODE = "[1-9]\\d{5}(?!\\d)";
    /**
     * 正则：正整数
     */
    public static final String REGEX_POSITIVE_INTEGER = "^[1-9]\\d*$";
    /**
     * 正则：负整数
     */
    public static final String REGEX_NEGATIVE_INTEGER = "^-[1-9]\\d*$";
    /**
     * 正则：整数
     */
    public static final String REGEX_INTEGER = "^-?[1-9]\\d*$";
    /**
     * 正则：非负整数(正整数 + 0)
     */
    public static final String REGEX_NOT_NEGATIVE_INTEGER = "^[1-9]\\d*|0$";
    /**
     * 正则：非正整数（负整数 + 0）
     */
    public static final String REGEX_NOT_POSITIVE_INTEGER = "^-[1-9]\\d*|0$";
    /**
     * 正则：正浮点数
     */
    public static final String REGEX_POSITIVE_FLOAT = "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$";
    /**
     * 正则：负浮点数
     */
    public static final String REGEX_NEGATIVE_FLOAT = "^-[1-9]\\d*\\.\\d*|-0\\.\\d*[1-9]\\d*$";

    /**
     * 手机号码验证
     *
     * @param phoneNum
     * @return
     */
    public static boolean mobileMumVerify(String phoneNum){
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9])|(14[5,7]))\\d{8}$");
        return p.matcher(phoneNum).matches();
    }

    /**
     * 邮箱验证
     *
     * @param mailAddress
     * @return
     */
    public static boolean mailAddressVerify(String mailAddress){
        if(mailAddress.contains(" ")){
            return false;
        } else {
            String emailExp = "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";
            // String emailExp =
            // "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
            Pattern p = Pattern.compile(emailExp);
            return p.matcher(mailAddress).matches();
        }
    }

    /**
     * 验证邮箱地址是否正确
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email){
        boolean flag = false;
        try{
            // String check =
            // "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            String check = "\\w+@\\w+(\\.\\w+)+";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch(Exception e){
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 校验密码是否正确
     *
     * @param pwd 以字母开头，长度在6~20之间，只能包含字符、数字和下划线。
     * @return
     */
    public static boolean checkPwd(String pwd){
        boolean flag = false;
        try{
            // String check = "^[a-zA-Z]\\w{5,19}$";
            // String check = "\\.{6,19}";
            // Pattern regex = Pattern.compile(check);
            // Matcher matcher = regex.matcher(pwd);
            // flag = matcher.matches();
            if(pwd.length() >= 6 && pwd.length() <= 20){
                flag = true;
            }
        } catch(Exception e){
            flag = false;
            e.printStackTrace();
        }

        return flag;
    }

    /**
     * 校验保险资格证
     *
     * @param zgz 以字母开头，长度在2~20之间，只能包含字符、数字和下划线。
     * @return
     */
    public static boolean checkZgz(String zgz){
        boolean flag = false;
        try{
            String check = "\\w[a-zA-Z]{1,19}";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(zgz);
            flag = matcher.matches();
        } catch(Exception e){
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 英文名
     *
     * @param str
     * @return
     */
    public static boolean checkEName(String str){
        String eReg = "^([a-z \\/\\.A-Z]){2,38}$";
        String fReg = "(^abc$)|(^abcd$)|(^abcde$)";
        // if (!check(str, eReg) || check(str, fReg)) {
        // return false;
        // }
        if(!check(str, eReg) || !checkSpace(str) || check(str, fReg)){
            return false;
        }
        return true;
    }

    public static boolean check(String input, String reg){
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    /**
     * 是否包含空格
     *
     * @param input
     * @return true 没包含空格     false 有空格
     */
    public static boolean checkSpace(String input){
        return check(input, "^[^\\s]+$");
    }

    /**
     * 汉字正则表达式
     *
     * @param str
     * @return
     */
    public static boolean checkCN(String str){
        if(CommonUtil.isEmpty(str)){
            return false;
        }

        int count = 0;
        Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
        char c[] = str.toCharArray();
        int str_length = c.length;

        for(int i = 0; i < str_length; i++){
            Matcher matcher = pattern.matcher(String.valueOf(c[i]));
            if(matcher.matches()){
                count++;
            }
        }

        return count == str_length;
    }

    /**
     * 输入全不是英文字母
     * @param str
     * @return
     */
    public static boolean checkEN(String str){
        int count = 0;
        Pattern pattern = Pattern.compile("[a-zA-Z]");
        str = str.trim().replace(" ", "");
        char c[] = str.toCharArray();
        int str_length = c.length;
        for(int i = 0; i < str_length; i++){
            Matcher matcher = pattern.matcher(String.valueOf(c[i]));
            if(matcher.matches()){
                count++;
            }
        }
        if(count == str_length){
            return true;
        } else {
            return false;
        }
    }

    /**
     * 输入类型仅限汉字和字母
     *
     * @param input
     * @return
     */
    public static boolean checkHzEn(String input){
        String str = "^[\\u4E00-\\u9FA5\\uF900-\\uFA2DA-Za-z]{2,10}$";
        Pattern pattern = Pattern.compile(str);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    /**
     * 校验座机号
     *
     * @param str
     * @return
     */
    public static boolean checkPhone(String str){
        boolean checkResult = check(str, "\\d{6,20}");
        return checkResult;
    }

    /**
     * 检查邮政编号
     *
     * @param str
     * @return
     */
    public static boolean checkPostCode(String str){
        boolean result = checkNum(str);
        if(result == true){
            if(str.length() == 6){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 输入的是数字的判断
     *
     * @param str
     * @return
     */
    public static boolean checkNum(String str){
        if(TextUtils.isEmpty(str)){
            return false;
        }
        int count = 0;
        Pattern pattern = Pattern.compile("[0-9]");
        str = str.trim();
        char c[] = str.toCharArray();
        int str_length = c.length;
        for(int i = 0; i < str_length; i++){
            Matcher matcher = pattern.matcher(String.valueOf(c[i]));
            if(matcher.matches()){
                count++;
            }
        }
        if(count == str_length){
            return true;
        } else {
            return false;
        }
    }

    /**
     * 汉字、字母、数字验证
     *
     * @param str
     * @return
     */
    public static boolean checkHz_zimu_shuzi(String str){
        Pattern pattern = Pattern.compile("^[0-9a-zA-Z\u4e00-\u9fa5]{3,20}$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 军官证校验
     *
     * @param str
     * @return
     */
    public static boolean checkMilitary(String str){
        String s1 = "^\\d*$";
        String s2 = "^.{1,}字第\\d{4,}.*$";
        String s3 = "^([A-Za-z0-9\\u4e00-\\u9fa5])*$";
        try{
            byte[] midbytes = str.getBytes("gbk");
            if(check(str, s1)){
                String s11 = "^\\d{4,20}$";
                if(check(str, s11)){
                    return true;
                } else {
                    return false;
                }
            } else if(midbytes.length < 10 || midbytes.length > 20){
                return false;
            } else if(!check(str, s2)){
                return false;
            } else if(!check(str, s3)){
                return false;
            }
        } catch(UnsupportedEncodingException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 台胞证及港澳通行证
     *
     * @param str
     * @return
     */
    public static boolean checkMTPs(String str){
        Pattern pattern = Pattern.compile("^([A-Z0-9\\(\\)])*$");
        Matcher matcher = pattern.matcher(str);
        boolean patternCheck = matcher.matches();
        if(!patternCheck || str.length() < 8){
            return false;
        }
        return true;
    }

    /**
     * 护照
     *
     * @param str
     * @return
     */
    public static boolean checkPassport(String str){
        Pattern pattern = Pattern.compile("^([A-Za-z0-9])*$");
        Matcher matcher = pattern.matcher(str);
        boolean patternCheck = matcher.matches();
        if(!patternCheck || str.length() < 3){
            return false;
        }
        return true;
    }

    /**
     * 验证百分比(%)
     *
     * @param str
     * @return
     */
    public static boolean checkPercent(String str){
        Pattern pattern = Pattern
                .compile("^(?!0(\\d|\\.0+$|$))\\d+(\\.\\d{1,2})?$");
        // Pattern pattern = Pattern.compile("\\d+\\.?\\%");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 是否全是空格
     *
     * @param input
     * @return true  全是空格
     */
    public static boolean checkIsOnlySpace(String input){
        return check(input, "^[\\s]+$");
    }

    public static String checkSpaceWithHint(String input){
        if(!checkSpace(input)){
            return "输入内容包含空格，请出新输入!";
        }
        return null;
    }

    /**
     * 描述：是否是中文.
     *
     * @param str 指定的字符串
     * @return 是否是中文:是为true，否则false
     */
    public static boolean isChinese(String str){
        Boolean isChinese = true;
        String chinese = "[\u0391-\uFFE5]";
        if(!CommonUtil.isEmpty(str)){
            // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
            for(int i = 0; i < str.length(); i++){
                // 获取一个字符
                String temp = str.substring(i, i + 1);
                // 判断是否为中文字符
                if(temp.matches(chinese)){
                } else {
                    isChinese = false;
                }
            }
        }
        return isChinese;
    }

    /**
     * 描述：是否包含中文.
     *
     * @param str 指定的字符串
     * @return 是否包含中文:是为true，否则false
     */
    public static Boolean isContainChinese(String str){
        Boolean isChinese = false;
        String chinese = "[\u0391-\uFFE5]";
        if(CommonUtil.isNotEmpty(str)){
            // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
            for(int i = 0; i < str.length(); i++){
                // 获取一个字符
                String temp = str.substring(i, i + 1);
                // 判断是否为中文字符
                if(temp.matches(chinese)){
                    isChinese = true;
                } else {

                }
            }
        }
        return isChinese;
    }

    /**
     * 座机正则匹配
     */
    public static boolean isLandLineNo(String str){
        Boolean isMobileNo = false;
        try{
            Pattern p = Pattern.compile("^(0[0-9]{2,3})?([2-9][0-9]{6,7})+([0-9]{1,4})?$");
            Matcher m = p.matcher(str);
            isMobileNo = m.matches();
        } catch(Exception e){
            e.printStackTrace();
        }
        return isMobileNo;
    }

    /**
     * 手机号格式验证.
     */
    public static Boolean isMobileNo(String str){
        Boolean isMobileNo = false;
        try{
            Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(17[0,5-9])|(18[0,5-9]))\\d{8}$");
            Matcher m = p.matcher(str);
            isMobileNo = m.matches();
        } catch(Exception e){
            e.printStackTrace();
        }
        return isMobileNo;
    }

    /**
     * 是否为数字
     */
    public static boolean isDigit(String digitString){
        if(!CommonUtil.isEmpty(digitString)){
            String regex = "[0-9]*";
            return isMatch(regex, digitString);
        }
        return false;
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
     * 是否为URL地址
     */
    public static boolean isUrl(String strIp){
        String strPattern = "^((https?)|(ftp))://(?:(\\s+?)(?::(\\s+?))?@)?([a-zA-Z0-9\\-.]+)"
                            + "(?::(\\d+))?((?:/[a-zA-Z0-9\\-._?,'+\\&%$=~*!():@\\\\]*)+)?$";
        return isMatch(strPattern, strIp);
    }

    /**
     * 是否含有特殊字符
     * @param str
     * @return
     */
    public static boolean isSpecialChar(String str) {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 描述：是否只是字母和数字.
     *
     * @param str 指定的字符串
     * @return 是否只是字母和数字:是为true，否则false
     */
    public static Boolean isNumberLetter(String str) {
        Boolean isNoLetter = false;
        String expr = "^[A-Za-z0-9]+$";
        if (str.matches(expr)) {
            isNoLetter = true;
        }
        return isNoLetter;
    }

    /**
     * 描述：是否只是数字.
     *
     * @param str 指定的字符串
     * @return 是否只是数字:是为true，否则false
     */
    public static Boolean isNumber(String str) {
        Boolean isNumber = false;
        String expr = "^[0-9]+$";
        if (str.matches(expr)) {
            isNumber = true;
        }
        return isNumber;
    }

    /**
     * 描述：是否是邮箱.
     *
     * @param str 指定的字符串
     * @return 是否是邮箱:是为true，否则false
     */
    public static Boolean isEmail(String str) {
        Boolean isEmail = false;
        String expr = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        if (str.matches(expr)) {
            isEmail = true;
        }
        return isEmail;
    }

}
