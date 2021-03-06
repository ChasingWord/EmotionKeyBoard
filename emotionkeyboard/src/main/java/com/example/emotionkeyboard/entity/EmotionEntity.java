package com.example.emotionkeyboard.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by chasing on 2017/9/15.
 * 一页表情
 */
public class EmotionEntity implements Serializable {
    private String mTheme;//表情标签主题
    private int mThemeIconId;//表情标签主题对应的图标id
    private int mThemeChildCount;//该主题所包含的表情页数
    private ArrayList<SingleEmotion> mEmotionList;//一页表情资源信息
    private boolean isEmotionIcon;//是否是表情，表情则发送在EditText上，图片则直接显示在对话框

    public EmotionEntity(String theme, int themeIconId, int themeChildCount, boolean isEmotionIcon) {
        mTheme = theme;
        mThemeIconId = themeIconId;
        mThemeChildCount = themeChildCount;
        this.isEmotionIcon = isEmotionIcon;
        mEmotionList = new ArrayList<>();
    }

    public String getTheme() {
        return mTheme;
    }

    public EmotionEntity setTheme(String theme) {
        mTheme = theme;
        return this;
    }

    public ArrayList<SingleEmotion> getEmotions() {
        return mEmotionList;
    }

    public EmotionEntity setEmotions(ArrayList<SingleEmotion> emotionList) {
        mEmotionList.clear();
        mEmotionList.addAll(emotionList);
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

    public int getThemeIconId() {
        return mThemeIconId;
    }

    public void setThemeIconId(int themeIconId) {
        mThemeIconId = themeIconId;
    }
}
