package com.davidleen29.tehui.adapters;


import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.lang.ResId;
import com.davidleen29.tehui.lang.UnMixable;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Circle;
import com.huiyou.dp.service.protocol.User;

/**
 * 银行简单列表项目
 */


public class MessageCommentAdapter extends AbstractAdapter<Circle.CircleReply> {



    private Circle.CircleMessage message;
    @Inject
    public MessageCommentAdapter(Context context) {
        super(context);
    }

    @Override
    protected UnMixable createViewHolder(int itemViewType) {
        return new ViewHolder();
    }




    @ResId(R.layout.list_item_message_comment)
    private class ViewHolder implements  Bindable<Circle.CircleReply>
    {

        @ResId(R.id.commenter)
        TextView commenter;

        @ResId(R.id.commentTo)
        TextView commentTo;

        @ResId(R.id.commentContent)
        TextView commentContent;

        @Override
        public void bindData(AbstractAdapter<Circle.CircleReply> adapter, Circle.CircleReply data, int position) {

            commenter.setText(String.valueOf(data.getReplyUserNickName()));
            commentTo.setText(String.valueOf(data.getUserNickName()));
            commentContent.setText(data.getContent());


         //   boolean isCommentToMessageOwner=false;

//            commentTo.setVisibility(message==);

        }
    }


    public void setMessage(Circle.CircleMessage message) {
        this.message = message;
    }
}
