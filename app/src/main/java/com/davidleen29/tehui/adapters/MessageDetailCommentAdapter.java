package com.davidleen29.tehui.adapters;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.helper.ImageLoaderHelper;
import com.davidleen29.tehui.lang.ResId;
import com.davidleen29.tehui.lang.UnMixable;
import com.davidleen29.tehui.utils.TimeUtils;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Circle;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 银行简单列表项目
 */


public class MessageDetailCommentAdapter extends AbstractAdapter<Circle.CircleReply> {



    private Circle.CircleMessage message;
    @Inject
    public MessageDetailCommentAdapter(Context context) {
        super(context);
    }

    @Override
    protected UnMixable createViewHolder(int itemViewType) {
        return new ViewHolder();
    }



    public void setMessage(Circle.CircleMessage message)
    {

        this.message=message;
        setDataArray(message.getCircleReplyList());
    }


    @ResId(R.layout.list_item_message_detail_comment)
    private class ViewHolder implements  Bindable<Circle.CircleReply>
    {

        @ResId(R.id.commenter)
        TextView commenter;

        @ResId(R.id.commentTo)
        TextView commentTo;

        @ResId(R.id.commentContent)
        TextView commentContent;

        @ResId(R.id.userPhoto)
        ImageView userPhoto;

        @ResId(R.id.time)
        TextView time;

        @ResId(R.id.replyHint)
        View replyHint;

        @ResId(R.id.replyHint1)
        View replyHint1;

        @Override
        public void bindData(AbstractAdapter<Circle.CircleReply> adapter, Circle.CircleReply data, int position) {


            ImageLoader.getInstance().displayImage(data.getHeadImg(),userPhoto, ImageLoaderHelper.getPortraitDisplayOptions());

            time.setText(TimeUtils.getReplyTimeMessage(data.getInsertTime()));
            //ImageLoader.getInstance().displayImage(data.getReplyUserHeadImg(),userPhoto,ImageLoaderHelper.getPortraitDisplayOptions());
            commenter.setText(String.valueOf(data.getUserNickName()));
            commentTo.setText(String.valueOf(data.getReplyUserNickName()));
            commentContent.setText(data.getContent());

            replyHint.setVisibility(message.getUserId() == data.getReplyUserId() ? View.GONE : View.VISIBLE);
            replyHint1.setVisibility(message.getUserId() == data.getReplyUserId() ? View.GONE : View.VISIBLE);
            commentTo.setVisibility(message.getUserId()==data.getReplyUserId()?View.GONE: View.VISIBLE);
        }
    }




}
