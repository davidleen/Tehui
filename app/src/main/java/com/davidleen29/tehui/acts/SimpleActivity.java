package com.davidleen29.tehui.acts;

import android.os.Bundle;

import com.davidleen29.tehui.lang.CException;
import com.davidleen29.tehui.tasks.ThTask;



public abstract class SimpleActivity<T> extends BaseActivity {


    @Override
    protected void init(Bundle bundle) {


        loadData();
    }

    protected final void loadData() {
        new ThTask<T>(this) {
            @Override
            public T call() throws Exception {
                return readOnBackground();
            }

            @Override
            protected void doOnSuccess(T data) {
                onDataLoaded(data);
            }
        }.execute();


    }


    protected abstract T readOnBackground() throws CException;


    protected abstract void onDataLoaded(T data);

}
