package com.davidleen29.tehui;

import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by david on 2015/10/5.
 */
@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 9)
public class MainActivityTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }



    @Test
    public void testChangeText_newActivity() {
        // Type text and then press the button.

        Assert.assertEquals(100,11);
    }
}