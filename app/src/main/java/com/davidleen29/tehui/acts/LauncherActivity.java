package com.davidleen29.tehui.acts;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.api.ApiManager;
import com.davidleen29.tehui.helper.CacheManager;
import com.davidleen29.tehui.tasks.ThTask;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.City;

import roboguice.inject.ContentView;


@ContentView(R.layout.activity_launcher)
public class LauncherActivity extends BaseActivity {



    @Inject
    ApiManager apiManager;



    @Inject
    CacheManager cacheManager;
    @Override
    protected void init(Bundle bundle) {

        loadCitysSilent();

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent=new Intent(LauncherActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);


    }


    private void loadCitysSilent()
    {
        ThTask<City.GetCitysResponse> task=   new ThTask<City.GetCitysResponse>(this)
        {
            @Override
            public City.GetCitysResponse call() throws Exception {
                return apiManager.getCities();
            }

            @Override
            protected void doOnSuccess(City.GetCitysResponse data) {

                if(data.getErrCode()==City.GetCitysResponse.ErrorCode.ERR_OK) {

                    cacheManager.setCityInfos(data);
                }

            }
        };
        task.setSilence(true);
        task.execute();
    }
}
