package com.example.emotionkeyboard.util;

import android.content.Context;

import com.example.emotionkeyboard.R;

/**
 * Created by hht on 2016/6/6.
 */
public class ResUtil {

    private static Class<R.mipmap> cls = R.mipmap.class;

    /**
     * 通过资源名称获取资源id
     */
    public static int getMipmapResId( String resName ){
        try{
            int value = cls.getDeclaredField(resName).getInt(null);
            return value;
        }catch ( Exception e ){
        }
        return -1;
    }

    /**
     * 通过资源id获取资源名称
     */
    public static String getMipmapResName( Context context, int resId ){
        String name = context.getResources().getResourceName(resId);
        String[] split = name.split("/");
        if( split.length < 2 )
            return null;
        return split[1];
    }
}
