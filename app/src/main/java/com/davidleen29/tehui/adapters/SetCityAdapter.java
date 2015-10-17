package com.davidleen29.tehui.adapters;

import android.content.Context;
import android.widget.TextView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.lang.ResId;
import com.davidleen29.tehui.lang.UnMixable;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.City;
import com.huiyou.dp.service.protocol.Common;
import com.huiyou.dp.service.protocol.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidleen29 on 2015/7/19.
 */
public class SetCityAdapter extends AbstractAdapter<City.CityInfo> {



    City.CityInfo setCity=null;

    @Inject
    public SetCityAdapter(Context context) {
        super(context);
    }






    @Override
    protected UnMixable createViewHolder(int itemViewType) {
        return new ViewHolder();
    }

    public void onClick(City.CityInfo cityInfo) {

        setCity=  cityInfo ;





    }

    @ResId(R.layout.list_item_city_set)
    public    class ViewHolder implements  Bindable<City.CityInfo>
    {

        @ResId(R.id.city)
        TextView city;

        @Override
        public void bindData(AbstractAdapter<City.CityInfo> adapter, City.CityInfo data, int position) {


            city.setText(data.getName());


        }
    }
}
