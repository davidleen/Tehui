package com.davidleen29.tehui.acts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.events.PeriodSetEvent;
import com.huiyou.dp.service.protocol.Common;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;


@ContentView(R.layout.activity_coupon_period)
public class CouponPeriodActivity extends BaseActivity {


    @InjectView(R.id.week)
    View week;

    @InjectView(R.id.month)
    View month;


    @Override
    protected void init(Bundle bundle) {

        week.setOnClickListener(this);
        month.setOnClickListener(this);


    }




    @Override
    protected void onViewClick(View v, int id) {


        switch (id) {

            case R.id.week:

            {
                Intent intent = new Intent(this, CouponPeriod2Activity.class);
                intent.putExtra(CouponPeriod2Activity.EXTRA_PERIOD_TYPE, Common.EffectivePeriodType.EffectivePeriodType_WEEK);
                startActivity(intent);

            }
                break;


            case R.id.month: {
                Intent intent = new Intent(this, CouponPeriod2Activity.class);
                intent.putExtra(CouponPeriod2Activity.EXTRA_PERIOD_TYPE, Common.EffectivePeriodType.EffectivePeriodType_MONTH);
                startActivity(intent);

            }
                break;
        }
    }



        public void onEvent(PeriodSetEvent event)
        {
            finish();
        }
        }



