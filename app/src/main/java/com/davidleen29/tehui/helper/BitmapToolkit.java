package com.davidleen29.tehui.helper;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;


public class BitmapToolkit {
    private final static String TAG = "BitmapToolkit";



    public final static String DIR_DEFAULT = Environment.getExternalStorageDirectory().getPath()
            + "/Tehui/";

    private String mDirectory = DIR_DEFAULT;

    // file name
    private String mName = "";

    private String mRemoteUrl = "";

    private String mSuffix = "";

    private String mPrefix = "";

    /**
     * <br>
     * Description: 实现图片缓存,下载的类. <br>
     * Author:hexy <br>
     * Date:2011-4-1上午10:30:09
     * 
     * @param dir 缓存文件夹绝对路径
     * @param url 图片远程的url, 读取本地图片无需用此构造函数
     * @param prefix 图片前缀
     * @param suffix 图片后缀
     */
    public BitmapToolkit(String dir, String url, String prefix, String suffix) {
        mDirectory = dir;
        mPrefix = prefix;
        mSuffix = suffix;
        mName =  String.valueOf(url.hashCode());
        mRemoteUrl = url;
        this.mkdirsIfNotExist();
    }

//    /**
//     * <br>
//     * Description:计算md5值 <br>
//     * Author:hexy <br>
//     * Date:2011-4-1上午10:12:47
//     *
//     * @param str
//     * @param str
//     * @return
//     */
//    private static String calculateMd5(String str) {
//        if (str == null || str.length() == 0)
//            return "";
//        return Utils.Md5(str.getBytes());
//    }

    public void mkdirsIfNotExist() {
        File dirFile = new File(getDirecotry());
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
    }

    public String getDirecotry() {
        return mDirectory;
    }

    public String getAbsolutePath() {
        return mDirectory + mPrefix + mName + mSuffix;
    }

    /*
     * 获取图片
     */
    public static Bitmap loadLocalBitmapRoughScaled(String path, int maxsize) {
        Bitmap bm = null;

        try {
            BitmapFactory.Options options = new BitmapFactory.Options();

            options.outHeight = maxsize;
            options.inJustDecodeBounds = true;
            options.inTempStorage = new byte[32 * 1024];
            // 获取这个图片的宽和高
            bm = BitmapFactory.decodeFile(path, options); // 此时返回bm为空

            options.inJustDecodeBounds = false;
            int be = options.outHeight / (maxsize / 10);
            if (be % 10 != 0)
                be += 10;

            be = be / 10;
            if (be <= 0)
                be = 1;

            options.inSampleSize = be;
            if (bm != null && !bm.isRecycled()) {
                bm.recycle();
                bm = null;
                System.gc();
            }
            bm = BitmapFactory.decodeFile(path, options);
            // Log.i(TAG, "getLocalBitmap width " + bm.getWidth() + " height " +
            // bm.getHeight());
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            bm = null;
        }
        return bm;
    }

    public static Bitmap compress(Bitmap bitmap, int size) {
        if (bitmap == null)
            return null;
        if (bitmap.isRecycled())
            return null;
        // create explicit picture
        int max = bitmap.getWidth() > bitmap.getHeight() ? bitmap.getWidth() : bitmap.getHeight();
        int min = bitmap.getWidth() < bitmap.getHeight() ? bitmap.getWidth() : bitmap.getHeight();
        min = size * min / max;
        max = size;
        if (bitmap.getWidth() > bitmap.getHeight()) {
            bitmap = Bitmap.createScaledBitmap(bitmap, max, min, false);
        } else {
            bitmap = Bitmap.createScaledBitmap(bitmap, min, max, false);
        }
        return bitmap;
    }

    /**
     * 获取图片压缩数据
     */
    public static byte[] loadLocalBitmapExactScaledBytes(String path, int size) {
        Bitmap bm = loadLocalBitmapExactScaled(path, size);
        byte[] result = BitmapToByteArray(bm);
        if(bm != null) {
            bm.recycle();
        }
        return result;
    }
    
    public static byte[] BitmapToByteArray(Bitmap bitmap) {
        byte[] result = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 80, stream);
        result = stream.toByteArray();

        return result;
    }
    
    public static Bitmap loadLocalBitmapExactScaled(String path, int size) {
        Bitmap bm = loadLocalBitmapRoughScaled(path, size * 2);
        if (bm == null) {
            return bm;
        }
        // rotate from exif
        int degree = getDegree(path);
        bm = rotateBitmap(bm, degree);
        return compress(bm, size);
    }

    public static Bitmap compressBitmap(String path, int size) {
        Bitmap bm = loadLocalBitmapRoughScaled(path, size * 2);
        return bm;
    }

    /**
     * get degree from exif
     */
    public static int getDegree(String filename) {
        int result = 0;
        int orientation = 0;
        try {
            ExifInterface exif = new ExifInterface(filename);
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);

        } catch (Exception e) {
        }

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                result = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                result = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                result = 270;
                break;
        }

        return result;
    }

    /**
     * <br>
     * Description:rotate Bitmap <br>
     * Author:hexy <br>
     * Date:2011-4-1上午10:23:00
     * 
     * @param bitmap
     * @param degree
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degree) {
        if (degree == 0)
            return bitmap;
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            Bitmap tempBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap
                    .getHeight(), matrix, true);

            // Bitmap操作完应该显示的释放
            bitmap.recycle();
            bitmap = tempBmp;
        } catch (OutOfMemoryError ex) {
            // Android123建议大家如何出现了内存不足异常，最好return 原始的bitmap对象。.
        }
        if (bitmap.isRecycled()) {
            bitmap = null;
        }
        return bitmap;
    }

    // 放大缩小图片
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float)w / width);
        float scaleHeight = ((float)h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        return newBmp;
    }

}
