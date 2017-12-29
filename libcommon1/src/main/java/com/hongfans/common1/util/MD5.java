package com.hongfans.common1.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5{

    public static String getVal(String plainText){
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            try{
                md.update(plainText.getBytes("GB2312"));
            } catch(UnsupportedEncodingException e){
                e.printStackTrace();
                return null;
            }
            byte b[] = md.digest();

            int i;
            StringBuilder buf = new StringBuilder("");
            for(byte aB : b){
                i = aB;
                if(i < 0){
                    i += 256;
                }
                if(i < 16){
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }

            return buf.toString();
        } catch(NoSuchAlgorithmException e){
            e.printStackTrace();
            return null;
        }
    }

    public static String getVal_GBK(String plainText){
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes("GBK"));
            byte b[] = md.digest();

            int i;
            StringBuilder buf = new StringBuilder("");
            for(byte aB : b){
                i = aB;
                if(i < 0){
                    i += 256;
                }
                if(i < 16){
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }

            return buf.toString();
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String getVal_UTF8(String plainText){
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes("UTF-8"));
            byte b[] = md.digest();

            int i;
            StringBuilder buf = new StringBuilder("");
            for(byte aB : b){
                i = aB;
                if(i < 0){
                    i += 256;
                }
                if(i < 16){
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }

            return buf.toString().toUpperCase();
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    // 微信add
    public final static String getMessageDigest(byte[] buffer){
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try{
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(buffer);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for(byte byte0 : md){
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch(Exception e){
            return null;
        }
    }

}
