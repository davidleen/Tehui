package com.davidleen29.tehui.utils;

import java.util.Random;

/**
 * 随机生成功能类
 *
 * Created by davidleen29 on 2015/7/21.
 */
public class RandomUtils {

    public static  Random  random=new Random();
    public static String generateDigitalString()
    {
      return  generateDigitalString(20);
    }

    public static String generateDigitalString(int n )
    {
        StringBuilder sb = new StringBuilder(n);
        for(int i=0; i < n; i++)
            sb.append( random.nextInt(10));
        return sb.toString();


    }
}
