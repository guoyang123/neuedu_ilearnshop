package com.neuedu.utils;

import java.math.BigDecimal;

/**
 * 价格计算工具类
 * */
public class BigDecimalUtils {

    /**
     * 加法运算
     * */
    public  static BigDecimal add(double d1,Double  d2){

       BigDecimal bigDecimal=new BigDecimal(String.valueOf(d1));
       BigDecimal bigDecimal1=new BigDecimal(String.valueOf(d2));
       return bigDecimal.add(bigDecimal1);
    }

    /**
     * 减法法运算
     * */
    public  static BigDecimal sub(double d1,Double  d2){

        BigDecimal bigDecimal=new BigDecimal(String.valueOf(d1));
        BigDecimal bigDecimal1=new BigDecimal(String.valueOf(d2));
        return bigDecimal.subtract(bigDecimal1);
    }
    /**
     * 乘法运算
     * */
    public  static BigDecimal mul(double d1,Double  d2){

        BigDecimal bigDecimal=new BigDecimal(String.valueOf(d1));
        BigDecimal bigDecimal1=new BigDecimal(String.valueOf(d2));
        return bigDecimal.multiply(bigDecimal1);
    }


    /**
     * 除法运算,保留两位小数，四舍五入
     *
     * */
    public  static BigDecimal div(double d1,Double  d2){

        BigDecimal bigDecimal=new BigDecimal(String.valueOf(d1));
        BigDecimal bigDecimal1=new BigDecimal(String.valueOf(d2));
        return bigDecimal.divide(bigDecimal1,2,BigDecimal.ROUND_HALF_UP);
    }
}
