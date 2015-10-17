package com.davidleen29.tehui.acts;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.widget.HeaderView;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_text_edit)
public class TextEditActivity extends BaseActivity {


       public   static  final String EXTRA_CONTENT="EXTRA_CONTENT";

    public   static  final String EXTRA_TITLE="EXTRA_TITLE";

    @InjectView(R.id.content)
    EditText content;


    @InjectView(R.id.delete)
    View delete;

    @InjectView(R.id.header)
    HeaderView header;

    @InjectExtra(value = EXTRA_CONTENT,optional = true)
    String contentValue;

    @InjectExtra(value = EXTRA_TITLE,optional = true)
    String titleValue;

    @Override
    protected void init(Bundle bundle) {


        if(contentValue!=null)
        content.setText(contentValue);
        if(titleValue!=null)
        header.setTitle(titleValue);

        header.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=getIntent();
                intent.putExtra(EXTRA_CONTENT, content.getText().toString().trim());
                setResult(RESULT_OK,intent);
                finish();

            }
        });

        delete.setOnClickListener(this);


    }


    @Override
    protected void onViewClick(View v, int id) {



        switch (id)
        {
            case R.id.delete:


                content.setText("");
                break;
        }
    }
}
