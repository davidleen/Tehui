package com.davidleen29.tehui.acts;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.adapters.MerchantSearchAdapter;
import com.davidleen29.tehui.api.ApiManager;
import com.davidleen29.tehui.helper.FootViewHelper;
import com.davidleen29.tehui.tasks.ThTask;
import com.davidleen29.tehui.utils.ToastUtils;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Merchant;
import com.huiyou.dp.service.protocol.User;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * 商家查询act
 */
@ContentView(R.layout.activity_merchat_search)
public class MerchantSearchActivity extends BaseActivity {


    public  static final String EXTRA_MERCHANT_INFO = "EXTRA_MERCHANT_INFO";
    @InjectView(R.id.content)
    EditText content;

    @InjectView(R.id.cancel)
    View cancel;
      @InjectView(R.id.list)
      ListView list;




    @InjectView(R.id.delete)
    View delete;


    @Inject
    MerchantSearchAdapter adapter;

    FootViewHelper helper;

    @Inject
    ApiManager apiManager;
    private Runnable runnable=new Runnable(){


        @Override
        public void run() {


            loadData();

        }
    };

    @Override
    protected void init(Bundle bundle) {



        delete.setOnClickListener(this);
        cancel.setOnClickListener(this);
        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                if (content.removeCallbacks(runnable))
                    content.postDelayed(runnable, 300);

            }
        });

        helper=new FootViewHelper(this, new FootViewHelper.PageListener() {
            @Override
            public void onPageChanged(int newPageIndex, int pageCount) {



                loadData(newPageIndex,pageCount);
            }
        });
        list.addFooterView(helper.getView());
        helper.setInfo(0,0,0);
        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Merchant.MerchantInfo info = adapter.getItem(position);
                Intent intent = getIntent();
                intent.putExtra(EXTRA_MERCHANT_INFO, info);
                setResult(RESULT_OK, intent);
                finish();


            }
        });



        loadData();

    }

    @Override
    protected void onViewClick(View v, int id) {


        switch (id){

            case R.id.cancel:

               finish();

                break;

            case R.id.delete:

                content.setText("");

                break;
        }
    }


    private void loadData()
    {
        loadData(0,20);
    }
    private void loadData(final  int pageIndex,final int pageCount)
    {



        final String keyString =content.getText().toString().trim();

        new ThTask<Merchant.FuzzyQueryMerchantResponse>(this,true)
        {


            @Override
            public Merchant.FuzzyQueryMerchantResponse call() throws Exception {
                return apiManager.fuzzyQueryMerchant(keyString,pageIndex,pageCount);
            }

            @Override
            protected void doOnSuccess(Merchant.FuzzyQueryMerchantResponse data) {





                if(data.getErrCode()== Merchant.FuzzyQueryMerchantResponse.ErrorCode.ERR_OK)
                {

                    helper.setInfo(pageIndex, pageCount, data.getTotalCount());
                    if(pageIndex==0)
                    {
                        adapter.setDataArray(data.getMerchantInfosList());
                    }else
                    {
                        adapter.addDataArray(data.getMerchantInfosList());

                    }




                }else
                {
                    ToastUtils.show(data.getErrMsg());
                }




            }
        }.execute();
    }
}
