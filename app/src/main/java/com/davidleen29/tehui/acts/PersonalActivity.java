package com.davidleen29.tehui.acts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.api.ApiManager;
import com.davidleen29.tehui.events.MessageSendEvent;
import com.davidleen29.tehui.events.UserInfoUpdateEvent;
import com.davidleen29.tehui.helper.CacheManager;
import com.davidleen29.tehui.helper.ImageLoaderHelper;
import com.davidleen29.tehui.helper.PictureHelper;
import com.davidleen29.tehui.tasks.ThTask;
import com.davidleen29.tehui.utils.StringUtils;
import com.davidleen29.tehui.utils.ToastUtils;
import com.davidleen29.tehui.widget.HeaderView;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Circle;
import com.huiyou.dp.service.protocol.Common;
import com.huiyou.dp.service.protocol.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.io.ByteArrayOutputStream;
import java.io.File;

import de.greenrobot.event.EventBus;
import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;


@ContentView(R.layout.activity_personal)
public class PersonalActivity extends BaseActivity {


    public static final String USER_INFO = "UserInfo";
    private static final int UPDATE_NICK_NAME = 556;
    private static final int UPDATE_SEX=557;
    @InjectView(R.id.photo)
    ImageView photo;

    @InjectView(R.id.name)
    TextView name;

    @InjectView(R.id.sex)
    TextView sex;


    @InjectView(R.id.ll_photo)
    View ll_photo;

    @InjectView(R.id.ll_name)
    View ll_name;

    @InjectView(R.id.ll_sex)
    View ll_sex;

    @InjectView(R.id.header)
    HeaderView header;

    @InjectExtra(value = USER_INFO,optional = true)
    User.UserSetUserInfo userSetUserInfo;



    @Inject
    CacheManager cacheManager;

    @Inject
    ApiManager apiManager;

    private String newUrl;
    private String newNickName;
    private Common.UserSex newUserSex;

    @Override
    protected void init(Bundle bundle) {
        super.init(bundle);


        ll_photo.setOnClickListener(this);
        registerForContextMenu(ll_photo);
        ll_name.setOnClickListener(this);
        ll_sex.setOnClickListener(this);
        header.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                savePersonalInfo();
            }
        });




        initValue();

    }


    private void initValue( )
    {
        if(userSetUserInfo==null) return;
        name.setText(userSetUserInfo.getNickName());
        ImageLoader.getInstance().displayImage(userSetUserInfo.getHeadImg(), photo,ImageLoaderHelper.getPortraitDisplayOptions());

        sex.setText(userSetUserInfo.getSex() == Common.UserSex.UserSex_FEMALE ? "女" : "男");
    }


    @Override
    protected void onViewClick(View v, int id) {



        switch (id)
        {
            case R.id.ll_photo:
                openContextMenu(ll_photo);
                break;
            case R.id.ll_name: {
                Intent intent = new Intent(this, TextEditActivity.class);
                intent.putExtra(TextEditActivity.EXTRA_CONTENT, name.getText().toString());
                intent.putExtra(TextEditActivity.EXTRA_TITLE, "昵称");
                startActivityForResult(intent, UPDATE_NICK_NAME);
            }
                break;
            case R.id.ll_sex: {
                Intent intent = new Intent(this, SexSelectActivity.class);
                intent.putExtra(SexSelectActivity.EXTRA_SEX, newUserSex==null?userSetUserInfo.getSex():newUserSex);
                startActivityForResult(intent, UPDATE_SEX);
            }

                 break;

        }

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(resultCode!=RESULT_OK)
            return;

        PictureHelper.PickData pickData=   PictureHelper.onResult(this, requestCode, resultCode, data);

        if(pickData!=null)
        {
            newUrl=Uri.fromFile(new File(pickData.localPath)).toString();
            ImageLoader.getInstance().displayImage(newUrl,photo, ImageLoaderHelper.getLocalPortraitDisplayOptions());

        }


        switch (requestCode)
        {
            case UPDATE_NICK_NAME:


                    newNickName=data.getStringExtra(TextEditActivity.EXTRA_CONTENT);
                    name.setText(newNickName);




                break;


            case UPDATE_SEX:


                newUserSex= (Common.UserSex) data.getSerializableExtra(SexSelectActivity.EXTRA_SEX);
                sex.setText(newUserSex == Common.UserSex.UserSex_FEMALE ? "女" : "男");



                break;
        }

    }


    private void savePersonalInfo() {




        new ThTask<User.SetUserInfoResponse>(this) {
            @Override
            public User.SetUserInfoResponse call() throws Exception {


                byte[] data=null;


                if(!StringUtils.isEmpty(newUrl)) {

                    Bitmap bitmap = ImageLoader.getInstance().loadImageSync(newUrl, new ImageSize(240, 320), ImageLoaderHelper.getLocalPortraitDisplayOptions());

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                    data = bos.toByteArray();
                    bos.close();
                }
                return apiManager.setUserInfo(userSetUserInfo.getUserId(),data,newNickName,newUserSex);
            }

            @Override
            protected void doOnSuccess(User.SetUserInfoResponse data) {


                if(data.getErrCode()== User.SetUserInfoResponse .ErrorCode.ERR_OK)
                {


                    ToastUtils.show("保存成功");
                    EventBus.getDefault().post(new UserInfoUpdateEvent());
                    finish();

                }else
                {
                    ToastUtils.show(data.getErrMsg());
                }


            }
        }.execute();
    }






}
