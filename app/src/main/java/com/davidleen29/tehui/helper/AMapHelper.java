package com.davidleen29.tehui.helper;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.DistanceUtil;
import com.davidleen29.tehui.BuildConfig;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.acts.MerchantDetailActivity;
import com.davidleen29.tehui.events.LocationInfo;
import com.davidleen29.tehui.utils.Log;
import com.davidleen29.tehui.utils.ToastUtils;
import com.google.inject.Guice;
import com.huiyou.dp.service.protocol.Merchant;

import net.sf.cglib.proxy.InterfaceMaker;

import java.util.ArrayList;
import java.util.List;

import roboguice.RoboGuice;

/**
 * Created by davidleen29 on 2015/7/20.
 */
public class AMapHelper implements BaiduMap.OnMapLoadedCallback, BaiduMap.OnMarkerClickListener, BaiduMap.OnMapLongClickListener, BaiduMap.OnMapClickListener {


    private static final String TAG = "AMapHelper";
    BaiduMap aMap;
    Context mContext;



    private int currentIndex;

    public AMapHelper(Context context,BaiduMap aMap)
    {
        this.aMap=aMap;
        mContext=context;
        setUpMap();

    }

    /**
     * 默认地图缩放指数。
     */
    private static final float DEFAULT_ZOOM_VALUE = 16;


    List<Merchant.MerchantInfo> merchantInfos=new ArrayList<>();
    List<Marker> markers=new ArrayList<>();

    public void updateMap(List<Merchant.MerchantInfo> newMerchentInfos)
    {


        merchantInfos.clear();
        for(Marker marker:markers)
        {
            marker.remove();

        }
        markers.clear();

        //清除弹出
        aMap.hideInfoWindow();



        merchantInfos.addAll(newMerchentInfos);
        currentIndex=-1;



        if(merchantInfos.size()==0)
        {

            moveToCurrentPosition();

        }
        else {





            for (Merchant.MerchantInfo merchantInfo : merchantInfos) {


                Marker marker = drawMarkers(merchantInfo);

                markers.add(marker);

            }
//            if(markers.size()>0)
//            {
//
//                showMarker(0);
//
//            }

           //  drawMarkerAndLocate(26,119);
        }





    }

    private void showMarker(int newIndex) {

        currentIndex=newIndex;

        Merchant.MerchantInfo merchantInfo=merchantInfos.get(currentIndex);

        aMap.setMyLocationData(new MyLocationData.Builder().latitude(merchantInfo.getY()).longitude(merchantInfo.getX()).build());
        LatLng ll = new LatLng(merchantInfo.getY(),
                merchantInfo.getX());
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll,DEFAULT_ZOOM_VALUE);
        aMap.animateMapStatus(u);


        showInfoWindow(markers.get(currentIndex));

    }
    private void setUpMap() {

        // aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
        aMap.setOnMapLoadedCallback(this);// 设置amap加载成功事件监听器
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器

        // aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
        aMap.setOnMapLongClickListener(this);
        // addMarkersToMap();// 往地图上添加marker
        // mListener = locationListener;
        aMap.setOnMapClickListener(this);







        setUpLoactionButton();







    }







    @Override
    public void onMapLoaded() {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {




            showInfoWindow(marker);
            return true;


    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }


    private  void showInfoWindow(Marker marker)
    {

        currentIndex=markers.indexOf(marker);




        if(currentIndex<=-1||currentIndex>=merchantInfos.size()) return;
        InfoWindow infoWindow = new InfoWindow(getInfoWindwow(merchantInfos.get(currentIndex)), marker.getPosition(),  PixelHelper.dp2px(-27));

        aMap.showInfoWindow(infoWindow);
    }






    /**
     * 设置定位按钮功能
     */
    private void setUpLoactionButton() {


      //  aMap.setMyLocationEnabled(true);


        aMap.getUiSettings().setAllGesturesEnabled(true);


        aMap.setOnMyLocationClickListener(new BaiduMap.OnMyLocationClickListener() {
            @Override
            public boolean onMyLocationClick() {


                return false;
            }
        });

    }


    public void moveToCurrentPosition()
    {

        CacheManager manager=     RoboGuice.getInjector(mContext).getInstance(CacheManager.class);
        LocationInfo locationInfo=manager.getLocationInfo();
        if(locationInfo!=null)
        {
          //  aMap.setMyLocationEnabled(true);
            aMap.setMyLocationData(new MyLocationData.Builder().latitude(locationInfo.x).longitude(locationInfo.y).build());
            LatLng ll = new LatLng(locationInfo.x,
                    locationInfo.y);
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll,DEFAULT_ZOOM_VALUE);
            aMap.animateMapStatus(u);
        }




    }


    /**
     * 绘制系统默认的1种marker背景图片
     */
    public Marker drawMarkers(Merchant.MerchantInfo merchantInfo) {



        LatLng latLng=new LatLng(merchantInfo.getY(),merchantInfo.getX());
//构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_map_bank);
//构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(latLng)
                .icon(bitmap).extraInfo( null);
//在地图上添加Marker，并显示
        Marker marker= (Marker) aMap.addOverlay(option);




        // marker.setExtraInfo(merchantInfo);

        // marker.setRotateAngle(90);// 设置marker旋转90度

        // myMarker.setTitle(arg0);


        return marker;

    }










    @Override
    public void onMapClick(LatLng latLng) {



        if(currentIndex>-1 )
        {
            aMap.hideInfoWindow();
            currentIndex=-1;
        }


    }



    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    public void translateTo(Merchant.MerchantInfo merchantInfo) {


        int index=merchantInfos.indexOf(merchantInfo);

        if(index>-1)
        showMarker(index);




    }




    private  View getInfoWindwow( Merchant.MerchantInfo merchantInfo)
    {

        View infoWindow = LayoutInflater.from(mContext).inflate(
                R.layout.view_map_marker, null);

        TextView name= (TextView) infoWindow.findViewById(R.id.
                name);
        name.setText(merchantInfo.getName());
        TextView bankCount= (TextView) infoWindow.findViewById(R.id.
                bankCount);
        bankCount.setText("银行优惠:"+merchantInfo.getTehuiBankCount());

        TextView shopCount= (TextView) infoWindow.findViewById(R.id.
                shopCount);
        shopCount.setText("商家优惠:"+merchantInfo.getMerchantCouponInfosCount());
        name.setText(merchantInfo.getName());
        infoWindow.setTag(merchantInfo);

        infoWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent=new Intent(mContext, MerchantDetailActivity.class);
                intent.putExtra(MerchantDetailActivity.EXTRA_MERCHANTINFO_ID,((Merchant.MerchantInfo)v.getTag()).getId());
                mContext.startActivity(intent);

            }
        });


        return infoWindow;
    }



    private void  translateLocation(BDLocation location)
    {
        // 开启定位图层
        aMap
                .setMyLocationEnabled(true);
// 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
// 设置定位数据
        aMap.setMyLocationData(locData);
// 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
//        aMap = BitmapDescriptorFactory
//                .fromResource(R.drawable.icon_geo);
//        MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker);
//        aMap.setMyLocationConfiguration();
      // 当不需要定位图层时关闭定位图层
    //    aMap.setMyLocationEnabled(false);
    }
}
