package com.example.emotionkeyboard.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.example.emotionkeyboard.R;
import com.example.emotionkeyboard.adapter.recycleradaper.RecyclerQuickAdapter;
import com.example.emotionkeyboard.adapter.recycleradaper.RecyclerViewHelper;

import java.util.Map;

/**
 * Created by chasing on 2017/9/15.
 * theme主题切换的Adapter
 */
public class EmotionTitleAdapter extends RecyclerQuickAdapter<Map<String, String>> {
    public static final String TITLE = "title";
    public static final String ICON = "icon";

    private int mSelectPosition;

    public EmotionTitleAdapter(Context context, int layoutId) {
        super(context, layoutId);
        mSelectPosition = 0;
    }

    public void setSelectPosition(int position) {
        if (mSelectPosition == position) return;
        int tempPosition = mSelectPosition;
        mSelectPosition = position;
        notifyItemChanged(tempPosition);
        notifyItemChanged(position);
    }

    @Override
    protected void convert(int viewType, RecyclerViewHelper helper, Map<String, String> map) {
        if (helper.getPosition() == mSelectPosition) {
            helper.setBackgroundColor(R.id.ll_emotion_title_container, ContextCompat.getColor(context, android.R.color.darker_gray));
        } else {
            helper.setBackgroundColor(R.id.ll_emotion_title_container, ContextCompat.getColor(context, android.R.color.transparent));
        }
        try {
            int iconId = Integer.valueOf(map.get(ICON));
            if (iconId > 0)
                helper.setImageResource(R.id.iv_emotion_title, iconId)
                        .setVisible(R.id.iv_emotion_title, true)
                        .setVisible(R.id.tv_emotion_title, false);
            else
                helper.setText(R.id.tv_emotion_title, map.get(TITLE))
                        .setVisible(R.id.iv_emotion_title, false)
                        .setVisible(R.id.tv_emotion_title, true);
        } catch (Exception e) {
            helper.setText(R.id.tv_emotion_title, map.get(TITLE))
                    .setVisible(R.id.iv_emotion_title, false)
                    .setVisible(R.id.tv_emotion_title, true);
        }
    }
}
