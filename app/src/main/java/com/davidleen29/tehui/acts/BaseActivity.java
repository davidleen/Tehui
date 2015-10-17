package com.davidleen29.tehui.acts;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.davidleen29.tehui.events.BaseEvent;
import com.davidleen29.tehui.events.LogoutEvent;
import com.davidleen29.tehui.helper.PictureHelper;
import com.davidleen29.tehui.utils.Log;
import com.umeng.analytics.MobclickAgent;


import de.greenrobot.event.EventBus;
import roboguice.activity.RoboFragmentActivity;
import roboguice.activity.event.OnCreateEvent;
import roboguice.event.Observes;

/**
 * 所有activity 基类， 提供公共方法
 *
 *
 *
 * Created by davidleen29 on 2015/7/13.
 */
public class BaseActivity  extends RoboFragmentActivity implements View.OnClickListener {


    public String TAG;





    protected void onActivityCreate(@Observes OnCreateEvent event)
    {
        Log.v(TAG, "onCreate ");

        EventBus.getDefault().register(this);
        init(event.getSavedInstanceState());




    }



    protected void init(Bundle bundle) {

    }




    protected BaseActivity() {
        super();
        TAG = ((Object) this).getClass().getName();


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v(TAG, "onActivityResult ---requestCode: " + requestCode + ",resultCode:" + resultCode + ",data:" + data);



    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.v(TAG, "onBackPressed ");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Log.v(TAG, "onConfigurationChanged    : " + newConfig);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {

        return super.onCreateView(name, context, attrs);

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        Log.v(TAG, "onDestroy ");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "onPause ");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onResume(this);
        Log.v(TAG, "onResume ");




    }
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();


        Log.v(TAG, "onAttachedToWindow ");
        }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.v(TAG, "onPostResume ");
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart ");
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.v(TAG, "onSaveInstanceState     bundle: " + outState);
    }



    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop ");
    }



    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.v(TAG, "onLowMemory    ");
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {



        menu.add(Menu.NONE,Menu.NONE,0,"拍照");
        menu.add(Menu.NONE, Menu.NONE, 1, "从手机中选择");
        menu.add(Menu.NONE, Menu.NONE, 2, "取消");


    }




    @Override
    public boolean onContextItemSelected(MenuItem item) {



        switch (item.getOrder())
        {
            case 0:


                PictureHelper.getPicFromCamera(this);

                break;
            case 1:
                PictureHelper.getPicFromAlbum(this);

                break;
            case 2:

                break;
        }
        return super.onContextItemSelected(item);

    }

    @Override
    public void onPanelClosed(int featureId, Menu menu) {
        super.onPanelClosed(featureId, menu);
    }

    @Override
    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        return super.onPreparePanel(featureId, view, menu);
    }


    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        return super.onCreatePanelMenu(featureId, menu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        boolean result = super.onCreateOptionsMenu(menu);
        //  menu.findItem(android.R.Id.home).setEnabled(false);
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses onFieldDataChangeListener the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }


        return result;
    }



    public boolean isOnTop()
    {
//        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
//
//        // get the info from the currently running flowActivity
//
//        List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(1);
//
//        Log.d("topActivity", "CURRENT Activity ::"
//                + taskInfo.get(0).topActivity.getClassName());
//        return taskInfo.get(0).topActivity.getClassName().equals(this.getClass().getName());
        return false;



    }


    @Override
    public void onClick(View v) {

        onViewClick(v,v.getId());
    }


    protected void onViewClick(View v,int id)
    {

    }




    public void onEvent(BaseEvent event)
    {}



    public void onEvent(LogoutEvent event)
    {

        //主界面重载该方法

        //登出事件  主界面启动登录界面，其他都关闭。
        finish();
    }
}
