package com.davidleen29.tehui.acts;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.api.ApiManager;
import com.davidleen29.tehui.helper.CacheManager;
import com.davidleen29.tehui.tasks.ThTask;
import com.davidleen29.tehui.utils.StringUtils;
import com.davidleen29.tehui.utils.ToastUtils;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Common;
import com.huiyou.dp.service.protocol.User;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;


@ContentView(R.layout.activity_comment)
public class CommentActivity extends BaseActivity {

    @InjectView(R.id.title1)
    TextView title1;
 @InjectView(R.id.title2)
    TextView title2;
 @InjectView(R.id.title3)
    TextView title3;
 @InjectView(R.id.title4)
    TextView title4;
     @InjectView(R.id.content)
     EditText content;

    @InjectView(R.id.btn_comment)
    View btn_comment;

    @Inject
    ApiManager apiManager;

    @Inject
    CacheManager cacheManager;

    TextView[] titles;
    @Override
    protected void init(Bundle bundle) {


        titles=new TextView[]{title1,title2,title3,title4};

        int length = titles.length;
        for (int i = 0; i < length; i++) {
            titles[i].setOnClickListener(this);
        }

        btn_comment.setOnClickListener(this);



    }

    @Override
    protected void onViewClick(View v, int id) {

        switch (id)
        {

            case R.id.title1:
            case R.id.title2:
            case R.id.title3:
            case R.id.title4:

                for(TextView tv:titles)
                {
                    tv.setSelected(tv==v);
                }

                break;

            case R.id.btn_comment:

                comment();

                break;
        }
    }

    private void comment() {


        String title="";

        for(TextView tv:titles)
        {
            if(tv.isSelected())
            {
                title=tv.getText().toString();
                break;
            }
        }

        if(StringUtils.isEmpty(title))
        {
            ToastUtils.show("请选择一项标题");
            return;
        }

        final String contentValue=content.getText().toString().trim();

        final String finalTitle = title;
        new   ThTask<User.AddFeedbackResponse>(this) {
            @Override
            protected void doOnSuccess(User.AddFeedbackResponse data) {

                if(data.getErrCode()==User.AddFeedbackResponse.ErrorCode.ERR_OK)
                {

                    ToastUtils.show("提交成功， 感谢您的反馈");
                    finish();


                }else
                {

                            ToastUtils.show(data.getErrMsg());




                }

            }

            @Override
            public User.AddFeedbackResponse call() throws Exception {
                return apiManager.addFeedback(cacheManager.getCurrentUser().getUserId(), finalTitle
                        ,contentValue);
            }


        }.execute();



    }
}
