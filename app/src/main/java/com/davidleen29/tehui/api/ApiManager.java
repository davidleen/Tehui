package com.davidleen29.tehui.api;

import android.os.Environment;
import android.util.DisplayMetrics;

import com.davidleen29.tehui.helper.CacheManager;
import com.davidleen29.tehui.lang.CException;
import com.davidleen29.tehui.utils.Log;
import com.davidleen29.tehui.utils.RandomUtils;
import com.davidleen29.tehui.utils.StringUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.protobuf.ByteString;
import com.google.protobuf.MessageLite;
import com.huiyou.dp.service.protocol.Circle;
import com.huiyou.dp.service.protocol.City;
import com.huiyou.dp.service.protocol.Common;
import com.huiyou.dp.service.protocol.Merchant;
import com.huiyou.dp.service.protocol.Sms;
import com.huiyou.dp.service.protocol.User;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.Response;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;


/**
 *
 * 远程访问调用方法
 * Created by davidleen29 on 2015/7/19.
 */
@Singleton
public class ApiManager {


    @Inject
    Client client;

    @Inject
    CacheManager cacheManager;
    @Inject
    public ApiManager()
    {




    }



    public Merchant.GetMerchantsResponse getMerchants(  long  categoryId,List<Integer> bankIds,Merchant.GetMerchantsRequest.ResultOrderField field,int index,int count) throws CException {


         String url=HttpUrl.getMerchants();
        Merchant.GetMerchantsRequest.Builder resqBuilder = Merchant.GetMerchantsRequest.newBuilder();

        if(categoryId>0)
         resqBuilder.setCategoryId((int) categoryId);//
        if(cacheManager.getCurrentCity()!=null)
        resqBuilder.setCityId(cacheManager.getCurrentCity().getId()) ;//
        resqBuilder.setResultOrderField(field);
         resqBuilder.addAllBankIds(bankIds);
        resqBuilder.setIndex(index*count);
        resqBuilder.setCount(count);

//        Merchant.GetMerchantsRequest resq = resqBuilder.build();
//
//
//        return client.post(url, resq, handler);


        return  invokeWithReflect(Merchant.GetMerchantsResponse.class, url, resqBuilder);
    }



    public Merchant.GetMerchantsByCoordinateResponse getMerchantsByCoordinate(  long  categoryId,List<Integer> bankIds,Merchant.GetMerchantsByCoordinateRequest.ResultOrderField field,Merchant.GetMerchantsByCoordinateRequest.ResultOrderType orderType,double latitude,double longtitude,int index,int count) throws CException {


         String url=HttpUrl.getMerchantsByCoordinate();



        Merchant.GetMerchantsByCoordinateRequest.Builder resqBuilder = Merchant.GetMerchantsByCoordinateRequest.newBuilder();

        if(categoryId>0)
         resqBuilder.setCategoryId((int) categoryId);//
        if(cacheManager.getCurrentCity()!=null)
        resqBuilder.setCityId(cacheManager.getCurrentCity().getId()) ;//
        resqBuilder.setResultOrderField(field);
        resqBuilder.setResultOrderType(orderType);
         resqBuilder.addAllBankIds(bankIds);
        if(Double.compare(latitude,0)!=0||Double.compare(longtitude,0)!=0)
        {

            resqBuilder.setX(longtitude);
            resqBuilder.setY(latitude);

        }

        resqBuilder.setIndex(index*count);
        resqBuilder.setCount(count);




        return  invokeWithReflect(Merchant.GetMerchantsByCoordinateResponse.class, url, resqBuilder);
    }





    public User.RegisterResponse register(String phone, String password, String tempUserName) throws CException {


        String url=HttpUrl.register();

        User.RegisterRequest.Builder resqBuilder = User.RegisterRequest.newBuilder();
        resqBuilder.setMobileNum(phone);
        resqBuilder.setPassword(password) ;//
        resqBuilder.setTempUserName(tempUserName);

        return invokeWithReflect(User.RegisterResponse.class,url, resqBuilder);
    }

    public User.LoginResponse loginAsGuest(String tempUserId) throws CException {


        String url=HttpUrl.login();
        AsyncCompletionHandler<User.LoginResponse> handler=     new AsyncCompletionHandler<User.LoginResponse>() {
            @Override
            public User.LoginResponse onCompleted(Response response) throws Exception {



                return User.LoginResponse.parseFrom(response.getResponseBodyAsBytes());

            }
        };


        DisplayMetrics metrics;
        User.LoginRequest.Builder resqBuilder = User.LoginRequest.newBuilder();
        resqBuilder.setLoginType(Common.LoginType.LoginType_TEMP);
       // resqBuilder.setPassword("1");
        resqBuilder.setUserName(tempUserId);//
        resqBuilder.setDeviceType(Common.DeviceType.DeviceType_ANDROID);
        resqBuilder.setSystemVersion("222");
        return client.post(url, resqBuilder.build(), handler);

    }


    public User.LoginResponse login(String mobileNum, String password) throws CException{


        String url=HttpUrl.login();
        AsyncCompletionHandler<User.LoginResponse> handler=     new AsyncCompletionHandler<User.LoginResponse>() {
            @Override
            public User.LoginResponse onCompleted(Response response) throws Exception {



                return User.LoginResponse.parseFrom(response.getResponseBodyAsBytes());

            }
        };

        User.LoginRequest.Builder resqBuilder = User.LoginRequest.newBuilder();
        resqBuilder.setLoginType(Common.LoginType.LoginType_NORMAL);
        resqBuilder.setPassword(password);
        resqBuilder.setUserName(mobileNum);//
        resqBuilder.setDeviceType(Common.DeviceType.DeviceType_ANDROID);
        resqBuilder.setSystemVersion("222");
        return client.post(url,resqBuilder.build(),handler);


    }



    /**
     * 查询用户的银行关注列表
     * @param userName
     * @return
     */
    public User.GetUserSetBankResponse  getUserSetBank(String userName) throws CException {


        String url=HttpUrl.getUserSetBank();
        AsyncCompletionHandler<User.GetUserSetBankResponse> handler=     new AsyncCompletionHandler<User.GetUserSetBankResponse>() {
            @Override
            public User.GetUserSetBankResponse onCompleted(Response response) throws Exception {
                return User.GetUserSetBankResponse.parseFrom(response.getResponseBodyAsBytes());

            }
        };

        User.GetUserSetBankRequest.Builder resqBuilder = User.GetUserSetBankRequest.newBuilder();
        resqBuilder.setUserName(userName);
        return client.post(url,resqBuilder.build(),handler);
    }

    /**
     * 设置用户银行关注
     * @param userName
     * @return
     */
    public User.UserSetBankResponse  userSetBank(String userName,List<Integer> bankIds) throws CException {


        String url=HttpUrl.userSetBank();
        AsyncCompletionHandler<User.UserSetBankResponse> handler=     new AsyncCompletionHandler<User.UserSetBankResponse>() {
            @Override
            public User.UserSetBankResponse onCompleted(Response response) throws Exception {
                return User.UserSetBankResponse.parseFrom(response.getResponseBodyAsBytes());

            }
        };

        User.UserSetBankRequest.Builder resqBuilder = User.UserSetBankRequest.newBuilder();
        resqBuilder.setUserName(userName);
        resqBuilder.addAllBankId(bankIds);
        return client.post(url,resqBuilder.build(),handler);
    }




    /**
     * 查询用户的类型关注列表
     * @param userId
     * @return
     */
    public User.GetUserSetCategoryResponse  getUserSetCategory(String userId) throws CException {


        String url=HttpUrl.getUserSetCategory();
        AsyncCompletionHandler<User.GetUserSetCategoryResponse> handler=     new AsyncCompletionHandler<User.GetUserSetCategoryResponse>() {
            @Override
            public User.GetUserSetCategoryResponse onCompleted(Response response) throws Exception {
                return User.GetUserSetCategoryResponse.parseFrom(response.getResponseBodyAsBytes());

            }
        };

        User.GetUserSetCategoryRequest.Builder resqBuilder = User.GetUserSetCategoryRequest.newBuilder();
        resqBuilder.setUserName(userId);
        return client.post(url,resqBuilder.build(),handler);
    }

    public User.UserSetCategoryResponse userSetCategory(String userName, List<Integer> categoryIds) throws CException {


        String url=HttpUrl.userSetCategory();
        AsyncCompletionHandler<User.UserSetCategoryResponse> handler=     new AsyncCompletionHandler<User.UserSetCategoryResponse>() {
            @Override
            public User.UserSetCategoryResponse onCompleted(Response response) throws Exception {
                return User.UserSetCategoryResponse.parseFrom(response.getResponseBodyAsBytes());

            }
        };

        User.UserSetCategoryRequest.Builder resqBuilder = User.UserSetCategoryRequest.newBuilder();
        resqBuilder.setUserName(userName);
        resqBuilder.addAllCategoryId(categoryIds);
        return client.post(url,resqBuilder.build(),handler);
    }



    public User.CheckRegisterIdentifyingCodeResponse checkRegisterIdentifyingCode(String code,String mobile ) throws CException {

        String url=HttpUrl.checkRegisterIdentifyingCode();
        AsyncCompletionHandler<User.CheckRegisterIdentifyingCodeResponse> handler=     new AsyncCompletionHandler<User.CheckRegisterIdentifyingCodeResponse>() {
            @Override
            public User.CheckRegisterIdentifyingCodeResponse onCompleted(Response response) throws Exception {
                return User.CheckRegisterIdentifyingCodeResponse.parseFrom(response.getResponseBodyAsBytes());

            }
        };

        User.CheckRegisterIdentifyingCodeRequest.Builder resqBuilder = User.CheckRegisterIdentifyingCodeRequest.newBuilder();
        resqBuilder.setCode(code);
        resqBuilder.setMobileNum(mobile);
        resqBuilder.setType(Common.SmsSendType.SmsSendType_REGISTER);
        return client.post(url,resqBuilder.build(),handler);

    }


    /**
     * 发送验证码
     * @return
     */
    public Sms.SendIdentifyingCodeResponse sendIdentifyingCode(String mobileName,Common.SmsSendType type) throws CException {
        String url=HttpUrl.sendIdentifyingCode();
        Sms.SendIdentifyingCodeRequest.Builder resqBuilder = Sms.SendIdentifyingCodeRequest.newBuilder();
        resqBuilder.setMobileNum(mobileName);
        resqBuilder.setSmsSendType(type);
        return invokeWithReflect(Sms.SendIdentifyingCodeResponse.class, url, resqBuilder);
    }



    /**
     *  a generic method  to reduce code
     * @return
     */
    public <T> T invokeWithReflect(final Class<T> aClass, String url, MessageLite.Builder resqBuilder) throws CException {




        AsyncCompletionHandler<T> handler=     new AsyncCompletionHandler<T>() {
            @Override
            public T onCompleted(Response response) throws Exception {

              Method mothod= aClass.getMethod("parseFrom", byte[].class);
                return (T) mothod.invoke(null, response.getResponseBodyAsBytes());

            }
        };


        return client.post(url, resqBuilder.build(), handler);
    }

    /**
     * 朋友圈发送消息
     * @param content
     * @param bytes
     * @return
     */
    public Circle.SendMessageResponse sendMessage(String content,byte[] bytes,Common.CircleMessageType type,String title,long merchantId) throws CException {
        String url=HttpUrl.sendMessage();

       User.UserInfo userInfo= cacheManager.getCurrentUser();
        Circle.SendMessageRequest.Builder resqBuilder = Circle.SendMessageRequest.newBuilder();
        resqBuilder.setCircleMessageType(type);
        if(cacheManager.getCurrentCity()!=null)
          resqBuilder.setCityId(cacheManager.getCurrentCity().getId());
        resqBuilder.setContent(content);
        if(bytes!=null)
         resqBuilder.setImg(ByteString.copyFrom(bytes));


        if(merchantId>0)
        {
            resqBuilder.setMerchantId(merchantId);

        }
        if(!StringUtils.isEmpty(title))
        {
            resqBuilder.setTitle(title);
        }
        resqBuilder.setUserId(userInfo.getUserId());


        return  invokeWithReflect(Circle.SendMessageResponse.class, url, resqBuilder);

    }



    /**
     * 获取朋友圈消息
     * @return
     */
    public Circle.GetMessagesResponse getMessages(  Common.CircleMessageType type,int pageIndex,int pageCount) throws CException {
        String url=HttpUrl.getMessages();
        Circle.GetMessagesRequest.Builder resqBuilder = Circle.GetMessagesRequest.newBuilder();
        resqBuilder.setCircleMessageType(type);
        if(cacheManager.getCurrentCity()!=null)
        resqBuilder.setCityId(cacheManager.getCurrentCity().getId());
        resqBuilder.setIndex(pageIndex*pageCount);
        resqBuilder.setCount(pageCount);

        return invokeWithReflect(Circle.GetMessagesResponse.class, url, resqBuilder);


       // return client.post(url,resqBuilder.build(),handler);
    }




    /**
     * 获取朋友圈消息
     * @return
     */
    public City.UserSetCityResponse userSetCity( City.CityInfo cityInfo  ) throws CException {
        String url=HttpUrl.userSetCity();
        AsyncCompletionHandler<City.UserSetCityResponse> handler=     new AsyncCompletionHandler<City.UserSetCityResponse>() {
            @Override
            public City.UserSetCityResponse onCompleted(Response response) throws Exception {
                return City.UserSetCityResponse.parseFrom(response.getResponseBodyAsBytes());

            }
        };


        City.UserSetCityRequest.Builder resqBuilder =City.UserSetCityRequest.newBuilder();


        resqBuilder.setUserId(cacheManager.getCurrentUser().getUserId());
        resqBuilder.setCityId(cityInfo.getId());


        City.UserSetCityResponse result= client.post(url, resqBuilder.build(), handler);

        if(result.getErrCode()==City.UserSetCityResponse.ErrorCode.ERR_OK)
        {


            cacheManager.setCurrentCity(cityInfo);

        }

        return result;
    }

    public City.GetCitysResponse getCities() throws CException {



        String url=HttpUrl.getCities();



        City.GetCitysRequest.Builder resqBuilder =City.GetCitysRequest.newBuilder();


        return invokeWithReflect(City.GetCitysResponse.class, url, resqBuilder);






    }



    public User.GetFavoritesResponse getFavorites() throws CException {

        String url=HttpUrl.getFavorites();
        AsyncCompletionHandler<User.GetFavoritesResponse> handler=     new AsyncCompletionHandler<User.GetFavoritesResponse>() {
            @Override
            public User.GetFavoritesResponse onCompleted(Response response) throws Exception {
                return User.GetFavoritesResponse.parseFrom(response.getResponseBodyAsBytes());

            }
        };

        User.UserInfo userInfo= cacheManager.getCurrentUser();
        User.GetFavoritesRequest.Builder resqBuilder = User.GetFavoritesRequest.newBuilder();
         resqBuilder.setIndex(0);
        resqBuilder.setCount(999);
        resqBuilder.setUserId(userInfo.getUserId());
        return client.post(url, resqBuilder.build(), handler);

    }


    /**
     * 获取用户信息接口
     * @return
     */
    public User.GetUserInfoResponse getUserInfo(long userId) throws CException {
        String url=HttpUrl.getUserInfo();
        User.GetUserInfoRequest.Builder resqBuilder = User.GetUserInfoRequest.newBuilder();
        resqBuilder.setUserId(userId);
        return invokeWithReflect(User.GetUserInfoResponse.class, url, resqBuilder);


    }




    /**
     * 获取用户信息接口
     * @param userId
     * @return
     */
    public User.SetUserInfoResponse setUserInfo(long userId,byte[] userPhoto,String nickName,Common.UserSex userSex) throws CException {
        String url=HttpUrl.setUserInfo();

        User.SetUserInfoRequest.Builder resqBuilder = User.SetUserInfoRequest.newBuilder();
        resqBuilder.setUserId(userId);
        if(userPhoto!=null)
            resqBuilder.setHeadImg(ByteString.copyFrom(userPhoto));

        if(nickName!=null)
            resqBuilder.setNickName(nickName);

        if(userSex!=null)
        {
            resqBuilder.setSex(userSex);
        }


        return invokeWithReflect(User.SetUserInfoResponse.class, url, resqBuilder);


    }


    /**
     * 模糊查询商户
     * @return
     */
    public Merchant.FuzzyQueryMerchantResponse fuzzyQueryMerchant(String keyString ,int pageIndex,int pageCount) throws CException {

        String url=HttpUrl.fuzzyQueryMerchant();

        Merchant.FuzzyQueryMerchantRequest.Builder resqBuilder = Merchant.FuzzyQueryMerchantRequest.newBuilder();
      City.CityInfo city=  cacheManager.getCurrentCity();
        resqBuilder.setCityId(city.getId());


        resqBuilder.setIndex(pageIndex*pageCount);
        resqBuilder.setCount(pageCount);
        //resqBuilder.setMerchantAddress(keyString);
        resqBuilder.setMatchingString(keyString);

        return invokeWithReflect(Merchant.FuzzyQueryMerchantResponse.class, url, resqBuilder);





    }


    /**
     *  用户获取优惠券
     * @param userId
     * @param merchant_coupon_id
     * @return
     * @throws CException
     */
    public User.FindCouponResponse findCouponRequest(long userId, long merchant_coupon_id ) throws CException {

        String url=HttpUrl.findCouponRequest();

        User.FindCouponRequest.Builder resqBuilder =  User.FindCouponRequest.newBuilder();
        resqBuilder.setUserId(userId);
        resqBuilder.setMerchantCouponId(merchant_coupon_id);



        return invokeWithReflect(User.FindCouponResponse.class, url, resqBuilder);


    }

    public User.ShowCardsResponse showCardsRequest(long userId) throws CException {
        String url=HttpUrl.showCardsRequest();

        User.ShowCardsRequest.Builder resqBuilder =  User.ShowCardsRequest.newBuilder();
        resqBuilder.setUserId(userId);
        resqBuilder.setIndex(0);
        resqBuilder.setCount(Integer.MAX_VALUE);


        return invokeWithReflect(User.ShowCardsResponse.class, url, resqBuilder);

    }


    public User.DeleteCardsResponse deleteCards(long userId,List<Long >cardIds) throws CException {

        String url=HttpUrl.deleteCards();

        User.DeleteCardsRequest.Builder resqBuilder =  User.DeleteCardsRequest.newBuilder();
        resqBuilder.setUserId(userId);
        resqBuilder.addAllCardIds(cardIds);
        return invokeWithReflect(User.DeleteCardsResponse.class, url, resqBuilder);

    }

    /**
     * 用户使用优惠券（商家扫码）
     * @param userId
     * @param qr_code
     * @return
     * @throws CException
     */
    public User.UseCardResponse useCard(long userId,String qr_code) throws CException {

        String url=HttpUrl.useCard();

        User.UseCardRequest.Builder resqBuilder =  User.UseCardRequest.newBuilder();
        resqBuilder.setUserId(userId);
        resqBuilder.setQrCode(qr_code);
        return invokeWithReflect(User.UseCardResponse.class, url, resqBuilder);

    }


    /**
     *  商户用户查询商家信息（同时显示优惠券信息）
     * @param userId
     * @return
     */
    public Merchant.GetMerchantInfoResponse getMerchantInfo(long userId) throws CException {
        String url=HttpUrl.getMerchantInfo();

        Merchant.GetMerchantInfoRequest.Builder resqBuilder =  Merchant.GetMerchantInfoRequest.newBuilder();
        resqBuilder.setUserId(userId);

        return invokeWithReflect(Merchant.GetMerchantInfoResponse.class, url, resqBuilder);
    }


    /**
     *  商家认领
     * @param userId
     * @param merchantId  商户id
     * @return
     * @throws CException
     */
    public User.IdentifyMerchantResponse identifyMerchant(long userId,long merchantId) throws CException {

        String url=HttpUrl.identifyMerchant();

        User.IdentifyMerchantRequest.Builder resqBuilder =  User.IdentifyMerchantRequest.newBuilder();
        resqBuilder.setUserId(userId);
        resqBuilder.setMerchantId(merchantId);

        return invokeWithReflect(User.IdentifyMerchantResponse.class, url, resqBuilder);

    }


    /**
     *  上传认证资料
     * @param userId
     * @param id_card //身份证正面
     * @param shop_card //营业执照
     * @return
     */
    public User.UploadIdentifyMerchantResponse uploadIdentifyMerchant(long userId, byte[] id_card,byte[] shop_card,String posCode ) throws CException {

        String url=HttpUrl.uploadIdentifyMerchant();

        User.UploadIdentifyMerchantRequest.Builder resqBuilder =  User.UploadIdentifyMerchantRequest.newBuilder();
        resqBuilder.setUserId(userId);
        resqBuilder.setBusinessLicence(ByteString.copyFrom(shop_card));
        resqBuilder.setIdentityCardFront(ByteString.copyFrom(id_card));
        resqBuilder.setPosCode(posCode);

        return invokeWithReflect(User.UploadIdentifyMerchantResponse.class, url, resqBuilder);

    }

    public User.AddMerchantCouponResponse addMerchantCoupon(long userId, long merchantId, String couponName, long endDateValue ) throws CException {

        return  addMerchantCoupon(userId, merchantId, couponName, endDateValue, null, null);

    }



    public User.AddMerchantCouponResponse addMerchantCoupon(long userId, long merchantId, String couponName, long endDateValue,Common.EffectivePeriodType type,List<Integer> dates) throws CException {

        String url=HttpUrl.addMerchantCoupon();
        User.AddMerchantCouponRequest.Builder resqBuilder =  User.AddMerchantCouponRequest.newBuilder();
        resqBuilder.setUserId(userId);
        resqBuilder.setMerchantId(merchantId);
        resqBuilder.setCouponName(couponName);
        resqBuilder.setEndDate(endDateValue);



        User.EffectivePeriod period = null;
        if(type!=null) {


            List<Integer> addOneList=new ArrayList<>();
            for(int i:dates)
            {
                addOneList.add(i+1);
            }
            User.EffectivePeriod.Builder builder = User.EffectivePeriod.newBuilder();
            switch (type) {
                case EffectivePeriodType_WEEK:
                    builder.setEffectivePeriodType(type);
                    builder.addAllDayOfWeeks(addOneList);
                    period = builder.build();
                    break;

                case EffectivePeriodType_MONTH:
                    builder.setEffectivePeriodType(type);
                    builder.addAllDayOfMonth(addOneList);
                    period = builder.build();
                    break;
            }
        }

        if(period!=null)
            resqBuilder.setEffectivePeriod(period);
        return invokeWithReflect(User.AddMerchantCouponResponse.class, url, resqBuilder);

    }


    /**
     * 添加商户收藏
     * @param userId
     * @param merchantId
     * @return
     * @throws CException
     */
    public User.AddFavoriteResponse addFavorite(long userId, long merchantId) throws CException {

        String url=HttpUrl.addFavorite();
        User.AddFavoriteRequest.Builder resqBuilder =  User.AddFavoriteRequest.newBuilder();
        resqBuilder.setUserId(userId);
        resqBuilder.setMerchantId(merchantId);

        return invokeWithReflect(User.AddFavoriteResponse.class, url, resqBuilder);

    }




    public User.GetIdentifyMerchantInfoResponse getIndetifyMerchantInfo(long userId) throws CException {

        String url=HttpUrl.getIndetifyMerchantInfo();
        User.GetIdentifyMerchantInfoRequest.Builder resqBuilder =  User.GetIdentifyMerchantInfoRequest.newBuilder();
        resqBuilder.setUserId(userId);

        return invokeWithReflect(User.GetIdentifyMerchantInfoResponse.class, url, resqBuilder);

    }


    /**
     * 删除收藏
     * @param userId
     * @param merchantIds
     * @return
     * @throws CException
     */
    public User.DeleteFavoriteResponse deleteFavorite(long userId, List<Long> merchantIds) throws CException {


        String url=HttpUrl.deleteFavorite();
        User.DeleteFavoriteRequest.Builder resqBuilder =  User.DeleteFavoriteRequest.newBuilder();
        resqBuilder.setUserId(userId);
        resqBuilder.addAllMerchantIds(merchantIds);

        return invokeWithReflect(User.DeleteFavoriteResponse.class, url, resqBuilder);
    }

    /**优惠券上下架
     *
     * @param userId
     * @param merchantCouponId
     * @param isUse
     * @return
     * @throws CException
     */
    public User.CouponOffOrUpShelvesResponse couponOffOrUpShelves(long userId, long  merchantCouponId,Common.BooleanValue isUse) throws CException {


        String url=HttpUrl.couponOffOrUpShelves();
        User.CouponOffOrUpShelvesRequest.Builder resqBuilder =  User.CouponOffOrUpShelvesRequest.newBuilder();
        resqBuilder.setUserId(userId);
        resqBuilder.setIsUse(isUse);
        resqBuilder.setMerchantCouponId(merchantCouponId);

        return invokeWithReflect(User.CouponOffOrUpShelvesResponse.class, url, resqBuilder);
    }



    /**优惠券上下架
     *
     * @param userId
     * @param content
     * @param title
     * @return
     * @throws CException
     */
    public User.AddFeedbackResponse addFeedback(long userId, String content   ,String title) throws CException {


        String url=HttpUrl.addFeedback();
        User.AddFeedbackRequest.Builder resqBuilder =  User.AddFeedbackRequest.newBuilder();
        resqBuilder.setUserId(userId);
        resqBuilder.setContent(content);
        resqBuilder.setContent(title);

        return invokeWithReflect(User.AddFeedbackResponse.class, url, resqBuilder);
    }


    /**
     *   PATH: /merchant/userGetMerchantInfo
     *   普通用户查询商家信息
     * @return
     */
    public Merchant.UserGetMerchantInfoResponse userGetMerchantInfo(long userId, long merchantId) throws CException {

        String url=HttpUrl.userGetMerchantInfo();
        Merchant.UserGetMerchantInfoRequest.Builder resqBuilder =   Merchant.UserGetMerchantInfoRequest.newBuilder();
        resqBuilder.setUserId(userId);
        resqBuilder.setMerchantId(merchantId);


        return invokeWithReflect(Merchant.UserGetMerchantInfoResponse.class, url, resqBuilder);
    }


    /**
     *
     * @return
     */
    public Circle.ReplyResponse reply(long userId,long ownerId,long messageId,String content) throws CException {




        String url=HttpUrl.reply();
        Circle.ReplyRequest.Builder resqBuilder =   Circle.ReplyRequest.newBuilder();
        resqBuilder.setUserId(userId);
        resqBuilder.setReplyUserId(ownerId);
        resqBuilder.setMessageId(messageId);
        resqBuilder.setContent(content);
        return invokeWithReflect(Circle.ReplyResponse.class, url, resqBuilder);


    }


    /**
     * 查询消息列表
     * @param userId
     * @param index
     * @param count
     * @return
     * @throws CException
     */
    public Circle.GetOwnMessagesResponse getOwnMessages(long userId,int index,int count) throws CException {

        String url=HttpUrl.getOwnMessages();
        Circle.GetOwnMessagesRequest.Builder resqBuilder =   Circle.GetOwnMessagesRequest.newBuilder();
        resqBuilder.setUserId(userId);
        resqBuilder.setIndex(index * count);
        resqBuilder.setCount(count);

        return invokeWithReflect(Circle.GetOwnMessagesResponse.class, url, resqBuilder);




    }


    public Circle.GetOneMessageResponse getOneMessage(long messageId) throws CException {


        String url=HttpUrl.getOneMessage();
        Circle.GetOneMessageRequest.Builder resqBuilder =   Circle.GetOneMessageRequest.newBuilder();
        resqBuilder.setMessageId(messageId);


        return invokeWithReflect(Circle.GetOneMessageResponse.class, url, resqBuilder);

    }

    public User.ForgetPasswordResponse forgetPassword(String  code,String phone, String password) throws CException {
        String url=HttpUrl.forgetPassword();
        User.ForgetPasswordRequest.Builder resqBuilder =  User.ForgetPasswordRequest.newBuilder();
        resqBuilder.setCode(code);
        resqBuilder.setMobileNum(phone);
        resqBuilder.setPassword(password);

        return invokeWithReflect(User.ForgetPasswordResponse.class, url, resqBuilder);
    }


    /**
     * 获取新消息列表
     * @param userId
     * @return
     */
    public Circle.GetNewRepliesResponse getNewReplies(long userId,int index, int count) throws CException {
        String url=HttpUrl.getNewReplies();
        Circle.GetNewRepliesRequest.Builder resqBuilder =  Circle.GetNewRepliesRequest.newBuilder();
        resqBuilder.setUserId(userId);
        resqBuilder.setCount(count);
        resqBuilder.setIndex(index * count);
        return invokeWithReflect(Circle.GetNewRepliesResponse.class, url, resqBuilder);
    }


    /**
     * 获最新消息个数
     * @param userId
     * @return
     */
    public Circle.GetNewRepliesCountResponse getNewRepliesCount(long userId) throws CException {
        String url=HttpUrl.getNewRepliesCount();
        Circle.GetNewRepliesCountRequest.Builder resqBuilder = Circle.GetNewRepliesCountRequest.newBuilder();
        resqBuilder.setUserId(userId);



        return invokeWithReflect(Circle.GetNewRepliesCountResponse.class, url, resqBuilder);
    }

    /**
     * 用户系统消息未读条数
     * @param userId
     * @return
     */
    public Circle.UserSystemMessagesNotReadCountResponse userSystemMessagesNotReadCount(long userId) throws CException {
        String url=HttpUrl.userSystemMessagesNotReadCount();
        Circle.UserSystemMessagesNotReadCountRequest.Builder resqBuilder = Circle.UserSystemMessagesNotReadCountRequest.newBuilder();
        resqBuilder.setUserId(userId);

        return invokeWithReflect(Circle.UserSystemMessagesNotReadCountResponse.class, url, resqBuilder);
    }



    /**
     * 用户系统消息未读条数
     * @param userId
     * @return
     */
    public Circle.UserGetSystemMessagesResponse userGetSystemMessages(long userId,int pageIndex, int pageCount) throws CException {
        String url=HttpUrl.userGetSystemMessages();
        Circle.UserGetSystemMessagesRequest.Builder resqBuilder = Circle.UserGetSystemMessagesRequest.newBuilder();
        resqBuilder.setUserId(userId);
        resqBuilder.setIndex(pageIndex*pageCount);
        resqBuilder.setCount(pageCount);

        return invokeWithReflect(Circle.UserGetSystemMessagesResponse.class, url, resqBuilder);
    }
}
