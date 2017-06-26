package com.hongfans.libcommon.util;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 输入信息验证
 */
public class REUtil{

    /**
     * 手机号码验证
     *
     * @param phoneNum
     * @return
     */
    public static boolean mobileMumVerify(String phoneNum){
        // Integer phone = 0;
        // try{
        // phone = Integer.parseInt(phoneNum);
        // String s = phone+"";
        // return s.length() >= 11;
        // }catch(Exception e){
        // return false;
        // }
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

//	/**
//	 * 检查空格
//	 *
//	 * @param str
//	 * @return
//	 */
//	public static boolean checkSpace(String str) {
//		// String tReg = "(^[\\n\\r\\t]{1,})|([\\n\\r\\t]{1,}$)";
//		// String lReg = "[\\n\\r\\t]{2,}";
//		String tReg = "(^[\\f\\n\\r\\t]{1,})|([\\f\\n\\r\\t]{1,}$)";
//		String lReg = "[\\f\\n\\r\\t]{2,}";
//		if (check(str, tReg) || check(str, lReg)) {
//			return false;
//		}
//		return true;
//	}

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
     * 中文名
     *
     * @param str
     * @return
     */
    public static boolean checkCName(String str){
        String s1 = "^([a-zA-Z\\u4e00-\\u9fa5 ])*$";
        String s2 = "(不详)|(不祥)|(未知)|(不知道)";

        if(str.contains(" ")){
            System.out.println("111111");
            return false;
        } else {
            try{
                byte[] midbytes = str.getBytes("gbk");
                if(!check(str, s1.replace("䶮", "好")) || !checkSpace(str)
                   || check(str, s2) || str.length() < 2
                   || str.length() > 50){
                    System.out.println(222);
                    return false;
                }
            } catch(Exception e){
                System.out.println(333);
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * 汉字正则表达式 如果传入的参数为空，返回为true，所以需要做非空判断
     *
     * @param str 非空字符串
     * @return
     */
    public static boolean checkHZ(String str){
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
        if(count == str_length){
            return true;
        } else {
            return false;
        }
    }

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

//	/**
//	 * 台胞证及港澳通行证
//	 *
//	 * @param str
//	 * @return
//	 */
//	public static boolean checkMTPs2(String str) {
//		if(str == null)
//			return false;
//
//		Pattern p = Pattern.compile("[A-Z][1-9]{8}");
//		Matcher matcher = p.matcher(str);
//		return matcher.matches();
//	}

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
     * 验证受益比例
     *
     * @param str
     * @return
     */
    public static boolean checkProportion(String str){
//		boolean result = check(str, "^+?(([1-9]\\d?)|(100))$");
        try{
            int parseInt = Integer.parseInt(str);
            if(parseInt > 100 || parseInt < 1){
                return false;
            }
        } catch(Exception e){
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

    public static boolean checkLength(String text, int min, int max){
        int length = 0;
        if(!TextUtils.isEmpty(text)){
            length = text.length();
        }
        return length >= min && length <= max;
    }

    public static boolean checkLength(String text, int min){
        int length = 0;
        if(!TextUtils.isEmpty(text)){
            length = text.length();
        }
        return length >= min;
    }

    public static String checkIdentifyPeriod(String date){
        if(date != null){
            String[] array = date.split("\\|");
            if(array != null){
                if(array.length > 0){
                    if(TextUtils.isEmpty(array[0])){
                        return "请填写证件起始日期";
                    }
                    if(date.indexOf("|") > 0){
                        if(array.length < 2 || TextUtils.isEmpty(array[1])){
                            return "请填写证件结束日期";
                        }
                    }
                } else {
                    return "请选择证件有效期";
                }
            }
        } else {
            return "请选择证件有效期";
        }
        return null;
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
}
