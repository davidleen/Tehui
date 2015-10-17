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
public class SetBankAdapter extends AbstractAdapter<User.UserSetBank> {


    List<User.UserSetBank > selectedBanks =new ArrayList<>();

    @Inject
    public SetBankAdapter(Context context) {
        super(context);
    }

    @Override
    public void setDataArray(List<User.UserSetBank> arrayData) {
        super.setDataArray(arrayData);

        selectedBanks.clear();
        for (User.UserSetBank setBank:arrayData)
        {
            if(setBank.getIsFollow()== Common.BooleanValue.BooleanValue_TRUE)
                 selectedBanks.add(setBank );
        }
    }


    public List<User.UserSetBank> getSelectedBanks() {
        return selectedBanks;
    }

    @Override
    protected UnMixable createViewHolder(int itemViewType) {
        return new ViewHolder();
    }

    public void onClick(User.UserSetBank setBank) {


        if(selectedBanks.indexOf(setBank)>-1)
        {
            selectedBanks.remove(setBank);
        }else
        {
            selectedBanks.add(setBank);
        }


    }

    @ResId(R.layout.list_item_bank_set)
    public    class ViewHolder implements  Bindable<User.UserSetBank>
    {

        @ResId(R.id.bankset)
      protected   TextView bankset;

        @Override
        public void bindData(AbstractAdapter<User.UserSetBank> adapter, User.UserSetBank data, int position) {


            bankset.setText(data.getBankName().trim());
            bankset.setSelected(selectedBanks.indexOf(data )>-1);


        }
    }
}
