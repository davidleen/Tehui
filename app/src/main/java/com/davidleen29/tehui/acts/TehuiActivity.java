package com.davidleen29.tehui.acts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.helper.CacheManager;
import com.davidleen29.tehui.helper.ImageLoaderHelper;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Common;
import com.huiyou.dp.service.protocol.Merchant;
import com.huiyou.dp.service.protocol.User;
import com.nostra13.universalimageloader.core.ImageLoader;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_tehui)
public class TehuiActivity extends BaseActivity {




    public static  final String EXTRA_TUIHUI_INFO="EXTRA_TUIHUI_INFO";

    public static  final String EXTRA_MERCHANT_INFO="EXTRA_MERCHANT_INFO";
            @InjectView(R.id.iv_bank)
            ImageView iv_bank;

    @InjectView(R.id.txt_bank)
    TextView txt_bank;

    @InjectView(R.id.txt_coupon_name)
    TextView txt_coupon_name;

    @InjectView(R.id.invite)
    TextView invite;

    @Inject
    CacheManager cacheManager;

    @InjectExtra(EXTRA_TUIHUI_INFO)
    Merchant.TehuiShortInfo data;
    @InjectExtra(EXTRA_MERCHANT_INFO)
    Merchant.MerchantInfo  merchantInfo;
    @Override
    protected void init(Bundle bundle) {


        txt_bank.setText(data.getBankName());
        txt_coupon_name.setText(data.getDetail());
//            //TODO  需要银行图片字段。
//            ImageLoader.getInstance().displayImage(data.get);





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




        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Merchant.TehuiShortInfo data = (Merchant.TehuiShortInfo) v.getTag();
                Intent intent = new Intent(v.getContext(), AddMessageActivity.class);

                //检查是否有卡
                boolean hasCard = false;
                if (cacheManager.getConcernBank() != null) {

                    for (User.UserSetBank setBank : cacheManager.getConcernBank().getUserSetBankList()) {
                        if (setBank.getIsFollow() == Common.BooleanValue.BooleanValue_TRUE&&setBank.getBankId() == data.getBankId()) {
                            hasCard = true;
                            break;
                        }
                    }
                }
                //判断是否有卡。

                if (hasCard) {
                    intent.putExtra(AddMessageActivity.EXTRA_MESSAGE_TYPE, Common.CircleMessageType.CircleMessageType_FIND_DATE);
                    intent.putExtra(AddMessageActivity.EXTRA_CONTENT, data.getBankName() + "在手，优惠我有，我们约起");
                } else {
                    intent.putExtra(AddMessageActivity.EXTRA_MESSAGE_TYPE, Common.CircleMessageType.CircleMessageType_FIND_CARD);
                    intent.putExtra(AddMessageActivity.EXTRA_CONTENT, "万能的朋友圈，请赐我一张" + data.getBankName() + "信用卡吧");
                }
                intent.putExtra(AddMessageActivity.EXTRA_TITLE, merchantInfo.getName());
                intent.putExtra(AddMessageActivity.EXTRA_MERCHANT_ID, merchantInfo.getId());
                v.getContext().startActivity(intent);
            }
        });
        invite.setTag(data);
        invite.setSelected(hasCard);
        invite.setText(hasCard ? "朋友圈约起" : "万能朋友圈求卡");


        String bankUrl="";
        for(User.UserSetBank userSetBank:cacheManager.getConcernBank().getUserSetBankList())
        {
            if(userSetBank.getBankId()==data.getBankId())
            {
                bankUrl=userSetBank.getImgSmall();
            }
        }


        ImageLoader.getInstance().displayImage(bankUrl, iv_bank, ImageLoaderHelper.getBankDisplayOptions());




    }
}
