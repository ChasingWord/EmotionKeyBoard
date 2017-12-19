package com.example.emotionkeyboard.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by chasing on 2017/12/19.
 */

public class BitmapUtil {
    /**
     * 加载本地图片
     */
    public static Bitmap getLocalBitmap(Context context, String url) {
        try {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inPreferredConfig = Bitmap.Config.RGB_565;
            opt.inPurgeable = true;
            opt.inInputShareable = true;
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis, null, opt);
        } catch (FileNotFoundException e) {
            return getSystemBitmap(context, url);
        }
    }

    /**
     * 加载系统图片
     */
    private static Bitmap getSystemBitmap(Context context, String url) {
        InputStream inputStream = null;
        try {
            inputStream = context.getContentResolver().openInputStream(Uri.parse(url));
            return BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (Exception ignored) {
            }
        }
        return null;
    }
}
