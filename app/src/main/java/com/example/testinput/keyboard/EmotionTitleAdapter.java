package com.example.testinput.keyboard;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.example.testinput.R;
import com.example.testinput.adapter.recycleradaper.RecyclerQuickAdapter;
import com.example.testinput.adapter.recycleradaper.RecyclerViewHelper;

/**
 * Created by chasing on 2017/9/15.
 */

public class EmotionTitleAdapter extends RecyclerQuickAdapter<String> {
    private int mSelectPosition;

    public EmotionTitleAdapter(Context context, int layoutId) {
        super(context, layoutId);
        mSelectPosition = 0;
    }

    public void setSelectPosition(int position){
        if (mSelectPosition == position) return;
        int tempPosition = mSelectPosition;
        mSelectPosition = position;
        notifyItemChanged(tempPosition);
        notifyItemChanged(position);
    }

    @Override
    protected void convert(int viewType, RecyclerViewHelper helper, String s) {
        helper.setText(R.id.tv_emotion_title, s);
        if (helper.getPosition() == mSelectPosition){
            helper.setBackgroundColor(R.id.ll_emotion_title_container, ContextCompat.getColor(context, android.R.color.darker_gray));
        } else {
            helper.setBackgroundColor(R.id.ll_emotion_title_container, ContextCompat.getColor(context, android.R.color.holo_green_light));
        }
    }
}
