package com.hongfans.common.encrypt;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by felix on 16/5/19.
 */
public class SHA1{

    public static String sha1Digest(String text){
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(text.getBytes());
            text = new BigInteger(1, messageDigest.digest()).toString(16);
        } catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }

        return text;
    }
}
