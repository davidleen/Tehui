package com.davidleen29.tehui.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;

import com.davidleen29.tehui.BuildConfig;
import com.davidleen29.tehui.lang.CException;
import com.davidleen29.tehui.utils.ToastUtils;

import roboguice.util.RoboAsyncTask;

/**
 * 任务基类  封装不定进度条 提供异常回调接口
 *
 *
 */
public abstract class ThTask<T> extends RoboAsyncTask<T> {


    protected ProgressDialog mPd = null;



    //判断是否后台运行，不显示进度条
    private boolean silence = false;

    private String  mMessage="";






    /**
     * @param context {当前活动}
     *
     */
    public ThTask(Context context ) {
        this(context, "数据加载中....");


    }


    /**
     * @param context {当前活动}
     * @param mesage   {提示信息}
     */
    public ThTask(Context context,String mesage ) {
        super(context);
        this.mMessage=mesage;

    }


    /**
     * @param context {当前活动}
     *
     */
    public ThTask(Context context,  boolean silence ) {
        super(context);

        this.silence=silence;


    }


    @Override
    protected void onPreExecute() throws Exception {
        super.onPreExecute();
        showProgressDialog();
    }




    @Override
    protected void onException(Exception e) throws RuntimeException {
        super.onException(e);

        hideProgressDialog();


        if(e instanceof CException) {

            CException mtException = (CException) e;




            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }


                ToastUtils.show( mtException.getMessage());

        }else
        {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void onInterrupted(Exception e) {
        super.onInterrupted(e);
        hideProgressDialog();

    }


    @Override
    protected void onSuccess(T data) throws Exception {

        hideProgressDialog();
        doOnSuccess(data);
    }


    protected abstract   void doOnSuccess(T data);


    public void setSilence(boolean silence) {
        this.silence = silence;
    }

    private void showProgressDialog() {
        if (!silence) {
            if (mPd == null) {


                if (context != null && mMessage!=null) {
                    mPd = new ProgressDialog(context);
                    mPd.setCancelable(false);
                    mPd.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode,
                                             KeyEvent event) {
                            // 屏蔽Search键(因为按搜索按键会被关闭)
                            switch (event.getKeyCode()) {
                                case KeyEvent.KEYCODE_SEARCH:
                                    return true;
                                case KeyEvent.KEYCODE_BACK:
                                    // cancelTask();
                                    // cancelConfirm();
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    mPd.setMessage(mMessage);
                }

            }
            mPd.show();

        }

    }

    private void hideProgressDialog() {
        if (!silence) {
            if ((mPd != null) && (mPd.isShowing())) {
                mPd.dismiss();
            }

        }

    }





    public   interface TaskCallBack<T>
    {

        public void  onSuccess(T config);

    }


}
