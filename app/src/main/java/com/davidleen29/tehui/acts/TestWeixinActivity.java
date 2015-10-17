package com.davidleen29.tehui.acts;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.utils.ToastUtils;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;


@ContentView(R.layout.activity_test_weixin)
public class TestWeixinActivity extends BaseActivity implements IWXAPIEventHandler {

    private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;


    @InjectView(R.id.sendTextSessition)
    View sendTextSessition;
 @InjectView(R.id.sendTextLine)
    View sendTextLine;

    @InjectView(R.id.testVersion)
    View testVersion;


    private IWXAPI weixiApi;

    @Override
    protected void init(Bundle bundle) {


        String appId=getResources().getString(R.string.weixin_app_id);
        weixiApi = WXAPIFactory.createWXAPI(this, appId, true);
        weixiApi.registerApp(appId);



        weixiApi.handleIntent(getIntent(), this);


        sendTextLine.setOnClickListener(this);

        sendTextSessition.setOnClickListener(this);
        testVersion.setOnClickListener(this);

    }


    @Override
    protected void onViewClick(View v, int id) {

        switch (id)
        {
            case R.id.sendTextLine:

                sendTextLine();

                break;
            case R.id.sendTextSessition:


                sendTextSessition();
                break;
          case R.id.testVersion:


              testVersion();
                break;
        }
    }

    private void testVersion() {

        int wxSdkVersion = weixiApi.getWXAppSupportAPI();
        if (wxSdkVersion >= TIMELINE_SUPPORTED_VERSION) {
            ToastUtils.show( "wxSdkVersion = " + Integer.toHexString(wxSdkVersion) + "\ntimeline supported");
        } else {
            ToastUtils.show( "wxSdkVersion = " + Integer.toHexString(wxSdkVersion) + "\ntimeline not supported");
        }



    }

    private void sendTextLine() {
        String text="bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb";
        WXTextObject textObject = new WXTextObject();

        textObject.text=text;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObject;
        msg.description = text;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction =  String.valueOf(System.currentTimeMillis()) ;
        req.message = msg;
        req.scene =   SendMessageToWX.Req.WXSceneTimeline;
        weixiApi.sendReq(req);
    }

    private void sendTextSessition() {

        String text="aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        WXTextObject textObject = new WXTextObject();

        textObject.text=text;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObject;
        msg.description = text;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction =  String.valueOf(System.currentTimeMillis()) ;
        req.message = msg;
        req.scene =   SendMessageToWX.Req.WXSceneSession;
        weixiApi.sendReq(req);



    }


    @Override
    public void onReq(BaseReq baseReq) {



    }

    @Override
    public void onResp(BaseResp baseResp) {
        String result    ;

        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result ="发送成功";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "发送取消";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "发送被拒绝";
                break;
            default:
                result ="发送返回";
                break;
        }

        ToastUtils.show(  result );
    }
}
