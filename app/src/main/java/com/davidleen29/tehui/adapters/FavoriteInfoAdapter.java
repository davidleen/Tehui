package com.davidleen29.tehui.adapters;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.api.HttpUrl;
import com.davidleen29.tehui.helper.AMapHelper;
import com.davidleen29.tehui.helper.CacheManager;
import com.davidleen29.tehui.helper.LocationHelper;
import com.davidleen29.tehui.lang.ResId;
import com.davidleen29.tehui.lang.UnMixable;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Common;
import com.huiyou.dp.service.protocol.Merchant;
import com.huiyou.dp.service.protocol.User;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 商户列表列表项目
 */


public class FavoriteInfoAdapter extends AbstractAdapter<User.FavoriteInfo> {



    boolean isEditable=false;
    @Inject
    public FavoriteInfoAdapter(Context context) {
        super(context);
    }

    @Override
    protected UnMixable createViewHolder(int itemViewType) {
        return new ViewHolder();
    }


    public boolean isEditable() {
        return isEditable;
    }

    public void setIsEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }



    private Set<User.FavoriteInfo> deleteItems=new HashSet<>();

    public Set<User.FavoriteInfo> getDeleteItems() {
        return deleteItems;
    }

    @Inject
    CacheManager cacheManager;

    public void clearDeleteItems()
    {
        deleteItems.clear();
        notifyDataSetChanged();
    }

    @ResId(R.layout.list_item_favorite_info)
    private class ViewHolder implements  Bindable<User.FavoriteInfo>
    {

        @ResId(R.id.ll_item_header)
        View ll_item_header;


        @ResId(R.id.header_icon)
        View header_icon;
        @ResId(R.id.item_header)
        TextView item_header;

        @ResId(R.id.item_icon)
        View item_icon;


        @ResId(R.id.shopPhoto)
        ImageView shopPhoto;
        @ResId(R.id.shopName)
        TextView shopName;

        @ResId(R.id.coupons)
        TextView coupons;
        @ResId(R.id.shopMap)
        TextView shopMap;

        @ResId(R.id.address)
        TextView address;



        @Override
        public void bindData(AbstractAdapter<User.FavoriteInfo> adapter, final User.FavoriteInfo data, int position) {


            Merchant.MerchantInfo merchantInfo=data.getMerchantInfo();
            header_icon.setVisibility(isEditable?View.VISIBLE:View.GONE);


            boolean isHeadIconSelected=false;
            for(User.FavoriteInfo info:deleteItems)
            {
                if(info.equals(data))
                {
                    isHeadIconSelected=true;
                    break;
                }
            }
            header_icon.setSelected(isHeadIconSelected);

            item_icon.setVisibility(isEditable ? View.VISIBLE : View.GONE);
            item_icon.setSelected(deleteItems.contains(data));

            switch (data.getFavoriteTimeInterval().getNumber())
            {
                case Common.FavoriteTimeInterval.FavoriteTimeInterval_ZERO_TO_ONE_MONTH_VALUE:

                    item_header.setText("一个月内收藏");
                    break;

                case Common.FavoriteTimeInterval.FavoriteTimeInterval_ONE_TO_THREE_MONTH_VALUE:
                    item_header.setText("三个月内收藏");

                    break;

                case Common.FavoriteTimeInterval.FavoriteTimeInterval_OVERFLOW_THREE_MONTH_VALUE:
                    item_header.setText("超过3个月");

                    break;

                default:

                    item_header.setText("全部");


            }


            ll_item_header.setVisibility(position == 0 || (!getItem(position - 1).getFavoriteTimeInterval().equals(data.getFavoriteTimeInterval())) ? View.VISIBLE : View.GONE);

            ll_item_header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<User.FavoriteInfo> relateIds = new ArrayList<>();
                    for (User.FavoriteInfo info : datas) {
                        if (info.getFavoriteTimeInterval().equals(data.getFavoriteTimeInterval()))

                            relateIds.add(info);
                    }
                    if (header_icon.isSelected()) {
                        deleteItems.removeAll(relateIds);
                    } else {
                        deleteItems.addAll(relateIds);
                    }

                    notifyDataSetChanged();

                }
            });

            ImageLoader.getInstance().displayImage(data.getMerchantInfo().getImgUrl(), shopPhoto) ;


            shopMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            StringBuilder couponsValue=new StringBuilder();
            for(Merchant.TehuiShortInfo tehuiInfo:data.getMerchantInfo().getTehuiShortInfosList())
            {
                couponsValue.append(tehuiInfo.getTehuiShort()).append(",");
            }

            if(couponsValue.length()>1) couponsValue.setLength(couponsValue.length()-1);
            coupons.setText(couponsValue.toString());
            address.setText(data.getMerchantInfo().getAddress());
            shopName.setText(data.getMerchantInfo().getName());

            double x=0,y=0;
            if(cacheManager.getLocationInfo()!=null)
            {
                x=cacheManager.getLocationInfo().x;
                y=cacheManager.getLocationInfo().y;
            }

            int distance= (int) LocationHelper.GetDistance(x, y, merchantInfo.getX(), merchantInfo.getY());
            shopMap.setText( LocationHelper.formatDistance(distance));


        }
    }

    public void setItemClick(User.FavoriteInfo item) {

        if(deleteItems.contains(item))
        {
            deleteItems.remove(item);
        }else
            deleteItems.add(item);
        notifyDataSetChanged();

    }
}
