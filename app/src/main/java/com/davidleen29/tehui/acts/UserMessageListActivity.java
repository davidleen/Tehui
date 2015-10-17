package com.davidleen29.tehui.acts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Config;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.davidleen29.tehui.BuildConfig;
import com.davidleen29.tehui.R;
import com.davidleen29.tehui.adapters.UserMessageAdapter;
import com.davidleen29.tehui.api.ApiManager;
import com.davidleen29.tehui.helper.CacheManager;
import com.davidleen29.tehui.lang.CException;
import com.davidleen29.tehui.utils.ToastUtils;
import com.davidleen29.tehui.widget.HeaderView;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Circle;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;


@ContentView(R.layout.activity_user_message_list)
public class UserMessageListActivity extends SimpleActivity<Circle.GetOwnMessagesResponse> {


    public static final String EXTRA_USER_ID = "EXTRA_USER_ID";
    public static final String EXTRA_USER_NAME = "EXTRA_USER_NAME";

    @InjectExtra(EXTRA_USER_ID)
    long userId;

    @InjectExtra(EXTRA_USER_NAME)
    String userName;

    @Inject
    ApiManager apiManager;

    @Inject
    CacheManager cacheManager;


    @Inject
    UserMessageAdapter adapter;

    @InjectView(R.id.list)
    ListView listView;

    @InjectView(R.id.header)
    HeaderView headerView;

    @Override
    protected void init(Bundle bundle) {
        super.init(bundle);

        headerView.setTitle(userName);

        listView.setAdapter(adapter);


//        List<Circle.CircleMessage> messages=new ArrayList<>();
//        if(BuildConfig.DEBUG)
//        {
//            for(int i=0;i<0;i++)
//            {
//                messages.add(  Circle.CircleMessage.newBuilder().build());
//
//
//            }
//        }
//        adapter.setDataArray(messages);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(parent.getContext(), MessageDetailActivity.class);
                intent.putExtra(MessageDetailActivity.EXTRA_MESSAGE_ID, ((Circle.CircleMessage) parent.getItemAtPosition(position)).getMessageId());
                startActivity(intent);
            }
        });

    }

    @Override
    protected Circle.GetOwnMessagesResponse readOnBackground() throws CException {
        return apiManager.getOwnMessages(userId,0,100 );
    }

    @Override
    protected void onDataLoaded(Circle.GetOwnMessagesResponse data) {


        if(data.getErrCode()==Circle.GetOwnMessagesResponse.ErrorCode.ERR_OK)
        {

             adapter.setDataArray(data.getMessageList());

        }else
        {


            ToastUtils.show(data.getErrMsg());
        }
    }
}
