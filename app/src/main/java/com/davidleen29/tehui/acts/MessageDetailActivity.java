package com.davidleen29.tehui.acts;


import android.content.Context;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.davidleen29.tehui.R;

import com.davidleen29.tehui.adapters.MessageDetailCommentAdapter;
import com.davidleen29.tehui.api.ApiManager;
import com.davidleen29.tehui.events.MessageSendEvent;
import com.davidleen29.tehui.frags.BaseDialogFragment;
import com.davidleen29.tehui.helper.CacheManager;
import com.davidleen29.tehui.helper.ImageLoaderHelper;
import com.davidleen29.tehui.lang.CException;
import com.davidleen29.tehui.lang.ResId;
import com.davidleen29.tehui.tasks.ThTask;
import com.davidleen29.tehui.utils.StringUtils;
import com.davidleen29.tehui.utils.TimeUtils;
import com.davidleen29.tehui.utils.ToastUtils;
import com.davidleen29.tehui.widget.LinearListView;
import com.google.inject.Inject;
import com.google.protobuf.Message;
import com.huiyou.dp.service.protocol.Circle;
import com.huiyou.dp.service.protocol.Common;
import com.huiyou.dp.service.protocol.Merchant;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import de.greenrobot.event.EventBus;
import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_message_detail)
public class MessageDetailActivity extends SimpleActivity<Circle.GetOneMessageResponse> {



    public static final String EXTRA_MESSAGE_ID = "EXTRA_MESSAGE_ID";
    public static final String EXTRA_ASK_FOR_COMMENT = "EXTRA_ASK_FOR_COMMENT";




    @InjectExtra(value = EXTRA_MESSAGE_ID,optional = true)
   long messageId;
    @InjectExtra(value = EXTRA_ASK_FOR_COMMENT,optional = true)
    boolean askForComment;

    @InjectView(R.id.userPhoto)
    ImageView userPhoto;

    @InjectView(R.id.userName)
    TextView userName;


    @InjectView(R.id.replyCount)
    TextView replyCount;

    @InjectView(R.id.content)
    TextView content;

    @InjectView(R.id.time)
    TextView time;

    @InjectView(R.id.picture)
    ImageView picture;
    @InjectView(R.id.comment)
    View comment;
    @InjectView(R.id.ll_comment)
    LinearListView ll_comment;


    @InjectView(R.id.edt_comment)
    EditText editText;


    @InjectView(R.id.send)
    View send;


    @Inject
    MessageDetailCommentAdapter adapter;


    @Inject
    ApiManager apiManager;

    @Inject
    CacheManager cacheManager;


    private long replyToUserId=-1;
    Circle.CircleMessage message;

    @Override
    protected void init(Bundle bundle) {


        ll_comment.setAdapter(adapter);
        ll_comment.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView parent, View v, int position, long id) {


                editText.requestFocus();
                replyToUserId = adapter.getItem(position).getUserId();
                editText.setHint("回复" + adapter.getItem(position).getUserNickName());
                showInputBoard();
            }
        });

        send.setOnClickListener(this);
        super.init(bundle);

    }

    @Override
    protected Circle.GetOneMessageResponse readOnBackground() throws CException {


      return  apiManager.getOneMessage(messageId);
    }

    @Override
    protected void onDataLoaded(Circle.GetOneMessageResponse data) {




        if(data.getErrCode()== Circle.GetOneMessageResponse.ErrorCode.ERR_OK)
        {

            initData(data.getMessage());
            if(askForComment)
            {

                getWindow().getDecorView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showInputBoard();
                    }
                },500);

                askForComment=false;
            }

        }else
        {
            switch (data.getErrCode().getNumber())
            {



                case Circle.GetOneMessageResponse.ErrorCode.ERR_MESSAGE_NOT_EXSITS_VALUE:

                    ToastUtils.show("消息不存在");

                    break;


                default:  ToastUtils.show(data.getErrMsg());
            }

        }



    }


    private void initData(Circle.CircleMessage data)
    {


        this.message=data;
        //set default reply to orign message
        resetReplyTo();
        userName.setText(data.getUserNickName());
        replyCount.setText("回复("+data.getCircleReplyCount()+")");

        content.setText(data.getContent());

        time.setText(TimeUtils.getReplyTimeMessage(data.getInsertTime()));

        ImageLoader.getInstance().displayImage(data.getHeadImg(),userPhoto, ImageLoaderHelper.getPortraitDisplayOptions());


        boolean hasPicture=!StringUtils.isEmpty(data.getImgUrl());

        if(hasPicture){
            ImageLoader.getInstance().displayImage( data.getImgUrl(),picture);
        }

        picture.setVisibility(hasPicture ? View.VISIBLE : View.GONE);




        List<Circle.CircleReply> replies= data.getCircleReplyList();
        boolean noReply=replies == null || replies.size() == 0;
        ll_comment.setVisibility(noReply ? View.GONE : View.VISIBLE);



        adapter.setMessage(data);


        userPhoto.setOnClickListener(this);


        comment.setOnClickListener(this);


    }


    @Override
    protected void onViewClick(View v, int id) {



        switch (id)
        {


            case R.id.comment:


                resetReplyTo();

                showInputBoard();
                break;

            case R.id.userPhoto:



                break;


            case R.id.send:

                sendMessage();



                break;
        }
    }


    /**
     * 发送消息
                */
        private void sendMessage()
        {


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
                dialogFragment.setParams(null, "您当前身份是游客，不能回复消息，马上注册？", "好的", "我再看看");

                dialogFragment.show(getSupportFragmentManager(),dialogFragment.getClass().getName());





                return ;
            }



            final String messageContent=editText.getText().toString().trim();

            new ThTask<Circle.ReplyResponse>(this)
        {


            @Override
            public Circle.ReplyResponse call() throws Exception {
                return apiManager.reply(cacheManager.getCurrentUser().getUserId(),replyToUserId,messageId,messageContent);
            }

            @Override
            protected void doOnSuccess(Circle.ReplyResponse data) {


                if(data.getErrCode()== Circle.ReplyResponse.ErrorCode.ERR_OK)
                {



                    ToastUtils.show("回复成功");

                    hideInputBoard();

                    EventBus.getDefault().post(new MessageSendEvent());
                    resetReplyTo();
                    loadData();

                }else
                {



                    switch (data.getErrCode().getNumber())
                    {
                        case Circle.ReplyResponse.ErrorCode.ERR_REPLY_USER_NOT_EXSITS_VALUE:

                            ToastUtils.show("回复对象不存在");

                            break;


                        case Circle.ReplyResponse.ErrorCode.ERR_CONTENT_IS_NULL_VALUE:

                        ToastUtils.show("回复消息内容为空");

                        break;
                        case Circle.ReplyResponse.ErrorCode.ERR_CONTENT_OVERFLOW_VALUE:

                        ToastUtils.show("回复消息内容超出字数限制(500字)");

                        break;
                        case Circle.ReplyResponse.ErrorCode.ERR_MESSAGE_NOT_EXSITS_VALUE:

                        ToastUtils.show("消息不存在");

                        break;
 case Circle.ReplyResponse.ErrorCode.ERR_USER_NOT_EXSITS_VALUE:

                        ToastUtils.show("回复用户不存在");

                        break;

                        default:  ToastUtils.show(data.getErrMsg());
                    }


                }




            }
        }.execute();

    }

    private void resetReplyTo() {

        replyToUserId=message.getUserId();
        editText.setHint("请输入评论");


    }


    private void  showInputBoard()
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }


    private void  hideInputBoard()
    {
        editText.setText("");
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
    }
}
