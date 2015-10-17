package com.davidleen29.tehui.acts;



import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.adapters.BankCouponAdapter;
import com.davidleen29.tehui.adapters.ShopCouponAdapter;
import com.davidleen29.tehui.api.ApiManager;
import com.davidleen29.tehui.api.HttpUrl;
import com.davidleen29.tehui.events.UserInfoUpdateEvent;
import com.davidleen29.tehui.frags.BaseDialogFragment;
import com.davidleen29.tehui.helper.CacheManager;
import com.davidleen29.tehui.helper.ImageLoaderHelper;
import com.davidleen29.tehui.lang.CException;
import com.davidleen29.tehui.tasks.ThTask;
import com.davidleen29.tehui.utils.ToastUtils;
import com.davidleen29.tehui.widget.HeaderView;
import com.davidleen29.tehui.widget.LinearListView;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Common;
import com.huiyou.dp.service.protocol.Merchant;
import com.huiyou.dp.service.protocol.User;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_merchant_detail)
public class MerchantDetailActivity extends SimpleActivity<Merchant.UserGetMerchantInfoResponse> {


    public static final String EXTRA_MERCHANTINFO_ID = "EXTRA_MERCHANTINFO_ID";
    @InjectView(R.id.list_bank_coupon)
    LinearListView list_bank_coupon;

    @InjectView(R.id.list_shop_coupon)
    LinearListView list_shop_coupon;
    @InjectView(R.id.shopPhoto)
    ImageView shopPhoto;



    @InjectView(R.id.header)
    HeaderView header;

    @InjectView(R.id.address)
    TextView address;
    @InjectView(R.id.call)
    View call;




    @Inject
    ApiManager apiManager;
    @Inject
    CacheManager cacheManager;

    @Inject
    BankCouponAdapter adapter;
    @Inject
    ShopCouponAdapter shopCouponAdapter;

    @InjectExtra(value = EXTRA_MERCHANTINFO_ID
            ,optional = false)
    long merchantInfo_id;


    Merchant.MerchantInfo merchantInfo;

    IWXAPI weixiApi;

    @Override
    protected void init(Bundle bundle) {


        super.init(bundle);


        String appId=getResources().getString(R.string.weixin_app_id);
        weixiApi = WXAPIFactory.createWXAPI(this,appId,true);
        weixiApi.registerApp(appId);
        list_bank_coupon.setAdapter(adapter);
        list_shop_coupon.setAdapter(shopCouponAdapter);
        call.setOnClickListener(this);



        adapter.setInviteListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                if (cacheManager.getCurrentUser().getUserType().equals(Common.UserType.UserType_TEMP)) {
                    BaseDialogFragment dialogFragment = new BaseDialogFragment() {

                        @Override
                        protected void onYesClick(View view) {
                            dismiss();
                            Intent intent = new Intent(MerchantDetailActivity.this, RegisterActivity.class);
                            startActivity(intent);

                        }

                        @Override
                        protected void onNoClick(View view) {

                            dismiss();
                        }
                    };
                    dialogFragment.setParams(null, "游客不能发送消息，请绑定手机", "好的", "我再看看");

                    dialogFragment.show(getSupportFragmentManager(), dialogFragment.getClass().getName());


                    return;
                }


                Merchant.TehuiShortInfo data = (Merchant.TehuiShortInfo) v.getTag();
                Intent intent = new Intent(v.getContext(), AddMessageActivity.class);

                //检查是否有卡
                boolean hasCard = false;
                if (cacheManager.getConcernBank() != null) {

                    for (User.UserSetBank setBank : cacheManager.getConcernBank().getUserSetBankList()) {
                        if (setBank.getIsFollow() == Common.BooleanValue.BooleanValue_TRUE&&setBank.getBankId() == data.getBankId()){
                            hasCard = true;
                            break;
                        }
                    }
                }
                //判断是否有卡。

                if (hasCard) {
                    intent.putExtra(AddMessageActivity.EXTRA_MESSAGE_TYPE, Common.CircleMessageType.CircleMessageType_FIND_DATE);
                    intent.putExtra(AddMessageActivity.EXTRA_CONTENT, data.getBankName() + "信用卡在手，优惠我有，我们约起");
                } else {
                    intent.putExtra(AddMessageActivity.EXTRA_MESSAGE_TYPE, Common.CircleMessageType.CircleMessageType_FIND_CARD);
                    intent.putExtra(AddMessageActivity.EXTRA_CONTENT, "万能的朋友圈，请赐我一张" + data.getBankName() + "信用卡吧");
                }
                intent.putExtra(AddMessageActivity.EXTRA_TITLE, merchantInfo.getName());
                intent.putExtra(AddMessageActivity.EXTRA_MERCHANT_ID, merchantInfo.getId());
                v.getContext().startActivity(intent);

        }});



        shopCouponAdapter.setOnFetchCouponListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //检测是否游客
                if (cacheManager.getCurrentUser().getUserType().equals(Common.UserType.UserType_TEMP)) {


                    BaseDialogFragment dialogFragment = new BaseDialogFragment() {

                        @Override
                        protected void onYesClick(View view) {
                            dismiss();
                            Intent intent = new Intent(MerchantDetailActivity.this, RegisterActivity.class);
                            startActivity(intent);

                        }

                        @Override
                        protected void onNoClick(View view) {

                            dismiss();
                        }
                    };
                    dialogFragment.setParams(null, "游客不能领券，请绑定手机", "好的", "我再看看");

                    dialogFragment.show(getSupportFragmentManager(), dialogFragment.getClass().getName());


                    return;
                }


                final Merchant.MerchantCouponInfo data = (Merchant.MerchantCouponInfo) v.getTag();


                new ThTask<User.FindCouponResponse>(v.getContext()) {
                    @Override
                    public User.FindCouponResponse call() throws Exception {
                        return apiManager.findCouponRequest(cacheManager.getCurrentUser().getUserId(), data.getId());
                    }

                    @Override
                    protected void doOnSuccess(User.FindCouponResponse data) {


                        if (data.getErrCode() == User.FindCouponResponse.ErrorCode.ERR_OK) {
                            ToastUtils.show("领券成功");
                        } else
                            ToastUtils.show(data.getErrMsg() + ",ERROR_CODE:" + data.getErrCode());

                    }
                }.execute();



        }});


    }

    @Override
    protected Merchant.UserGetMerchantInfoResponse readOnBackground() throws CException {
        return apiManager.userGetMerchantInfo(cacheManager.getCurrentUser().getUserId(), merchantInfo_id);
    }

    @Override
    protected void onDataLoaded(Merchant.UserGetMerchantInfoResponse data) {


        if(data.getErrCode()==Merchant.UserGetMerchantInfoResponse.ErrorCode.ERR_OK)
        {


            this.merchantInfo=data.getMerchantInfo();
            initValue(merchantInfo);

        }else
        {
            switch (data.getErrCode().getNumber())
            {
                case Merchant.UserGetMerchantInfoResponse.ErrorCode.ERR_USER_NOT_EXSITS_VALUE:
                    ToastUtils.show("用户不存在");
                    finish();
                    break;

                case Merchant.UserGetMerchantInfoResponse.ErrorCode.ERR_MERCHANT_NOT_EXSITS_VALUE:
                    ToastUtils.show("商户不存在");
                    finish();
                    break;

                default:
                    ToastUtils.show("未知错误");
                    finish();
            }



        }


    }


    private void initValue(final Merchant.MerchantInfo merchantInfo)
    {


        address.setText( merchantInfo.getAddress());

        ImageLoader.getInstance().displayImage(merchantInfo.getImgUrl(), shopPhoto,ImageLoaderHelper.getMerchantDisplayOption());
        adapter.setDataArray(merchantInfo.getTehuiShortInfosList());


        //过滤数据

        List<Merchant.MerchantCouponInfo> data=new ArrayList<>();


        header.setRight1Selected(merchantInfo.getIsFavorite().equals(Common.BooleanValue.BooleanValue_TRUE));
        for(Merchant.MerchantCouponInfo info:merchantInfo.getMerchantCouponInfosList())
        {
            if(info.getIsUse().equals(Common.BooleanValue.BooleanValue_TRUE))
            {
                data.add(info);

            }
        }


        shopCouponAdapter.setDataArray(data);

        header.setOnRight1ClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!v.isSelected())
                    addFavorite();

            }
        });

        header.setOnRight2ClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check user type

                if (cacheManager.getCurrentUser().getUserType().equals(Common.UserType.UserType_TEMP)) {



                    BaseDialogFragment dialogFragment=   new BaseDialogFragment()
                    {

                        @Override
                        protected void onYesClick(View view) {
                            dismiss();
                            Intent intent =new Intent(MerchantDetailActivity.this,RegisterActivity.class);
                            startActivity(intent);

                        }

                        @Override
                        protected void onNoClick(View view) {

                            dismiss();
                            sendMessageToWX();

                        }
                    };
                    dialogFragment.setParams(null,"建议绑定手机，否则信息会丢失","好的","不用啦");

                    dialogFragment.show(getSupportFragmentManager(),dialogFragment.getClass().getName());




                } else {

                    sendMessageToWX();

                }
            }
        });



        list_bank_coupon.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView parent, View v, int position, long id) {


                Intent intent=new Intent(MerchantDetailActivity.this,TehuiActivity.class);

                intent.putExtra(TehuiActivity.EXTRA_MERCHANT_INFO,merchantInfo);
                intent.putExtra(TehuiActivity.EXTRA_TUIHUI_INFO,adapter.getItem(position));
                startActivity(intent);
            }
        });

    }



    private void sendMessageToWX()
    {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = HttpUrl.WEB_HOME ;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "我在\""+getResources().getString(R.string.app_name)+"\"捡到了大实惠";
        msg.description = "描述";
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        msg.thumbData = ImageLoaderHelper.bitmap2Byte(thumb);  // 设置缩略图
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        weixiApi.sendReq(req);


//
//        ImageLoader.getInstance().loadImage(merchantInfo.getImgUrl(), new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String imageUri, View view) {
//
//            }
//
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//
//            }
//
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//
//
//
//
//
//
//
//                WXImageObject imgObj = new WXImageObject(loadedImage);
//
//
//                WXMediaMessage msg = new WXMediaMessage();
//                msg.mediaObject = imgObj;
//                int THUMB_SIZE = 150;
//                Bitmap thumbBmp = Bitmap.createScaledBitmap(loadedImage, THUMB_SIZE, THUMB_SIZE, true);
//                msg.thumbData = ImageLoaderHelper.bitmap2Byte(thumbBmp);  // 设置缩略图
//                thumbBmp.recycle();
//
//
//                msg.title=merchantInfo.getName();
//                msg.description=merchantInfo.getAddress();
//
//                SendMessageToWX.Req req = new SendMessageToWX.Req();
//                req.transaction = buildTransaction("img");
//                req.message = msg;
//                req.scene = SendMessageToWX.Req.WXSceneTimeline;
//                weixiApi.sendReq(req);
//            }
//
//            @Override
//            public void onLoadingCancelled(String imageUri, View view) {
//
//            }
//        });
//
//
     }


    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }


    private void addFavorite()
    {


        new ThTask<User.AddFavoriteResponse>(this) {
            @Override
            public User.AddFavoriteResponse call() throws Exception {
                return apiManager.addFavorite(cacheManager.getCurrentUser().getUserId(), merchantInfo.getId());
            }

            @Override
            protected void doOnSuccess(User.AddFavoriteResponse data) {


                if(data.getErrCode()==User.AddFavoriteResponse.ErrorCode.ERR_OK)
                {

                    EventBus.getDefault().post(new UserInfoUpdateEvent());

                   header.setRight1Selected(true);
                }else
                {
                    ToastUtils.show(data.getErrMsg());
                }



            }
        }.execute();

    }


    @Override
    protected void onViewClick(View v, int id) {

        switch (id)

        {
            case R.id.call:





                BaseDialogFragment dialogFragment=   new BaseDialogFragment()
                {

                    @Override
                    protected void onYesClick(View view) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);

                        callIntent.setData(Uri.parse("tel:" + merchantInfo.getTel()));
                        startActivity(callIntent);
                        dismiss();

                    }
                };
                dialogFragment.setParams(null,merchantInfo.getTel(),"呼叫");

                dialogFragment.show(getSupportFragmentManager(),dialogFragment.getClass().getName());

                break;
        }
    }


    @Override
    protected void onDestroy() {

        weixiApi.unregisterApp();
        super.onDestroy();
    }
}
