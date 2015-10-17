package com.davidleen29.tehui.acts;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.davidleen29.tehui.R;
import com.huiyou.dp.service.protocol.Common;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_sex_select)
public class SexSelectActivity extends BaseActivity {

    public static final String EXTRA_SEX = "EXTRA_SEX";



    @InjectView(R.id.male)
    View male;

    @InjectView(R.id.female)
    View female;

    @InjectExtra(value = EXTRA_SEX,optional = true)
    Common.UserSex userSex;
    @Override
    protected void init(Bundle bundle) {


        male.setSelected(userSex== Common.UserSex.UserSex_MALE);
        female.setSelected(userSex== Common.UserSex.UserSex_FEMALE);
        male.setOnClickListener(this);
        female.setOnClickListener(this);

    }

    @Override
    protected void onViewClick(View v, int id) {
        Intent intent=getIntent();
        switch (id)
        {
            case R.id.female:

                intent.putExtra(EXTRA_SEX, Common.UserSex.UserSex_FEMALE);
                setResult(RESULT_OK, intent);
                finish();

                break;
            case R.id.male:
                intent.putExtra(EXTRA_SEX, Common.UserSex.UserSex_MALE);
                setResult(RESULT_OK,intent);
                finish();
                break;
        }



    }
}
