package com.davidleen29.tehui.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by davidleen29 on 2015/7/28.
 */
public class TimeUtils {
    public static SimpleDateFormat FORMAT_MM_DD = new SimpleDateFormat("MM-dd");

    public static SimpleDateFormat FORMAT_YY_MM_DD = new SimpleDateFormat("yy-MM-dd");
    public static SimpleDateFormat FORMAT_YYYY_MM_DD = new SimpleDateFormat("yyyy.MM.dd");
    public  static  int getExpireDayNumber(long expireTime)
    {

      return (int) ((expireTime- Calendar.getInstance().getTimeInMillis())/(1000*60*60*24));
    }

    public static String longToYY_MM_DD(long endDate) {


        Date date=new Date(endDate);
      return FORMAT_YY_MM_DD.format(date);
    }
    public static String longToMM_DD(long endDate) {


        Date date=new Date(endDate);
        return FORMAT_MM_DD.format(date);
    }
    public static String longToYYYY_MM_DD(long time) {


        Date date=new Date(time);

        return FORMAT_YYYY_MM_DD.format(date);
    }



    private static long[] timeStep=new long[]{60,60*60,60*60*24,60*60*24*2,60*60*24*3};
    private static String[] message=new String[]{"刚刚","一小时以内","今天","昨天","前天"};
    public static String getReplyTimeMessage(long time)
    {

    long passTimeInSecend=   ( Calendar.getInstance().getTimeInMillis() - time) /1000;

        int length = timeStep.length;
        for (int i = 0; i < length; i++) {


            if(passTimeInSecend<=timeStep[i])
                return message[i];
        }





        return longToYY_MM_DD(time);

    }

    private static long[] dayStep=new long[]{ 60*60*24,60*60*24*2,60*60*24*3};
    private static String[] dayMessage=new String[]{ "今天","昨天","前天"};
    /**
     * 以日期为基准单位
     * @param time
     * @return
     */
    public static String getSendDayMessage(long time)
    {

        long passTimeInSecend=   ( Calendar.getInstance().getTimeInMillis() - time) /1000;

        int length = dayStep.length;
        for (int i = 0; i < length; i++) {


            if(passTimeInSecend<=dayStep[i])
                return message[i];
        }





        return longToMM_DD(time);

    }
}
