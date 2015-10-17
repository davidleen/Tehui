package com.davidleen29.tehui.acts;


import android.content.Intent;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.api.ApiManager;
import com.davidleen29.tehui.events.RegisterCompleteEvent;
import com.davidleen29.tehui.frags.BaseDialogFragment;
import com.davidleen29.tehui.helper.CacheManager;
import com.davidleen29.tehui.tasks.ThTask;
import com.davidleen29.tehui.utils.StringUtils;
import com.davidleen29.tehui.utils.ToastUtils;
import com.davidleen29.tehui.widget.HeaderView;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Common;
import com.huiyou.dp.service.protocol.User;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;


/**
 * 注册界面 同时也是忘记密码界面
 */
@ContentView(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {





    public static final String EXTRA_TYPE="EXTRA_TYPE";
    public static final String EXTRA_PHONE="EXTRA_PHONE";
    @InjectView(R.id.c_password)
    View c_password;

    @InjectView(R.id.edt_password)
    EditText edt_password;
    @InjectView(R.id.edt_phone)
    EditText edt_phone;


    @InjectExtra(value = EXTRA_PHONE,optional = true)
    String phone;

    @InjectExtra(value = EXTRA_TYPE,optional = true)
    Common.SmsSendType  type= Common.SmsSendType.SmsSendType_REGISTER;

    @Inject
    CacheManager cacheManager;

    @InjectView(R.id.header)
    HeaderView header;



    @InjectView(R.id.nextStep)
    View nextStep;
    @Override
    protected void init(Bundle bundle) {



        c_password.setOnClickListener(this);
        nextStep.setOnClickListener(this);
        if(type.equals(  Common.SmsSendType.SmsSendType_REGISTER))
        {

            edt_password.setHint("请输入密码");
            header.setTitle("绑定手机(1/2)");
        }else
        {  edt_password.setHint("请输入新密码");
            header.setTitle("忘记密码(1/2)");
        }

    }




    @Override
    protected void onViewClick(View v, int id) {


        switch (id)
        {
            case R.id.nextStep:


                register();


                break;


            case R.id.c_password:

                v.setSelected(!v.isSelected());

                edt_password.setInputType(InputType.TYPE_CLASS_TEXT|(v.isSelected() ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_TEXT_VARIATION_PASSWORD));
                edt_password.setSelection(edt_password.getText().length());
                break;
        }


    }

    private  void register()
    {

        final String phone=edt_phone.getText().toString().trim();
        final String password=edt_password.getText().toString().trim();

        if(phone.length()!=11|| !StringUtils.isDigitalString(phone))
        {
            ToastUtils.show("手机号码不正确， 请输入11位数字");
            edt_phone.requestFocus();
            edt_phone.setSelection(phone.length());
            return;
        }



        if(password.length()<6)
        {
            ToastUtils.show("密码至少六位");
            edt_password.requestFocus();
            edt_password.setSelection(password.length());
            return;
        }




        if(type.equals(  Common.SmsSendType.SmsSendType_FORGET_PASSWORD))
        {
            Intent intent=new Intent(RegisterActivity.this,Register2Activity.class);
            intent.putExtra(Register2Activity.EXTRA_MOBILE_NUM,phone);
            intent.putExtra(Register2Activity.EXTRA_PASSWORD,password);
            intent.putExtra(Register2Activity.EXTRA_TYPE,type);
            startActivity(intent);


            return ;
        }

        final String  tempUserName=cacheManager.getCurrentUser()==null?"":cacheManager.getCurrentUser().getUserName();
        new ThTask<User.RegisterResponse>(this) {


            @Inject
            ApiManager apiManager;

            @Override
            public User.RegisterResponse call() throws Exception {


                return apiManager.register(phone,password,tempUserName);
            }

            @Override
            protected void doOnSuccess(User.RegisterResponse data) {



                if(data.getErrCode()== User.RegisterResponse.ErrorCode.ERR_OK)
                {

                    Intent intent=new Intent(RegisterActivity.this,Register2Activity.class);
                    intent.putExtra(Register2Activity.EXTRA_MOBILE_NUM,phone);
                    intent.putExtra(Register2Activity.EXTRA_PASSWORD,password);
                    intent.putExtra(Register2Activity.EXTRA_TYPE,type);
                    startActivity(intent);

                }else
                {



                    switch (data.getErrCode().getNumber())
                    {
                        case User.RegisterResponse.ErrorCode.ERR_MOBILE_NUM_REGISTERED_VALUE:




                            BaseDialogFragment dialogFragment=   new BaseDialogFragment()
                            {

                                @Override
                                protected void onYesClick(View view) {
                                    dismiss();
                                    Intent intent =new Intent(RegisterActivity.this,LoginActivity.class);
                                    intent.putExtra(LoginActivity.EXTRA_MOBILE,phone);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();

                                }


                            };
                            dialogFragment.setParams(null, "当前手机号已经注册过，是否马上登录？", "好的" );

                            dialogFragment.show(getSupportFragmentManager(), dialogFragment.getClass().getName());


                            break;
                        default:  ToastUtils.show(data.getErrMsg());

                    }

                }



            }
        }.execute();



    }

    public void onEvent(RegisterCompleteEvent event)
    {
        finish();
    }


}
