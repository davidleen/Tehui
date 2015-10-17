package com.davidleen29.tehui.frags;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.baidu.mapapi.map.SupportMapFragment;
import com.davidleen29.tehui.R;
import com.davidleen29.tehui.acts.MerchantSearchActivity;
import com.davidleen29.tehui.acts.MerchantDetailActivity;
import com.davidleen29.tehui.adapters.BankConditionAdapter;
import com.davidleen29.tehui.adapters.CategoryConditionAdapter;
import com.davidleen29.tehui.adapters.ConditionAdapter;
import com.davidleen29.tehui.adapters.ShopInfoAdapter;
import com.davidleen29.tehui.api.ApiManager;
import com.davidleen29.tehui.events.AppInitCompleteEvent;
import com.davidleen29.tehui.events.CitySetEvent;
import com.davidleen29.tehui.events.ConcernBankChangedEvent;
import com.davidleen29.tehui.events.ConcernCategoryChangedEvent;
import com.davidleen29.tehui.events.LocationInfo;
import com.davidleen29.tehui.events.MerchantUpdateEvent;
import com.davidleen29.tehui.events.SetCurrentCityEvent;
import com.davidleen29.tehui.helper.CacheManager;
import com.davidleen29.tehui.helper.FootViewHelper;
import com.davidleen29.tehui.tasks.ThTask;
import com.davidleen29.tehui.helper.AMapHelper;
import com.davidleen29.tehui.utils.ArrayUtils;
import com.davidleen29.tehui.utils.ToastUtils;
import com.davidleen29.tehui.widget.HeaderView;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Common;
import com.huiyou.dp.service.protocol.Merchant;
import com.huiyou.dp.service.protocol.User;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import roboguice.inject.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {




    private static final int REQUEST_SEARCH_MERCHANT=1000;
     @Inject
     ApiManager apiManager;
    @InjectView(R.id.listView)
    ListView listView;


    @InjectView(R.id.list_conditions)
    View list_conditions;

    @InjectView(R.id.list_sites)
    ListView list_order;

    @InjectView(R.id.list_banks)
    ListView list_banks;


    @InjectView(R.id.list_categorys)
    ListView list_catogery;

    @InjectView(R.id.map_panel)
    FrameLayout map_panel;



    @InjectView(R.id.swipeView)
    SwipeRefreshLayout swipeView;


    @InjectView(R.id.header)
    HeaderView header;


    @Inject
    ShopInfoAdapter adapter;

    @InjectView(R.id.tab_bank)
    TextView tab_bank;
    @InjectView(R.id.tab_type)
    TextView tab_type;
    @InjectView(R.id.tab_site)
    TextView tab_site;


    AMapHelper aMapHelper;
    TextView[] tabs;

    SupportMapFragment fragment;

    @Inject
    CacheManager cacheManager;



    static List<String> orderList;
    static{
        String[] temp=new String[]{"离我最近","热门商户"  };
        orderList = ArrayUtils.changeArrayToList(temp);
    }
    @Inject
    ConditionAdapter conditionAdapter;
    @Inject
    BankConditionAdapter bankConditionAdapter;

    @Inject
    CategoryConditionAdapter categoryConditionAdapter;


    private FootViewHelper footViewHelper;

    private static User.UserSetBank ALL_BANK= User.UserSetBank.newBuilder().setBankId(-1).setBankName("所有银行").build();
    private static User.UserSetCategory ALL_CATEGORY= User.UserSetCategory.newBuilder().setCategoryId(-1).setCategoryName("所有类型").build();

    private User.UserSetBank selectedBank=ALL_BANK;
    private User.UserSetCategory selectCategory=ALL_CATEGORY;
    private int selectedOrder=0;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_main, container, false);
        return v;
    }



    @Override
    public void onDestroyView() {

//        FragmentTransaction transaction= getChildFragmentManager().beginTransaction();
//        transaction.remove(fragment);
//
//        transaction.commit();
//        fragment=null;

        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();

        fragment.onResume();

    }

    @Override
    public void onPause() {

//        FragmentTransaction transaction= getChildFragmentManager().beginTransaction();
//        transaction.remove(fragment);
//
//        transaction.commit();
//        fragment=null;
        fragment.onPause();
        super.onPause();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        setHeaderViewTitle();
        swipeView.setOnRefreshListener(this);
        swipeView.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        tabs=new TextView[]{tab_bank,tab_type,tab_site};

        for (int i = 0,length=tabs.length; i < length; i++) {
            tabs[i].setOnClickListener(this);
        }



        adapter.setOnMapIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Merchant.MerchantInfo merchantInfo= (Merchant.MerchantInfo) v.getTag();
                aMapHelper.translateTo(merchantInfo);
                //切换到地图模式
                header.performRight1Click();

            }
        });


        footViewHelper=new FootViewHelper(getActivity(), new FootViewHelper.PageListener() {
            @Override
            public void onPageChanged(int newPageIndex, int pageCount) {
                loadData(newPageIndex,pageCount);
            }
        });
        listView.addFooterView(footViewHelper.getView());
        listView.setAdapter(adapter);


        list_banks.setAdapter(bankConditionAdapter);
        list_catogery.setAdapter(categoryConditionAdapter);
        list_order.setAdapter(conditionAdapter);


        list_banks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                hideCondition();
                selectedBank = (User.UserSetBank) parent.getItemAtPosition(position);
                bankConditionAdapter.setSelectedItem(selectedBank);
                bankConditionAdapter.notifyDataSetChanged();
                tabs[0].setText(selectedBank.getBankName().trim());
                if (tabs[0].isSelected())
                    tabs[0].setSelected(false);

                loadData();
            }
        });
        list_catogery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hideCondition();
                selectCategory = (User.UserSetCategory) parent.getItemAtPosition(position);
                categoryConditionAdapter.setSelectedItem(selectCategory);
                categoryConditionAdapter.notifyDataSetChanged();
                tabs[1].setText(selectCategory.getCategoryName().trim());
                if(tabs[1].isSelected())
                    tabs[1].setSelected(false);
                loadData();
            }
        });
        list_order.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                hideCondition();
                selectedOrder=position;

                tabs[2].setText(String.valueOf(parent.getItemAtPosition(position)));
                conditionAdapter.setSelectedItem(tabs[2].getText().toString());
                conditionAdapter.notifyDataSetChanged();
                if (tabs[2].isSelected())
                    tabs[2].setSelected(false);
                loadData();
            }
        });





        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(getActivity(), MerchantDetailActivity.class);
                intent.putExtra(MerchantDetailActivity.EXTRA_MERCHANTINFO_ID, adapter.getItem(position).getId());
                startActivity(intent);
            }
        });


        header.setOnRight1ClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean selected = v.isSelected();
                v.setSelected(!selected);
                swipeView.setVisibility(!selected ? View.GONE : View.VISIBLE);
                map_panel.setVisibility(selected ? View.GONE : View.VISIBLE);


//                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//                if (selected)
//                    transaction.remove(fragment);
//                else
//                    transaction.replace(R.id.map_panel, fragment);
//                transaction.commit();


            }
        });
        header.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), MerchantSearchActivity.class);
                startActivityForResult(intent, REQUEST_SEARCH_MERCHANT);

            }
        });


        header.setHeadTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                EventBus.getDefault().post(new SetCurrentCityEvent());

            }
        });






        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {




                if(firstVisibleItem+visibleItemCount>=totalItemCount )

                    footViewHelper.becomeVisible();

            }
        });





        FragmentTransaction transaction= getChildFragmentManager().beginTransaction();
        fragment  =SupportMapFragment.newInstance();
        transaction.replace(R.id.map_panel, fragment);
        transaction.commit();



        initBankData();
        initCategoryData();
        initSiteData();

        if(cacheManager.hasInit())
        loadData();


    }
    private void loadData()
    {
        loadData(0, 10);

    }
    private void loadData(final int pageIndex, final int pageCount)
    {



       final List<Integer> bankIds=new ArrayList<>();
        if(selectedBank.getBankId()==-1) {
            if(cacheManager.getConcernBank()!=null&&cacheManager.getConcernBank().getUserSetBankList()!=null) {
                for (User.UserSetBank setBank : cacheManager.getConcernBank().getUserSetBankList()) {
                    if (setBank.getBankId() != -1) {
                        bankIds.add((int) setBank.getBankId());
                    }
                }
            }
        }else
        {
            bankIds.add((int) selectedBank.getBankId());
        }

        final LocationInfo event=cacheManager.getLocationInfo();
        final Merchant.GetMerchantsByCoordinateRequest.ResultOrderField orderField;
        final   Merchant.GetMerchantsByCoordinateRequest.ResultOrderType orderType;
        if(selectedOrder==0)
        {
            orderField=Merchant.GetMerchantsByCoordinateRequest.ResultOrderField.BY_DISTANCE;
           orderType=   Merchant.GetMerchantsByCoordinateRequest.ResultOrderType.ASC;

        }else
        {

             orderField=Merchant.GetMerchantsByCoordinateRequest.ResultOrderField.BY_HOT;
             orderType=   Merchant.GetMerchantsByCoordinateRequest.ResultOrderType.DESC;
        }

        new ThTask<Merchant.GetMerchantsByCoordinateResponse>(getActivity(),true) {



            @Override
            public Merchant.GetMerchantsByCoordinateResponse call() throws Exception {
               return apiManager.getMerchantsByCoordinate(selectCategory.getCategoryId(), bankIds, orderField,orderType,event==null?0:event.x,event==null?0:event.y, pageIndex, pageCount);
            }

            @Override
            protected void doOnSuccess(Merchant.GetMerchantsByCoordinateResponse data) {


                if(swipeView.isRefreshing())
                    swipeView.setRefreshing(false);

                if(!Merchant.GetMerchantsByCoordinateResponse.ErrorCode.ERR_OK.equals(data.getErrCode()) )
                {

                    switch(data.getErrCode().getNumber())
                    {
                        case Merchant.GetMerchantsByCoordinateResponse.ErrorCode.ERR_REQUEST_INVALID_VALUE:

                            ToastUtils.show("请求参数异常");
                            break;
                        default:
                        ToastUtils.show("未知错误");
                        break;
                    }


                }else{




                    footViewHelper.setInfo(pageIndex,pageCount,data.getTotalCount());

                    if(pageIndex==0)
                    adapter.setDataArray(data.getMerchantInfosList() );
                    else
                        adapter.addDataArray(data.getMerchantInfosList());



                    updateMap(adapter.getDatas());
                    if(orderField.equals(    Merchant.GetMerchantsByCoordinateRequest.ResultOrderField.BY_DISTANCE))
                    {
                        aMapHelper.moveToCurrentPosition();
                    }


                }



            }
        }.execute();
        swipeView.setRefreshing(true);

    }


    /**
     * 更新地图展示坐标
     * @param merchantInfoList
     */
    private void updateMap(List<Merchant.MerchantInfo> merchantInfoList) {

        if(aMapHelper==null)
        {
            aMapHelper=new AMapHelper(getActivity(),fragment.getBaiduMap());
        }
        try {
            aMapHelper.updateMap(merchantInfoList);
        }catch (Throwable t)
        {

            Log.w(TAG,t.getMessage());
        }


    }


    @Override
    protected void onViewClick(View v, int id) {


        switch (id)
        {
            case R.id.tab_bank:
                list_banks.setVisibility(View.VISIBLE);
                list_order.setVisibility(View.GONE);
                list_catogery.setVisibility(View.GONE);

                handleTabClick(v);
                break;
            case R.id.tab_type:

                list_banks.setVisibility(View.GONE);
                list_order.setVisibility(View.GONE);
                list_catogery.setVisibility(View.VISIBLE);

                handleTabClick(v);
                break;
            case R.id.tab_site:
                list_banks.setVisibility(View.GONE);
                list_order.setVisibility(View.VISIBLE);
                list_catogery.setVisibility(View.GONE);
                handleTabClick(v);
                break;

        }


    }


    private void handleTabClick(View v)
    {
        for(View tab:tabs)
        {

            if(tab==v)
            {

                if(tab.isSelected())
                {
                    hideCondition();
                }else
                {
                    showCondition();
                }
                tab.setSelected(!tab.isSelected());


            } else

                tab.setSelected(tab==v);
        }



    }

    private void hideCondition()
    {
        list_conditions.setVisibility(View.GONE);
        list_conditions.clearAnimation();
        list_conditions.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.collapse_bottom));

    }
    private void showCondition()
    {

        list_conditions.setVisibility(View.VISIBLE);
        list_conditions.clearAnimation();
        Animation animation=AnimationUtils.loadAnimation(getActivity(), R.anim.extend_top_in);
        list_conditions.startAnimation(animation);


    }


    public void onEvent(MerchantUpdateEvent merchantUpdateEvent)
    {


        loadData();
    }


    public void onEventMainThread(   AppInitCompleteEvent event)
    {
        initCategoryData();
        initBankData();
        loadData();
    }



    public void onEvent(CitySetEvent event  )
    {
        setHeaderViewTitle();

    }


    /**
     * 设置定制标题
     */
    private void setHeaderViewTitle()
    {

        if(cacheManager.getCurrentCity()!=null)

            header.setTitle(cacheManager.getCurrentCity().getName()+"  优惠商户");
        else

            header.setTitle( "优惠商户");
    }
    /**
     * 关注类型数据发生变化
     * @param event
     */
    public void onEvent(ConcernCategoryChangedEvent event)
    {
        updateCategoryData();
    }


    private  void initCategoryData()
    {

        selectCategory=ALL_CATEGORY;
        List<User.UserSetCategory> newList=new ArrayList<>();
        newList.add(selectCategory);
        if(cacheManager.getConcernCategory()!=null)
        {
            for(User.UserSetCategory category:cacheManager.getConcernCategory().getUserSetCategoryList())
            {
                if(category.getIsFollow()== Common.BooleanValue.BooleanValue_TRUE)
                {
                    newList.add(category);
                }
            }

        } categoryConditionAdapter.setSelectedItem(selectCategory);
        categoryConditionAdapter.setDataArray(newList);

    }


    private void initSiteData()
    {


        conditionAdapter.setSelectedItem(tab_site.getText().toString().trim());
        conditionAdapter.setDataArray(orderList);

    }

    private void updateCategoryData()
    {
        initCategoryData();
        loadData();
    }

    /**
     * 关注银行数据发生变化
     * @param event
     */
    public void onEvent(ConcernBankChangedEvent event)
    {
        updateBankData();
    }


    //初始化银行选择数据
    private  void initBankData()
    {

        selectedBank=ALL_BANK;
        List<User.UserSetBank> newList = new ArrayList<>();
        newList.add(selectedBank);
        if(cacheManager.getConcernBank() !=null)
        {
            for(User.UserSetBank setBank:cacheManager.getConcernBank().getUserSetBankList())
            {
                if(setBank.getIsFollow()== Common.BooleanValue.BooleanValue_TRUE)
                {
                    newList.add(setBank);
                }
            }




        }
        bankConditionAdapter.setSelectedItem(selectedBank);
        bankConditionAdapter.setDataArray(newList);

    }


    private void updateBankData()
    {
        initBankData();
        loadData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode!= Activity.RESULT_OK) return;
        switch (requestCode)
        {
            case REQUEST_SEARCH_MERCHANT:


                Merchant.MerchantInfo merchantInfo= (Merchant.MerchantInfo) data.getSerializableExtra(MerchantSearchActivity.EXTRA_MERCHANT_INFO);


                Intent intent = new Intent(getActivity(), MerchantDetailActivity.class);
                intent.putExtra(MerchantDetailActivity.EXTRA_MERCHANTINFO_ID, merchantInfo.getId());
                startActivity(intent);

//                MerchantDetailActivity
//                List<Merchant.MerchantInfo> datas=new ArrayList<>();
//                datas.add(merchantInfo);
//                adapter.setDataArray(datas);


                break;
        }

    }


    @Override
    public void onRefresh() {





        loadData();
    }





}
