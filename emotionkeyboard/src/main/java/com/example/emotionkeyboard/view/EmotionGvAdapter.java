package com.example.emotionkeyboard.view;

import android.content.Context;
import android.widget.GridView;

import com.example.emotionkeyboard.R;
import com.example.emotionkeyboard.adapter.EnhancedQuickAdapter;
import com.example.emotionkeyboard.adapter.ViewHelper;
import com.example.emotionkeyboard.entity.SingleEmotion;

/**
 * Created by chasing on 2017/9/25.
 * 表情列表Adapter
 */
class EmotionGvAdapter extends EnhancedQuickAdapter<SingleEmotion> {
    private GridView mGridView;
    private int mRowCount;

    EmotionGvAdapter(GridView gridView, Context context, int layoutResId, int rowCount) {
        super(context, layoutResId);
        mGridView = gridView;
        mRowCount = rowCount;
    }

    @Override
    protected void convert(ViewHelper helper, SingleEmotion item, boolean itemChanged) {
        if (mGridView.getHeight() != 0) {
            helper.setViewHeight(R.id.iv_emotion, mGridView.getHeight() / mRowCount);
            if (mRowCount == EmotionFragment.ROW_COUNT_EMOTION)
                helper.setViewWidth(R.id.iv_emotion, mGridView.getWidth() / EmotionFragment.COLUMN_COUNT_EMOTION);
            else
                helper.setViewWidth(R.id.iv_emotion, mGridView.getWidth() / EmotionFragment.COLUMN_COUNT_IMG);
        }
        if (item.getEmotionResId() != -1)
            helper.setImageResource(R.id.iv_emotion, item.getEmotionResId());
    }
}
