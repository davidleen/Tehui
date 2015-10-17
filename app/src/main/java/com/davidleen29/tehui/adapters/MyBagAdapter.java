package com.davidleen29.tehui.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.davidleen29.tehui.acts.QRActivity;
import com.davidleen29.tehui.R;
import com.davidleen29.tehui.lang.ResId;
import com.davidleen29.tehui.lang.UnMixable;
import com.davidleen29.tehui.utils.TimeUtils;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Common;
import com.huiyou.dp.service.protocol.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 我的卡包适配类
 */
public class MyBagAdapter extends AbstractAdapter<User.CardInfo> {


    public void setIsEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
        notifyDataSetChanged();
    }

    private boolean  isEditMode=false;


    public boolean isEditMode() {
        return isEditMode;
    }

    @Inject
    public MyBagAdapter(Context context) {
        super(context);
    }

    @Override
    protected UnMixable createViewHolder(int itemViewType) {
        return new ViewHolder();
    }




    @ResId(R.layout.list_item_my_bag)
    private class ViewHolder implements  Bindable<User.CardInfo>
    {



        @ResId(R.id.item_header)
        TextView item_header;


        @ResId(R.id.item_header_icon)
        View item_header_icon;

        @ResId(R.id.ll_item_header)
        View ll_item_header;
        @ResId(R.id.item_icon)
        View item_icon;

        @ResId(R.id.txt_coupon_name)
        TextView txt_coupon_name;

        @ResId(R.id.shopName)
        TextView shopName;

        @ResId(R.id.time)
        TextView time;

        @ResId(R.id.qr)
        View qr;





        @Override
        public void bindData(AbstractAdapter<User.CardInfo> adapter, final User.CardInfo data, int position) {



            item_header_icon.setVisibility(isEditMode ? View.VISIBLE : View.GONE);

            boolean isHeadIconSelected=false;
            for(User.CardInfo info:deleteItems)
            {
                if(info.getIsUse().equals(data.getIsUse()))
                {
                    isHeadIconSelected=true;
                    break;
                }
            }

            item_header_icon.setSelected(isHeadIconSelected);
            item_icon.setVisibility(isEditMode ? View.VISIBLE : View.GONE);
            item_icon.setSelected(deleteItems.contains(data));
            item_header.setText(data.getIsUse() == Common.BooleanValue.BooleanValue_TRUE ? "已使用" : "未使用");
            ll_item_header.setVisibility(position == 0 || getItem(position - 1).getIsUse() != data.getIsUse() ? View.VISIBLE : View.GONE);
            txt_coupon_name.setText(data.getMerchantCouponName());
            shopName.setText(data.getMerchantName());
            int expireDayNumber=TimeUtils.getExpireDayNumber(data.getEndDate());
            time.setSelected(expireDayNumber<=3);

            time.setText(expireDayNumber<=0?"已过期":"还有" + expireDayNumber + "天到期");



            ll_item_header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        List<User.CardInfo> relateIds=new ArrayList<>();
                        for(User.CardInfo info:datas)
                        {
                            if(info.getIsUse().equals(data.getIsUse()))
                            relateIds.add(info);
                        }
                        if(item_header_icon.isSelected())
                        {

                            deleteItems.removeAll(relateIds);

                        }else
                        {
                            deleteItems.addAll(relateIds);
                        }

                    notifyDataSetChanged();

                }
            });


            qr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent=new Intent(v.getContext(), QRActivity.class);
                    intent.putExtra(QRActivity.EXTRA_CODE,data.getQrCode());
                    context.startActivity(intent);


                }
            });

        }
    }
    public void setItemClick(User.CardInfo item) {

        if(deleteItems.contains(item))
        {
            deleteItems.remove(item);
        }else
            deleteItems.add(item);
        notifyDataSetChanged();

    }

    private Set<User.CardInfo> deleteItems=new HashSet<>();


    public Set<User.CardInfo> getDeleteItems() {
        return deleteItems;
    }

    public void clearDeleteItems()
    {
        deleteItems.clear();
        notifyDataSetChanged();
    }
}
