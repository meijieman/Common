package com.major.base.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/*
 * double保留两位小数
 *
 */
//4种方法，都是四舍五入
public class FloatUtil {

    public static void main(String[] args){
        double d = 111231.5585;
        method1(d);
    }

    /**
     * BigDecimal
     */
    public static void method1(double d){
        BigDecimal bg = new BigDecimal(d);
        double d1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        System.out.println(d1);
    }

    /**
     * DecimalFormat转换最简便
     */
    public static void method2(double d){
        DecimalFormat df = new DecimalFormat("#.00");
//		DecimalFormat df = new DecimalFormat(".00");
        System.out.println(df.format(d));
    }

    /**
     * String.format打印最简便
     */
    public static void method3(double d){
        System.out.println(String.format("%.2f", d));
    }

    /**
     * NumberFormat
     *
     * @param d
     */
    public static void method4(double d){
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);//设置最大小数位数
        System.out.println(nf.format(d));
    }
}
