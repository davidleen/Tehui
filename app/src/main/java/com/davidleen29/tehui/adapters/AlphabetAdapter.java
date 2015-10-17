package com.davidleen29.tehui.adapters;


import android.content.Context;
import android.view.View;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.lang.ResId;
import com.davidleen29.tehui.lang.UnMixable;
import com.davidleen29.tehui.utils.ArrayUtils;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.City;

import java.util.ArrayList;
import java.util.List;

/**
 * 字幕适配
 */


public class AlphabetAdapter extends AbstractAdapter<String>   {

    private int selectItem;

    @Inject
    public AlphabetAdapter(Context context) {
        super(context);
    }

    @Override
    protected UnMixable createViewHolder(int itemViewType) {
        return new ViewHolder();
    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
        notifyDataSetChanged();
    }

    @ResId(R.layout.list_item_alphabet)
    private class ViewHolder implements  Bindable<String>
    {

        @ResId(R.id.contentView)
        TextView contentView;



        @Override
        public void bindData(AbstractAdapter<String> adapter, String data, int position) {

            contentView.setText(data);
            contentView.setSelected(selectItem==position);


        }
    }



}
