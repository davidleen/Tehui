package com.davidleen29.tehui.acts;

import android.content.Intent;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.adapters.FavoriteInfoAdapter;
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

@ContentView(R.layout.activity_favor)
public class MyFavorActivity extends SimpleActivity<User.GetFavoritesResponse> {


    @InjectView(R.id.header)
    HeaderView header;


    @InjectView(R.id.list)
    ListView list;
    @InjectView(R.id.delete)
    View delete;


    @Inject
    CacheManager cacheManager;

    @Inject
    FavoriteInfoAdapter favoriteInfoAdapter;



    @Inject
    ApiManager apiManager;
    @Override
    protected void init(Bundle bundle) {
        super.init(bundle);
        list.setAdapter(favoriteInfoAdapter);
        list.setSelector(new StateListDrawable());
        header.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                favoriteInfoAdapter.setIsEditable(!favoriteInfoAdapter.isEditable());
                favoriteInfoAdapter.notifyDataSetChanged();

                updateView();


            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if(favoriteInfoAdapter.isEditable()) {

                    favoriteInfoAdapter.setItemClick(favoriteInfoAdapter.getItem(position));
                }else
                {

                    Intent intent=new Intent(MyFavorActivity.this,MerchantDetailActivity.class);
                    intent.putExtra(MerchantDetailActivity.EXTRA_MERCHANTINFO_ID,favoriteInfoAdapter.getItem(position).getMerchantInfo().getId());
                    startActivity(intent);
                }
            }
        });
        delete.setOnClickListener(this);
    }


    @Override
    protected void onViewClick(View v, int id) {


        switch (id)
        {
            case R.id.delete:

                deleteFavorite();

                break;
        }

    }

    private void updateView() {


        header.setRightText(favoriteInfoAdapter.isEditable()?"完成":"编辑");
        delete.setVisibility(favoriteInfoAdapter.isEditable()?View.VISIBLE:View.GONE);


    }

    @Override
    protected User.GetFavoritesResponse readOnBackground() throws CException {
        return apiManager.getFavorites();
    }

    @Override
    protected void onDataLoaded(User.GetFavoritesResponse data) {


        if(data.getErrCode()== User.GetFavoritesResponse.ErrorCode.ERR_OK)
        {





            List<User.FavoriteInfo> datas=new ArrayList<>();
            datas.addAll(data.getFavoriteInfosList());
            ArrayUtils.SortList(datas, new Comparator<User.FavoriteInfo>() {
                @Override
                public int compare(User.FavoriteInfo lhs, User.FavoriteInfo rhs) {
                    return rhs.getFavoriteTimeInterval().compareTo(lhs.getFavoriteTimeInterval());
                }
            });



            favoriteInfoAdapter.setDataArray(datas);

        }else
        {
            ToastUtils.show(data.getErrMsg());
        }





    }


    /**
     * 删除收藏
     */
    private void deleteFavorite()
    {


        final List<Long > merchantIds=new ArrayList<>();
        for(User.FavoriteInfo favoriteInfo:favoriteInfoAdapter.getDeleteItems())
        {
            merchantIds.add(favoriteInfo.getMerchantInfo().getId());
        }

        if(merchantIds.size()==0)
        {
            ToastUtils.show("请选择要删除的收藏");
            return;
        }




        new ThTask<User.DeleteFavoriteResponse>(this) {
            @Override
            public User.DeleteFavoriteResponse call() throws Exception {
                return apiManager.deleteFavorite(cacheManager.getCurrentUser().getUserId(), merchantIds);
            }

            @Override
            protected void doOnSuccess(User.DeleteFavoriteResponse data) {


                if(data.getErrCode()==User.DeleteFavoriteResponse.ErrorCode.ERR_OK)
                {
                    loadData();
                    EventBus.getDefault().post(new UserInfoUpdateEvent());
                }else
                {



                    switch (data.getErrCode().getNumber())
                    {
                        case User.DeleteFavoriteResponse.ErrorCode.ERR_USER_NOT_EXSITS_VALUE:
                            ToastUtils.show("用户不存在");
                            break;


                        case User.DeleteFavoriteResponse.ErrorCode.ERR_UNKNOWN_VALUE:

                            ToastUtils.show("未知错误");
                            break;

                        case User.DeleteFavoriteResponse.ErrorCode.ERR_REQUEST_INVALID_VALUE:

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
