package com.davidleen29.tehui.acts;

import android.os.AsyncTask;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.davidleen29.tehui.R;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_about_us)
public class AboutUsActivity extends BaseActivity {


    @InjectView(R.id.logo)
    View logo;

    @Override
    protected void init(Bundle bundle) {


//        logo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//
//                 String s=null;
//                s.length();
//            }
//        });


    }
}
