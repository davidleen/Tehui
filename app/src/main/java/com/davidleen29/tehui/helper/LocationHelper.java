package com.davidleen29.tehui.helper;

import android.app.Application;
import android.content.Context;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.davidleen29.tehui.events.LocationInfo;
import com.davidleen29.tehui.utils.Log;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;

import roboguice.RoboGuice;

/**
 * Created by davidleen29 on 2015/8/10.
 */
@Singleton
public class LocationHelper implements BDLocationListener {



    Context context;



    CacheManager cacheManager;

    private LocationClient locationClient;

    public LocationHelper(Context newContext )
    {
        context=newContext;
        cacheManager  = RoboGuice.getInjector(context).getInstance(CacheManager.class);
        locationClient =new LocationClient(context);
        locationClient.registerLocationListener(this);    //注册监听函数

    }


    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，
        int span=1000;

        option.setScanSpan(10000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        locationClient.setLocOption(option);
    }






    public void stopLocationUpdate()
    {
        if(locationClient!=null&&locationClient.isStarted())
        locationClient.stop();
        locationClient =null;
    }

    /**
     * 启动当前地理定位城市
     */
    public   void startLocationUpdate( )
    {



        if(locationClient!=null&&!locationClient.isStarted()) {

            initLocation();
            locationClient.start();
        }
    }










    private static final  double EARTH_RADIUS = 6378.137;//地球半径
    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }
    /**
     * 计算两点之间距离 以米为单位
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */

    public static double GetDistance(double lat1, double lng1, double lat2, double lng2)
    {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 1000) ;
        return s;
    }

    public static java.text.DecimalFormat   kmFormat  =new   java.text.DecimalFormat("##.#");
    public static String formatDistance(double distance)
    {

      return  distance < 1000 ? ((int) (distance) + "m" ): (distance < 100000 ? (kmFormat.format(distance / 1000f) + "km") : "***km");
    }



    @Override
    public void onReceiveLocation(BDLocation location) {

        //Receive Location
        StringBuffer sb = new StringBuffer(256);
        sb.append("time : ");
        sb.append(location.getTime());
        sb.append("\nerror code : ");
        sb.append(location.getLocType());
        sb.append("\nlatitude : ");
        sb.append(location.getLatitude());
        sb.append("\nlontitude : ");
        sb.append(location.getLongitude());
        sb.append("\nradius : ");
        sb.append(location.getRadius());
        sb.append("\n 坐标类型:"+location.getCoorType());
        if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果
            sb.append("\nspeed : ");
            sb.append(location.getSpeed());// 单位：公里每小时
            sb.append("\nsatellite : ");
            sb.append(location.getSatelliteNumber());
            sb.append("\nheight : ");
            sb.append(location.getAltitude());// 单位：米
            sb.append("\ndirection : ");
            sb.append(location.getDirection());// 单位度
            sb.append("\naddr : ");
            sb.append(location.getAddrStr());
            sb.append("\ndescribe : ");
            sb.append("gps定位成功");
            setCurrentPosition(location);

        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果
            sb.append("\naddr : ");
            sb.append(location.getAddrStr());
            //运营商信息
            sb.append("\noperationers : ");
            sb.append(location.getOperators());
            sb.append("\ndescribe : ");
            sb.append("网络定位成功");
            setCurrentPosition(location);
        } else {
            locationClient.requestLocation();
            if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
        }
        sb.append("\nlocationdescribe : ");
        sb.append(location.getLocationDescribe());// 位置语义化信息
        List<Poi> list = location.getPoiList();// POI数据
        if (list != null) {
            sb.append("\npoilist size = : ");
            sb.append(list.size());
            for (Poi p : list) {
                sb.append("\npoi= : ");
                sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
            }
        }
        Log.i("BaiduLocationApiDem", sb.toString());





    }



    private void setCurrentPosition(BDLocation location)
    {



       // CoordinateConvert.conve
        cacheManager.setLocationInfo(new LocationInfo(location.getCity(), location.getLatitude(), location.getLongitude(),location.getCoorType()));
    }
}
