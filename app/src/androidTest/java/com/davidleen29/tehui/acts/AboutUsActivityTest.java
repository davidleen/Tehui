package com.davidleen29.tehui.acts;


import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;



/**
 * Created by david on 2015/10/4.
 */

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class AboutUsActivityTest   {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testInit() throws Exception {


        Assert.assertEquals(3, 5);


    }

    @Test
    public void testChangeText_newActivity() {
        // Type text and then press the button.

        Assert.assertEquals(100,11);
    }
}