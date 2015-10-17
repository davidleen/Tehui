package com.davidleen29.tehui.acts;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.events.LogoutEvent;

import de.greenrobot.event.EventBus;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;


@ContentView(R.layout.activity_setting)
public class SettingActivity extends BaseActivity {



    @InjectView(R.id.comment)
    View comment;

    @InjectView(R.id.aboutUs)
    View aboutUs;

    @InjectView(R.id.logout)
    View logout;
    @Override
    protected void init(Bundle bundle) {



        comment.setOnClickListener(this);
        aboutUs.setOnClickListener(this);
        logout.setOnClickListener(this);


    }


    @Override
    protected void onViewClick(View v, int id) {



        switch (id)
        {


            case R.id.logout:

                EventBus.getDefault().post(new LogoutEvent());
                break;
            case R.id.aboutUs:
            {
                Intent intent=new Intent( this, AboutUsActivity.class);
                startActivityForResult(intent,-1);
            }break;
            case R.id.comment:{
                Intent intent=new Intent( this, CommentActivity.class);
                startActivityForResult(intent,-1);
            }break;
        }
    }
}
