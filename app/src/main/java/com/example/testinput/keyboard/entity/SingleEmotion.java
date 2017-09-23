package com.example.testinput.keyboard.entity;

/**
 * Created by chasing on 2017/9/23.
 */

public class SingleEmotion {
    private String mEmotionName;
    private int mEmotionResId;

    public SingleEmotion setEmotionName(String emotionName){
        mEmotionName = emotionName;
        return this;
    }

    public SingleEmotion setEmotionResId(int emotionResId){
        mEmotionResId = emotionResId;
        return this;
    }

    public String getEmotionName(){
        return mEmotionName;
    }

    public int getEmotionResId(){
        return mEmotionResId;
    }
}
