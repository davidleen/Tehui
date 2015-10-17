package com.davidleen29.tehui.helper;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 一些常量数据定义
 * Created by davidleen29 on 2015/8/21.
 */
public class Constraints {

    public static  String [] WEEKS=new String[]{  "星期一" ,"星期二","星期三","星期四","星期五","星期六","星期天"
    };

    public static List<String> WEEKLIST=new ArrayList<>();
      static
    {
        for(String s:WEEKS)
        {
            WEEKLIST.add(s);
        }

    }
}
