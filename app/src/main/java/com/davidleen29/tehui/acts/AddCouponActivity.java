package com.davidleen29.tehui.acts;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.api.ApiManager;
import com.davidleen29.tehui.events.PeriodSetEvent;
import com.davidleen29.tehui.helper.CacheManager;
import com.davidleen29.tehui.helper.Constraints;
import com.davidleen29.tehui.tasks.ThTask;
import com.davidleen29.tehui.utils.StringUtils;
import com.davidleen29.tehui.utils.TimeUtils;
import com.davidleen29.tehui.utils.ToastUtils;
import com.davidleen29.tehui.widget.HeaderView;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Common;
import com.huiyou.dp.service.protocol.Merchant;
import com.huiyou.dp.service.protocol.User;

import java.util.Calendar;
import java.util.Date;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_add_coupon)
public class AddCouponActivity extends BaseActivity {


    public static final String EXTRA_MERCHANT_ID = "EXTRA_MERCHANT_ID";
    @InjectView(R.id.header)
    HeaderView header;


    @InjectView(R.id.delete)
    View delete;
    @InjectView(R.id.name )
    TextView name;

    @InjectView(R.id.ll_date)
    View ll_date;

    @InjectView(R.id.endDate)
    TextView endDate;
    @InjectView(R.id.ll_period)
    View ll_period;

    @InjectView(R.id.period)
    TextView period;


    @InjectExtra(EXTRA_MERCHANT_ID)
    long merchantId;


    @Inject
    CacheManager cacheManager;
    @Inject
    ApiManager apiManager;
    private long lastSetData;


    private PeriodSetEvent periodSetEvent;
    @Override
    protected void init(Bundle bundle) {


        ll_date.setOnClickListener(this);
        delete.setOnClickListener(this);
        ll_period.setOnClickListener(this);
        header.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                addCoupon();
            }
        });

        lastSetData=Calendar.getInstance().getTimeInMillis()+60l*60*1000*24*30;
        endDate.setText(TimeUtils.longToYYYY_MM_DD(lastSetData));


        setPeriodData(periodSetEvent);
    }



    private void addCoupon() {
     final   long endDateValue=lastSetData;
        final String couponName=name.getText().toString().trim();

        if(StringUtils.isEmpty(couponName))
        {
            ToastUtils.show("名称不能为空");
            name.requestFocus();
            return;
        }

        new   ThTask<User.AddMerchantCouponResponse>(this) {
            @Override
            protected void doOnSuccess(User.AddMerchantCouponResponse data) {

                if(data.getErrCode()==User.AddMerchantCouponResponse.ErrorCode.ERR_OK)
                {
                    setResult(RESULT_OK);
                    finish();
                }else
                {
                    switch (data.getErrCode().getNumber())
                    {
                        case User.AddMerchantCouponResponse.ErrorCode.ERR_USER_NOT_EXSITS_VALUE:
                            ToastUtils.show("用户不存在");
                            break;




                        case User.AddMerchantCouponResponse.ErrorCode.ERR_COUPON_NAME_OVERFLOW_VALUE:

                            ToastUtils.show("优惠名称字数超出限制(100字)");
                            break;
                        case User.AddMerchantCouponResponse.ErrorCode.ERR_DATE_LESS_THAN_NOW_VALUE:

                            ToastUtils.show("日期小于当前时间");
                            break;
                        case User.AddMerchantCouponResponse.ErrorCode.ERR_MERCHANT_NOT_EXSITS_VALUE:

                            ToastUtils.show("商户不存在");
                            break;
                        case User.AddMerchantCouponResponse.ErrorCode.ERR_NO_POWER_VALUE:

                            ToastUtils.show("没有权限为该商户添加优惠券（用户与商户id不匹配或审核未通过）");
                            break;

                        default:
                            ToastUtils.show(data.getErrMsg());
                    }

                    ToastUtils.show(data.getErrMsg());
                }

            }

            @Override
            public User.AddMerchantCouponResponse call() throws Exception {
                if(periodSetEvent!=null)
                        return apiManager.addMerchantCoupon(cacheManager.getCurrentUser().getUserId(), merchantId, couponName,endDateValue,periodSetEvent.periodType,periodSetEvent.items);
                else
                    return

                            apiManager.addMerchantCoupon(cacheManager.getCurrentUser().getUserId(), merchantId, couponName,endDateValue);
            }


        }.execute();


    }


    @Override
    protected void onViewClick(View v, int id) {






        switch (id)
        {

            case R.id.ll_period:


                Intent intent=new Intent(this,CouponPeriodActivity.class);
                startActivity(intent);





                break;


            case R.id.delete:

                name.setText("");
                break;

            case R.id.ll_date:

                final Calendar calendar= Calendar.getInstance() ;
                if(lastSetData>0)
                {
                    calendar.setTimeInMillis(lastSetData);
                }

                DatePickerDialog datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        calendar.set(year,monthOfYear,dayOfMonth);

                        lastSetData=calendar.getTimeInMillis();

                      endDate.setText(TimeUtils.longToYYYY_MM_DD(lastSetData));

                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE));
                datePickerDialog.show();


                break;
        }
    }


    public void onEvent(PeriodSetEvent event)
    {

        this.periodSetEvent=event;
        setPeriodData(event);

    }

    private void setPeriodData(PeriodSetEvent event)
    {
        if(event==null)
        {
            period.setText("");
            return;
        }
        StringBuilder  textValue=new StringBuilder();
        textValue.append( event.periodType.equals(Common.EffectivePeriodType.EffectivePeriodType_WEEK)?"每周":"每月");
        textValue.append("  ");
        for(int index:event.items)
        {
            if(event.periodType.equals(Common.EffectivePeriodType.EffectivePeriodType_WEEK))
            {
                textValue.append(Constraints.WEEKS[index]+",");
            }else
            {

                textValue.append((index+1)+"号"+",");
            }


        }
        textValue.setLength(textValue.length() - 1);

        period.setText(textValue);
    }
}
