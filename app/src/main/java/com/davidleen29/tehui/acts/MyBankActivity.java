package com.davidleen29.tehui.acts;


import android.content.Intent;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.adapters.AbstractAdapter;
import com.davidleen29.tehui.adapters.SetBankAdapter;
import com.davidleen29.tehui.adapters.SetBankConcernAdapter;
import com.davidleen29.tehui.adapters.SetBankUnConcernAdapter;
import com.davidleen29.tehui.api.ApiManager;
import com.davidleen29.tehui.api.HttpUrl;
import com.davidleen29.tehui.events.ConcernBankChangedEvent;
import com.davidleen29.tehui.events.ConcernCategoryChangedEvent;
import com.davidleen29.tehui.events.UserInfoUpdateEvent;
import com.davidleen29.tehui.helper.CacheManager;
import com.davidleen29.tehui.lang.CException;
import com.davidleen29.tehui.lang.UnMixable;
import com.davidleen29.tehui.tasks.ThTask;
import com.davidleen29.tehui.utils.ToastUtils;
import com.davidleen29.tehui.widget.HeaderView;
import com.davidleen29.tehui.widget.LinearListView;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightGridView;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Common;
import com.huiyou.dp.service.protocol.User;

import java.util.ArrayList;
import java.util.List;


import de.greenrobot.event.EventBus;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;


@ContentView(R.layout.activity_my_bank)
public class MyBankActivity extends SimpleActivity<User.GetUserSetBankResponse>   {

    @Inject
    ApiManager apiManager;

    @InjectView(R.id.concern)
    ExpandableHeightGridView concern;

    @InjectView(R.id.unConcern)
    ExpandableHeightGridView unConcern;

    @Inject
    CacheManager cacheManager;


    @Inject
    SetBankConcernAdapter concernAdapter;


    @Inject
    SetBankUnConcernAdapter unConcernAdapter;


    @InjectView(R.id.apply)
    View apply;




    @InjectView(R.id.header)
    HeaderView header;

    boolean hasChange=false;
    @Override
    protected void init(Bundle bundle) {
        super.init(bundle);


        concern.setAdapter(concernAdapter);
        unConcern.setAdapter(unConcernAdapter);
        concern.setExpanded(true);
        unConcern.setExpanded(true);
        //clear the selector
        concern.setSelector(new StateListDrawable());
        unConcern.setSelector(new StateListDrawable());


        concern.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                hasChange = true;
                User.UserSetBank setBank = concernAdapter.getItem(position);

                unConcernAdapter.addItem(setBank);
                concernAdapter.removeItem(setBank);
                concernAdapter.notifyDataSetChanged();
                unConcernAdapter.notifyDataSetChanged();

            }
        });
        unConcern.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                hasChange=true;
                User.UserSetBank setBank=  unConcernAdapter.getItem(position);


                concernAdapter.addItem(setBank);

                unConcernAdapter.removeItem(setBank);
                concernAdapter.notifyDataSetChanged();
                unConcernAdapter.notifyDataSetChanged();


            }
        });

        apply.setOnClickListener(this);


    }

    @Override
    protected User.GetUserSetBankResponse readOnBackground() throws CException {
        return apiManager.getUserSetBank(cacheManager.getCurrentUser().getUserName());
    }

    @Override
    protected void onDataLoaded(User.GetUserSetBankResponse data) {


        if(data.getErrCode()== User.GetUserSetBankResponse.ErrorCode.ERR_OK)
        {


            List<User.UserSetBank> concernList=new ArrayList<>();
            List<User.UserSetBank> unConcernList=new ArrayList<>();
            for(User.UserSetBank setBank:data.getUserSetBankList())
            {
                if(setBank.getIsFollow()== Common.BooleanValue.BooleanValue_TRUE)
                {
                    concernList.add(setBank);
                }else
                {
                    unConcernList.add(setBank);
                }
            }

            concernAdapter.setDataArray(concernList);

            unConcernAdapter.setDataArray(unConcernList);
        }else
        {
            ToastUtils.show(data.getErrMsg());
        }

    }


    @Override
    public void onBackPressed() {


        if(hasChange)
        {


          final   List<Integer> integers=new ArrayList<>();

            for(User.UserSetBank  bank:concernAdapter.getDatas())
            {

                integers.add((int) bank.getBankId());
            }

            new ThTask<User.UserSetBankResponse>(this) {

                User.GetUserSetBankResponse getUserSetBankResponse;
                @Override
                public User.UserSetBankResponse call() throws Exception {
                    User.UserSetBankResponse result= apiManager.userSetBank(cacheManager.getCurrentUser().getUserName(),integers);

                    if(result.getErrCode()== User.UserSetBankResponse.ErrorCode.ERR_OK)
                    {
                        getUserSetBankResponse=apiManager.getUserSetBank(cacheManager.getCurrentUser().getUserName());

                    }

                    return result;
                }

                @Override
                protected void doOnSuccess(User.UserSetBankResponse data) {

                    if(data.getErrCode()== User.UserSetBankResponse.ErrorCode.ERR_OK)
                    {


                        cacheManager.setConcernBank(getUserSetBankResponse);
                        EventBus.getDefault().post(new UserInfoUpdateEvent());
                        EventBus.getDefault().post(new ConcernBankChangedEvent());
                        MyBankActivity.super.onBackPressed();


                    }else
                    {
                        ToastUtils.show(data.getErrMsg());
                    }



                }
            }.execute();




        }else
            super.onBackPressed();



    }


    @Override
    protected void onViewClick(View v, int id) {

        switch (id)
        {
            case R.id.apply:


                Uri uri = Uri.parse(HttpUrl.WEB_HOME);
                  Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
                break;
        }
    }
}
