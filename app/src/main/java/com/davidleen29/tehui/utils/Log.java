package com.davidleen29.tehui.utils;


import com.davidleen29.tehui.BuildConfig;
import com.davidleen29.tehui.api.ApiManager;

/**
 * Created by davidleen29@gmail.com onFieldDataChangeListener 14-2-17.
 */
public class Log {
    public static void v(String tag, String message) {

        i(tag,message,null);
    }

    public static void i(String tag, String message) {

        i(tag,message,null);
    }


    public static void e(String tag, String message) {

        e(tag, message, null);

    }


    public static void e(String tag, String message,Throwable t) {
        if(BuildConfig.DEBUG) {

            if(null!=t)
            {
                t.printStackTrace();
            }

            android.util.Log.e(tag, message);
        }
    }


    public static void i(String tag, String message,Throwable t) {
        if(BuildConfig.DEBUG) {

            if(null!=t)
            {
                t.printStackTrace();
            }

            android.util.Log.i(tag, message);
        }
    }

    public static void d(String tag, String message) {

        d(tag,message,null);

    }
    public static void d(String tag, String message,Throwable t) {
        if(BuildConfig.DEBUG) {

            if(null!=t)
            {
                t.printStackTrace();
            }

            android.util.Log.d(tag, message);
        }
    }

    public static void v(String tag, String message,Throwable t) {
        if(BuildConfig.DEBUG) {

            if(null!=t)
            {
                t.printStackTrace();
            }

            android.util.Log.v(tag, message);
        }
    }



}
