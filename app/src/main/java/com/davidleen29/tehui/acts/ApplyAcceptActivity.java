package com.davidleen29.tehui.acts;

import com.davidleen29.tehui.R;
import com.huiyou.dp.service.protocol.Common;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;

@ContentView(R.layout.activity_apply_accept)
public class ApplyAcceptActivity extends BaseActivity {


    public static final String EXTRA_IDENTIFY_STATUE = "EXTRA_IDENTIFY_STATUE";


    @InjectExtra(value = EXTRA_IDENTIFY_STATUE,optional = true)
    Common.IdentifyMerchantPassStatus   statue;
}
