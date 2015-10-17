package com.davidleen29.tehui.acts;

import android.graphics.drawable.StateListDrawable;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.adapters.SetCategoryAdapter;
import com.davidleen29.tehui.api.ApiManager;
import com.davidleen29.tehui.events.SetFlowCompleteEvent;
import com.davidleen29.tehui.helper.CacheManager;
import com.davidleen29.tehui.lang.CException;
import com.davidleen29.tehui.tasks.ThTask;
import com.davidleen29.tehui.utils.ToastUtils;
import com.davidleen29.tehui.widget.HeaderView;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.User;

import java.util.ArrayList;
import java.util.List;


import de.greenrobot.event.EventBus;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_set_category)
public class SetCategoryActivity extends SimpleActivity<User.GetUserSetCategoryResponse> {



    @InjectView(R.id.gridView)
    GridView gridView;
    @InjectView(R.id.header)
    HeaderView header;



    @Inject
    SetCategoryAdapter adapter;

    @Inject
    ApiManager apiManager;

    @Inject
    CacheManager cacheManager;

    @Override
    protected void init(Bundle bundle) {
        super.init(bundle);

        gridView.setAdapter(adapter);
        //clear the selector
        gridView.setSelector(new StateListDrawable());


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                User.UserSetCategory setCategory = adapter.getItem(position);
                adapter.onClick(setCategory);
                SetCategoryAdapter.ViewHolder holder = (SetCategoryAdapter.ViewHolder) view.getTag();
                holder.bindData(adapter, setCategory, position);
            }
        });

        header.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                saveCategorySet();


            }
        });
    }



    private void saveCategorySet() {
        final List<User.UserSetCategory> bankSetIds=adapter.getSelectedCategorys();

        final List<Integer> integers=new ArrayList<>();
        for(User.UserSetCategory temp:bankSetIds) {
            integers.add((int) temp.getCategoryId());
        }

        new ThTask<User.UserSetCategoryResponse>(this) {

            User.GetUserSetCategoryResponse getUserSetCategoryResponse;

            @Override
            public User.UserSetCategoryResponse call() throws Exception {
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

                    finish();


                    EventBus.getDefault().post(new SetFlowCompleteEvent());

                }else
                {
                    ToastUtils.show(data.getErrMsg());
                }



            }
        }.execute();


    }

    @Override
    protected User.GetUserSetCategoryResponse readOnBackground() throws CException {



        return apiManager.getUserSetCategory(cacheManager.getCurrentUser().getUserName());
    }

    @Override
    protected void onDataLoaded(User.GetUserSetCategoryResponse data) {


        if(data.getErrCode()== User.GetUserSetCategoryResponse.ErrorCode.ERR_OK) {
            adapter.setDataArray(data.getUserSetCategoryList());
        }else
            ToastUtils.show(data.getErrMsg());
    }





}
