package com.davidleen29.tehui.adapters;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.api.HttpUrl;
import com.davidleen29.tehui.lang.ResId;
import com.davidleen29.tehui.lang.UnMixable;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Merchant;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 商户列表列表项目
 */


public class PictureAdapter extends AbstractAdapter<String> {
    @Inject
    public PictureAdapter(Context context) {
        super(context);
    }

    @Override
    protected UnMixable createViewHolder(int itemViewType) {
        return new ViewHolder();
    }




    @ResId(R.layout.list_item_picture)
    private class ViewHolder implements  Bindable<String>
    {


        @Override
        public void bindData(AbstractAdapter<String> adapter, String data, int position) {



        }
    }
}
