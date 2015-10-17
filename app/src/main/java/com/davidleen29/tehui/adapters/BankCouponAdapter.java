package com.davidleen29.tehui.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.helper.CacheManager;
import com.davidleen29.tehui.helper.ImageLoaderHelper;
import com.davidleen29.tehui.lang.ResId;
import com.davidleen29.tehui.lang.UnMixable;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Common;
import com.huiyou.dp.service.protocol.Merchant;
import com.huiyou.dp.service.protocol.User;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by davidleen29 on 2015/7/19.
 */
public class BankCouponAdapter extends AbstractAdapter<Merchant.TehuiShortInfo> {


    @Inject
    CacheManager cacheManager;
    @Inject
    public BankCouponAdapter(Context context) {
        super(context);
    }

    @Override
    protected UnMixable createViewHolder(int itemViewType) {
        return new ViewHolder();
    }

    @ResId(R.layout.list_item_bank_coupon)
    private class ViewHolder implements  Bindable<Merchant.TehuiShortInfo>
    {


        @ResId(R.id.txt_bank)
        TextView txt_bank;

        @ResId(R.id.txt_coupon_name)
        TextView txt_coupon_name;

        @ResId(R.id.iv_bank)
        ImageView iv_bank;
        @ResId(R.id.invite)
        TextView invite;
        @Override
        public void bindData(AbstractAdapter<Merchant.TehuiShortInfo> adapter, final Merchant.TehuiShortInfo data, int position) {



            txt_bank.setText(data.getBankName());
            txt_coupon_name.setText(data.getTehuiShort());
//            //TODO  需要银行图片字段。
//            ImageLoader.getInstance().displayImage(data.get);


            String bankUrl="";
            for(User.UserSetBank userSetBank:cacheManager.getConcernBank().getUserSetBankList())
            {
                if(userSetBank.getBankId()==data.getBankId())
                {
                    bankUrl=userSetBank.getImgSmall();
                }
            }


            ImageLoader.getInstance().displayImage(bankUrl,iv_bank, ImageLoaderHelper.getBankDisplayOptions());


            //检查是否有卡
            boolean hasCard=false;
            if(cacheManager.getConcernBank()!=null)
            {

                for(User.UserSetBank setBank:cacheManager.getConcernBank().getUserSetBankList())
                {
                    if(setBank.getIsFollow() == Common.BooleanValue.BooleanValue_TRUE&&setBank.getBankId()==data.getBankId())
                    {
                        hasCard=true;
                        break;
                    }
                }
            }




                invite.setOnClickListener(inviteListener);
            invite.setTag(data);
            invite.setSelected(hasCard);
            invite.setText(hasCard?"朋友圈约起":"万能朋友圈求卡");
        }
    }


    public void setInviteListener(View.OnClickListener onClickListener)
    {

        this.inviteListener=onClickListener;

    }

    private View.OnClickListener inviteListener;
}
