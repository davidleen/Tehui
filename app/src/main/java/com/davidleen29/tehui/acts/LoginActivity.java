package com.davidleen29.tehui.acts;


import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.api.ApiManager;
import com.davidleen29.tehui.events.LoginCompleteEvent;
import com.davidleen29.tehui.events.RegisterCompleteEvent;
import com.davidleen29.tehui.helper.CacheManager;
import com.davidleen29.tehui.tasks.ThTask;
import com.davidleen29.tehui.utils.StringUtils;
import com.davidleen29.tehui.utils.ToastUtils;

import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Common;
import com.huiyou.dp.service.protocol.User;

import de.greenrobot.event.EventBus;
import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;


@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity   {


    public static final String EXTRA_MOBILE = "EXTRA_MOBILE";
    @InjectView(R.id.login)
    View login;


    @InjectView(R.id.register)
    View register;


    @InjectView(R.id.c_password)
    View c_password;

    @InjectView(R.id.edt_password)
    EditText edt_password;
    @InjectView(R.id.edt_phone)
    EditText edt_phone;

    @InjectView(R.id.btn_forget)
    TextView  btnForget;

    @Inject
    CacheManager cacheManager;

    @Inject
    ApiManager apiManager;

    @InjectExtra(value = EXTRA_MOBILE,optional = true)
    String mobile;

    @Override
    protected void init(Bundle bundle) {



        if(!StringUtils.isEmpty(mobile))
        edt_phone.setText(mobile);
        login.setOnClickListener(this);
        c_password.setOnClickListener(this);
        btnForget.setOnClickListener(this);
        register.setOnClickListener(this);

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {

        final String mobileNum=edt_phone.getText().toString().trim();
        final String password=edt_password.getText().toString().trim();

        new ThTask<User.LoginResponse>(this) {
            @Override
            public User.LoginResponse call() throws Exception {
               return apiManager.login(mobileNum,password);

            }

            @Override
            protected void doOnSuccess(User.LoginResponse data) {

                if(data.getErrCode()== User.LoginResponse.ErrorCode.ERR_OK)
                {

                    cacheManager.setCurrentUser(data.getUserInfo());
                    EventBus.getDefault().post( new LoginCompleteEvent());
                    finish();

                }else
                {







































                    switch (data.getErrCode().getNumber())
                    {


//                        case 999:
//                            ToastUtils.show("手机号码当天发送的短信验证码已经超过允许次数");
//                            edt_phone.requestFocus();
//                            break;
//
//                        case 998:
//                            ToastUtils.show("当前IP当天发送的短信验证码已经超过允许次数");
//                            edt_phone.requestFocus();
//                            break;
//
//
//                        case 997:
//                            ToastUtils.show("获取成功， 请注意短信查收");
//
//                            edt_password.requestFocus();
//                            break;
//
//                        case 996:
//                            ToastUtils.show("短时间重复发送短信，请稍后再发送验证");
//                            edt_password.requestFocus();
//                            break;


                        case User.LoginResponse.ErrorCode.ERR_USER_LOCKED_VALUE:
                            ToastUtils.show("该用户已经被锁定，不能登录");
                            edt_phone.requestFocus();
                            break;

                        case User.LoginResponse.ErrorCode.ERR_USER_NOT_EXSITS_VALUE:
                            ToastUtils.show("用户不存在");
                            edt_phone.requestFocus();
                            break;


                        case User.LoginResponse.ErrorCode.ERR_WRONG_PASSWORD_VALUE:
                            ToastUtils.show("密码错误");
                            edt_password.requestFocus();
                            break;







                        default:
                            ToastUtils.show(data.getErrMsg());
                    }


                }

            }
        }.execute();

        checkRequest();

    }

    int  timeInterval=0;
    int requestTimeADay=0;
    int MaxRequestTimeADay=5;
    int requestTimeAIP=0;
    int MaxRequestTimeAIP=5;


    /**
     * 短信验证码检查代码
     * @return
     */
    private int checkRequest()
    {



        //间隔小于120秒
        if(timeInterval<120)
        {
            return 996;
        }
        //当天请求次数超过阈值
        if(requestTimeADay>=MaxRequestTimeADay)
            return  999;
        //同ip请求次数超过阈值
        if(requestTimeAIP>=MaxRequestTimeAIP)
            return  998;


        //请求状态正常。
        return 997;

    }

    @Override
    protected void onViewClick(View v,int id) {

        switch (v.getId())
        {

            case R.id.register: {
                Intent intent = new Intent(this, RegisterActivity.class);
                intent.putExtra(RegisterActivity.EXTRA_TYPE, Common.SmsSendType.SmsSendType_REGISTER);
                startActivity(intent);
                break;
            }
                case R.id.login:

                attemptLogin();
                break;
            case R.id.c_password:

                v.setSelected(!v.isSelected());
                edt_password.setInputType(InputType.TYPE_CLASS_TEXT | (v.isSelected() ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_TEXT_VARIATION_PASSWORD));
                edt_password.setSelection(edt_password.getText().length());
                break;


            case R.id.btn_forget: {

                Intent intent = new Intent(this, RegisterActivity.class);
                intent.putExtra(RegisterActivity.EXTRA_PHONE, edt_phone.getText().toString().trim());
                intent.putExtra(RegisterActivity.EXTRA_TYPE, Common.SmsSendType.SmsSendType_FORGET_PASSWORD);

                startActivity(intent);
            }
                break;
        }

    }




    public void onEvent(RegisterCompleteEvent event)
    {

        edt_password.setText(event.password);
        edt_phone.setText(event.phoneNumber);

        if(Common.SmsSendType.SmsSendType_FORGET_PASSWORD.equals(event.type ))
            ToastUtils.show("密码设置成功， 正在为您自动登录...");
        else
            ToastUtils.show("注册完成， 正在为您自动登录...");
        attemptLogin();
    }

}

