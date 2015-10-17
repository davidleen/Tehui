package com.davidleen29.tehui.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.davidleen29.tehui.R;

/**
 * Created by davidleen29 on 2015/8/3.
 */
public class FootViewHelper {

    View view;



    private int index;
    private int count;
    private int totalCount;


    boolean hasStopRunnable=true;

    private Runnable runnable;
    public FootViewHelper(Context context, final PageListener pageListener)
    {

        view= LayoutInflater.from(context).inflate(R.layout.list_footer_loadding,null);
        runnable=new Runnable() {
            @Override
            public void run() {
                if(hasStopRunnable)return;
                pageListener.onPageChanged(index+1,count);
            }
        };

    }



    public View getView()
    {
        return view;
    }

    public void setInfo(int index, int count,int totalCount)
    {
        this.index=index;
        this.count=count;
        this.totalCount=totalCount;
        update();

    }

    private void update() {


        view.setVisibility(isLastPage() ? View.GONE : View.VISIBLE);
        hasStopRunnable=true;


    }

    private boolean isLastPage()
    {


        return (index+1)*count>=totalCount ;

    }



    public void becomeVisible()
    {

        if(isLastPage()) return;
        if(!hasStopRunnable)return;
        view.removeCallbacks(runnable);
        view.postDelayed(runnable,500);
        hasStopRunnable=false;



    }



    public interface  PageListener
    {

        public void onPageChanged(int newPageIndex, int pageCount);
    }
}
