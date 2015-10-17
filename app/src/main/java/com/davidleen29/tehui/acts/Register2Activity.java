package com.davidleen29.tehui.acts;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.api.ApiManager;
import com.davidleen29.tehui.events.RegisterCompleteEvent;
import com.davidleen29.tehui.tasks.ThTask;
import com.davidleen29.tehui.utils.ToastUtils;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Common;
import com.huiyou.dp.service.protocol.Sms;
import com.huiyou.dp.service.protocol.User;


import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;
import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;


@ContentView(R.layout.activity_register_2)
public class Register2Activity extends BaseActivity {


    public  static final String EXTRA_MOBILE_NUM ="EXTRA_MOBILE_NUM" ;
    public  static final String EXTRA_PASSWORD ="EXTRA_PASSWORD" ;
    public static final String EXTRA_TYPE = "EXTRA_TYPE";

    @InjectView(R.id.done)
    View done;

    @Inject
    ApiManager apiManager;

    @InjectExtra(EXTRA_MOBILE_NUM)
    String mobile;

    @InjectExtra(EXTRA_TYPE)
    Common.SmsSendType type;

    @InjectExtra(EXTRA_PASSWORD)
    String password;

    @InjectView(R.id.phone_number)
    TextView phone_number;


    @InjectView(R.id.verifyCode)
    EditText verifyCode;
    @InjectView(R.id.send)
    View send;

    @InjectView(R.id.timeRemain)
    TextView timeRemain;


    @Override
    protected void init(Bundle bundle) {


        done.setOnClickListener(this);
        send.setOnClickListener(this);

        phone_number.setText(mobile);

        sendVerifyMessage();


    }



    private void sendVerifyMessage()
    {

        send.setEnabled(false);

        new ThTask<Sms.SendIdentifyingCodeResponse>(this) {
            @Override
            public Sms.SendIdentifyingCodeResponse call() throws Exception {
                return apiManager.sendIdentifyingCode( mobile,type );
            }

            @Override
            protected void doOnSuccess(Sms.SendIdentifyingCodeResponse data) {
                if(data.getErrCode()== Sms.SendIdentifyingCodeResponse.ErrorCode.ERR_OK)
                {




                    ToastUtils.show("效验码已经发送 请查收...");

                }else
                {
                    ToastUtils.show(data.getErrMsg());
                }

            }
        }.execute();



        timeRemain.setVisibility(View.VISIBLE);
        //开始倒数计时
        final Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            int totalPeriod = 60 * 1000;
            int lastTime = 0;

            @Override
            public void run() {

                lastTime += 1000;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        timeRemain.setText((totalPeriod - lastTime) / 1000 + "秒");
                    }
                });

                if (lastTime >= totalPeriod) {
                    timer.cancel();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            send.setEnabled(true);
                            timeRemain.setVisibility(View.GONE);

                        }
                    });

                }
            }
        }, 0, 1000);
    }




    @Override
    protected void onViewClick(View v, int id) {


        switch (id)
        {

            case R.id.send  :


                sendVerifyMessage();

                break;
            case R.id.done:

                checkVerifyCode();

                break;
        }


    }



    private void checkVerifyCode()
    {

       final  String code=verifyCode.getText().toString().trim();


        if(type.equals(  Common.SmsSendType.SmsSendType_FORGET_PASSWORD))
        {

            new ThTask<User.ForgetPasswordResponse>(this) {

                @Inject
                ApiManager apiManager;

                @Override
                public User.ForgetPasswordResponse call() throws Exception {



                        return apiManager.forgetPassword(code, mobile, password);
                }

                @Override
                protected void doOnSuccess(User.ForgetPasswordResponse data) {

                    if (data.getErrCode() == User.ForgetPasswordResponse.ErrorCode.ERR_OK) {


                        EventBus.getDefault().post(new RegisterCompleteEvent(mobile, password,type));
                        finish();

                    } else {

                        switch (data.getErrCode().getNumber())
                        {
                            case User.ForgetPasswordResponse.ErrorCode.ERR_CODE_EXPIRE_VALUE:

                                ToastUtils.show("验证码超期， 请重新发送");
                                break;
                            case User.ForgetPasswordResponse.ErrorCode.ERR_NOT_EXISTS_CODE_VALUE:


                                ToastUtils.show("验证码不存在");
                                break;

                            case User.ForgetPasswordResponse.ErrorCode.ERR_PASSWORD_PATTERN_VALUE:


                                ToastUtils.show("密码格式错误,密码至少六位");
                                break;

                            case User.ForgetPasswordResponse.ErrorCode.ERR_WRONG_CODE_VALUE:


                                ToastUtils.show("验证码错误");
                                break;
                            default:

                                ToastUtils.show( data.getErrMsg());
                        }


                    }

                }
            }.execute();



        }
        else {

            new ThTask<User.CheckRegisterIdentifyingCodeResponse>(this) {

                @Inject
                ApiManager apiManager;

                @Override
                public User.CheckRegisterIdentifyingCodeResponse call() throws Exception {



                        return apiManager.checkRegisterIdentifyingCode(code, mobile);
                }

                @Override
                protected void doOnSuccess(User.CheckRegisterIdentifyingCodeResponse data) {

                    if (data.getErrCode() == User.CheckRegisterIdentifyingCodeResponse.ErrorCode.ERR_OK) {


                        EventBus.getDefault().post(new RegisterCompleteEvent(mobile, password,type));
                        finish();

                    } else {

                        switch (data.getErrCode().getNumber()) {
                            case User.CheckRegisterIdentifyingCodeResponse.ErrorCode.ERR_CODE_EXPIRE_VALUE:

                                ToastUtils.show("验证码超期， 请重新发送");
                                break;
                            case User.ForgetPasswordResponse.ErrorCode.ERR_NOT_EXISTS_CODE_VALUE:


                                ToastUtils.show("验证码不存在");
                                break;

                            case User.CheckRegisterIdentifyingCodeResponse.ErrorCode.ERR_MOBILE_NUM_REGISTERED_VALUE:

                                ToastUtils.show("该号码已经注册");
                                break;

                            case User.CheckRegisterIdentifyingCodeResponse.ErrorCode.ERR_WRONG_CODE_VALUE:


                                ToastUtils.show("验证码错误");
                                break;


                            default:

                                ToastUtils.show(data.getErrMsg());
                        }
                    }

                }
            }.execute();
        }
    }



}
