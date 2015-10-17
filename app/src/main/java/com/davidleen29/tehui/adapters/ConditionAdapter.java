package com.davidleen29.tehui.adapters;


import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.lang.ResId;
import com.davidleen29.tehui.lang.UnMixable;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.User;

/**
 * 商户列表列表项目
 */


public class ConditionAdapter extends AbstractAdapter<String> {

    private String selectedItem;
    @Inject
    public ConditionAdapter(Context context) {
        super(context);
    }

    @Override
    protected UnMixable createViewHolder(int itemViewType) {
        return new ViewHolder();
    }


    User.UserSetBank bank;


    @ResId(R.layout.list_item_condition)
    private class ViewHolder implements  Bindable<String>
    {

        @ResId(R.id.condition)
        TextView contentView;

        @ResId(R.id.checkBox)
        View checkBox;

        @Override
        public void bindData(AbstractAdapter<String> adapter, String data, int position) {

            contentView.setText(data);
            boolean selected=data.equals(selectedItem);
            contentView.setSelected(selected);
            checkBox.setSelected(selected) ;
        }
    }



    public void setSelectedItem(String item)
    {
        selectedItem=item;
    }
}
