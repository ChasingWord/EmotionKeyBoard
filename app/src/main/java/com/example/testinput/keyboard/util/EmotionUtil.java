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
import android.util.ArrayMap;
import android.util.Log;
import android.widget.TextView;

import com.example.testinput.R;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.R.attr.key;

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
    public static final int EMOTION_CLASSIC_TYPE = 0x0001;//经典表情
    /**
     * key-表情文字;
     * value-表情图片资源
     */
    public static HashMap<String, Integer> EMPTY_MAP;
    public static HashMap<String, Integer> EMOTION_CLASSIC_MAP;

    static {
        EMPTY_MAP = new HashMap<>();
        EMOTION_CLASSIC_MAP = new HashMap<>();
        EMOTION_CLASSIC_MAP.put("[ha]", R.mipmap.ic_launcher);
        EMOTION_CLASSIC_MAP.put("[ha1]", R.mipmap.ic_launcher);
        EMOTION_CLASSIC_MAP.put("[ha2]", R.mipmap.ic_launcher);
        //其它表情类型Map ....
    }

    public static SpannableString setImage(Context context, int textSize, int start, String key, int imgRes, String source) {
        SpannableString spannableString = new SpannableString(source);
        int size = textSize * 13 / 10;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imgRes);
        Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
        ImageSpan span = new ImageSpan(context, scaleBitmap);
        spannableString.setSpan(span, start, start + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * 根据名称获取当前表情图标R值
     *
     * @param EmotionType 表情类型标志符
     * @param imgName     名称
     * @return
     */
    public static int getImgByName(int EmotionType, String imgName) {
        Integer integer = null;
        switch (EmotionType) {
            case EMOTION_CLASSIC_TYPE:
                integer = EMOTION_CLASSIC_MAP.get(imgName);
                break;
            default:
                Log.e("test", "the emojiMap is null!!");
                break;
        }
        return integer == null ? -1 : integer;
    }

    /**
     * 根据类型获取表情数据
     *
     * @param EmotionType 表情类型标志符
     */
    public static HashMap<String, Integer> getEmojiMap(int EmotionType) {
        HashMap EmojiMap = null;
        switch (EmotionType) {
            case EMOTION_CLASSIC_TYPE:
                EmojiMap = EMOTION_CLASSIC_MAP;
                break;
            default:
                EmojiMap = EMPTY_MAP;
                break;
        }
        return EmojiMap;
    }

    public static void getEmotionContent(int emotion_map_type, final Context context, final TextView tv, String msg) {
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
            Integer imgRes = getImgByName(emotion_map_type, key);
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
