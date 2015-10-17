package com.davidleen29.tehui.acts;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.adapters.CouponAdapter;
import com.davidleen29.tehui.adapters.ShopInfoAdapter;
import com.davidleen29.tehui.lang.CException;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Merchant;

import java.util.ArrayList;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_coupon_off_shell)
public class CouponOffShellActivity extends BaseActivity  {


    public static final String EXTRA_DATA = "EXTRA_DATA";

    @InjectExtra(EXTRA_DATA)
    ArrayList<Merchant.MerchantCouponInfo> couponInfos;


    @Inject
    CouponAdapter adapter;

    @InjectView(R.id.list)
    ListView listView;

    @Override
    protected void init(Bundle bundle) {
        super.init(bundle);

        listView.setAdapter(adapter);
        initValue();
    }




    private void initValue()
    {

        adapter.setDataArray(couponInfos);
    }
}
