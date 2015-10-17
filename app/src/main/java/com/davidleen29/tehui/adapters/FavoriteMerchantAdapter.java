//package com.davidleen29.tehui.adapters;
//
//
//import android.content.Context;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.davidleen29.tehui.R;
//import com.davidleen29.tehui.api.HttpUrl;
//import com.davidleen29.tehui.lang.ResId;
//import com.davidleen29.tehui.lang.UnMixable;
//import com.google.inject.Inject;
//import com.huiyou.dp.service.protocol.Common;
//import com.huiyou.dp.service.protocol.Merchant;
//import com.huiyou.dp.service.protocol.User;
//import com.nostra13.universalimageloader.core.ImageLoader;
//
///**
// * 收藏商户适配
// */
//
//
//public class FavoriteMerchantAdapter extends AbstractAdapter<User.FavoriteInfo> {
//    @Inject
//    public FavoriteMerchantAdapter(Context context) {
//        super(context);
//    }
//
//    @Override
//    protected UnMixable createViewHolder(int itemViewType) {
//        return new ViewHolder();
//    }
//
//
//
//
//    @ResId(R.layout.list_item_favorite_info)
//    private class ViewHolder implements  Bindable<User.FavoriteInfo>
//    {
//
//        @ResId(R.id.shopPhoto)
//        ImageView shopPhoto;
//        @ResId(R.id.shopName)
//        TextView shopName;
//
//        @ResId(R.id.coupons)
//        TextView coupons;
//        @ResId(R.id.shopMap)
//        View shopMap;
//
//        @ResId(R.id.address)
//        TextView address;
//
//        @ResId(R.id.coupon_valid)
//        View coupon_valid;
//
//        @Override
//        public void bindData(AbstractAdapter<User.FavoriteInfo> adapter, User.FavoriteInfo data, int position) {
//
//
//            Merchant.MerchantInfo tehuiInfo=data.getMerchantInfo();
//            shopName.setText(tehuiInfo.getName());
//            coupons.setText(tehuiInfo.gette());
//            address.setText(tehuiInfo.getAddress());
//            coupon_valid.setVisibility(data.getIsOverdue()== Common.BooleanValue.BooleanValue_TRUE?View.VISIBLE:View.GONE);
//            shopMap.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//
//
//
//
//            ImageLoader.getInstance().displayImage(HttpUrl.image(tehuiInfo.getImg()),shopPhoto);
//
//
//
//        }
//    }
//}
