package com.hongfans.libcommon.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPUtils{

    /**
     * 根据域名得到ip
     *
     * @param url
     * @return
     */
    public static String getIPByDomain(String url){
        try{
            InetAddress inetAddress = InetAddress.getByName(url);
            return inetAddress.getHostAddress();
        } catch(UnknownHostException e){
            e.printStackTrace();
            return null;
        }
    }
}
