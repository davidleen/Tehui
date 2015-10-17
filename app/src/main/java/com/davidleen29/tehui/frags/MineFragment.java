package com.davidleen29.tehui.frags;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.acts.ApplyAcceptActivity;
import com.davidleen29.tehui.acts.MyBagActivity;
import com.davidleen29.tehui.acts.MyFavorActivity;
import com.davidleen29.tehui.acts.MyBankActivity;
import com.davidleen29.tehui.acts.MyCategoryActivity;
import com.davidleen29.tehui.acts.NewMessageActivity;
import com.davidleen29.tehui.acts.PersonalActivity;
import com.davidleen29.tehui.acts.RegisterActivity;
import com.davidleen29.tehui.acts.SettingActivity;
import com.davidleen29.tehui.acts.ShopActivity;
import com.davidleen29.tehui.acts.ShopApplyActivity;
import com.davidleen29.tehui.acts.SystemMessageActivity;
import com.davidleen29.tehui.api.ApiManager;
import com.davidleen29.tehui.events.AppInitCompleteEvent;
import com.davidleen29.tehui.events.OnLoadSystemMessageCountEvent;
import com.davidleen29.tehui.events.SystemMessageReadEvent;
import com.davidleen29.tehui.events.UserInfoRefreshEvent;
import com.davidleen29.tehui.events.UserInfoUpdateEvent;
import com.davidleen29.tehui.helper.CacheManager;
import com.davidleen29.tehui.helper.ImageLoaderHelper;
import com.davidleen29.tehui.tasks.ThTask;
import com.davidleen29.tehui.utils.Log;
import com.davidleen29.tehui.utils.ToastUtils;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Circle;
import com.huiyou.dp.service.protocol.Common;
import com.huiyou.dp.service.protocol.User;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.greenrobot.event.EventBus;
import roboguice.inject.InjectView;


public class MineFragment extends BaseFragment {

    @InjectView(R.id.ll_messages)
    View ll_messages;

    @InjectView(R.id.ll_shop)
    View ll_shop;
    @InjectView(R.id.ll_banks)
    View ll_banks;
    @InjectView(R.id.ll_favors)
    View ll_favors;

    @InjectView(R.id.ll_bags)
    View ll_bags;

    @InjectView(R.id.ll_categorys)
    View ll_categorys;

    @InjectView(R.id.userPhoto)
    ImageView userPhoto;

    @InjectView(R.id.userName)
    TextView userName;


    @InjectView(R.id.bankCount)
    TextView bankCount;

    @InjectView(R.id.bagCount)
    TextView bagCount;

    @InjectView(R.id.favorCount)
    TextView favorCount;

    @InjectView(R.id.categoryCount)
    TextView categoryCount;


    @InjectView(R.id.bindPhone)
    View binPhone;


    @InjectView(R.id.setting)
    View setting;

    @InjectView(R.id.ll_shop_apply)
    View ll_shop_apply;


    @InjectView(R.id.bindPhone)
    View bindPhone;


    @InjectView(R.id.phoneNumber)
    TextView phoneNumber;
       @InjectView(R.id.messageCount)
    TextView messageCount;





    @Inject
    CacheManager cacheManager;


    @Inject
    ApiManager apiManager;

    private User.UserSetUserInfo userSetUserInfo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        super.onViewCreated(view, savedInstanceState);
        ll_messages.setOnClickListener(this);
        ll_shop.setOnClickListener(this);
        ll_banks.setOnClickListener(this);
        ll_favors.setOnClickListener(this);
        ll_bags.setOnClickListener(this);
        ll_categorys.setOnClickListener(this);
        userName.setOnClickListener(this);
        userPhoto.setOnClickListener(this);
        setting.setOnClickListener(this);
        ll_shop_apply.setOnClickListener(this);
        bindPhone.setOnClickListener(this);

        if(cacheManager.hasInit()) {
            loadMessageCount();
            loadData();
        }



    }


    @Override
    protected void onViewClick(View v, int id) {

        switch (id)
        {
            case R.id.ll_messages:
            {
                Intent intent=new Intent( getActivity(), SystemMessageActivity.class);
                startActivityForResult(intent,-1);
            }break;

            case R.id.ll_banks:
            {
                Intent intent=new Intent( getActivity(), MyBankActivity.class);
                startActivityForResult(intent,-1);
            }break;
            case R.id.ll_categorys:
            {
                Intent intent=new Intent( getActivity(), MyCategoryActivity.class);
                startActivityForResult(intent,-1);
            }break;
            case R.id.ll_shop:
            {



                loadIdentifyMerchantInfo();

            }break;

            case R.id.ll_favors:
            {
                Intent intent=new Intent( getActivity(), MyFavorActivity.class);
                startActivityForResult(intent,-1);
            }break;


            case R.id.ll_bags:
            {


                Intent intent=new Intent( getActivity(), MyBagActivity.class);
                startActivityForResult(intent,-1);
            }
                break;

            case R.id.setting:
            {


                Intent intent=new Intent( getActivity(), SettingActivity.class);
                startActivityForResult(intent,-1);
            }
            break;

            case R.id.ll_shop_apply:
            {
                Intent intent=new Intent( getActivity(), ShopApplyActivity.class);
                startActivityForResult(intent,-1);


            }break;

            case R.id.userName:
            case R.id.userPhoto: {
                Intent intent = new Intent(getActivity(), PersonalActivity.class);
                if (userSetUserInfo != null)
                    intent.putExtra(PersonalActivity.USER_INFO, userSetUserInfo);
                startActivityForResult(intent, -1);
            }  break;

            case R.id.bindPhone: {

                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                startActivityForResult(intent, -1);
            }   break;
        }
    }



    public MineFragment() {

    }

    public void onEventMainThread(   AppInitCompleteEvent event)
    {


        loadData();
        loadMessageCount();
    }



    private void loadData()
    {




        new ThTask<User.GetUserInfoResponse>(getActivity(),true)
        {

            @Override
            public User.GetUserInfoResponse call() throws Exception {
                return apiManager.getUserInfo(cacheManager.getCurrentUser().getUserId());
            }

            @Override
            protected void doOnSuccess(User.GetUserInfoResponse data) {



                if(data.getErrCode()==User.GetUserInfoResponse.ErrorCode.ERR_OK)
                {
                    initValue(data.getUserSetUserInfo());
                    EventBus.getDefault().post(new UserInfoRefreshEvent(data.getUserSetUserInfo()));
                }
                else
                    ToastUtils.show(data.getErrMsg());


            }
        }.execute();











    }


    /**
     * 读取未读消息接口
     */
    private void loadMessageCount()
    {


        messageCount.setText("");
        new ThTask<Circle.UserSystemMessagesNotReadCountResponse>(getActivity(),true)
        {

            @Override
            public Circle.UserSystemMessagesNotReadCountResponse call() throws Exception {
                return apiManager.userSystemMessagesNotReadCount(cacheManager.getCurrentUser().getUserId());
            }

            @Override
            protected void doOnSuccess(Circle.UserSystemMessagesNotReadCountResponse data) {



                if(data.getErrCode()==Circle.UserSystemMessagesNotReadCountResponse.ErrorCode.ERR_OK)
                {


                    EventBus.getDefault().post(new OnLoadSystemMessageCountEvent(data.getNotReadCount()));
                }
                else
                    ToastUtils.show(data.getErrMsg());




            }
        }.execute();

    }


    public void onEvent(UserInfoUpdateEvent event)
    {
        loadData();
    }
    public void onEvent(SystemMessageReadEvent event)
    {
       loadMessageCount();
    }
    private void initValue(User.UserSetUserInfo userSetUserInfo)
    {
            this.userSetUserInfo=userSetUserInfo;

        User.UserInfo userInfo=cacheManager.getCurrentUser();
        binPhone.setVisibility(userInfo.getUserType() == Common.UserType.UserType_TEMP ? View.VISIBLE : View.GONE);
        phoneNumber.setVisibility(userInfo.getUserType() == Common.UserType.UserType_TEMP ? View.GONE : View.VISIBLE);
        userName.setText(userSetUserInfo.getNickName());
        ImageLoader.getInstance().displayImage(userSetUserInfo.getHeadImg(), userPhoto, ImageLoaderHelper.getPortraitDisplayOptions());
        bankCount.setText(String.valueOf(userSetUserInfo.getUsersetBankCount()));
        categoryCount.setText(String.valueOf(userSetUserInfo.getUsersetCategoryCount()));
        bagCount.setText(String.valueOf(userSetUserInfo.getUsersetCardCount()));
        favorCount.setText(String.valueOf(userSetUserInfo.getUsersetFavoriteCount()));
        phoneNumber.setText(userInfo.getPhone());

    }


    /**
     * 读取用户认真商户信息。
     */
    private void loadIdentifyMerchantInfo() {


        if(cacheManager.getCurrentUser().getUserType().equals(Common.UserType.UserType_TEMP))
        {





            BaseDialogFragment dialogFragment=   new BaseDialogFragment()
            {

                @Override
                protected void onYesClick(View view) {
                    dismiss();
                    Intent intent = new Intent(getActivity(), RegisterActivity.class);
                    startActivity(intent);

                }

                @Override
                protected void onNoClick(View view) {

                    dismiss();
                }
            };
            dialogFragment.setParams(null, "您当前身份是游客，不能认领商家，马上注册？", "好的", "我再看看");

            dialogFragment.show(getChildFragmentManager(),dialogFragment.getClass().getName());

            return;
        }


        new ThTask<User.GetIdentifyMerchantInfoResponse>(getActivity())
        {

            @Override
            public User.GetIdentifyMerchantInfoResponse call() throws Exception {
                return apiManager.getIndetifyMerchantInfo(cacheManager.getCurrentUser().getUserId());
            }

            @Override
            protected void doOnSuccess(User.GetIdentifyMerchantInfoResponse data) {



                if(data.getErrCode()==User.GetIdentifyMerchantInfoResponse.ErrorCode.ERR_OK)
                {

                    switch (data.getIdentifyMerchantInfo().getIsPass().getNumber())
                    {

                        case  Common.IdentifyMerchantPassStatus.IdentifyMerchantPassStatus_PASSING_VALUE:
                        {
                            Intent intent=new Intent( getActivity(), ApplyAcceptActivity.class);
                            intent.putExtra(ApplyAcceptActivity.EXTRA_IDENTIFY_STATUE,Common.IdentifyMerchantPassStatus.IdentifyMerchantPassStatus_PASSING);
                            startActivityForResult(intent, -1);

                        }
                            break;

                        case  Common.IdentifyMerchantPassStatus.IdentifyMerchantPassStatus_NOT_PASS_VALUE:
                        {

                            Intent intent=new Intent( getActivity(), ApplyAcceptActivity.class);
                            intent.putExtra(ApplyAcceptActivity.EXTRA_IDENTIFY_STATUE,Common.IdentifyMerchantPassStatus.IdentifyMerchantPassStatus_NOT_PASS);
                            startActivityForResult(intent, -1);

                        }
                            break;

                        case  Common.IdentifyMerchantPassStatus.IdentifyMerchantPassStatus_RESOURCE_NOT_UPLOAD_VALUE: {
                            Intent intent = new Intent(getActivity(), ShopApplyActivity.class);
                            intent.putExtra(ShopApplyActivity.EXTRA_MERCHANT_INFO, data.getIdentifyMerchantInfo());
                            startActivityForResult(intent, -1);
                            ToastUtils.show("请上传营业执照与身份证");
                        }
                            break;
                        default: {
                            Intent intent = new Intent(getActivity(), ShopActivity.class);
                            intent.putExtra(ShopActivity.EXTRA_MERCHANTINFO_ID, data.getIdentifyMerchantInfo().getMerchantId());
                            startActivityForResult(intent, -1);
                        }
                    }



                }
                else
                {


                    switch (data.getErrCode().getNumber())
                    {
                        case User.GetIdentifyMerchantInfoResponse.ErrorCode.ERR_USER_NOT_IDENTIFY_MERCHANT_VALUE:
                            Intent intent=new Intent( getActivity(), ShopApplyActivity.class);
                            startActivityForResult(intent, -1);

                            break;
                        default:

                            ToastUtils.show(data.getErrMsg());
                            break;
                    }
                }





            }
        }.execute();


       ;
    }



    //读取到新系统消息记录数据
    public void onEvent(OnLoadSystemMessageCountEvent event)
    {

        messageCount.setText(String.valueOf(event.messageCount));




    }
}
