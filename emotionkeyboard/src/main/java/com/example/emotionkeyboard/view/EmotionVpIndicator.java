package com.example.emotionkeyboard.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.emotionkeyboard.R;
import com.example.emotionkeyboard.entity.EmotionEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chasing on 2017/9/15.
 * 表情键盘的ViewPager指示器
 */
public class EmotionVpIndicator extends LinearLayout {
    private ViewPager mViewPager;
    private Context mContext;
    private ArrayList<ImageView> mIndicatorViews;
    private Drawable mDrawableSelect;
    private Drawable mDrawableNormal;
    private float mIndicatorSide, mIndicatorMarginLeft, mIndicatorMarginTop, mIndicatorMarginRight, mIndicatorMarginBottom;
    private List<EmotionEntity> mEmotionEntities;
    private int mVpPreSelectPosition = 0;
    private int mIndicatorSelectPosition = 0;

    private List<String> mAllTitle;
    private String mCurrentTitle;

    public EmotionVpIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mIndicatorViews = new ArrayList<>();
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.EmoticonsIndicatorView, 0, 0);
        try {
            mDrawableSelect = a.getDrawable(R.styleable.EmoticonsIndicatorView_indicatorSelect);
            mDrawableNormal = a.getDrawable(R.styleable.EmoticonsIndicatorView_indicatorNormal);
            mIndicatorSide = a.getDimension(R.styleable.EmoticonsIndicatorView_indicatorSide, getResources().getDimension(R.dimen.indicator_side));
            mIndicatorMarginLeft = a.getDimension(R.styleable.EmoticonsIndicatorView_indicatorMarginLeft, getResources().getDimension(R.dimen.indicator_margin));
            mIndicatorMarginRight = a.getDimension(R.styleable.EmoticonsIndicatorView_indicatorMarginRight, getResources().getDimension(R.dimen.indicator_margin));
            mIndicatorMarginTop = a.getDimension(R.styleable.EmoticonsIndicatorView_indicatorMarginTop, getResources().getDimension(R.dimen.indicator_margin));
            mIndicatorMarginBottom = a.getDimension(R.styleable.EmoticonsIndicatorView_indicatorMarginBottom, getResources().getDimension(R.dimen.indicator_margin));
        } finally {
            a.recycle();
        }

        if (mDrawableNormal == null) {
            mDrawableNormal = ContextCompat.getDrawable(getContext(), R.mipmap.indicator_point_nomal);
        }
        if (mDrawableSelect == null) {
            mDrawableSelect = ContextCompat.getDrawable(getContext(), R.mipmap.indicator_point_select);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        setLayoutParams(layoutParams);
    }

    public void resetEmotionEntity(List<EmotionEntity> emotionEntities){
        mEmotionEntities = emotionEntities;
        play("other");
    }

    /**
     * 绑定目标ViewPager及数据源
     */
    public void bindViewPager(ViewPager viewPager, final List<EmotionEntity> emotionEntities, List<String> allTitle) {
        mEmotionEntities = emotionEntities;
        mViewPager = viewPager;

        mAllTitle = new ArrayList<>();
        mAllTitle.addAll(allTitle);

        updateIndicatorViews(0);
        mCurrentTitle = emotionEntities.get(0).getTheme();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == mVpPreSelectPosition) return;
                if (isNeedUpdate(position)) {
                    updateIndicatorViews(position);
                } else {
                    refreshIndicatorViews(position);
                }
                mVpPreSelectPosition = position;
                mCurrentTitle = mEmotionEntities.get(position).getTheme();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        try {
            //设置ViewPager的页面切换速度为0，避免间隔较大的页面跳转产生问题
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(mViewPager.getContext());
            mScroller.set(mViewPager, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否需要更新指示器数量
     *
     * @param position 切换的下标
     */
    private boolean isNeedUpdate(int position) {
        EmotionEntity preEntity = mEmotionEntities.get(mVpPreSelectPosition);
        EmotionEntity curEntity = mEmotionEntities.get(position);
        return !preEntity.getTheme().equals(curEntity.getTheme());
    }

    /**
     * 更新指示器数量
     * 由一个表情集跳到另一个表情集的时候调用
     *
     * @param position 切换的下标
     */
    private void updateIndicatorViews(int position) {
        int themeChildCount = mEmotionEntities.get(position).getThemeChildCount();
        if (mIndicatorViews.size() < themeChildCount) {
            for (int i = mIndicatorViews.size(); i < themeChildCount; i++) {
                ImageView indicatorView = new ImageView(mContext);
                indicatorView.setScaleType(ImageView.ScaleType.CENTER);
                indicatorView.setImageDrawable(mDrawableNormal);
                LinearLayout.LayoutParams layoutParams = new LayoutParams((int) mIndicatorSide, (int) mIndicatorSide);
                layoutParams.setMargins((int) mIndicatorMarginLeft, (int) mIndicatorMarginTop, (int) mIndicatorMarginRight, (int) mIndicatorMarginBottom);
                indicatorView.setLayoutParams(layoutParams);
                mIndicatorViews.add(indicatorView);
                addView(indicatorView);
            }
        }
        for (int i = 0; i < mIndicatorViews.size(); i++) {
            if (i < themeChildCount) {
                mIndicatorViews.get(i).setVisibility(VISIBLE);
            } else {
                mIndicatorViews.get(i).setVisibility(GONE);
            }
            mIndicatorViews.get(i).setImageDrawable(mDrawableNormal);
        }
        if (position >= mVpPreSelectPosition) {
            mIndicatorViews.get(0).setImageDrawable(mDrawableSelect);
            mIndicatorSelectPosition = 0;
        } else {
            mIndicatorViews.get(themeChildCount - 1).setImageDrawable(mDrawableSelect);
            mIndicatorSelectPosition = themeChildCount - 1;
        }
    }

    /**
     * 刷新指示器图标
     * 在同一个表情集切换时调用
     *
     * @param position 切换的下标
     */
    private void refreshIndicatorViews(int position) {
        if (position > mVpPreSelectPosition) {
            mIndicatorViews.get(mIndicatorSelectPosition).setImageDrawable(mDrawableNormal);
            mIndicatorViews.get(mIndicatorSelectPosition + 1).setImageDrawable(mDrawableSelect);
            mIndicatorSelectPosition += 1;
        } else {
            mIndicatorViews.get(mIndicatorSelectPosition).setImageDrawable(mDrawableNormal);
            mIndicatorViews.get(mIndicatorSelectPosition - 1).setImageDrawable(mDrawableSelect);
            mIndicatorSelectPosition -= 1;
        }
    }

    /**
     * 直接跳转到某一个表情集
     *
     * @param title 表情集的名称
     */
    public void play(String title) {
        for (int i = 0; i < mEmotionEntities.size(); i++) {
            if (mEmotionEntities.get(i).getTheme().equals(title)) {
                mVpPreSelectPosition = i;
                break;
            }
        }
        updateIndicatorViews(mVpPreSelectPosition);
        mViewPager.setCurrentItem(mVpPreSelectPosition);
    }

    /**
     * 获取当前的表情标签主题
     */
    public String getCurrentTitle() {
        return mCurrentTitle;
    }

    /**
     * 获取当前的表情标签主题的索引
     */
    public int getCurrentTitlePosition() {
        for (int i = 0; i < mAllTitle.size(); i++) {
            if (mAllTitle.get(i).equals(mCurrentTitle)) return i;
        }
        return -1;
    }
}
