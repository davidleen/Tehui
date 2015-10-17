//package com.davidleen29.tehui.adapters;
//
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.davidleen29.tehui.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by davidleen29 on 2015/7/19.
// */
//public abstract class BaseRecycleAdapter<T,H extends RecyclerView.ViewHolder>  extends RecyclerView.Adapter<H> {
//
//
//
//    List<T> mDatas;
//
//    public BaseRecycleAdapter()
//    {
//
//        mDatas=new ArrayList<T>();
//    }
//
//
//    public void setDataArray(List<T> datas)
//
//    {
//        mDatas.clear();
//        mDatas.addAll(datas);
//        notifyDataSetChanged();
//
//    }
//
//    @Override
//    public void onBindViewHolder(H holder, int position) {
//
//
//        onBindViewHolder(holder, mDatas.get(position),position);
//
//    }
//
//
//
//
//    // Replace the contents of a view (invoked by the layout manager)
//
//    public abstract void onBindViewHolder(H holder,T data, int position)  ;
//
//    // Return the size of your dataset (invoked by the layout manager)
//    @Override
//    public int getItemCount() {
//        return mDatas.size();
//    }
//
//
//}
