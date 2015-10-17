package com.davidleen29.tehui.acts;

import com.davidleen29.tehui.acts.util.SystemUiHider;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.api.ApiManager;
import com.davidleen29.tehui.events.LoginCompleteEvent;
import com.davidleen29.tehui.helper.CacheManager;
import com.davidleen29.tehui.tasks.ThTask;
import com.davidleen29.tehui.utils.ToastUtils;

import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.User;

import de.greenrobot.event.EventBus;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */

@ContentView(R.layout.activity_first_open)
public class FirstOpen extends BaseActivity {



    @InjectView(R.id.login)
    Button login;


    @InjectView(R.id.enter)
    Button enter;


    @InjectView(R.id.viewPager)
    ViewPager viewPager;



    @Inject
    CacheManager cacheManager;





    @Override
    protected void init(Bundle bundle) {



        login.setOnClickListener(this);
        enter.setOnClickListener(this);


     final   int[] layoutOuts=new int[]  {R.layout.page_item_first_open_1,R.layout.page_item_first_open_2};


        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return layoutOuts.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }


            @Override
            public Object instantiateItem(ViewGroup container, int position) {
              View v=   LayoutInflater.from(container.getContext()).inflate(layoutOuts[position],null);

                container.addView(v);
                return v;
            }
        });
       // viewPager.setCurrentItem(0);

    }


    @Override
    protected void onViewClick(View v,int id)
    {
        switch (v.getId())
        {


            case R.id.login: {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
                break;

            case R.id.enter: {
                loginAsGuest();

            }
                break;
        }
    }


    /**
     *
     */
    private  void loginAsGuest()
    {


        String currentTempUserId="";
        if(cacheManager.getCurrentUser()!=null)
        {
            currentTempUserId=cacheManager.getCurrentUser().getUserName();
        }

        final String finalCurrentTempUserId = currentTempUserId;
        new ThTask<User.LoginResponse>(this)
        {

            @Inject
            ApiManager apiManager;
            @Override
            public User.LoginResponse call() throws Exception {

              return  apiManager.loginAsGuest(finalCurrentTempUserId);
            }

            @Override
            protected void doOnSuccess(User.LoginResponse data) {


                if(data.getErrCode()== User.LoginResponse.ErrorCode.ERR_OK)
                {



                    cacheManager.setCurrentUser(data.getUserInfo());
                    EventBus.getDefault().post(new LoginCompleteEvent());

                }else
                {

                    ToastUtils.show(data.getErrMsg());
                }

            }
        }.execute();

    }



    public void onEvent(LoginCompleteEvent event)
    {
        finish();
    }


    @Override
    public void onBackPressed() {

        //avoid back

    }
}
