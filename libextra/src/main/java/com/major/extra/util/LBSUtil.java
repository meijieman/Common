package com.major.extra.util;


public class LBSUtil{

    private final static double EARTH_RADIUS = 6378137.0;

    /**
     * 经纬度算距离
     *
     * @param lat_a 纬度a
     * @param lon_a 经度a
     * @param lat_b 纬度b
     * @param lon_b 经度b
     * @return
     */
    public static double gps2m(double lat_a, double lon_a, double lat_b, double lon_b){
        double radLat1 = (lat_a * Math.PI / 180.0);
        double radLat2 = (lat_b * Math.PI / 180.0);
        double a = radLat1 - radLat2;
        double b = (lon_a - lon_b) * Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s *= EARTH_RADIUS;

        return s;
    }

    /** 格式化距离 */
    public static String distanceReverse(double paramInt){
        String str1 = null;
        float f = (float)(paramInt / 1000.0F);// 2004
        String str2 = String.format("%1.2f", f);
        str1 = str2 + "km";
        return str1;
    }
}
