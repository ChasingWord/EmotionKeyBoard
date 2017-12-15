package com.example.testinput;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.emotionkeyboard.adapter.recycleradaper.RecyclerQuickAdapter;
import com.example.emotionkeyboard.adapter.recycleradaper.RecyclerViewHelper;
import com.example.emotionkeyboard.util.EmotionUtil;

/**
 * Created by chasing on 2017/12/14.
 */

public class ChatListAdapter extends RecyclerQuickAdapter<String> {
    public ChatListAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    protected void convert(int viewType, RecyclerViewHelper helper, String s) {
        try {
            Integer resId = Integer.valueOf(s);
            ImageView iv = helper.getView(R.id.iv_chat_pic);
            Glide.with(context)
                    .load(resId)
                    .into(iv);
        } catch (Exception e){
            EmotionUtil.setEmotionContent(context, (TextView) helper.getView(R.id.tv_chat_content), s);
        }
    }
}
