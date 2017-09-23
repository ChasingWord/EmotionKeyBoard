package com.example.testinput.keyboard;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by chasing on 2017/9/15.
 */

public class EmotionEntity implements Serializable {
    private String mTheme;
    private int mThemeChildCount;
    private HashMap<String, Integer> mEmotionMap;
    private boolean isEmotionIcon;

    public EmotionEntity(String theme,HashMap<String, Integer> emotionMap, int themeChildCount, boolean isEmotionIcon){
        mEmotionMap = new HashMap<>();
        mTheme = theme;
        mEmotionMap.putAll(emotionMap);
        mThemeChildCount = themeChildCount;
        this.isEmotionIcon = isEmotionIcon;
    }

    public String getTheme() {

        return mTheme;
    }

    public void setTheme(String theme) {
        mTheme = theme;
    }

    public HashMap<String, Integer> getEmotionMap() {
        return mEmotionMap;
    }

    public void setEmotionMap(HashMap<String, Integer> emotionMap) {
        mEmotionMap.clear();
        mEmotionMap.putAll(emotionMap);
    }

    public int getThemeChildCount() {
        return mThemeChildCount;
    }

    public void setThemeChildCount(int themeChildCount) {
        this.mThemeChildCount = themeChildCount;
    }

    public boolean isEmotionIcon() {
        return isEmotionIcon;
    }

    public void setEmotionIcon(boolean emotionIcon) {
        isEmotionIcon = emotionIcon;
    }
}
