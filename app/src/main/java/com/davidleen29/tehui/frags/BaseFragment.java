package com.davidleen29.tehui.frags;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.events.BaseEvent;
import com.davidleen29.tehui.utils.Log;

import de.greenrobot.event.EventBus;
import roboguice.fragment.RoboFragment;

/**
 * A simple {@link Fragment}
 */
public class BaseFragment extends RoboFragment implements View.OnClickListener {




protected String TAG;


    public static BaseFragment newInstance(String param1, String param2) {
        BaseFragment fragment = new BaseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public BaseFragment() {

        TAG=this.getClass().getName();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
        EventBus.getDefault().register(this);

    }


    @Override
    public void onDestroy() {


        EventBus.getDefault().unregister(this);
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {


        onViewClick(v, v.getId());
    }

    protected void onViewClick(View v,int id)
    {}




    public void onEvent(BaseEvent event)
    {}

}
