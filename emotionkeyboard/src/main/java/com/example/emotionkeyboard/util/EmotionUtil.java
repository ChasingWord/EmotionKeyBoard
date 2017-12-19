package com.example.emotionkeyboard.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.example.emotionkeyboard.R;

import java.util.LinkedHashMap;
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
    public static final int EMOTION_CLASSIC_TYPE1 = 0x0001;
    public static final int EMOTION_CLASSIC_TYPE2 = 0x0002;
    public static final int EMOTION_CLASSIC_TYPE3 = 0x0003;
    public static final int EMOTION_CLASSIC_TYPE4 = 0x0004;

    /**
     * key-表情文字;
     * value-表情图片资源
     * LinkedHashMap维护顺序，保证有序
     */
    private static LinkedHashMap<String, Integer> EMPTY_MAP;
    private static LinkedHashMap<String, Integer> EMOTION_CLASSIC_MAP1;
    private static LinkedHashMap<String, Integer> EMOTION_CLASSIC_MAP2;
    private static LinkedHashMap<String, Integer> EMOTION_CLASSIC_MAP3;
    private static LinkedHashMap<String, Integer> EMOTION_CLASSIC_MAP4;

    static {
        EMPTY_MAP = new LinkedHashMap<>();
        EMOTION_CLASSIC_MAP1 = new LinkedHashMap<>();
        EMOTION_CLASSIC_MAP1.put("[我1]", R.mipmap.ic_launcher_round);
        EMOTION_CLASSIC_MAP1.put("[我2]", R.mipmap.ic_launcher_round);
        EMOTION_CLASSIC_MAP1.put("[我3]", R.mipmap.ic_launcher_round);
        EMOTION_CLASSIC_MAP1.put("[我4]", R.mipmap.ic_launcher_round);
        EMOTION_CLASSIC_MAP1.put("[我5]", R.mipmap.ic_launcher_round);
        EMOTION_CLASSIC_MAP1.put("[我6]", R.mipmap.ic_launcher_round);
        EMOTION_CLASSIC_MAP1.put("[我7]", R.mipmap.ic_launcher_round);
        EMOTION_CLASSIC_MAP1.put("[我8]", R.mipmap.ic_launcher_round);
        EMOTION_CLASSIC_MAP1.put("[我9]", R.mipmap.ic_launcher_round);
        EMOTION_CLASSIC_MAP1.put("[我10]", R.mipmap.ic_launcher_round);
        EMOTION_CLASSIC_MAP1.put("[我11]", R.mipmap.ic_launcher_round);
        EMOTION_CLASSIC_MAP1.put("[我12]", R.mipmap.ic_launcher_round);
        EMOTION_CLASSIC_MAP1.put("[我13]", R.mipmap.ic_launcher_round);
        EMOTION_CLASSIC_MAP1.put("[我14]", R.mipmap.ic_launcher_round);
        EMOTION_CLASSIC_MAP1.put("[我15]", R.mipmap.ic_launcher_round);
        EMOTION_CLASSIC_MAP1.put("[我16]", R.mipmap.ic_launcher_round);

        //其它表情类型Map ....
        EMOTION_CLASSIC_MAP2 = new LinkedHashMap<>();
        EMOTION_CLASSIC_MAP2.put("[我20]", R.mipmap.ic_launcher_round);
        EMOTION_CLASSIC_MAP2.put("[我21]", R.mipmap.ic_launcher_round);
        EMOTION_CLASSIC_MAP2.put("[我22]", R.mipmap.ic_launcher_round);
        EMOTION_CLASSIC_MAP2.put("[我23]", R.mipmap.ic_launcher_round);
        EMOTION_CLASSIC_MAP2.put("[我24]", R.mipmap.ic_launcher_round);
        EMOTION_CLASSIC_MAP2.put("[我25]", R.mipmap.ic_launcher_round);
        EMOTION_CLASSIC_MAP2.put("[我26]", R.mipmap.ic_launcher_round);
        EMOTION_CLASSIC_MAP2.put("[我27]", R.mipmap.ic_launcher_round);
        EMOTION_CLASSIC_MAP2.put("[我28]", R.mipmap.ic_launcher_round);
        EMOTION_CLASSIC_MAP2.put("[我29]", R.mipmap.ic_launcher_round);

        EMOTION_CLASSIC_MAP3 = new LinkedHashMap<>();
        EMOTION_CLASSIC_MAP3.put("[我17]", R.mipmap.ic_launcher_round);
        EMOTION_CLASSIC_MAP3.put("[我18]", R.mipmap.ic_launcher_round);
        EMOTION_CLASSIC_MAP3.put("[我19]", R.mipmap.ic_launcher_round);

        EMOTION_CLASSIC_MAP4 = new LinkedHashMap<>();
        EMOTION_CLASSIC_MAP4.put("[gif1]", R.mipmap.gif1);
        EMOTION_CLASSIC_MAP4.put("[gif2]", R.mipmap.gif2);
        EMOTION_CLASSIC_MAP4.put("[gif3]", R.mipmap.gif3);
        EMOTION_CLASSIC_MAP4.put("[gif4]", R.mipmap.gif4);
        EMOTION_CLASSIC_MAP4.put("[gif5]", R.mipmap.gif5);
    }

    /**
     * 根据名称获取当前表情图标R值
     *
     * @param imgName 名称
     * @return resId
     */
    public static int getImgByName(String imgName) {
        Integer integer = EMOTION_CLASSIC_MAP1.get(imgName);
        if (integer == null) {
            integer = EMOTION_CLASSIC_MAP2.get(imgName);
        }
        if (integer == null) {
            integer = EMOTION_CLASSIC_MAP3.get(imgName);
        }
        return integer == null ? -1 : integer;
    }

    /**
     * 根据类型获取表情数据
     *
     * @param EmotionType 表情类型标志符
     */
    public static LinkedHashMap<String, Integer> getEmotionMap(int EmotionType) {
        LinkedHashMap<String, Integer> EmotionMap;
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
            case EMOTION_CLASSIC_TYPE4:
                EmotionMap = EMOTION_CLASSIC_MAP4;
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
        String regexEmotion = "\\[([\u4e00-\u9fa5\\w])+\\]";    //   “\u4e00-\u9fa5”匹配 “一-龥”的所有中文字符  “\w”匹配所有英文数字下划线
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
                int size = (int) (tv.getTextSize() * 13 / 10f);
                Bitmap bitmap = BitmapFactory.decodeResource(res, imgRes);
                Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
                ImageSpan span = new ImageSpan(context, scaleBitmap);
                spannableString.setSpan(span, start, start + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        tv.setText(spannableString);
    }
}
