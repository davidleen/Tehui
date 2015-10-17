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
 * 类型简单列表项目
 */


public class CategoryConditionAdapter extends AbstractAdapter<User.UserSetCategory> {

    private User.UserSetCategory selectedItem;
    @Inject
    public CategoryConditionAdapter(Context context) {
        super(context);
    }

    @Override
    protected UnMixable createViewHolder(int itemViewType) {
        return new ViewHolder();
    }




    @ResId(R.layout.list_item_condition)
    private class ViewHolder implements  Bindable<User.UserSetCategory>
    {

        @ResId(R.id.condition)
        TextView contentView;

        @ResId(R.id.checkBox)
        View checkBox;

        @Override
        public void bindData(AbstractAdapter<User.UserSetCategory> adapter, User.UserSetCategory data, int position) {
            
            contentView.setText(data.getCategoryName().trim());
            boolean selected=data.equals(selectedItem);
            contentView.setSelected(selected);
            checkBox.setSelected(selected) ;
        }
    }



    public void setSelectedItem(User.UserSetCategory item)
    {
        selectedItem=item;
    }
}
