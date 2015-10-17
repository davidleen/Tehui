package com.davidleen29.tehui.helper;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by davidleen29 on 2015/7/30.
 */
public class PixelHelper {


    private static Context appContext;


    public static void init(Context context)
    {
        appContext=context;
    }
    public static int dp2px(float  dp)
    {


        DisplayMetrics metrics = appContext.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);

    }
}
