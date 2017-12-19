package com.example.emotionkeyboard.view.manager;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.emotionkeyboard.R;
import com.example.emotionkeyboard.adapter.recycleradaper.RecyclerQuickAdapter;
import com.example.emotionkeyboard.adapter.recycleradaper.RecyclerViewHelper;

/**
 * Created by chasing on 2017/12/18.
 */

public class ManagerAdapter extends RecyclerQuickAdapter<String> {
    private int mViewHeight;

    public ManagerAdapter(Context context, int layoutId, int viewHeight) {
        super(context, layoutId);
        mViewHeight = viewHeight;
    }

    @Override
    protected void convert(int viewType, RecyclerViewHelper helper, String item) {
        ImageView iconView = helper.getView(R.id.iv_emotion);
        try {
            // 转换成功则为mipmap资源id
            int resId = Integer.valueOf(item);
            if (resId > 0) {
//                helper.setImageResource(R.id.iv_emotion, resId);
                Glide.with(context)
                        .load(resId)
                        .asBitmap()
                        .into(iconView);
            }
        } catch (Exception e) {
            // 转换失败则说明为路径
            if (!TextUtils.isEmpty(item)) {
                Glide.with(context)
                        .load(item)
                        .asBitmap()
                        .into(iconView);
            }
        }
        helper.setViewHeight(R.id.iv_emotion, mViewHeight);
    }
}
