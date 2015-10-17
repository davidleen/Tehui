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
public class SetBankUnConcernAdapter extends SetBankAdapter  {

    @Inject
    public SetBankUnConcernAdapter(Context context) {
        super(context);
    }


    @Override
    protected UnMixable createViewHolder(int itemViewType) {
        return new ViewHolder();
    }

    @ResId(R.layout.list_item_bank_set)
    public    class ViewHolder implements  Bindable<User.UserSetBank>
    {

        @ResId(R.id.bankset)
      protected   TextView bankset;

        @Override
        public void bindData(AbstractAdapter<User.UserSetBank> adapter, User.UserSetBank data, int position) {


            bankset.setText(data.getBankName().trim());
            bankset.setSelected(false);


        }
    }
}
