package com.example.emotionkeyboard.entity;

import java.io.Serializable;

/**
 * Created by chasing on 2017/9/23.
 * 单个表情
 */
public class SingleEmotion implements Serializable {
    private String mEmotionName;//表情名称
    private int mEmotionResId;//表情资源id（存放在mipmap的表情）
    private String mEmotionFilePath;//表情资源路径（存放在本地的表情）

    public SingleEmotion(){
        mEmotionName = "";
        mEmotionResId = 0;
        mEmotionFilePath = "";
    }

    public SingleEmotion setEmotionName(String emotionName) {
        mEmotionName = emotionName;
        return this;
    }

    public SingleEmotion setEmotionResId(int emotionResId) {
        mEmotionResId = emotionResId;
        return this;
    }

    public String getEmotionName() {
        return mEmotionName;
    }

    public int getEmotionResId() {
        return mEmotionResId;
    }

    public String getEmotionFilePath() {
        return mEmotionFilePath;
    }

    public void setEmotionFilePath(String emotionFilePath) {
        mEmotionFilePath = emotionFilePath;
    }
}
