package com.davidleen29.tehui.helper;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.davidleen29.tehui.events.LocationInfo;
import com.davidleen29.tehui.utils.StringUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.huiyou.dp.service.protocol.City;
import com.huiyou.dp.service.protocol.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;


/**
 * 缓存处理列。
 * Created by davidleen29 on 2015/7/20.
 */
@Singleton
public class CacheManager {


    public static final String TAG="CacheManager";

    public static final String PRE_USER_INFO="/userInfo";
    public static final String PRE_CITY_RESPONSE="/CITY_RESPONSE";
    public static final String PRE_CITY_INFO="/PRE_CITY_INFO";

    @Inject
    Application context;



    User.UserInfo currentUser;

    @Inject
    Gson gson;
    private LocationInfo locationInfo;

    @Inject
    public CacheManager( ) {

    }






    public User.UserInfo getCurrentUser()
    {
        if(currentUser==null)
        {



                try {
                    FileInputStream fis=new FileInputStream(context.getExternalCacheDir() + PRE_USER_INFO);
                    currentUser= User.UserInfo.parseFrom(fis);
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }




        }
        return currentUser;
    }




    public void setCurrentUser(User.UserInfo userInfo)  {
        currentUser=userInfo;

        //save to file
        if(userInfo==null)
        {
            new File(context.getExternalCacheDir()+PRE_USER_INFO).delete();

        }else {

            try {
                FileOutputStream fos = new FileOutputStream(context.getExternalCacheDir() + PRE_USER_INFO);

                currentUser.writeTo(fos);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }



    private City.GetCitysResponse response;


    public City.GetCitysResponse getCityList(){

        if(response==null)
        {



            try {


                 FileInputStream fis=new FileInputStream(context.getExternalCacheDir() + PRE_CITY_RESPONSE);
                response= City.GetCitysResponse.parseFrom(fis);
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }




        }
        return response;


    }

    public void setCityInfos(City.GetCitysResponse citysResponse)
    {
        //save to file
        this.response=citysResponse;
        if(citysResponse==null)
        {
            new File(context.getExternalCacheDir()+PRE_CITY_RESPONSE).delete();

        }else {
            try {

                FileOutputStream fos = new FileOutputStream(context.getExternalCacheDir() + PRE_CITY_RESPONSE);
                response.writeTo(fos);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }




    private City.CityInfo cityInfo;
    public void setCurrentCity(City.CityInfo cityInfo) {


        //save to file
        this.cityInfo=cityInfo;

        if(cityInfo==null)
        {
            new File(context.getExternalCacheDir()+PRE_CITY_INFO).delete();

        }else {
            try {

                FileOutputStream fos = new FileOutputStream(context.getExternalCacheDir() + PRE_CITY_INFO);
                cityInfo.writeTo(fos);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public City.CityInfo getCurrentCity()
    {
        if(cityInfo==null)
        {



            try {
                FileInputStream fis=new FileInputStream(context.getExternalCacheDir() + PRE_CITY_INFO);
                cityInfo= City.CityInfo.parseFrom(fis);
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return cityInfo;

    }





    //是否已经设置感兴趣数据
    private boolean concernDataSetted=false;

    private static final String PRE_SET_CONCERN="/PRE_SET_CONCERN";



    public boolean  isSetConcern()
    {


        SharedPreferences sharedPreferences=    context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
         return    sharedPreferences.getBoolean(PRE_SET_CONCERN,false);

    }



    public void  setConcern()
    {

        setConcern(true);



    }

    public void  setConcern(boolean concerned)
    {


        SharedPreferences sharedPreferences=    context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putBoolean(PRE_SET_CONCERN,concerned);
        editor.commit();


    }
    /**
     * 系统是否已经初始化
     * @return
     */
    public boolean hasInit()
    {

       return getCurrentUser()!=null&&getCurrentCity()!=null&&isSetConcern();
    }


    /***
     */
    public static final String PRE_CONCERN_BANK="/PRE_CONCERN_BANK";
    private User.GetUserSetBankResponse concernBank;
    public static final String PRE_CONCERN_CATEGORY="/PRE_CONCERN_CATEGORY";
    private  User.GetUserSetCategoryResponse concernCategory;


    public  User.GetUserSetCategoryResponse getConcernCategory() {
        if (concernCategory == null) {


            try {
                FileInputStream fis = new FileInputStream(context.getExternalCacheDir() + PRE_CONCERN_CATEGORY);
                concernCategory = User.GetUserSetCategoryResponse.parseFrom(fis);
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        return concernCategory;
    }




    public void setConcernCategory( User.GetUserSetCategoryResponse concernCatogery) {
        this.concernCategory = concernCatogery;


        if(concernCategory==null)
        {
            new File(context.getExternalCacheDir()+PRE_CONCERN_CATEGORY).delete();

        }else {
            try {

                FileOutputStream fos = new FileOutputStream(context.getExternalCacheDir() + PRE_CONCERN_CATEGORY);
                concernCategory.writeTo(fos);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public User.GetUserSetBankResponse getConcernBank() {
        if (concernBank==null)
        {
            try {
                FileInputStream fis = new FileInputStream(context.getExternalCacheDir() + PRE_CONCERN_BANK);
                concernBank = User.GetUserSetBankResponse.parseFrom(fis);
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }




        }
        return concernBank;
    }

    public void setConcernBank(User.GetUserSetBankResponse concernBank) {
        this.concernBank = concernBank;

        if(concernBank==null)
        {
            new File(context.getExternalCacheDir()+PRE_CONCERN_BANK).delete();

        }else {
            try {

                FileOutputStream fos = new FileOutputStream(context.getExternalCacheDir() + PRE_CONCERN_BANK);
                concernBank.writeTo(fos);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




    }


    /**
     * 清楚所有缓存数据
     */
    public void clear() {

        setConcernBank(null);
        setConcernCategory(null);
        setCityInfos(null);
        setCurrentUser(null);
        setCurrentCity(null);
        setConcern(false);



    }

    public static final String PRE_LOCATION="/PRE_LOCATION";


    public void setLocationInfo(LocationInfo locationInfo) {
        this.locationInfo = locationInfo;

        try{

            SharedPreferences sharedPreferences=    context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor= sharedPreferences.edit();
            editor.putString(PRE_LOCATION, gson.toJson(locationInfo));
            editor.commit();
        }catch (Throwable t)
        {
            t.printStackTrace();
        }
    }

    public LocationInfo getLocationInfo() {

        if(locationInfo==null)
        {
            SharedPreferences sharedPreferences=    context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
            String temp=    sharedPreferences.getString (PRE_LOCATION,"");
            try {
                if (!StringUtils.isEmpty(temp)) {
                    locationInfo = gson.fromJson(temp, LocationInfo.class);
                }
            }catch (Throwable t)
            {
                t.printStackTrace();
            }
        }



        return locationInfo;
    }
}
