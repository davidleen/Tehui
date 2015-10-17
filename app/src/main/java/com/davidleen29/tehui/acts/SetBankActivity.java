package com.davidleen29.tehui.acts;

import android.content.Intent;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.adapters.SetBankAdapter;
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

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_set_bank)
public class SetBankActivity extends SimpleActivity<User.GetUserSetBankResponse> {



    @Inject
    ApiManager apiManager;

    @InjectView(R.id.gridView)
        GridView gridView;

    @Inject
    CacheManager cacheManager;

    @Inject
    SetBankAdapter adapter;

    @InjectView(R.id.header)
    HeaderView header;

    @Override
    protected void init(Bundle bundle) {



        super.init(bundle);




        gridView.setAdapter(adapter);

        //clear the selector
        gridView.setSelector(new StateListDrawable());


               gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                   @Override
                   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                       User.UserSetBank setBank = adapter.getItem(position);
                       adapter.onClick(setBank);
                       SetBankAdapter.ViewHolder holder = (SetBankAdapter.ViewHolder) view.getTag();
                       holder.bindData(adapter, setBank, position);
                   }
               });

        header.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                saveBanSet();


            }
        });


    }


    @Override
    protected User.GetUserSetBankResponse readOnBackground() throws CException {


        return apiManager.getUserSetBank(cacheManager.getCurrentUser().getUserName());
    }

    @Override
    protected void onDataLoaded(User.GetUserSetBankResponse data) {

        if(data.getErrCode()== User.GetUserSetBankResponse.ErrorCode.ERR_OK)
        {

            adapter.setDataArray(data.getUserSetBankList());
        }else
        {
            ToastUtils.show(data.getErrMsg());
        }

    }


    private  void saveBanSet()
    {


        final List<User.UserSetBank> bankSetIds=adapter.getSelectedBanks();

        final List<Integer> integers=new ArrayList<>();
        for(User.UserSetBank temp:bankSetIds) {
            integers.add((int) temp.getBankId());
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
                    Intent intent=new Intent(SetBankActivity.this,SetCategoryActivity.class);
                    startActivity(intent);
                    finish();


                }else
                {
                    ToastUtils.show(data.getErrMsg());
                }



            }
        }.execute();



    }




    public void onEvent(SetFlowCompleteEvent event)
    {
        finish();
    }



    @Override
    public void onBackPressed() {


        //avoid back
    }
}
