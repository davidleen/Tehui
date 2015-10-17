package com.davidleen29.tehui.acts;

import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.adapters.MyBagAdapter;
import com.davidleen29.tehui.api.ApiManager;
import com.davidleen29.tehui.events.UserInfoUpdateEvent;
import com.davidleen29.tehui.helper.CacheManager;
import com.davidleen29.tehui.lang.CException;
import com.davidleen29.tehui.tasks.ThTask;
import com.davidleen29.tehui.utils.ArrayUtils;
import com.davidleen29.tehui.utils.ToastUtils;
import com.davidleen29.tehui.widget.HeaderView;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


import de.greenrobot.event.EventBus;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;


@ContentView(R.layout.activity_my_bag)
public class MyBagActivity extends SimpleActivity<User.ShowCardsResponse> {



    @InjectView(R.id.listView)
    ListView listView;

    @InjectView(R.id.delete)
    View delete;


    @Inject
    ApiManager apiManager;

    @Inject
    CacheManager cacheManager;

    @InjectView(R.id.header)
    HeaderView header;

    @Inject
    MyBagAdapter adapter;


    @Override
    protected void init(Bundle bundle) {
        super.init(bundle);
        listView.setAdapter(adapter);
        //clear the selector
        listView.setSelector(new StateListDrawable());
        header.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!adapter.isEditMode()) {
                    adapter.setIsEditMode(true);


                } else {


                    adapter.setIsEditMode(false);


                }
                updateByMode();


            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                adapter.setItemClick(adapter.getItem(position));


            }
        });



        delete.setOnClickListener(this);

        updateByMode();
    }


    @Override
    protected void onViewClick(View v, int id) {


        switch (id)
        {
            case R.id.delete:

                deleteCards();
                break;
        }

    }

    private void updateByMode()
    {
        boolean isEditMode=adapter.isEditMode();
        header.setRightText(isEditMode ? "完成" : "编辑");
        delete.setVisibility(isEditMode ? View.VISIBLE : View.GONE);
    }

    @Override
    protected User.ShowCardsResponse readOnBackground() throws CException {
        return apiManager.showCardsRequest(cacheManager.getCurrentUser().getUserId());
    }

    @Override
    protected void onDataLoaded(User.ShowCardsResponse data) {
        if(data.getErrCode()==User.ShowCardsResponse.ErrorCode.ERR_OK)
        {

            List<User.CardInfo> datas=new ArrayList<>();
            datas.addAll(data.getCardInfosList());
            ArrayUtils.SortList(datas, new Comparator<User.CardInfo>() {
                @Override
                public int compare(User.CardInfo lhs, User.CardInfo rhs) {
                    return rhs.getIsUse().compareTo(lhs.getIsUse());
                }
            });
            adapter.setDataArray(datas);
        }else
        {
            ToastUtils.show(data.getErrMsg());
        }


    }


    private void deleteCards()
    {
      final  List<Long > cardIds=new ArrayList<>();
     for(   User.CardInfo cardInfo: adapter.getDeleteItems())
        {
            cardIds.add(cardInfo.getId());
        }

        if(cardIds.size()==0)
        {

           ToastUtils.show("请挑选删除条目");
            return;
        }

        new ThTask<User.DeleteCardsResponse>(this) {
            @Override
            public User.DeleteCardsResponse call() throws Exception {
                return apiManager.deleteCards(cacheManager.getCurrentUser().getUserId(),cardIds);
            }

            @Override
            protected void doOnSuccess(User.DeleteCardsResponse data) {


                if(data.getErrCode()==User.DeleteCardsResponse.ErrorCode.ERR_OK)
                {
                   loadData();
                    EventBus.getDefault().post(new UserInfoUpdateEvent());
                }else
                {



                    switch (data.getErrCode().getNumber())
                    {
                        case User.DeleteCardsResponse.ErrorCode.ERR_USER_NOT_EXSITS_VALUE:
                            ToastUtils.show("用户不存在");
                            break;


                        case User.DeleteCardsResponse.ErrorCode.ERR_UNKNOWN_VALUE:

                            ToastUtils.show("未知错误");
                            break;

                        case User.DeleteCardsResponse.ErrorCode.ERR_REQUEST_INVALID_VALUE:

                            ToastUtils.show("请求异常");
                            break;


                        default:
                            ToastUtils.show(data.getErrMsg());
                    }
                }



            }
        }.execute();



    }
}
