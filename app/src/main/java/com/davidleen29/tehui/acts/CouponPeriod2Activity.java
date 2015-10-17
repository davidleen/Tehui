package com.davidleen29.tehui.acts;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.acts.BaseActivity;
import com.davidleen29.tehui.adapters.StringMultiAdapter;
import com.davidleen29.tehui.events.PeriodSetEvent;
import com.davidleen29.tehui.helper.Constraints;
import com.davidleen29.tehui.utils.ArrayUtils;
import com.davidleen29.tehui.widget.HeaderView;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.greenrobot.event.EventBus;
import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_coupon_period2)
public class CouponPeriod2Activity extends BaseActivity {


    public static final String EXTRA_PERIOD_TYPE = "EXTRA_PERIOD_TYPE";
     @InjectView(R.id.listView)
    ListView listView;
 @InjectView(R.id.header)
 HeaderView header;


    @Inject
    StringMultiAdapter adapter;


    @InjectExtra(EXTRA_PERIOD_TYPE)
    Common.EffectivePeriodType periodType;

    @Override
    protected void init(Bundle bundle) {

        listView.setAdapter(adapter);

        List<String >  data=new ArrayList<>();

        switch (periodType)
        {

            case EffectivePeriodType_WEEK:


              data.addAll(Constraints.WEEKLIST);



                break;
            case EffectivePeriodType_MONTH:
                for(int i=1;i<32;i++)
                {
                    data.add(i+"号");
                }

                break;

        }

        adapter.setDataArray(data);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                adapter.onItemClick(position);
            }
        });
        header.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                setResult(RESULT_OK);


                PeriodSetEvent event=new PeriodSetEvent();
                List<Integer> datas=new ArrayList<Integer>();



                datas.addAll(adapter.selectedItems);

                int size = datas.size();
                int[] intArray=new int[size];
                for (int i = 0; i < size; i++) {

                    intArray[i]=datas.get(i);
                }
                Arrays.sort(intArray);

                event.items=new ArrayList<Integer>();
                for (int i = 0; i < size; i++) {
                    event.items.add(intArray[i]);

                }

                event.periodType=periodType;
                EventBus.getDefault().post(event);
                finish();

            }
        });





    }
}
