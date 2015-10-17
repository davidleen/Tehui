package com.davidleen29.tehui.frags;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.utils.StringUtils;

import roboguice.fragment.RoboDialogFragment;
import roboguice.inject.InjectView;

/**
 *
 * 确认城市对话框
 * Created by davidleen29 on 2015/7/24.
 */
public    class BaseDialogFragment extends RoboDialogFragment {


    public static final String TITLE="PARAM_CITY_TITLE";
    public static final String MESSAGE="PARAM_CITY_MESSAGE";


    public static final String BUTTON_YES="PARAM_BUTTON_YES";

    public static final String BUTTON_NO="PARAM_BUTTON_NO";



    @InjectView(R.id.title)
    TextView title;

    @InjectView(R.id.message)
    TextView message;

    @InjectView(R.id.btn_no)
    TextView btn_no;

    @InjectView(R.id.btn_yes)
    TextView btn_yes;



    String titleString;


    String messageString;


    String yesString;


    String noString;




    public static BaseDialogFragment newInstance(String title , String message,String buttonYes)
    {
      return  newInstance(title,message,buttonYes,null);
    }
    public static BaseDialogFragment newInstance(String title , String message,String buttonYes,String buttonNo)
    {
        BaseDialogFragment fragment=new BaseDialogFragment();

        fragment.setParams(title, message, buttonYes, buttonNo);

        return fragment;

    }
    public void setParams(String title , String message,String buttonYes )
    {
        setParams(title, message, buttonYes, null);

    }
    public void setParams(String title , String message,String buttonYes,String buttonNo)
    {

        Bundle bundle=new Bundle();
        if(title!=null)
            bundle.putString(TITLE, title);
        bundle.putString(MESSAGE, message);

        if(buttonYes!=null)
            bundle.putString(BUTTON_YES, buttonYes);
        if(buttonNo!=null)
            bundle.putString(BUTTON_NO, buttonNo);

        setArguments(bundle);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.appDialog);

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

      //  getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
     //  getDialog().setCancelable(false);
        setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        View view = inflater.inflate(R.layout.dialog_confirm, container);

        Bundle bundle=getArguments();
        messageString=bundle.getString(MESSAGE);
        titleString=bundle.getString(TITLE);
        yesString=bundle.getString(BUTTON_YES);
        noString=bundle.getString(BUTTON_NO);





        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title.setVisibility(StringUtils.isEmpty(titleString) ? View.GONE : View.VISIBLE);
        title.setText(StringUtils.isEmpty(titleString) ? "" : titleString);
        message.setText(StringUtils.isEmpty(messageString) ? "" : messageString);
        btn_yes
                .setText(StringUtils.isEmpty(yesString) ? "确定" : yesString);
        btn_no
                .setText(StringUtils.isEmpty(noString) ? "取消" : noString);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onYesClick(v);


            }
        });



        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (StringUtils.isEmpty(noString)) {
                    dismiss();
                } else {
                    onNoClick(v);
                }


            }
        });




    }

    protected   void onYesClick(View view) {

    }

    protected   void onNoClick(View view) {

    }

}
