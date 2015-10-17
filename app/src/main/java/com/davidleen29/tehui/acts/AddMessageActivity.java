package com.davidleen29.tehui.acts;

import android.content.Intent;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.api.ApiManager;
import com.davidleen29.tehui.events.MessageSendEvent;
import com.davidleen29.tehui.helper.ImageLoaderHelper;
import com.davidleen29.tehui.helper.PictureHelper;
import com.davidleen29.tehui.tasks.ThTask;
import com.davidleen29.tehui.utils.StringUtils;
import com.davidleen29.tehui.utils.ToastUtils;
import com.davidleen29.tehui.widget.HeaderView;

import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Circle;
import com.huiyou.dp.service.protocol.Common;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

/**
 * 发布消息界面
 */
@ContentView(R.layout.activity_add_message)
public class AddMessageActivity extends BaseActivity {


    public static final String EXTRA_MESSAGE_TYPE = "EXTRA_MESSAGE_TYPE";
    public static final String EXTRA_CONTENT = "EXTRA_CONTENT";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_MERCHANT_ID = "EXTRA_MERCHANT_ID";
    @InjectView(R.id.header)
    HeaderView headerView;

    @InjectView(R.id.message)
    EditText message;


    @InjectView(R.id.addPicture)
    View addPicture;


    @InjectView(R.id.ll_picture1)
    View ll_picture1;
    @InjectView(R.id.image1)
    ImageView image1;
    @InjectView(R.id.delete1)
    View delete1;

    @InjectView(R.id.ll_picture2)
    View ll_picture2;
    @InjectView(R.id.image2)
    ImageView image2;
    @InjectView(R.id.delete2)
    View delete2;



    @InjectView(R.id.ll_picture3)
    View ll_picture3;
    @InjectView(R.id.image3)
    ImageView image3;
    @InjectView(R.id.delete3)
    View delete3;


    @Inject
    ApiManager apiManager;



    View[] ll_pictures;
    ImageView[] images;

    List<String> picturePaths=new ArrayList<>();
    public static  final int MAX_PICTURE_COUNT=1;


    @InjectExtra(value = EXTRA_MESSAGE_TYPE ,optional = true)
    public Common.CircleMessageType type=Common.CircleMessageType.CircleMessageType_FREEDOM;

    @InjectExtra(value = EXTRA_CONTENT ,optional = true)
    public String content;
    @InjectExtra(value = EXTRA_TITLE ,optional = true)
    public String messageTitle;

    @InjectExtra(value =EXTRA_MERCHANT_ID ,optional = true)
    public long merchantId;

    @Override
    protected void init(Bundle bundle) {
        super.init(bundle);



         addPicture.setOnClickListener(this);
        registerForContextMenu(addPicture);
        delete1.setOnClickListener(this);
        delete2.setOnClickListener(this);
        delete3.setOnClickListener(this);
        ll_pictures=new View[]{ll_picture1,ll_picture2,ll_picture3};
        images=new ImageView[]{image1,image2,image3};


        headerView.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        } );




        initValue();

    }

    private void  initValue()
    {

        if(!StringUtils.isEmpty(content))
        message.setText(content);

        invalidatePictures();
    }

    @Override
    protected void onViewClick(View v, int id) {


        switch (id)
        {
            case R.id.addPicture:



                openContextMenu(addPicture);


            break;


            case R.id.delete1:
                picturePaths.remove(0);
                invalidatePictures();
                break;
            case R.id.delete2:
                picturePaths.remove(1);
                invalidatePictures();
                break;
            case R.id.delete3:
                picturePaths.remove(2);
                invalidatePictures();
                break;
        }
    }








    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {




        PictureHelper.PickData pickData=   PictureHelper.onResult(AddMessageActivity.this, requestCode, resultCode, data);

        if(pickData!=null)
        {
            picturePaths.add(Uri.fromFile(new File(pickData.localPath)).toString());
            invalidatePictures();

        }

    }




    private void invalidatePictures()
    {

        int pathSize=picturePaths.size();
        for(int i=0,count=ll_pictures.length ;i<count;i++)
        {


            ll_pictures[i].setVisibility(i < pathSize ? View.VISIBLE : View.GONE);
            if(i<pathSize)
            ImageLoader.getInstance().displayImage(picturePaths.get(i), images[i], ImageLoaderHelper.getLocalDisplayOptions());

        }

        addPicture.setVisibility(pathSize==MAX_PICTURE_COUNT? View.GONE:View.VISIBLE);



    }


    /**
     * 发布消息
     */
    private void sendMessage()
    {


       final  String text=message.getText().toString().trim();






        new ThTask<Circle.SendMessageResponse>(this)
        {
            @Override
            public Circle.SendMessageResponse call() throws Exception {

                byte[] data=null;

                if(picturePaths.size()>0) {
                    Bitmap bitmap = ImageLoader.getInstance().loadImageSync(picturePaths.get(0), new ImageSize(1920, 1080), ImageLoaderHelper.getLocalDisplayOptions());

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                    data = bos.toByteArray();
                    bos.close();
                }
              return  apiManager.sendMessage(text,data,type,messageTitle,merchantId);


            }

            @Override
            protected void doOnSuccess(Circle.SendMessageResponse data) {


                if(data.getErrCode()== Circle.SendMessageResponse.ErrorCode.ERR_OK)
                {


                    ToastUtils.show("发布成功");
                    EventBus.getDefault().post(new MessageSendEvent());
                    finish();

                }else
                {
                    ToastUtils.show(data.getErrMsg());
                }

            }
        }.execute();


    }
}
