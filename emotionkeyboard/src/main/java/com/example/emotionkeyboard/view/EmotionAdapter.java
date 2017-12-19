package com.example.emotionkeyboard.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.emotionkeyboard.R;
import com.example.emotionkeyboard.adapter.EnhancedQuickAdapter;
import com.example.emotionkeyboard.adapter.ViewHelper;
import com.example.emotionkeyboard.adapter.recycleradaper.RecyclerQuickAdapter;
import com.example.emotionkeyboard.adapter.recycleradaper.RecyclerViewHelper;
import com.example.emotionkeyboard.entity.SingleEmotion;
import com.example.emotionkeyboard.util.FileUtil;

import java.io.File;

/**
 * Created by chasing on 2017/9/25.
 * 表情列表Adapter
 */
class EmotionAdapter extends RecyclerQuickAdapter<SingleEmotion> {
    private RecyclerView mGridView;
    private int mRowCount;

    EmotionAdapter(RecyclerView gridView, Context context, int layoutResId, int rowCount) {
        super(context, layoutResId);
        mGridView = gridView;
        mRowCount = rowCount;
    }

    @Override
    protected void convert(int viewType, RecyclerViewHelper helper, SingleEmotion item) {
        if (mGridView.getHeight() != 0) {
            helper.setViewHeight(R.id.iv_emotion, mGridView.getHeight() / mRowCount);
            if (mRowCount == EmotionFragment.ROW_COUNT_EMOTION)
                helper.setViewWidth(R.id.iv_emotion, mGridView.getWidth() / EmotionFragment.COLUMN_COUNT_EMOTION);
            else
                helper.setViewWidth(R.id.iv_emotion, mGridView.getWidth() / EmotionFragment.COLUMN_COUNT_IMG);
        }
        if (item.getEmotionResId() > 0) {
            helper.setImageResource(R.id.iv_emotion, item.getEmotionResId());
        } else if (!TextUtils.isEmpty(item.getEmotionFilePath())){
            ImageView iconView = helper.getView(R.id.iv_emotion);
            Glide.with(context)
                    .load(new File(item.getEmotionFilePath()))
                    .asBitmap()
                    .into(iconView);
        }
    }
}
