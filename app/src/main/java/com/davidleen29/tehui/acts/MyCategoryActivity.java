package com.davidleen29.tehui.acts;

import android.graphics.drawable.StateListDrawable;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.adapters.SetBankAdapter;
import com.davidleen29.tehui.adapters.SetCategoryAdapter;
import com.davidleen29.tehui.adapters.SetCategoryConcernAdapter;
import com.davidleen29.tehui.adapters.SetCategoryUnConcernAdapter;
import com.davidleen29.tehui.api.ApiManager;
import com.davidleen29.tehui.events.ConcernCategoryChangedEvent;
import com.davidleen29.tehui.events.SetFlowCompleteEvent;
import com.davidleen29.tehui.events.UserInfoUpdateEvent;
import com.davidleen29.tehui.helper.CacheManager;
import com.davidleen29.tehui.lang.CException;
import com.davidleen29.tehui.tasks.ThTask;
import com.davidleen29.tehui.utils.ToastUtils;
import com.davidleen29.tehui.widget.HeaderView;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Common;
import com.huiyou.dp.service.protocol.User;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;


@ContentView(R.layout.activity_my_category)
public class MyCategoryActivity extends SimpleActivity<User.GetUserSetCategoryResponse> {

    @Inject
    ApiManager apiManager;

    @InjectView(R.id.concern)
    GridView concern;

    @InjectView(R.id.unConcern)
    GridView unConcern;

    @Inject
    CacheManager cacheManager;

    @Inject
    SetCategoryConcernAdapter concernAdapter;


    @Inject
    SetCategoryUnConcernAdapter unConcernAdapter;



    @InjectView(R.id.header)
    HeaderView header;

    private boolean hasChanged=false;
    @Override
    protected void init(Bundle bundle) {
        super.init(bundle);


        concern.setAdapter(concernAdapter);
        unConcern.setAdapter(unConcernAdapter);

        //clear the selector
        concern.setSelector(new StateListDrawable());
        unConcern.setSelector(new StateListDrawable());

        concern.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                hasChanged = true;
                User.UserSetCategory setCategory = concernAdapter.getItem(position);

                unConcernAdapter.addItem(setCategory);
                concernAdapter.removeItem(setCategory);
                concernAdapter.notifyDataSetChanged();
                unConcernAdapter.notifyDataSetChanged();

            }
        });
        unConcern.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                hasChanged = true;
                User.UserSetCategory setCategory = unConcernAdapter.getItem(position);


                concernAdapter.addItem(setCategory);

                unConcernAdapter.removeItem(setCategory);
                concernAdapter.notifyDataSetChanged();
                unConcernAdapter.notifyDataSetChanged();


            }
        });





    }

    @Override
    protected User.GetUserSetCategoryResponse readOnBackground() throws CException {
        return apiManager.getUserSetCategory(cacheManager.getCurrentUser().getUserName());
    }

    @Override
    protected void onDataLoaded(User.GetUserSetCategoryResponse data) {


        if(data.getErrCode()== User.GetUserSetCategoryResponse.ErrorCode.ERR_OK)
        {


            List<User.UserSetCategory> concernList=new ArrayList<>();
            List<User.UserSetCategory> unConcernList=new ArrayList<>();
            for(User.UserSetCategory setBank:data.getUserSetCategoryList())
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

        if(hasChanged)
        {

            final List<Integer> integers=new ArrayList<>();

            for(User.UserSetCategory temp:concernAdapter.getDatas()) {
                integers.add((int) temp.getCategoryId());
            }

            new ThTask<User.UserSetCategoryResponse>(this) {


                User.GetUserSetCategoryResponse getUserSetCategoryResponse;
                @Override
                public User.UserSetCategoryResponse call() throws Exception {
                   // return apiManager.userSetCategory(cacheManager.getCurrentUser().getUserName(), integers);

                    User.UserSetCategoryResponse response=   apiManager.userSetCategory(cacheManager.getCurrentUser().getUserName(), integers);
                    if(response.getErrCode()== User.UserSetCategoryResponse.ErrorCode.ERR_OK) {
                        getUserSetCategoryResponse  =  apiManager.getUserSetCategory(cacheManager.getCurrentUser().getUserName());
                    }
                    return response;
                }

                @Override
                protected void doOnSuccess(User.UserSetCategoryResponse data) {

                    if(data.getErrCode()== User.UserSetCategoryResponse.ErrorCode.ERR_OK)
                    {



                        cacheManager.setConcernCategory(getUserSetCategoryResponse);
                        EventBus.getDefault().post(new UserInfoUpdateEvent());
                        EventBus.getDefault().post(new ConcernCategoryChangedEvent());
                        MyCategoryActivity.super.onBackPressed();

                    }else
                    {
                        ToastUtils.show(data.getErrMsg());
                    }



                }
            }.execute();

        }else
        super.onBackPressed();
    }
}
