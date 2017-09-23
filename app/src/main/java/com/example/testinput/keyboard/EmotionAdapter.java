package com.example.testinput.keyboard;

import android.content.Context;

import com.example.testinput.R;
import com.example.testinput.adapter.recycleradaper.RecyclerQuickAdapter;
import com.example.testinput.adapter.recycleradaper.RecyclerViewHelper;

/**
 * Created by chasing on 2017/9/15.
 */

public class EmotionAdapter extends RecyclerQuickAdapter<Integer> {
    public EmotionAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    protected void convert(int viewType, RecyclerViewHelper helper, Integer integer) {
        helper.setImageResource(R.id.iv_emotion, integer);
    }
}
