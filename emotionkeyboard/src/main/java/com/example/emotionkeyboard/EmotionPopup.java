package com.example.emotionkeyboard;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.emotionkeyboard.entity.SingleEmotion;

import java.io.File;

/**
 * Created by chasing on 2017/12/15.
 */

public class EmotionPopup {
    private ImageView mImageView;
    private Context mContext;
    private RequestListener<Integer, GlideDrawable> imgListener;
    private RequestListener<File, GlideDrawable> loadFileListener;
    private RequestListener<File, GifDrawable> gifFileListener;
    private boolean isShow;

    public EmotionPopup(Activity context) {
        mContext = context;
        mImageView = new ImageView(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.emotion_popup),
                context.getResources().getDimensionPixelOffset(R.dimen.emotion_popup));
        mImageView.setLayoutParams(layoutParams);
        mImageView.setBackgroundResource(R.drawable.shape_emotion_popup);
        mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
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
        loadFileListener = new RequestListener<File, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, File model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, File model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                if (isShow)
                    mImageView.setVisibility(View.VISIBLE);
                return false;
            }
        };
        gifFileListener = new RequestListener<File, GifDrawable>() {
            @Override
            public boolean onException(Exception e, File model, Target<GifDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GifDrawable resource, File model, Target<GifDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
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

    public void show(int x, int y, SingleEmotion emotion) {
        isShow = true;
        mImageView.setX(x);
        mImageView.setY(y - mContext.getResources().getDimensionPixelSize(R.dimen.emotion_popup));
        if (emotion.getEmotionResId()>0){
            Glide.with(mContext)
                    .load(emotion.getEmotionResId())
                    .listener(imgListener)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(mImageView);
        } else {
            if (emotion.getEmotionFilePath().endsWith(".gif")){
                Glide.with(mContext)
                        .load(new File(emotion.getEmotionFilePath()))
                        .asGif()
                        .listener(gifFileListener)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(mImageView);
            } else {
                Glide.with(mContext)
                        .load(new File(emotion.getEmotionFilePath()))
                        .listener(loadFileListener)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(mImageView);
            }
        }
    }

    public void hide() {
        isShow = false;
        mImageView.setVisibility(View.GONE);
    }
}
