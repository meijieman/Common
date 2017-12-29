package com.hongfans.common.util;

/**
 * Created by zza on 2016/8/6. Copy from internet.
 */

import android.text.TextUtils;

import com.hongfans.common.log.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * 类说明：  	apk 签名信息获取工具类
 */
public class SignUtils {

    private static final String TAG = SignUtils.class.getSimpleName();

    private static String bytes2Hex(byte[] src) {
        char[] res = new char[src.length * 2];
        final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        for (int i = 0, j = 0; i < src.length; i++) {
            res[j++] = hexDigits[src[i] >>> 4 & 0x0f];
            res[j++] = hexDigits[src[i] & 0x0f];
        }

        return new String(res);
    }

    public static String getFileMD5(File file) {
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) > 0) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    private static String getMd5ByFile(File file) {
        String value = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);

            MessageDigest digester = MessageDigest.getInstance("MD5");
            byte[] bytes = new byte[8192];
            int byteCount;
            while ((byteCount = in.read(bytes)) > 0) {
                digester.update(bytes, 0, byteCount);
            }
            value = bytes2Hex(digester.digest());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    /**
     * 判断文件的MD5是否为指定值
     *
     * @param file
     * @param md5
     * @return
     */
    public static boolean checkMd5(File file, String md5) {
        if (TextUtils.isEmpty(md5)) {
            throw new RuntimeException("md5 cannot be empty");
        }

        String fileMd5 = getMd5ByFile(file);

        LogUtil.i(TAG + String.format(" file's md5=%s, real md5=%s", fileMd5, md5));

        return md5.equalsIgnoreCase(fileMd5);
    }

    /**
     * 判断文件的MD5是否为指定值
     *
     * @param filePath
     * @param md5
     * @return
     */
    public static boolean checkMd5(String filePath, String md5) {
        try {
            return checkMd5(new File(filePath), md5);
        } catch (Exception e) {
            return false;
        }
    }
}
