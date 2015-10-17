package com.davidleen29.tehui.adapters;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.api.HttpUrl;
import com.davidleen29.tehui.helper.ImageLoaderHelper;
import com.davidleen29.tehui.lang.ResId;
import com.davidleen29.tehui.lang.UnMixable;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Merchant;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 商户列表列表项目
 */


public class ShopInfoAdapter extends AbstractAdapter<Merchant.MerchantInfo> {



    @Inject
    public ShopInfoAdapter(Context context) {
        super(context);
    }

    @Override
    protected UnMixable createViewHolder(int itemViewType) {
        return new ViewHolder();
    }




    @ResId(R.layout.list_item_shop_info)
    private class ViewHolder implements  Bindable<Merchant.MerchantInfo>
    {

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

        private StringBuilder builder=new StringBuilder();
        @Override
        public void bindData(AbstractAdapter<Merchant.MerchantInfo> adapter, Merchant.MerchantInfo data, int position) {


            shopName.setText(data.getName());





            List<Merchant.TehuiShortInfo> tehuiInfos=data.getTehuiShortInfosList();
            builder.setLength(0);

            for(Merchant.TehuiShortInfo info:tehuiInfos)
            {
                builder.append(info.getTehuiShort()+",");
            }

            if(builder.length()>0)
                builder.setLength(builder.length()-1);

            coupons.setText(builder.toString());

           // shopMap.setText(data.getDistance() < 1000 ? ((int) (data.getDistance()) + "m" ): (data.getDistance() < 100000 ? (kmFormat.format(data.getDistance() / 1000f) + "km") : "***km"));


            shopMap.setText(data.getDistance()+data.getDistanceSuffix());

            ImageLoader.getInstance().displayImage(data.getImgUrl(), shopPhoto, ImageLoaderHelper.getMerchantDisplayOption());
            address.setText(data.getAddress());



            if(onMapIconClickListener!=null)
            {
                shopMap.setTag(data);
                shopMap.setOnClickListener(onMapIconClickListener);
            }





        }
    }



    private View.OnClickListener onMapIconClickListener=null;

    public void setOnMapIconClickListener(View.OnClickListener onMapIconClickListener) {
        this.onMapIconClickListener = onMapIconClickListener;
    }

}
