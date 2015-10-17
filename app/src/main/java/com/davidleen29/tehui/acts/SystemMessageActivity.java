package com.davidleen29.tehui.acts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.adapters.NewMessageAdapter;
import com.davidleen29.tehui.adapters.SystemMessageAdapter;
import com.davidleen29.tehui.api.ApiManager;
import com.davidleen29.tehui.events.SystemMessageReadEvent;
import com.davidleen29.tehui.events.UserInfoUpdateEvent;
import com.davidleen29.tehui.helper.CacheManager;
import com.davidleen29.tehui.helper.FootViewHelper;
import com.davidleen29.tehui.tasks.ThTask;
import com.davidleen29.tehui.utils.ToastUtils;
import com.davidleen29.tehui.widget.HeaderView;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Circle;
import com.huiyou.dp.service.protocol.Common;

import java.util.List;

import de.greenrobot.event.EventBus;
import roboguice.RoboGuice;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;


/**
 * 我的新消息act
 */
@ContentView(R.layout.activity_system_message)
public class SystemMessageActivity extends BaseActivity  {



    @InjectView(R.id.listView)
    ListView listView;



    @Inject
    ApiManager apiManager;

    @Inject
    CacheManager cacheManager;

    @InjectView(R.id.header)
    HeaderView header;

    @Inject
    SystemMessageAdapter adapter;


    FootViewHelper footViewHelper;

    @Override
    protected void init(Bundle bundle) {
        super.init(bundle);




        footViewHelper=new FootViewHelper(this, new FootViewHelper.PageListener() {
            @Override
            public void onPageChanged(int newPageIndex, int pageCount) {

                loadData( newPageIndex,pageCount);
            }
        });

        listView.addFooterView(footViewHelper.getView());
        listView.setAdapter(adapter);
        footViewHelper.setInfo(0,0,0);


      //  listView.setSelector(new StateListDrawable());
//        header.setOnRightTextClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                clearNewMessage();
//
//
//            }
//        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//            //   jump to detail mesageList;
//
//                Intent intent=new Intent(SystemMessageActivity.this,MessageDetailActivity.class);
//                intent.putExtra(MessageDetailActivity.EXTRA_MESSAGE_ID, ((Circle.CircleReply) parent.getItemAtPosition(position)).getMessageId());
//                startActivity(intent);


            }
        });



        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


                if (firstVisibleItem + visibleItemCount >= totalItemCount)

                    footViewHelper.becomeVisible();
            }
        });



        loadData(0, 10);

    }

    private void loadData(final int newPageIndex, final int pageCount) {



        new ThTask<Circle.UserGetSystemMessagesResponse>(this) {
            @Override
            public Circle.UserGetSystemMessagesResponse call() throws Exception {
                return apiManager.userGetSystemMessages(cacheManager.getCurrentUser().getUserId(), newPageIndex, pageCount);
            }

            @Override
            protected void doOnSuccess(Circle.UserGetSystemMessagesResponse data) {


                if(data.getErrCode()==Circle.UserGetSystemMessagesResponse.ErrorCode.ERR_OK)
                {



                    footViewHelper.setInfo(newPageIndex, pageCount, data.getTotalCount());
                    List<Circle.SystemMessage> result=data.getSystemMessagesList();
                    boolean hasNewMessage=false
                            ;
                    for(Circle.SystemMessage message:result)
                    {
                        if(message.getIsRead().equals(Common.BooleanValue.BooleanValue_FALSE))
                        {
                            hasNewMessage=true;
                            break;
                        }
                    }

                    if(newPageIndex==0)
                        adapter.setDataArray(result);
                    else
                        adapter.addDataArray(result);

                    if(hasNewMessage)
                    {
                        EventBus.getDefault().post(new SystemMessageReadEvent());
                    }

                }else
                {

//                    switch (data.getErrCode().getNumber())
//                    {
//
//                        case Circle.GetNewRepliesResponse.ErrorCode.
//                    }
                    ToastUtils.show(data.getErrCode()+"=="+data.getErrMsg());
                }

            }
        }.execute();

    }





}
