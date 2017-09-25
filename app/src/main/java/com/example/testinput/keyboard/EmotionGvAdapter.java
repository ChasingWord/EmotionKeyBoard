package com.example.testinput.keyboard;

import android.content.Context;
import android.util.Log;
import android.widget.GridView;

import com.example.testinput.R;
import com.example.testinput.adapter.EnhancedQuickAdapter;
import com.example.testinput.adapter.ViewHelper;
import com.example.testinput.keyboard.entity.SingleEmotion;

/**
 * Created by chasing on 2017/9/25.
 */

public class EmotionGvAdapter extends EnhancedQuickAdapter<SingleEmotion> {
    private GridView mGridView;
    private int mRowCount;

    public EmotionGvAdapter(GridView gridView, Context context, int layoutResId, int rowCount) {
        super(context, layoutResId);
        mGridView = gridView;
        mRowCount = rowCount;
    }

    @Override
    protected void convert(ViewHelper helper, SingleEmotion item, boolean itemChanged) {
        if (mGridView.getHeight() != 0)
            helper.setViewHeight(R.id.iv_emotion, mGridView.getHeight() / mRowCount);
        if (item.getEmotionResId() != -1)
            helper.setImageResource(R.id.iv_emotion, item.getEmotionResId());
    }
}
