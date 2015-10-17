package com.davidleen29.tehui.adapters;


import android.content.Context;
import android.widget.TextView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.helper.Constraints;
import com.davidleen29.tehui.lang.ResId;
import com.davidleen29.tehui.lang.UnMixable;
import com.davidleen29.tehui.utils.TimeUtils;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Merchant;

/**
 * 商户列表列表项目
 */


public class CouponAdapter extends AbstractAdapter<Merchant.MerchantCouponInfo> {
    @Inject
    public CouponAdapter(Context context) {
        super(context);
    }

    @Override
    protected UnMixable createViewHolder(int itemViewType) {
        return new ViewHolder();
    }




    @ResId(R.layout.list_item_coupon)
    private class ViewHolder implements  Bindable<Merchant.MerchantCouponInfo>
    {

        @ResId(R.id.couponName)
        TextView couponName;
            @ResId(R.id.useCount)
        TextView useCount;
            @ResId(R.id.giveCount)
        TextView giveCount;
            @ResId(R.id.endDate)
        TextView endDate;
            @ResId(R.id.period)
        TextView period;




        StringBuilder text=new StringBuilder();

        @Override
        public void bindData(AbstractAdapter<Merchant.MerchantCouponInfo> adapter, Merchant.MerchantCouponInfo data, int position) {

            couponName.setText(data.getName());
            useCount.setText(String.valueOf(data.getUseCount()));
            giveCount.setText(String.valueOf(data.getGiveCount()));
            endDate.setText(TimeUtils.longToYY_MM_DD(data.getEndDate()));
            text.setLength(0);
            switch (data.getEffectivePeriodType())
            {
                case EffectivePeriodType_WEEK:

                    text.append("每周  ");

                    for(int i:data.getDaysList())
                    {
                        text.append( Constraints.WEEKS[i-1]+",");
                    }

                    break;
                case EffectivePeriodType_MONTH:

                    text.append("每月  ");

                    for(int i:data.getDaysList())
                    {
                        text.append(   i+"号,");
                    }


                    break;
            }

            if(text.length()>0)
            text.setLength(text.length()-1);

            period.setText(text.toString());

        }
    }
}
