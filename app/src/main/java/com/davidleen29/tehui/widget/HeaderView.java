package com.davidleen29.tehui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.helper.ImageLoaderHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * TODO: document your custom view class.
 */
public class HeaderView extends LinearLayout {
    private String mTitle; // TODO: use a default from R.string...
    private String mTabTitle;
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable rightIcon1Drawable;
    private Drawable rightIcon2Drawable;
    private Drawable leftIconDrawable;
    private Drawable mHeadTitleLeftIconDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;
    private String mRightText;

    private TextView titleView;
    private ImageView rightIcon1;
    private ImageView rightIcon2;
    private TextView text_right;
    private ImageView  leftIcon;
    private View tabTitle;
    private  TextView[] tabTitles;
    private TextView tabTitle1;
    private TextView tabTitle2;
    private TextView tabTitle3;


    public HeaderView(Context context) {
        super(context);
        init(null, 0);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {


        //make  customerView;
        LayoutInflater inflater=LayoutInflater.from(getContext());
        this.addView(inflater.inflate(R.layout.view_header, null));
        titleView= (TextView) findViewById(R.id.title);
        rightIcon1= (ImageView) findViewById(R.id.icon_right_1);
        rightIcon2= (ImageView) findViewById(R.id.icon_right_2);
        text_right= (TextView) findViewById(R.id.text_right);
        leftIcon= (ImageView) findViewById(R.id.icon_left);

        tabTitle=   findViewById(R.id.tabTitle);

        tabTitle1= (TextView) findViewById(R.id.tabTitle1);

        tabTitle2= (TextView) findViewById(R.id.tabTitle2);
        tabTitle3= (TextView) findViewById(R.id.tabTitle3);
        tabTitles=new TextView[]{tabTitle1,tabTitle2,tabTitle3};

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.HeaderView, defStyle, 0);


        if(a.hasValue(R.styleable.HeaderView_headTitle)) {
            mTitle
                    = a.getString(
                    R.styleable.HeaderView_headTitle);
        }

        if(a.hasValue(R.styleable.HeaderView_tabTitle)) {
            mTabTitle
                    = a.getString(
                    R.styleable.HeaderView_tabTitle);
        }

        if(a.hasValue(R.styleable.HeaderView_headTitleLeftIcon)) {
            mHeadTitleLeftIconDrawable
                    = a.getDrawable(
                    R.styleable.HeaderView_headTitleLeftIcon);
             mHeadTitleLeftIconDrawable.setCallback(this);
        }



        if (a.hasValue(R.styleable.HeaderView_rightText)) {
            mRightText
                    = a.getString(
                    R.styleable.HeaderView_rightText);
        }
        mExampleColor = a.getColor(
                R.styleable.HeaderView_exampleColor,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.HeaderView_exampleDimension,
                mExampleDimension);



        if (a.hasValue(R.styleable.HeaderView_rightIcon1)) {
            rightIcon1Drawable = a.getDrawable(
                    R.styleable.HeaderView_rightIcon1);
            rightIcon1Drawable.setCallback(this);
        }

        if (a.hasValue(R.styleable.HeaderView_rightIcon2)) {
            rightIcon2Drawable = a.getDrawable(
                    R.styleable.HeaderView_rightIcon2);
            rightIcon2Drawable.setCallback(this);
        }


        if (a.hasValue(R.styleable.HeaderView_leftIcon)) {
            leftIconDrawable = a.getDrawable(
                    R.styleable.HeaderView_leftIcon);
            leftIconDrawable.setCallback(this);
        }




        a.recycle();








        configVaule();
    }

    private void configVaule()
    {
        if(mTitle!=null)
            titleView.setText(mTitle);
        if(mHeadTitleLeftIconDrawable!=null)
        {
            titleView.setCompoundDrawablesWithIntrinsicBounds(mHeadTitleLeftIconDrawable,null,null,null);
           // titleView.setCompoundDrawables(mHeadTitleLeftIconDrawable,null,null,null);
        }
        if(rightIcon1!=null)
        rightIcon1.setImageDrawable(rightIcon1Drawable);
        if(rightIcon2!=null)
        rightIcon2.setImageDrawable(rightIcon2Drawable);
        if(leftIconDrawable!=null)
            leftIcon.setImageDrawable(leftIconDrawable);

        text_right.setText(mRightText == null ? "" : mRightText);
        if(mTabTitle!=null)
        {

            String[] tabs=mTabTitle.split(",");


          int tabLength=  tabs.length;



            for (int i = 0,count= tabTitles.length; i < count; i++)
            {
                final int index=i;
                TextView aTab=tabTitles[i];

                if(i<tabLength)
                {

                    aTab.setText(tabs[i]);
                }
                aTab.setVisibility(i<tabLength?View.VISIBLE: View.GONE);
                aTab.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        for(View tab:tabTitles)
                        {
                            tab.setSelected(tab==v);
                        }
                        if(onTabClickListener!=null)
                        {
                            onTabClickListener.onTabClick(tabTitles, index);
                        }
                    }
                });
            }

        }


        rightIcon1.setVisibility(rightIcon1Drawable==null? View.GONE:View.VISIBLE);
        rightIcon2.setVisibility(rightIcon2Drawable==null? View.GONE:View.VISIBLE);
        text_right.setVisibility(mRightText==null? View.GONE:View.VISIBLE);
        titleView.setVisibility(mTitle==null? View.GONE:View.VISIBLE);
        tabTitle.setVisibility(mTabTitle==null? View.GONE:View.VISIBLE);





        //default   back

        leftIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                if(v.getContext() instanceof Activity)
                {

                    ((Activity)v.getContext()).onBackPressed();
                }

            }
        });



    }





    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param title The title attribute value to use.
     */
    public void setTitle(String title) {
        mTitle = title;
        titleView.setText(title );

    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getExampleColor() {
        return mExampleColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;

    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getExampleDimension() {
        return mExampleDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;

    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return rightIcon1Drawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        rightIcon1Drawable = exampleDrawable;
    }

    public void performTabClick(int index) {

        if(index>=0&&index< tabTitles.length)
        {

            tabTitles[index].performClick();
        }
    }

    public void setOnRight1ClickListener(OnClickListener listener) {
        rightIcon1.setOnClickListener(listener);
    }

    public void setOnRightTextClickListener(OnClickListener listener) {


        text_right.setOnClickListener(listener);
    }

    public void setRight1Selected(boolean b) {
        rightIcon1.setSelected(b);
    }

    public void setRightText(String newVavlue) {

        mRightText=newVavlue;
        text_right.setText(newVavlue);

    }

    public void setOnRight2ClickListener(OnClickListener onClickListener) {

        rightIcon2.setOnClickListener(onClickListener);
    }

    public int getCurrentHeadTabIndex() {


        for (int i = 0; i < tabTitles.length; i++) {
          if( tabTitles[i].isSelected())
              return i;
        }
        return -1;
    }

    public void performRight1Click() {

        rightIcon1.performClick();

    }


    public interface OnTabClickListener
    {

        public void onTabClick(View[] tabs,int clickIndex);

    }




    protected OnTabClickListener onTabClickListener;



    public void setOnTabClickListener(OnTabClickListener onTabClickListener) {
        this.onTabClickListener = onTabClickListener;
    }



    public void  setOnLeftClickListener(OnClickListener onLeftClickListener)
    {
        leftIcon.setOnClickListener(onLeftClickListener);
    }
    public void  setHeadTitleClickListener(OnClickListener onLeftClickListener)
    {
        titleView.setOnClickListener(onLeftClickListener);
    }

    public void setLeftIconUrl(String url)
    {

        ImageLoader.getInstance().displayImage(url,leftIcon, ImageLoaderHelper.getCircleDisplayOptions());
    }


}
