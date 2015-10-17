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
import com.huiyou.dp.service.protocol.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 城市列表列表项目
 */


public class CityListAdapter extends AbstractAdapter<City.CityInfo>  implements SectionIndexer {



    String[] sectionString;
    Integer[] section;
    @Inject
    public CityListAdapter(Context context) {
        super(context);
    }


    @Override
    public void setDataArray(List<City.CityInfo> arrayData) {

        super.setDataArray(arrayData);
        List<Integer> sessionList=new ArrayList<>();
        int size=arrayData.size();
        String formerSection="";
        for(int i=0;i<size;i++)
        {
            City.CityInfo cityInfo=arrayData.get(i);

            if(!formerSection.equals(cityInfo.getFirstLetter()))
            {
                sessionList.add(i);
                formerSection=cityInfo.getFirstLetter();
            }
        }

        int leng=sessionList.size();
        section=new Integer[leng];
        for (int i = 0; i < leng; i++) {
            section[i]=sessionList.get(i);
        }


        sectionString=new String[leng];
        for (int i = 0; i < leng; i++) {
            sectionString[i]=getItem(section[i]).getFirstLetter();
        }



    }


    public String[] getSectionString() {
        return sectionString;
    }

    @Override
    protected UnMixable createViewHolder(int itemViewType) {
        return new ViewHolder();
    }




    @Override
    public Object[] getSections() {
        return section;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {


        return section[sectionIndex];
    }

    @Override
    public int getSectionForPosition(int position) {
        if(section==null ) return -1;

        for (int i = 0; i < section.length; i++) {

            if(section[i]>=position)
            {
                return i-1;
            }
        }
        return -1;
    }


    @ResId(R.layout.list_item_city_list)
    private class ViewHolder implements  Bindable<City.CityInfo>
    {


        @ResId(R.id.name)
        TextView name;

        @ResId(R.id.sesstion)
        TextView sesstion;

        @Override
        public void bindData(AbstractAdapter<City.CityInfo> adapter, City.CityInfo data, int position) {

            sesstion.setText(data.getFirstLetter() );
            name.setText(data.getName());
            sesstion.setVisibility(position==0||!data.getFirstLetter().equals(getItem(position-1).getFirstLetter())?View.VISIBLE:View.GONE);


        }
    }



}
