package com.davidleen29.tehui.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by davidleen29   qq:67320337
 *
 */

public class ToastUtils {


    private static Context appContext;
    public static void show( String message) {
        Toast.makeText(appContext, message, Toast.LENGTH_LONG).show();
    }

    public static void setAppContext(Context context)
    {
        appContext=context;
    }
}
