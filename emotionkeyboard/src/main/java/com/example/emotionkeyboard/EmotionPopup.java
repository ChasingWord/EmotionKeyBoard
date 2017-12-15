package com.example.emotionkeyboard;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * Created by chasing on 2017/12/15.
 */

public class EmotionPopup {
    private ImageView mImageView;
    private Context mContext;
    private RequestListener<Integer, GlideDrawable> imgListener;
    private boolean isShow;

    public EmotionPopup(Activity context) {
        mContext = context;
        mImageView = new ImageView(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.emotion_popup),
                context.getResources().getDimensionPixelOffset(R.dimen.emotion_popup));
        mImageView.setLayoutParams(layoutParams);
        mImageView.setBackgroundResource(R.drawable.shape_emotion_popup);
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mImageView.setPadding(20, 20, 20, 20);
        imgListener = new RequestListener<Integer, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, Integer model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, Integer model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                if (isShow)
                    mImageView.setVisibility(View.VISIBLE);
                return false;
            }
        };

        ViewGroup decorView = (ViewGroup) context.getWindow().getDecorView();
        View child = decorView.getChildAt(0);
        if (!(child instanceof FrameLayout)) {
            decorView.removeView(child);
            FrameLayout frameLayout = new FrameLayout(context);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            frameLayout.setLayoutParams(params);
            frameLayout.addView(child);
            frameLayout.addView(mImageView);
            decorView.addView(frameLayout);
        } else {
            ((FrameLayout) child).addView(mImageView);
        }

        mImageView.setVisibility(View.GONE);
    }

    public void show(int x, int y, int resId) {
        isShow = true;
        mImageView.setX(x);
        mImageView.setY(y - mContext.getResources().getDimensionPixelSize(R.dimen.emotion_popup));
        Glide.with(mContext)
                .load(resId)
                .listener(imgListener)
                .into(mImageView);
    }

    public void hide() {
        isShow = false;
        mImageView.setVisibility(View.GONE);
    }
}
