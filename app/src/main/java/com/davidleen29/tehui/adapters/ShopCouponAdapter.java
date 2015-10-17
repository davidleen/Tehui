package com.davidleen29.tehui.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.api.ApiManager;
import com.davidleen29.tehui.helper.CacheManager;
import com.davidleen29.tehui.lang.ResId;
import com.davidleen29.tehui.lang.UnMixable;
import com.davidleen29.tehui.tasks.ThTask;
import com.davidleen29.tehui.utils.ToastUtils;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Merchant;
import com.huiyou.dp.service.protocol.User;

/**
 * Created by davidleen29 on 2015/7/19.
 */
public class ShopCouponAdapter extends AbstractAdapter<Merchant.MerchantCouponInfo> {


    @Inject
    ApiManager apiManager;
    @Inject
    CacheManager cacheManager;
    @Inject
    public ShopCouponAdapter(Context context) {
        super(context);
    }

    @Override
    protected UnMixable createViewHolder(int itemViewType) {
        return new ViewHolder();
    }

    @ResId(R.layout.list_item_shop_coupon)
    private class ViewHolder implements  Bindable<Merchant.MerchantCouponInfo>
    {



        @ResId(R.id.fetch)
        View fetch;

        @ResId(R.id.couponName)
        TextView couponName;


        @ResId(R.id.shopPhoto)
        ImageView shopPhoto;

        @Override
        public void bindData(AbstractAdapter<Merchant.MerchantCouponInfo> adapter, final Merchant.MerchantCouponInfo data, int position) {
              couponName.setText(data.getName());




            fetch.setTag(data);

            fetch.setOnClickListener(onFetchCouponListener);



        }
    }


    private View.OnClickListener onFetchCouponListener;
    public void setOnFetchCouponListener(View.OnClickListener listener)
    {
        this.onFetchCouponListener=listener;
    }
}
