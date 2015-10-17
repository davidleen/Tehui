package com.davidleen29.tehui.adapters;



    import java.util.ArrayList;
    import java.util.List;

    import android.content.Context;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.BaseAdapter;

    import com.davidleen29.tehui.lang.UnMixable;
    import com.davidleen29.tehui.lang.ViewUtil;
    import com.huiyou.dp.service.protocol.Circle;

/**
     * a abstract class do some job to reduce code about bind view to holder and
     * easy on use baseAdapter
     *
     * @author davidleen29
     * @创建时间 2013年11月14日
     * @param <D>
     */
    public abstract class AbstractAdapter<D> extends BaseAdapter {

        protected Context context;
        protected String TAG;
        protected LayoutInflater inflater;
        protected List<D> datas;

        public AbstractAdapter(Context context) {
            this(context, null);
        }

        /**
         * set the datas Arrays and notify update
         *
         * @param arrayData
         */
        public void setDataArray(List<D> arrayData) {
            datas.clear();
            if (arrayData != null) {
                datas.addAll(arrayData);
            }

            notifyDataSetChanged();
        }

        /**
         * constructor
         *
         * @param context
         * @param initDatas
         */
        public AbstractAdapter(Context context, List<D> initDatas) {

            this.context = context;
            TAG = this.getClass().getName();
            if (initDatas == null) {
                this.datas = new ArrayList<D>();
            } else {
                this.datas = initDatas;
            }

            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {

            return datas.size();
        }

        @Override
        public D getItem(int position) {

            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }




            public void addItem(D data)
            {

                datas.add(data);
            }


        public void removeItem(D data)
        {

            datas.remove(data);
        }




        /**
         * get the binder base on viewType;
         * 获取对应itemType的控件集合绑定工具
         *
         * @param itemViewType
         * @return
         */
        protected abstract UnMixable createViewHolder(int itemViewType);

        @SuppressWarnings("unchecked")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int type = getItemViewType(position);

            Bindable<D> holder;
            if (convertView == null) {


                UnMixable obj = createViewHolder(type);
                convertView= ViewUtil.getViewByAnnotate(obj, context);
                convertView.setTag(obj);
            }
            holder = (Bindable<D>) convertView.getTag();
            D data = getItem(position);

            holder.bindData(this,data, position);
            return convertView;

        }

    public List<D> getDatas() {
        return datas;
    }

    public   void addDataArray(List<D> arrayData)
    {

        if (arrayData != null) {
            datas.addAll(arrayData);
        }

        notifyDataSetChanged();

    }

    /**
         * a bindable interface with a bindData method
         * 数据绑定类 包含绑定的控件成员 绑定方法。
         *
         * @author davidleen29
         * @创建时间 2013年11月13日
         * @param <D>
         */


        public interface Bindable<D> extends UnMixable {
            /**
             * bind the data message
             *
             * @param data
             *            the data params
             * @param position
             *            the position of data in the datasArray
             */
            public abstract void bindData(AbstractAdapter<D> adapter,D data, int position);

        }

        protected Context getContext() {
            return context;
        }
}
