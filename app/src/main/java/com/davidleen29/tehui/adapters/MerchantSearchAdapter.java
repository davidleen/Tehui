package com.davidleen29.tehui.adapters;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.lang.ResId;
import com.davidleen29.tehui.lang.UnMixable;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Merchant;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 商户列表列表项目
 */


public class MerchantSearchAdapter extends AbstractAdapter<Merchant.MerchantInfo> {
    @Inject
    public MerchantSearchAdapter(Context context) {
        super(context);
    }

    @Override
    protected UnMixable createViewHolder(int itemViewType) {
        return new ViewHolder();
    }




    @ResId(R.layout.list_item_merchant_search)
    private class ViewHolder implements  Bindable<Merchant.MerchantInfo>
    {


        @ResId(R.id.shopName)
        TextView shopName;

        @ResId(R.id.address)
        TextView address;

        @Override
        public void bindData(AbstractAdapter<Merchant.MerchantInfo> adapter, Merchant.MerchantInfo data, int position) {


            shopName.setText(data.getName());


            address.setText(data.getAddress());


        }
    }
}
