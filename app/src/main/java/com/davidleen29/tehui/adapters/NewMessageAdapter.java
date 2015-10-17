package com.davidleen29.tehui.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.acts.QRActivity;
import com.davidleen29.tehui.helper.ImageLoaderHelper;
import com.davidleen29.tehui.lang.ResId;
import com.davidleen29.tehui.lang.UnMixable;
import com.davidleen29.tehui.utils.TimeUtils;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Circle;
import com.huiyou.dp.service.protocol.Common;
import com.huiyou.dp.service.protocol.User;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 我的卡包适配类
 */
public class NewMessageAdapter extends AbstractAdapter<Circle.CircleReply> {





    @Inject
    public NewMessageAdapter(Context context) {
        super(context);
    }

    @Override
    protected UnMixable createViewHolder(int itemViewType) {
        return new ViewHolder();
    }




    @ResId(R.layout.list_item_new_message)
    private class ViewHolder implements  Bindable<Circle.CircleReply>
    {






        @ResId(R.id.userPhoto)
        ImageView userPhoto;

        @ResId(R.id.content)
        TextView content;

        @ResId(R.id.time)
        TextView time;


        @ResId(R.id.commenter)
        TextView commenter;



        @ResId(R.id.message)
        ImageView message;





        @Override
        public void bindData(AbstractAdapter<Circle.CircleReply> adapter, final Circle.CircleReply data, int position) {



            commenter.setText(data.getUserNickName());

            time.setText( TimeUtils.getReplyTimeMessage(data.getInsertTime()));
            content.setText(data.getContent());

           ImageLoader.getInstance().displayImage(data.getReplyUserHeadImg(), userPhoto, ImageLoaderHelper.getPortraitDisplayOptions());

            ImageLoader.getInstance().displayImage( data.getMessageImg(),message, ImageLoaderHelper.getMerchantDisplayOption());
           // message.setText(data.get);


        }
    }


}
