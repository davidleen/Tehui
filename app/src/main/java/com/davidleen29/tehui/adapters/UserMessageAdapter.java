package com.davidleen29.tehui.adapters;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.helper.ImageLoaderHelper;
import com.davidleen29.tehui.lang.ResId;
import com.davidleen29.tehui.lang.UnMixable;
import com.davidleen29.tehui.utils.StringUtils;
import com.davidleen29.tehui.utils.TimeUtils;
import com.davidleen29.tehui.widget.LinearListView;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Circle;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 个人消息列表项目
 */


public class UserMessageAdapter extends AbstractAdapter<Circle.CircleMessage> {



    @Inject
    public UserMessageAdapter(Context context ) {
        super(context);

    }


    @Override
    protected UnMixable createViewHolder(int itemViewType) {
        return new ViewHolder();
    }




    @ResId(R.layout.list_item_user_message)
    private class ViewHolder implements  Bindable<Circle.CircleMessage>
    {


        @ResId(R.id.day)
        TextView day;




        @ResId(R.id.replyCount)
        TextView replyCount;

        @ResId(R.id.content)
        TextView content;

        @ResId(R.id.time)
        TextView time;

        @ResId(R.id.picture)
        ImageView picture;
        @ResId(R.id.comment)
        View comment;
        @ResId(R.id.ll_comment)
        LinearListView ll_comment;



        @Override
        public void bindData(AbstractAdapter<Circle.CircleMessage> adapter, Circle.CircleMessage data, int position) {

                day.setText(TimeUtils.getSendDayMessage(data.getInsertTime()));
            replyCount.setText("回复("+data.getCircleReplyCount()+")");

                content.setText(data.getContent());

            time.setText(TimeUtils.getReplyTimeMessage(data.getInsertTime()));

           boolean hasPicture=!StringUtils.isEmpty(data.getImgUrl());

            if(hasPicture){
                ImageLoader.getInstance().displayImage( data.getImgUrl(),picture);
            }

            picture.setVisibility(hasPicture ? View.VISIBLE : View.GONE);

            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


            List<Circle.CircleReply> replies= data.getCircleReplyList();
            boolean noReply=replies == null || replies.size() == 0;
            ll_comment.setVisibility(noReply ? View.GONE : View.VISIBLE);
            if(!noReply)
            {
                if(ll_comment.getmAdapter()==null)
                    ll_comment.setAdapter(new MessageCommentAdapter(getContext()));
                AbstractAdapter<Circle.CircleReply> commentAdapter= (AbstractAdapter<Circle.CircleReply>) ll_comment.getmAdapter();
                commentAdapter.setDataArray(data.getCircleReplyList());

            }

        }
    }


}
