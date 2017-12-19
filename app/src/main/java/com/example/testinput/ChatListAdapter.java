package com.example.testinput;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.emotionkeyboard.adapter.recycleradaper.RecyclerQuickAdapter;
import com.example.emotionkeyboard.adapter.recycleradaper.RecyclerViewHelper;
import com.example.emotionkeyboard.util.EmotionUtil;

import java.io.File;
import java.util.Map;

/**
 * Created by chasing on 2017/12/14.
 */

public class ChatListAdapter extends RecyclerQuickAdapter<Map<String, String>> {
    public static final String CONTENT = "content";
    public static final String RES_ID = "resId";
    public static final String PIC_FILE_PATH = "picFilePath";

    public ChatListAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    protected void convert(int viewType, RecyclerViewHelper helper, Map<String, String> item) {
        String resId = item.get(RES_ID);
        if (!TextUtils.isEmpty(resId) && Integer.valueOf(resId) > 0) {
            ImageView iv = helper.getView(R.id.iv_chat_pic);
            Glide.with(context)
                    .load(Integer.valueOf(resId))
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(iv);
            helper.setVisible(R.id.tv_chat_content, false)
                    .setVisible(R.id.iv_chat_pic, true);
        } else {
            String picFilePath = item.get(PIC_FILE_PATH);
            if (!TextUtils.isEmpty(picFilePath)) {
                ImageView iv = helper.getView(R.id.iv_chat_pic);
                Glide.with(context)
                        .load(new File(picFilePath))
                        .placeholder(R.mipmap.ic_launcher_round)
                        .into(iv);
                helper.setVisible(R.id.tv_chat_content, false)
                        .setVisible(R.id.iv_chat_pic, true);
            } else {
                String content = item.get(CONTENT);
                EmotionUtil.setEmotionContent(context, (TextView) helper.getView(R.id.tv_chat_content), content);
                helper.setVisible(R.id.iv_chat_pic, false)
                        .setVisible(R.id.tv_chat_content, true);
            }
        }
    }
}
