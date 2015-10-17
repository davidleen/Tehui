package com.davidleen29.tehui.adapters;

import android.content.Context;
import android.widget.TextView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.lang.ResId;
import com.davidleen29.tehui.lang.UnMixable;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Common;
import com.huiyou.dp.service.protocol.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidleen29 on 2015/7/19.
 */
public class SetCategoryAdapter extends AbstractAdapter<User.UserSetCategory> {


    List<User.UserSetCategory > selectedCategorys =new ArrayList<>();

    @Inject
    public SetCategoryAdapter(Context context) {
        super(context);
    }

    @Override
    public void setDataArray(List<User.UserSetCategory> arrayData) {
        super.setDataArray(arrayData);

        selectedCategorys.clear();
        for (User.UserSetCategory category:arrayData)
        {
            if(category.getIsFollow()== Common.BooleanValue.BooleanValue_TRUE)
                selectedCategorys.add(category );
        }
    }


    public List<User.UserSetCategory> getSelectedCategorys() {
        return selectedCategorys;
    }

    @Override
    protected UnMixable createViewHolder(int itemViewType) {
        return new ViewHolder();
    }

    public void onClick(User.UserSetCategory setCategory) {


        if(selectedCategorys.indexOf(setCategory)>-1)
        {
            selectedCategorys.remove(setCategory);
        }else
        {
            selectedCategorys.add(setCategory);
        }


    }

    @ResId(R.layout.list_item_category_set)
    public    class ViewHolder implements  Bindable<User.UserSetCategory>
    {

        @ResId(R.id.categorySet)
        TextView categorySet;

        @Override
        public void bindData(AbstractAdapter<User.UserSetCategory> adapter, User.UserSetCategory data, int position) {


            categorySet.setText(data.getCategoryName().trim());
            categorySet.setSelected(selectedCategorys.indexOf(data)>-1);


        }
    }
}
