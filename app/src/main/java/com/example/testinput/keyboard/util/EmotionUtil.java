package com.example.testinput.keyboard.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.TextView;

import com.example.testinput.R;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chasing on 2017/9/23.
 * 存放所有表情的集合
 * 表情获取及显示的辅助类
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class EmotionUtil {

    /**
     * 表情类型标志符
     */
    public static final int EMOTION_CLASSIC_TYPE1 = 0x0001;//经典表情
    public static final int EMOTION_CLASSIC_TYPE2 = 0x0002;//经典表情
    public static final int EMOTION_CLASSIC_TYPE3 = 0x0003;//经典表情
    /**
     * key-表情文字;
     * value-表情图片资源
     */
    private static HashMap<String, Integer> EMPTY_MAP;
    private static HashMap<String, Integer> EMOTION_CLASSIC_MAP1;
    private static HashMap<String, Integer> EMOTION_CLASSIC_MAP2;
    private static HashMap<String, Integer> EMOTION_CLASSIC_MAP3;

    static {
        EMPTY_MAP = new HashMap<>();
        EMOTION_CLASSIC_MAP1 = new HashMap<>();
        EMOTION_CLASSIC_MAP1.put("[ha]", R.mipmap.ic_launcher);
        EMOTION_CLASSIC_MAP1.put("[ha1]", R.mipmap.ic_launcher);
        EMOTION_CLASSIC_MAP1.put("[ha2]", R.mipmap.ic_launcher);
        //其它表情类型Map ....
        EMOTION_CLASSIC_MAP2 = new HashMap<>();
        for (int i = 0; i < 15; i++) {
            EMOTION_CLASSIC_MAP2.put("[ha" + i + "]", R.mipmap.ic_launcher);
        }

        EMOTION_CLASSIC_MAP3 = new HashMap<>();
        for (int i = 0; i < 44; i++) {
            EMOTION_CLASSIC_MAP3.put("[ha" + i + "]", R.mipmap.ic_launcher);
        }
    }

    /**
     * 根据名称获取当前表情图标R值
     *
     * @param imgName     名称
     * @return resId
     */
    public static int getImgByName(String imgName) {
        Integer integer = EMOTION_CLASSIC_MAP1.get(imgName);
        if (integer == null){
            integer = EMOTION_CLASSIC_MAP2.get(imgName);
        }
        if (integer == null){
            integer = EMOTION_CLASSIC_MAP3.get(imgName);
        }
        return integer == null ? -1 : integer;
    }

    /**
     * 根据类型获取表情数据
     *
     * @param EmotionType 表情类型标志符
     */
    public static HashMap<String, Integer> getEmotionMap(int EmotionType) {
        HashMap EmotionMap;
        switch (EmotionType) {
            case EMOTION_CLASSIC_TYPE1:
                EmotionMap = EMOTION_CLASSIC_MAP1;
                break;
            case EMOTION_CLASSIC_TYPE2:
                EmotionMap = EMOTION_CLASSIC_MAP2;
                break;
            case EMOTION_CLASSIC_TYPE3:
                EmotionMap = EMOTION_CLASSIC_MAP3;
                break;
            default:
                EmotionMap = EMPTY_MAP;
                break;
        }
        return EmotionMap;
    }

    public static void setEmotionContent(final Context context, final TextView tv, String msg) {
        SpannableString spannableString = new SpannableString(msg);
        Resources res = context.getResources();
        String regexEmotion = "\\[([\u4e00-\u9fa5\\w])+\\]";
        Pattern patternEmotion = Pattern.compile(regexEmotion);
        Matcher matcherEmotion = patternEmotion.matcher(spannableString);
        while (matcherEmotion.find()) {
            // 获取匹配到的具体字符
            String key = matcherEmotion.group();
            // 匹配字符串的开始位置
            int start = matcherEmotion.start();
            // 利用表情名字获取到对应的图片
            Integer imgRes = getImgByName(key);
            if (imgRes != -1) {
                // 压缩表情图片
                int size = (int) tv.getTextSize() * 13 / 10;
                Bitmap bitmap = BitmapFactory.decodeResource(res, imgRes);
                Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
                ImageSpan span = new ImageSpan(context, scaleBitmap);
                spannableString.setSpan(span, start, start + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        tv.setText(spannableString);
    }
}
