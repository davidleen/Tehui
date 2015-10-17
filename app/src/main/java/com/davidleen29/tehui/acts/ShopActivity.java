package com.davidleen29.tehui.acts;




import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.davidleen29.tehui.R;
import com.davidleen29.tehui.adapters.CouponAdapter;
import com.davidleen29.tehui.api.ApiManager;
import com.davidleen29.tehui.events.CitySetEvent;
import com.davidleen29.tehui.frags.BaseDialogFragment;
import com.davidleen29.tehui.helper.CacheManager;
import com.davidleen29.tehui.helper.PixelHelper;
import com.davidleen29.tehui.lang.CException;
import com.davidleen29.tehui.tasks.ThTask;
import com.davidleen29.tehui.utils.ToastUtils;
import com.davidleen29.tehui.widget.HeaderView;
import com.google.inject.Inject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.huiyou.dp.service.protocol.City;
import com.huiyou.dp.service.protocol.Common;
import com.huiyou.dp.service.protocol.Merchant;
import com.huiyou.dp.service.protocol.User;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;


import de.greenrobot.event.EventBus;
import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;


/**
 * 我是商家
 */
@ContentView(R.layout.activity_shop)
public class ShopActivity extends SimpleActivity<Merchant.GetMerchantInfoResponse> {


    private static final int REQUEST_ADD_COUPON = 1001;
    public static final String EXTRA_MERCHANTINFO_ID = "EXTRA_MERCHANTINFO_ID";
    @InjectView(R.id.list)
    SwipeMenuListView listView;



    @InjectView(R.id.header)
    HeaderView headerView;


    @InjectView(R.id.shopLogo)
    ImageView shopLogo;
  @InjectView(R.id.shopName)
  TextView shopName;

    @InjectView(R.id.shopPhone)
    TextView shopPhone;

    @InjectView(R.id.addCoupon)
    View addCoupon;


    @InjectView(R.id.shellOff)
    View shellOff;



    @Inject
    CacheManager cacheManager;
    @Inject
    ApiManager apiManager;
    @Inject
    CouponAdapter adapter;



    @InjectExtra(EXTRA_MERCHANTINFO_ID)
    public long merchantId;




    private Merchant.MerchantInfo merchantInfo;

     @Override
      protected void init(Bundle bundle) {



         headerView.setOnRightTextClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {


                 IntentIntegrator integrator = new IntentIntegrator(ShopActivity.this);
//                 integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
//                 integrator.setPrompt("Scan a barcode");
                 integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                 integrator.setPrompt("消费者从[我的]-[我的卡包]中打开二维码放入框内，即可自动扫描");


                 integrator.setCaptureActivity(QrCaptureActivityActivity.class);
                 integrator.setCameraId(0);  // Use a specific camera of the device
                 integrator.setBeepEnabled(false);
                 integrator.initiateScan();

             }
         });



         //add footView transparent
         ImageView imageView=new ImageView(this);
         imageView.setImageResource(R.mipmap.icon_add_green);
         imageView.setPadding(0, 0, 0, PixelHelper.dp2px(20));
         imageView.setVisibility(View.INVISIBLE);
         listView.addFooterView(imageView);
         listView.setAdapter(adapter);

         addCoupon.setOnClickListener(this);
         shellOff.setOnClickListener(this);




         shopPhone.setOnClickListener(this);

         configSwipeDeleteMenu();

         loadData();




    }

    private void configSwipeDeleteMenu() {



        //配置横向滑动菜单

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
//                 SwipeMenuItem openItem = new SwipeMenuItem(
//                         getApplicationContext());
//                 // set item background
//                 openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
//                         0xCE)));
//                 // set item width
//                 openItem.setWidth(dp2px(90));
//                 // set item title
//                 openItem.setTitle("Open");
//                 // set item title fontsize
//                 openItem.setTitleSize(18);
//                 // set item title font color
//                 openItem.setTitleColor(Color.WHITE);
//                 // add to menu
//                 menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                deleteItem.setTitle("下架");
                deleteItem.setTitleColor(Color.WHITE);
                deleteItem.setTitleSize(18);

                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(PixelHelper.dp2px(80));
                // set a icon
              //  deleteItem.setIcon(R.drawable.ic_delete);


                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
// set creator
        listView.setMenuCreator(creator);


        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:


                        deleteCouponConfirm((Merchant.MerchantCouponInfo) listView.getItemAtPosition(position));


                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });


    }

    private void deleteCouponConfirm(final Merchant.MerchantCouponInfo merchantCouponInfo) {




        BaseDialogFragment dialogFragment=   new BaseDialogFragment()
        {

            @Override
            protected void onYesClick(View view) {
                dismiss();
                deleteCoupon(merchantCouponInfo);

            }

        };
        dialogFragment.setParams("下架确认", "确定下架该优惠券？", "是" );

        dialogFragment.show(getSupportFragmentManager(), dialogFragment.getClass().getName());





    }

    private void deleteCoupon(final Merchant.MerchantCouponInfo merchantCouponInfo)
    {

        // delete


        new   ThTask<User.CouponOffOrUpShelvesResponse>(this) {
            @Override
            protected void doOnSuccess(User.CouponOffOrUpShelvesResponse data) {

                if(data.getErrCode()==User.CouponOffOrUpShelvesResponse.ErrorCode.ERR_OK)
                {

                    adapter.removeItem(merchantCouponInfo);
                    adapter.notifyDataSetChanged();

                }else
                {
                    switch (data.getErrCode().getNumber())
                    {
                        case User.CouponOffOrUpShelvesResponse.ErrorCode.ERR_USER_NOT_EXSITS_VALUE:
                            ToastUtils.show("用户不存在");
                            break;




                        case User.CouponOffOrUpShelvesResponse.ErrorCode.ERR_PASS_STATUS_PASSING_VALUE:

                            ToastUtils.show("该商户认领正在审核中");
                            break;


                        default:
                            ToastUtils.show(data.getErrMsg());
                    }




                }

            }

            @Override
            public User.CouponOffOrUpShelvesResponse call() throws Exception {
                return apiManager.couponOffOrUpShelves(cacheManager.getCurrentUser().getUserId(), merchantCouponInfo.getId(), Common.BooleanValue.BooleanValue_FALSE);
            }


        }.execute();

    }

    @Override
    protected Merchant.GetMerchantInfoResponse readOnBackground() throws CException {
        return apiManager.getMerchantInfo(cacheManager.getCurrentUser().getUserId());
    }

    @Override
    protected void onDataLoaded(Merchant.GetMerchantInfoResponse data) {


        if(data.getErrCode()==Merchant.GetMerchantInfoResponse.ErrorCode.ERR_OK)
        {

            initValue(data.getMerchantInfo());


        }else
        {
            ToastUtils.show(data.getErrMsg());
        }


    }

    private void initValue(Merchant.MerchantInfo merchantInfo) {


        this.merchantInfo=merchantInfo;
        ImageLoader.getInstance().displayImage(merchantInfo.getImgUrl(), shopLogo);
        shopName.setText(merchantInfo.getName());
        shopPhone.setText(merchantInfo.getTel());
        //过滤数据

        List<Merchant.MerchantCouponInfo> data=new ArrayList<>();
        for(Merchant.MerchantCouponInfo info:merchantInfo.getMerchantCouponInfosList())
        {
            if(info.getIsUse()== Common.BooleanValue.BooleanValue_TRUE)
            {
                data.add(info);
            }
        }


        adapter.setDataArray(data);






    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (result != null) {
            String contents = result.getContents();
            if (contents != null) {
                userCard(contents);
            } else {
                ToastUtils.show("未扫描到任何信息");
            }
        }

        if(resultCode!=RESULT_OK) return ;


        switch (requestCode)
        {
            case REQUEST_ADD_COUPON:

                loadData();
                break;

        }

    }


    /**
     * 商家处理扫描
     * @param qr_code
     */
    private void userCard(final String qr_code)
    {



        new   ThTask<User.UseCardResponse>(this) {

            @Inject
            ApiManager apiManager;




            @Override
            protected void doOnSuccess(User.UseCardResponse data) {

                if(data.getErrCode()==User.UseCardResponse.ErrorCode.ERR_OK)
                {


                    ToastUtils.show("消费成功");

                    loadData();


                }else
                {

                    switch (data.getErrCode().getNumber())
                    {

                        case User.UseCardResponse.ErrorCode.ERR_CARD_EXPIRE_VALUE:

                            ToastUtils.show("该优惠券已经超期，不能使用。");

                            break;

                        case User.UseCardResponse.ErrorCode.ERR_CARD_IS_USED_VALUE:

                        ToastUtils.show("该优惠券已经使用过了。");

                        break;

                        case User.UseCardResponse.ErrorCode.ERR_CARD_NOT_EXSITS_VALUE:

                            ToastUtils.show("该优惠券不存在，不能使用。请核对商家");

                        break;




                        default:   ToastUtils.show(data.getErrCode().name()+data.getErrMsg());
                    }


                }

            }

            @Override
            public User.UseCardResponse call() throws Exception {
                return apiManager.useCard(cacheManager.getCurrentUser().getUserId(),qr_code);
            }


        }.execute();


    }

    @Override
    protected void onViewClick(View v, int id) {
        switch (id)
        {
            case R.id.addCoupon: {
                Intent intent = new Intent(this, AddCouponActivity.class);
                intent.putExtra(AddCouponActivity.EXTRA_MERCHANT_ID, merchantInfo == null ? -1 : merchantInfo.getId());
                startActivityForResult(intent, REQUEST_ADD_COUPON);
                break;
            }
            case R.id.shellOff:

            {
                Intent intent = new Intent(this, CouponOffShellActivity.class);
                ArrayList<Merchant.MerchantCouponInfo> data=new ArrayList<>();
                for(Merchant.MerchantCouponInfo info:merchantInfo.getMerchantCouponInfosList())
                {
                    if(info.getIsUse()== Common.BooleanValue.BooleanValue_FALSE)
                    {
                        data.add(info);
                    }
                }
                intent.putExtra(CouponOffShellActivity.EXTRA_DATA,data);
                startActivityForResult(intent, -1);
            }
                break;

            case R.id.shopPhone:

            {


                BaseDialogFragment dialogFragment=   new BaseDialogFragment()
                {

                    @Override
                    protected void onYesClick(View view) {
                        dismiss();
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + merchantInfo.getTel()));
                        startActivity(callIntent);

                    }

                };
                dialogFragment.setParams(null, merchantInfo.getTel(), "呼叫" );

                dialogFragment.show(getSupportFragmentManager(), dialogFragment.getClass().getName());


            }
            break;
        }
    }


}
