package com.example.testinput.keyboard.entity;

/**
 * Created by chasing on 2017/9/23.
 * 单个表情
 */
public class SingleEmotion {
    private String mEmotionName;//表情名称
    private int mEmotionResId;//表情资源id

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
}
