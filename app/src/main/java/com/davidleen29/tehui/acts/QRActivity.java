package com.davidleen29.tehui.acts;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.acts.BaseActivity;
import com.davidleen29.tehui.lang.CException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.encoder.QRCode;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

/**
 * 生成二维码图片
 */
@ContentView(R.layout.activity_qr)
public class QRActivity extends SimpleActivity<Bitmap> {

    public static final String EXTRA_CODE = "EXTRA_CODE";


    @InjectView(R.id.iv_qr)
    ImageView qr;

    @InjectExtra(EXTRA_CODE)
    String content;

    @Override
    protected void init(Bundle bundle) {

        super.init(bundle);


//        qr.setImageBitmap(generateQRImage(content));

    }

    @Override
    protected Bitmap readOnBackground() throws CException {
        return generateQRImage(content);
    }

    @Override
    protected void onDataLoaded(Bitmap data) {
        qr.setImageBitmap(generateQRImage(content));
    }

    private Bitmap generateQRImage(String content)
    {

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenHeight = displaymetrics.heightPixels;
        int screenWidth = displaymetrics.widthPixels;

        int newWidth=screenWidth>screenHeight?screenWidth/2:screenHeight/2;

        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, newWidth, newWidth);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.TRANSPARENT);
                }
            }
         return bmp;
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return null;
    }


}
