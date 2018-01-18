package com.example.emotionkeyboard.view.manager;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.emotionkeyboard.R;
import com.example.emotionkeyboard.adapter.recycleradaper.RecyclerQuickAdapter;
import com.example.emotionkeyboard.adapter.recycleradaper.RecyclerViewHelper;
import com.example.emotionkeyboard.util.FileUtil;

/**
 * Created by chasing on 2017/12/18.
 */

public class ManagerAdapter extends RecyclerQuickAdapter<String> {
    private int mViewHeight;

    private boolean isManage;

    public ManagerAdapter(Context context, int layoutId, int viewHeight) {
        super(context, layoutId);
        mViewHeight = viewHeight;
    }

    public boolean isManage() {
        return isManage;
    }

    public void setManage(boolean isManage) {
        this.isManage = isManage;
    }

    @Override
    protected void convert(int viewType, final RecyclerViewHelper helper, final String item) {
        ImageView iconView = helper.getView(R.id.iv_emotion);
        try {
            // 转换成功则为mipmap资源id
            int resId = Integer.valueOf(item);
            if (resId > 0) {
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

        if (helper.getPosition() != 0)
            helper.setVisible(R.id.ib_delete, isManage);
        else
            helper.setVisible(R.id.ib_delete, false);
        helper.setOnClickListener(R.id.ib_delete, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileUtil.deleteLocalPicFile(item);
                remove(item);
                if (mOnClickDeletePicListener != null){
                    mOnClickDeletePicListener.onClickDelete();
                }
            }
        });
    }

    private OnClickDeletePicListener mOnClickDeletePicListener;
    public void setOnClickDeletePicListener(OnClickDeletePicListener onClickDeletePicListener){
        mOnClickDeletePicListener = onClickDeletePicListener;
    }
    public interface OnClickDeletePicListener{
        void onClickDelete();
    }
}
