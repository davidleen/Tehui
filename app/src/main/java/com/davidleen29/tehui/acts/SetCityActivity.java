package com.davidleen29.tehui.acts;

import android.content.Context;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.adapters.AlphabetAdapter;
import com.davidleen29.tehui.adapters.CityListAdapter;
import com.davidleen29.tehui.adapters.SetCityAdapter;
import com.davidleen29.tehui.api.ApiManager;
import com.davidleen29.tehui.events.CitySetEvent;
import com.davidleen29.tehui.helper.CacheManager;
import com.davidleen29.tehui.lang.CException;
import com.davidleen29.tehui.tasks.SetCityTask;
import com.davidleen29.tehui.tasks.ThTask;
import com.davidleen29.tehui.utils.ArrayUtils;
import com.davidleen29.tehui.utils.Log;
import com.davidleen29.tehui.utils.StringUtils;
import com.davidleen29.tehui.utils.ToastUtils;
import com.davidleen29.tehui.widget.HeaderView;
import com.davidleen29.tehui.widget.LinearListView;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightGridView;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.City;
import com.huiyou.dp.service.protocol.Common;
import com.huiyou.dp.service.protocol.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import de.greenrobot.event.EventBus;
import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;


@ContentView(R.layout.activity_set_city)
public class SetCityActivity extends SimpleActivity<City.GetCitysResponse> {


    public static final String PARAM_CITY="PARAM_CITY";


    @InjectExtra(value = PARAM_CITY,optional = true)
    City.CityInfo city;


    @InjectView(R.id.header)
    HeaderView header;


    ExpandableHeightGridView siteCity;

    ExpandableHeightGridView hotCitys;

    @Inject
    SetCityAdapter siteAdapter;



    @Inject
    SetCityAdapter hotAdapter;



    @Inject
    CacheManager cacheManager;



    @InjectView(R.id.listView)
    ListView listView;





    @Inject
    AlphabetAdapter alphabetAdapter;


    @Inject
    CityListAdapter cityListAdapter;

    @InjectView(R.id.alphabetList)
    LinearListView alphabetList;

    @Inject
    ApiManager apiManager;
    @Override
    protected void init(Bundle bundle) {

            super.init(bundle);



        View  listHeader= LayoutInflater.from(this).inflate(R.layout.list_header_set_city,null);
        listView.addHeaderView(listHeader);
        listView.setAdapter(cityListAdapter);

        hotCitys= (ExpandableHeightGridView)listHeader.findViewById(R.id.hotCity);
        hotCitys.setExpanded(true);

        siteCity= (ExpandableHeightGridView)listHeader.findViewById(R.id.city);
        siteCity.setExpanded(true);
        //clear the selector
        hotCitys.setSelector(new StateListDrawable());
        siteCity.setSelector(new StateListDrawable());

        hotCitys.setAdapter(hotAdapter);
        siteCity.setAdapter(siteAdapter);
        alphabetList.setAdapter(alphabetAdapter);


        alphabetList.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView parent, View v, int position, long id) {

                int sectionIdex = position;
              //  alphabetAdapter.setSelectItem(position);
                int cityListPosition = cityListAdapter.getPositionForSection(sectionIdex);

                listView.setSelection(cityListPosition + 1);
            }
        });


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                int position=firstVisibleItem;
                int sectionIndex=cityListAdapter.getSectionForPosition(position);
                Log.d(TAG,"sectionIndex:"+sectionIndex+",position:"+position+",firstVisibleItem:"+firstVisibleItem);

                alphabetAdapter.setSelectItem(sectionIndex  );


            }
        });

        if(city!=null) {
            siteAdapter.addItem(city);
            header.setTitle("当前城市" + (city==null? "" : ("-" + city.getName())));
        }



        siteCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setCity(siteAdapter.getItem(position));
            }
        });


        hotCitys.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                setCity(hotAdapter.getItem(position));
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


              City.CityInfo cityInfo= (City.CityInfo) parent.getItemAtPosition(position);
                setCity(cityInfo);

            }
        });


    }

    private void setCity(final City.CityInfo item) {


      new   ThTask<City.UserSetCityResponse>(this) {

            @Inject
            ApiManager apiManager;




            @Override
            protected void doOnSuccess(City.UserSetCityResponse data) {

                if(data.getErrCode()==City.UserSetCityResponse.ErrorCode.ERR_OK)
                {


                    EventBus.getDefault().post( new CitySetEvent());
                    finish();


                }else
                {
                    ToastUtils.show(data.getErrMsg());
                }

            }

            @Override
            public City.UserSetCityResponse call() throws Exception {
                return apiManager.userSetCity(item );
            }


    }.execute();

    }

    @Override
    protected City.GetCitysResponse readOnBackground() throws CException {

     return    apiManager.getCities();


    }

    @Override
    protected void onDataLoaded(City.GetCitysResponse data) {


        if(data.getErrCode()== City.GetCitysResponse.ErrorCode.ERR_OK)
        {
            cacheManager.setCityInfos(data);

            List<City.CityInfo> hotCitys=new ArrayList<>();
            for(City.CityInfo info:data.getCityInfoList())
            {
                if(info.getIsHot()== Common.BooleanValue.BooleanValue_TRUE)
                {
                    hotCitys.add(info);
                }
            }

            hotAdapter.setDataArray(hotCitys);


            List<City.CityInfo> allCitys=new ArrayList<>();

            allCitys.addAll(data.getCityInfoList());


            ArrayUtils.SortList(allCitys, new Comparator<City.CityInfo>() {
                @Override
                public int compare(City.CityInfo lhs, City.CityInfo rhs) {
                    return lhs.getFirstLetter().compareTo(rhs.getFirstLetter());
                }
            });

            cityListAdapter.setDataArray(allCitys);

            alphabetAdapter.setDataArray(ArrayUtils.changeArrayToList(cityListAdapter.getSectionString()));
        }else
        {
            ToastUtils.show(data.getErrMsg());
        }


    }


    @Override
    public void onBackPressed() {

        if(cacheManager.getCurrentCity()==null)
        {

            ToastUtils.show("城市必须设置.");

        }else

        super.onBackPressed();
    }
}
