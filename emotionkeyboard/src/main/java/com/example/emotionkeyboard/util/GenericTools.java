package com.example.emotionkeyboard.util;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;

public class GenericTools {
    /**
     * 返回屏幕高度
     */
    public final static int getScreenHeight(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        display.getHeight();
        DisplayMetrics metric = new DisplayMetrics();
        display.getMetrics(metric);
        return metric.heightPixels;
    }

    /**
     * 返回屏幕宽度
     */
    public final static int getScreenWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        display.getHeight();
        DisplayMetrics metric = new DisplayMetrics();
        display.getMetrics(metric);
        return metric.widthPixels;
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public final static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 获取设备像素
     *
     * @param activity
     * @return int[] index 0宽,index 1 高
     */
    public final static int[] getDevicePixel(Activity activity) {
        int px[] = new int[2];
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        // 获得手机的宽度和高度像素单位为px
        px[0] = dm.widthPixels;
        px[1] = dm.heightPixels;
        return px;
    }

    /**
     * 获取设备密度系数
     *
     * @param activity
     * @return
     */
    public final static float getDensity(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.density;
    }

    /**
     * dp转px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * px转dp
     *
     * @param context
     * @param pxValue
     * @return
     */

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 是否存在sd卡
     *
     * @return
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
}