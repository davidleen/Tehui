package com.davidleen29.tehui.acts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.api.ApiManager;
import com.davidleen29.tehui.helper.CacheManager;
import com.davidleen29.tehui.helper.ImageLoaderHelper;
import com.davidleen29.tehui.helper.PictureHelper;
import com.davidleen29.tehui.tasks.ThTask;
import com.davidleen29.tehui.utils.StringUtils;
import com.davidleen29.tehui.utils.ToastUtils;
import com.davidleen29.tehui.widget.HeaderView;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Merchant;
import com.huiyou.dp.service.protocol.User;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

/**
 * 商家申请
 */
@ContentView(R.layout.activity_shop_apply)
public class ShopApplyActivity extends BaseActivity {


    private static final int REQUEST_MERCHANT_SEARCH = 1101;
    public static final String EXTRA_MERCHANT_INFO = "EXTRA_MERCHANT_INFO";


    @InjectView(R.id.header)
    HeaderView header;
    @InjectView(R.id.ll_shop_card)
    View ll_shop_card;

    @InjectView(R.id.ll_id_card)
    View ll_id_card;
    @InjectView(R.id.ll_relate_shop)
    View ll_relate_shop;


    @InjectView(R.id.relate_shop)
    TextView relate_shop;
    @InjectView(R.id.shop_card)
    ImageView shop_card;
    @InjectView(R.id.id_card)
    ImageView id_card;

    @InjectView(R.id.shop_name)
    TextView shop_name;
    @InjectView(R.id.shop_phone)
    TextView shop_phone;
    @InjectView(R.id.shop_address)
    TextView shop_address;

    @InjectView(R.id.shop_memo)
    TextView shop_memo;
    @InjectView(R.id.edt_pos)
    EditText edt_pos;

    @Inject
    ApiManager apiManager;
    @Inject
    CacheManager cacheManager;
    private View lastRequestPhotoView;

    String id_card_url;
    String shop_card_url;





    @InjectExtra(value = EXTRA_MERCHANT_INFO,optional = true)
    private User.IdentifyMerchantInfo identifyMerchantInfo;


    private Merchant.MerchantInfo newMerchantInfo;

    @Override
    protected void init(Bundle bundle) {

        ll_relate_shop.setOnClickListener(this);
        header.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                uploadIdentifyMerchant();



            }
        });


        ll_id_card.setOnClickListener(this);
        ll_shop_card.setOnClickListener(this);
        if(identifyMerchantInfo !=null)
        {
            initValue(identifyMerchantInfo);
        }

    }

    @Override
    protected void onViewClick(View v, int id) {

        switch (id) {

            case R.id.ll_relate_shop: {


                if(identifyMerchantInfo ==null) {

                    Intent intent = new Intent(this, MerchantSearchActivity.class);
                    startActivityForResult(intent, REQUEST_MERCHANT_SEARCH);
                }


            }
            break;
            case R.id.ll_id_card:{

                lastRequestPhotoView=ll_id_card;
                unregisterForContextMenu(ll_shop_card);
                registerForContextMenu(ll_id_card);
                openContextMenu(ll_id_card);


            }break;


            case R.id.ll_shop_card:{
                lastRequestPhotoView=ll_shop_card;
                unregisterForContextMenu(ll_id_card);
                registerForContextMenu(ll_shop_card);
                openContextMenu(ll_shop_card);
            }break;

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
            case REQUEST_MERCHANT_SEARCH:

                Merchant.MerchantInfo merchantInfo = (Merchant.MerchantInfo) data.getSerializableExtra(MerchantSearchActivity.EXTRA_MERCHANT_INFO);
                identifyMerchant(merchantInfo);


                break;
        }



        PictureHelper.PickData pickData=   PictureHelper.onResult(this, requestCode, resultCode, data);
        if(pickData!=null)
        {
           String url=Uri.fromFile(new File(pickData.localPath)).toString();

            if(lastRequestPhotoView==ll_id_card)
            {
                id_card_url=url;
                ImageLoader.getInstance().displayImage(id_card_url,id_card,ImageLoaderHelper.getLocalDisplayOptions());

            }else
            {
                shop_card_url=url;
                ImageLoader.getInstance().displayImage(shop_card_url,shop_card, ImageLoaderHelper.getLocalDisplayOptions());
            }


        }


    }


    /**
     * 验证选中商家
     * @param merchantInfo
     */
    private void identifyMerchant(final Merchant.MerchantInfo merchantInfo) {


        new   ThTask<User.IdentifyMerchantResponse>(this) {
            @Override
            protected void doOnSuccess(User.IdentifyMerchantResponse data) {

                if(data.getErrCode()==User.IdentifyMerchantResponse.ErrorCode.ERR_OK)
                {

                    initValue(merchantInfo);

                }else
                {
                    switch (data.getErrCode().getNumber())
                    {
                        case User.IdentifyMerchantResponse.ErrorCode.ERR_USER_NOT_EXSITS_VALUE:
                            ToastUtils.show("用户不存在");
                            break;


                        case User.IdentifyMerchantResponse.ErrorCode.ERR_MERCHANT_NOT_EXSITS_VALUE:

                            ToastUtils.show("商户不存在");
                            break;

                        case User.IdentifyMerchantResponse.ErrorCode.ERR_PASS_STATUS_PASSING_VALUE:

                            ToastUtils.show("该认领正在审核中");
                            break;

                        case User.IdentifyMerchantResponse.ErrorCode.ERR_MERCHANT_IDENTIFIED_VALUE:

                            ToastUtils.show("商户已被认领");
                            break;

                        case User.IdentifyMerchantResponse.ErrorCode.ERR_USER_IDENTIFIED_VALUE:

                            ToastUtils.show("用户已认领过商家");
                            break;
                        default:
                            ToastUtils.show(data.getErrMsg());
                    }




                }

            }

            @Override
            public User.IdentifyMerchantResponse call() throws Exception {
                return apiManager.identifyMerchant(cacheManager.getCurrentUser().getUserId(), merchantInfo.getId());
            }


        }.execute();





    }



    private void uploadIdentifyMerchant()
    {

            if(identifyMerchantInfo ==null&&newMerchantInfo==null)
            {
                ToastUtils.show("请先关联商家");
                return;
            }

            if(StringUtils.isEmpty(shop_card_url))
            {
                ToastUtils.show("请先拍摄营业执照");
                return;
            }
            if(StringUtils.isEmpty(id_card_url))
            {
                ToastUtils.show("请先拍摄身份证正面");
                return;
            }

        final String posCode=edt_pos.getText().toString().trim();
        if(StringUtils.isEmpty(posCode)||posCode.length()!=15)
        {
            ToastUtils.show("请输入15位POS机编码");
            edt_pos.requestFocus();
            return;
        }



            Bitmap bitmap = ImageLoader.getInstance().loadImageSync(id_card_url, new ImageSize(1920, 1080), ImageLoaderHelper.getLocalDisplayOptions());

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);

      final   byte[] id_card_data = bos.toByteArray();

            bitmap = ImageLoader.getInstance().loadImageSync(shop_card_url, new ImageSize(1920, 1080), ImageLoaderHelper.getLocalDisplayOptions());
            bos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        final      byte[]     shop_card_data = bos.toByteArray();
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        new   ThTask<User.UploadIdentifyMerchantResponse>(this) {
            @Override
            protected void doOnSuccess(User.UploadIdentifyMerchantResponse data) {

                if(data.getErrCode()==User.UploadIdentifyMerchantResponse.ErrorCode.ERR_OK)
                {



                    ToastUtils.show("提交成功");
                    Intent intent=new Intent(ShopApplyActivity.this,ApplyAcceptActivity.class);
                    startActivity(intent);
                    finish();



                }else
                {

                    switch (data.getErrCode().getNumber())
                    {
                        case User.UploadIdentifyMerchantResponse.ErrorCode.ERR_PASS_STATUS_NOT_PASS_VALUE:

                            ToastUtils.show("本轮审核未通过，需重新提交商家认领申请");
                            break;

                        default:
                            ToastUtils.show(data.getErrMsg());
                    }


                }

            }

            @Override
            public User.UploadIdentifyMerchantResponse call() throws Exception {
                return apiManager.uploadIdentifyMerchant(cacheManager.getCurrentUser().getUserId(),  id_card_data,  shop_card_data,posCode);
            }


        }.execute();



    }

    public void initValue(Merchant.MerchantInfo merchantInfo )
    {

       this.newMerchantInfo=merchantInfo;
        relate_shop.setText(merchantInfo.getName());
        shop_name.setText(merchantInfo.getName());
        shop_address.setText(merchantInfo.getAddress());
        shop_phone.setText(merchantInfo.getTel());
        //TODO  缺失字段
        shop_memo.setText("字段暂缺");

    }


    public void initValue(User.IdentifyMerchantInfo merchantInfo)
    {

      //   this.identifyMerchantInfo =merchantInfo;
        relate_shop.setText(merchantInfo.getMerchantName());
        shop_name.setText(merchantInfo.getMerchantName());
        shop_address.setText(merchantInfo.getMerchantAddress());
        shop_phone.setText(merchantInfo.getMerchantTel());
        //TODO  缺失字段
        shop_memo.setText("字段暂缺");

    }
}
