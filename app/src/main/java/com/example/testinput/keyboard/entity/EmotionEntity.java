package com.example.testinput.keyboard.entity;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by chasing on 2017/9/15.
 */
public class EmotionEntity implements Serializable {
    private String mTheme;
    private int mThemeChildCount;
    private HashMap<String, Integer> mEmotionMap;
    private boolean isEmotionIcon;//是否是表情，表情则发送在EditText上，图片则直接显示在对话框

    public EmotionEntity(String theme,int themeChildCount, boolean isEmotionIcon){
        mEmotionMap = new HashMap<>();
        mTheme = theme;
        mThemeChildCount = themeChildCount;
        this.isEmotionIcon = isEmotionIcon;
    }

    public String getTheme() {
        return mTheme;
    }

    public EmotionEntity setTheme(String theme) {
        mTheme = theme;
        return this;
    }

    public HashMap<String, Integer> getEmotionMap() {
        return mEmotionMap;
    }

    public EmotionEntity setEmotionMap(HashMap<String, Integer> emotionMap) {
        mEmotionMap.clear();
        mEmotionMap.putAll(emotionMap);
        return this;
    }

    public int getThemeChildCount() {
        return mThemeChildCount;
    }

    public EmotionEntity setThemeChildCount(int themeChildCount) {
        this.mThemeChildCount = themeChildCount;
        return this;
    }

    public boolean isEmotionIcon() {
        return isEmotionIcon;
    }

    public EmotionEntity setIsEmotionIcon(boolean emotionIcon) {
        isEmotionIcon = emotionIcon;
        return this;
    }
}
