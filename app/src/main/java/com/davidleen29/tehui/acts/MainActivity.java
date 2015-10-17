package com.davidleen29.tehui.acts;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.davidleen29.tehui.BuildConfig;
import com.davidleen29.tehui.R;
import com.davidleen29.tehui.ThApplication;
import com.davidleen29.tehui.api.ApiManager;
import com.davidleen29.tehui.events.AppInitCompleteEvent;
import com.davidleen29.tehui.events.CitySetEvent;
import com.davidleen29.tehui.events.LocationInfo;
import com.davidleen29.tehui.events.LoginCompleteEvent;
import com.davidleen29.tehui.events.LogoutEvent;
import com.davidleen29.tehui.events.OnLoadSystemMessageCountEvent;
import com.davidleen29.tehui.events.SetCurrentCityEvent;
import com.davidleen29.tehui.events.SetFlowCompleteEvent;
import com.davidleen29.tehui.frags.BaseDialogFragment;
import com.davidleen29.tehui.frags.FriendFragment;
import com.davidleen29.tehui.frags.MainFragment;
import com.davidleen29.tehui.frags.MineFragment;
import com.davidleen29.tehui.helper.CacheManager;


import com.davidleen29.tehui.helper.LocationHelper;
import com.davidleen29.tehui.tasks.ThTask;
import com.davidleen29.tehui.utils.Log;
import com.davidleen29.tehui.utils.ToastUtils;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.City;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import roboguice.activity.event.OnCreateEvent;
import roboguice.activity.event.OnDestroyEvent;
import roboguice.event.Observes;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity   {

    @InjectView(R.id.main_main)
    View main_main;

    @InjectView(R.id.main_friend)
    View main_friend;

    @InjectView(R.id.main_mine)
    View main_mine;


    @InjectView(R.id.new_system_message)
    View new_system_message;



    @InjectView(R.id.contentPanel)
    ViewPager viewPager;

    private View[] buttons;

    @Inject
    CacheManager cacheManager;

    @Inject
    ApiManager apiManager;


   LocationHelper locationHelper;


    @Override
    protected void init(Bundle bundle) {

        locationHelper=new LocationHelper(getApplicationContext());


        main_friend.setOnClickListener(this);
        main_main.setOnClickListener(this);
        main_mine.setOnClickListener(this);

        buttons=new View[]{main_main,main_friend,main_mine};
       final List<Fragment> fragmentList=new ArrayList<Fragment>();
        fragmentList.add(new MainFragment());
        fragmentList.add(new FriendFragment());
        fragmentList.add(new MineFragment());

        FragmentPagerAdapter pagerAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {

             return   fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };

        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


                for (int i = 0, count = buttons.length; i < count; i++)
                    buttons[i].setSelected(i == position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setOffscreenPageLimit(2);



        buttons[0].performClick();






//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//
//                initFlow();
//
//            }
//        });


        initFlow();

    }


    @Override
    protected void onActivityCreate(@Observes OnCreateEvent event) {
        super.onActivityCreate(event);
       locationHelper.startLocationUpdate();

    }
    protected void onActivityDestroy(@Observes OnDestroyEvent event) {


        locationHelper.stopLocationUpdate();
    }


    private  void initFlow()
    {
        if(cacheManager.getCurrentUser()==null)
        {

            Intent intent=new Intent(this,FirstOpen.class) ;
            startActivity(intent);

        }else
        {
            EventBus.getDefault().post(new LoginCompleteEvent());
        }
    }







    @Override
    protected void onViewClick(View v,int id) {


        for (int i = 0,count=buttons.length; i < count; i++) {
            buttons[i].setSelected(buttons[i]==v);
            if(buttons[i]==v)
            {
                viewPager.setCurrentItem(i);
            }


        }





    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        if(BuildConfig.DEBUG) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent intent=new Intent(this,TestWeixinActivity.class) ;
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }











    /**
     *
     * @param cityName
     */
    private void showCityDialog(String cityName)
    {



        City.CityInfo cityInfo=null;

        if(cacheManager.getCityList()!=null)
        {
            for(City.CityInfo tempCityInfo:cacheManager.getCityList().getCityInfoList())
            {
                if(cityName.indexOf(tempCityInfo.getName())>-1 )
                {
                    cityInfo=tempCityInfo;
                    break;
                }
            }
        }

        if(cityInfo==null)
        {

            ToastUtils.show("未能自动定位到城市，请手动设置城市。");
            Intent intent=new Intent(this,SetCityActivity.class);
            startActivity(intent);

        }else {

            final City.CityInfo finalCityInfo = cityInfo;
            BaseDialogFragment dialogFragment=   new BaseDialogFragment()
            {

                @Override
                protected void onYesClick(View view) {

                    new   ThTask<City.UserSetCityResponse>(getActivity()) {

                        @Inject
                        ApiManager apiManager;




                        @Override
                        protected void doOnSuccess(City.UserSetCityResponse data) {

                            if(data.getErrCode()==City.UserSetCityResponse.ErrorCode.ERR_OK)
                            {



                                EventBus.getDefault().post(new CitySetEvent());
                                dismiss();


                            }else
                            {
                                ToastUtils.show(data.getErrMsg());
                            }

                        }

                        @Override
                        public City.UserSetCityResponse call() throws Exception {
                            return apiManager.userSetCity(finalCityInfo );
                        }


                    }.execute();




                }

                @Override
                protected void onNoClick(View view) {
                    Intent intent=new Intent(getActivity(), SetCityActivity.class);

                    intent.putExtra(SetCityActivity.PARAM_CITY, finalCityInfo);
                    startActivity(intent);
                    dismiss();

                }
            };
            dialogFragment.setParams("您当前位置是", cityInfo.getName(), "是的", "手动调整");

            dialogFragment.show(getSupportFragmentManager(),dialogFragment.getClass().getName());


//            ConfirmCityDialogFragment dialogFragment = ConfirmCityDialogFragment.newInstance(cityInfo);
//            dialogFragment.show(getSupportFragmentManager(), "TEST");
//            getSupportFragmentManager().beginTransaction().show(dialogFragment).commit();
        }

    }





    public void onEvent(LoginCompleteEvent event)
    {


        Log.d(TAG, "LoginCompleteEvent");
         if(!cacheManager.isSetConcern())
         {
             //first login set bank  set catogery
             Intent intent=new Intent(this,SetBankActivity.class);
             startActivity(intent);

         }
        else
         {
             EventBus.getDefault().post(new SetFlowCompleteEvent());

         }

    }




        public void onEventMainThread(SetFlowCompleteEvent event)
        {

            Log.d(TAG, "SetFlowCompleteEvent");
            cacheManager.setConcern();
            if(cacheManager.getCurrentCity()==null)
            {

                getWindow().getDecorView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showCityDialog(cacheManager.getLocationInfo() == null ? "" : cacheManager.getLocationInfo().cityName);
                    }
                },500);



            }else
            {
                EventBus.getDefault().post(new CitySetEvent());
            }



        }
        public void onEventMainThread(SetCurrentCityEvent event  )
        {


            getWindow().getDecorView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showCityDialog(cacheManager.getLocationInfo() == null ? "" : cacheManager.getLocationInfo().cityName);
                }
            },500);


        }

    public void onEvent(CitySetEvent event  )
    {

        Log.d(TAG, "CitySetEvent");
        EventBus.getDefault().postSticky(new AppInitCompleteEvent());
    }



    public void onEvent(LogoutEvent event)
    {

        //登出事件  主界面启动登录界面，其他都关闭。


        //clear all localMessage
        cacheManager.clear();
        Intent intent=new Intent(this,FirstOpen.class);
        startActivity(intent);


    }


    //读取到新系统消息记录数据
    public void onEvent(OnLoadSystemMessageCountEvent event)
    {



        new_system_message.setVisibility(event.messageCount>0?View.VISIBLE: View.GONE);


    }



}
