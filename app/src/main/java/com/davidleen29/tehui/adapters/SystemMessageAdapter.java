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
import com.huiyou.dp.service.protocol.Common;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 我的卡包适配类
 */
public class SystemMessageAdapter extends AbstractAdapter<Circle.SystemMessage> {





    @Inject
    public SystemMessageAdapter(Context context) {
        super(context);
    }

    @Override
    protected UnMixable createViewHolder(int itemViewType) {
        return new ViewHolder();
    }




    @ResId(R.layout.list_item_system_message)
    private class ViewHolder implements  Bindable<Circle.SystemMessage>
    {






        @ResId(R.id.time)
        TextView time;
        @ResId(R.id.new_system_message)
        View new_system_message;

        @ResId(R.id.content)
        TextView content;







        @Override
        public void bindData(AbstractAdapter<Circle.SystemMessage> adapter, final Circle.SystemMessage data, int position) {



            new_system_message.setVisibility(data.getIsRead().equals(Common.BooleanValue.BooleanValue_TRUE)? View.INVISIBLE:View.VISIBLE);

            time.setText(TimeUtils.getReplyTimeMessage(data.getInsertTime()));
            content.setText(data.getContent());

        }
    }


}
