package com.davidleen29.tehui.adapters;


import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.lang.ResId;
import com.davidleen29.tehui.lang.UnMixable;
import com.davidleen29.tehui.utils.TimeUtils;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Merchant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 商户列表列表项目
 */


public class StringMultiAdapter extends AbstractAdapter<String> {



    public Set<Integer> selectedItems=new HashSet<>();



    @Inject
    public StringMultiAdapter(Context context) {
        super(context);
    }

    @Override
    protected UnMixable createViewHolder(int itemViewType) {
        return new ViewHolder();
    }




    @ResId(R.layout.list_item_string_multi)
    private class ViewHolder implements  Bindable<String>
    {

        @ResId(R.id.condition)
        TextView condition;
        @ResId(R.id.checkBox)
        View checkBox;




        @Override
        public void bindData(AbstractAdapter<String> adapter, String data, int position) {

            condition.setText(data);

            checkBox.setSelected(selectedItems.contains(position));

        }
    }


    public void onItemClick(int position)
    {


        if(selectedItems.contains(position))
        {
            selectedItems.remove(position);
        }else
            selectedItems.add(position);
        notifyDataSetChanged();
    }
}
