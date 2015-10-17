package com.davidleen29.tehui.tasks;

import android.content.Context;

import com.davidleen29.tehui.api.ApiManager;
import com.davidleen29.tehui.utils.ToastUtils;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.City;

/**
 *
 * 设置城任务
 * Created by davidleen29 on 2015/7/24.
 */
public class SetCityTask extends  ThTask<City.UserSetCityResponse>{

    @Inject
    ApiManager apiManager;

        private City.CityInfo cityInfo;

    public SetCityTask(Context context,City.CityInfo cityInfo) {
        super(context);
        this.cityInfo=cityInfo;
    }

    @Override
    protected void doOnSuccess(City.UserSetCityResponse data) {

        if(data.getErrCode()==City.UserSetCityResponse.ErrorCode.ERR_OK)
        {



        }else
        {
            ToastUtils.show(data.getErrMsg());
        }

    }

    @Override
    public City.UserSetCityResponse call() throws Exception {
        return apiManager.userSetCity(cityInfo );
    }
}
