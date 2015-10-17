package com.davidleen29.tehui;

import android.app.Application;
import android.util.DisplayMetrics;

import com.baidu.mapapi.SDKInitializer;
import com.davidleen29.tehui.helper.LocationHelper;
import com.davidleen29.tehui.helper.PixelHelper;
import com.davidleen29.tehui.utils.ToastUtils;
import com.google.inject.Inject;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.File;

/**
 * Created by davidleen29 on 2015/7/19.
 */
public class ThApplication extends Application {




    @Override
    public void onCreate() {

        super.onCreate();
        ToastUtils.setAppContext(this);
        PixelHelper.init(this);

        initImageLoader();

        registerWeixin();

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(getApplicationContext());


    }

    private void registerWeixin() {




    }





    /**
     * 初始化ImageLoader组件
     */
    private void initImageLoader() {


        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        //默认显示属性。
        DisplayImageOptions options=new  DisplayImageOptions.Builder().showImageForEmptyUri(R.mipmap.icon_image_empty).showImageOnFail(R.mipmap.icon_image_lost).showImageOnLoading(R.mipmap.icon_image_loading).cacheInMemory(true).cacheOnDisk(true)
                .build();

        File cacheDir = StorageUtils.getCacheDirectory(this);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCacheExtraOptions(width, height) // default = device screen dimensions
                        //sd 卡上缓存图片大小无限制。
                .diskCacheExtraOptions(Integer.MAX_VALUE, Integer.MAX_VALUE, null)
                        //  .taskExecutor(...)
                        // .taskExecutorForCachedImages(...)
                .threadPoolSize(5) // default  线程池数
                .threadPriority(Thread.NORM_PRIORITY - 2) // default 线程优先级别
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default 线程执行顺序
                .denyCacheImageMultipleSizesInMemory()
               // .memoryCache(new LruMemoryCache(2 * 1024 * 1024))  //内存缓存数  2M
              //   .memoryCacheSize(10 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default


               // .diskCache(new UnlimitedDiscCache(cacheDir)) // default
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .imageDownloader(new BaseImageDownloader(this)) // default
                .imageDecoder(new BaseImageDecoder(true)) // default
                .defaultDisplayImageOptions(options) // default
               // .writeDebugLogs()



                .build();
        ImageLoader.getInstance().init(config);


//        Acceptable URIs examples
//        String imageUri = "http://site.com/image.png"; // from Web
//        String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
//        String imageUri = "content://media/external/audio/albumart/13"; // from content provider
//        String imageUri = "assets://image.png"; // from assets
//        String imageUri = "drawable://" + R.drawable.image; // from drawables (only images, non-9patch)
    }
}
