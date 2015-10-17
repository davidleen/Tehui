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
 * 银行简单列表项目
 */


public class BankConditionAdapter extends AbstractAdapter<User.UserSetBank> {

    private User.UserSetBank selectedItem;
    @Inject
    public BankConditionAdapter(Context context) {
        super(context);
    }

    @Override
    protected UnMixable createViewHolder(int itemViewType) {
        return new ViewHolder();
    }




    @ResId(R.layout.list_item_condition)
    private class ViewHolder implements  Bindable<User.UserSetBank>
    {

        @ResId(R.id.condition)
        TextView contentView;

        @ResId(R.id.checkBox)
        View checkBox;

        @Override
        public void bindData(AbstractAdapter<User.UserSetBank> adapter, User.UserSetBank data, int position) {
            
            contentView.setText(data.getBankName().trim());
            boolean selected=data.equals(selectedItem);
            contentView.setSelected(selected);
            checkBox.setSelected(selected) ;
        }
    }



    public void setSelectedItem(User.UserSetBank item)
    {
        selectedItem=item;
    }
}
