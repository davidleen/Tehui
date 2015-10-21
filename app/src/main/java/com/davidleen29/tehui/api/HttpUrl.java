package com.davidleen29.tehui.api;

/**
 * 接口串串
 * Created by davidleen29 on 2015/7/19.
 */
public class HttpUrl {

    public static  final String URL_ENCODING="UTF-8";


    public static String IP="59.56.25.196";
    public static String port="80";
    public  static
    String BaseUrl="http://"+IP+":"+port+"/HuiyouService";



    public static final String WEB_HOME="http://123.59.69.223/HuiyouResource/";
    public static final String getMerchants()
    {

        return BaseUrl+"/merchant/getMerchants";

    }

    public static final String register()
    {

        return BaseUrl+"/user/register";

    }

    public static String login() {
        return BaseUrl+"/user/login";
    }

    public static String getUserSetBank() {

        return BaseUrl+"/user/getUserSetBank";
    }


    public static String userSetBank() {

        return BaseUrl+"/user/userSetBank";
    }

    public static String getUserSetCategory() {
        return BaseUrl+"/user/getUserSetCategory";
    }

    public static String userSetCategory() {
        return BaseUrl+"/user/userSetCategory";

    }

    public static String sendIdentifyingCode() {
        return BaseUrl+"/sms/sendIdentifyingCode";
    }

    public static String image(String imgUrl) {


        return BaseUrl+imgUrl;
    }

    public static String sendMessage() {

        return BaseUrl+"/circle/sendMessage";
    }

    public static String getMessages() {
        return BaseUrl+"/circle/getMessages";
    }

    public static String userSetCity() {
        return BaseUrl+"/city/userSetCity";

    }

    public static String getCities() {
        return BaseUrl+"/city/getCitys";
    }

    public static String addFavorite() {

        return BaseUrl+"/user/addFavorite";

    }


    public static String getFavorites() {

        return BaseUrl+"/user/getFavorites";

    }


    public static String deleteFavorite() {

        return BaseUrl+"/user/deleteFavorite";

    }

    public static String checkRegisterIdentifyingCode() {
        return BaseUrl+"/user/checkRegisterIdentifyingCode";
    }

    public static String getUserInfo() {
        return BaseUrl+"/user/getUserInfo";

    }


    public static String setUserInfo() {
        return BaseUrl+"/user/setUserInfo";

    }

    public static String fuzzyQueryMerchant() {
        return BaseUrl+"/merchant/fuzzyQueryMerchant";

    }

    public static String findCouponRequest() {
        return BaseUrl+"/user/findCoupon";

    }

    public static String showCardsRequest() {

        return BaseUrl+"/user/showCards";
    }

    public static String deleteCards() {

        return BaseUrl+"/user/deleteCards";
    }

    public static String useCard() {

        return BaseUrl+"/user/useCard";
    }

    public static String getMerchantInfo() {

        return BaseUrl+"/merchant/getMerchantInfo";
    }

    public static String identifyMerchant() {

        return BaseUrl+"/user/identifyMerchant";
    }

    public static String uploadIdentifyMerchant() {

        return BaseUrl+"/user/uploadIdentifyMerchant";
    }

    public static String addMerchantCoupon() {

        return BaseUrl+"/user/addMerchantCoupon";
    }

    public static String getIndetifyMerchantInfo() {
        return BaseUrl+"/user/getIdentifyMerchantInfo";
    }

    public static String couponOffOrUpShelves() {
        return BaseUrl+"/user/couponOffOrUpShelves";

    }

    public static String userGetMerchantInfo() {

        return BaseUrl+"/merchant/userGetMerchantInfo";
    }

    public static String reply() {

        return BaseUrl+"/circle/reply";
    }

    public static String getOwnMessages() {

        return BaseUrl+"/circle/getOwnMessages";
    }

    public static String addFeedback() {

        return BaseUrl+"/user/addFeedback";
    }

    public static String getOneMessage() {

        return BaseUrl+"/circle/getOneMessage";
    }

    public static String forgetPassword() {

        return BaseUrl+"/user/forgetPassword";
    }

    public static String getNewReplies() {


        return BaseUrl+"/circle/getNewReplies";
    }


    public static String getNewRepliesCount() {


        return BaseUrl+"/circle/getNewRepliesCount";
    }

    public static String getMerchantsByCoordinate() {


        return BaseUrl+"/merchant/getMerchantsByCoordinate";

    }

    public static String userSystemMessagesNotReadCount() {
        return BaseUrl+"/circle/userSystemMessagesNotReadCount";
    }

    public static String userGetSystemMessages() {

        return BaseUrl+"/circle/userGetSystemMessages";


    }
}
