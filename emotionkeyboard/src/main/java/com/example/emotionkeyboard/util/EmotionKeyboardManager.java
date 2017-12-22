package com.example.emotionkeyboard.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by chasing on 2017/9/14.
 * 表情键盘的控制器
 */
public class EmotionKeyboardManager {
    private static final String SHARE_PREFERENCE_SOFT_INPUT_HEIGHT = "softInputHeight";
    private static final String SHARE_PREFERENCE_NAME = "emotionKeyboard";
    private View mContentView;
    private View mEmotionView;
    private View mFunctionView;
    private Activity mActivity;
    private EditText mEtInput;
    private InputMethodManager mInputMethodManager;
    private SharedPreferences mSp;

    private int mSoftInputHeight;

    private EmotionKeyboardManager() {
    }

    /**
     * 外部静态调用
     */
    public static EmotionKeyboardManager with(Activity activity) {
        EmotionKeyboardManager emotionInputDetector = new EmotionKeyboardManager();
        emotionInputDetector.mActivity = activity;
        emotionInputDetector.mInputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        emotionInputDetector.mSp = activity.getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return emotionInputDetector;
    }

    /**
     * 绑定内容view，此view用于固定bar的高度，防止跳闪
     */
    public EmotionKeyboardManager bindToContent(View contentView) {
        mContentView = contentView;
        mContentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideSoftInput();
                hideAllView();
                return false;
            }
        });
        return this;
    }

    /**
     * 绑定编辑框
     */
    public EmotionKeyboardManager bindToEditText(EditText editText) {
        mEtInput = editText;
        mEtInput.requestFocus();
        mEtInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP && mEmotionView.isShown()) {
                    lockContentHeight();//显示软件盘时，锁定内容高度，防止跳闪。
                    hideEmotionLayout();//隐藏表情布局，显示软件盘
                    hideFunctionLayout();
                    showSoftInput();
                    //软件盘显示后，释放内容高度
                    mEtInput.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            unlockContentHeightDelayed();
                        }
                    }, 200L);
                }
                return false;
            }
        });
        return this;
    }

    /**
     * 绑定表情按钮
     */
    public EmotionKeyboardManager bindToEmotionButton(View emotionButton) {
        emotionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEmotionView.isShown()) {
                    lockContentHeight();//显示软件盘时，锁定内容高度，防止跳闪。
                    hideEmotionLayout();//隐藏表情布局，显示软件盘
                    showSoftInput();
                    unlockContentHeightDelayed();//软件盘显示后，释放内容高度
                } else {
                    if (isSoftInputShown()) {//同上
                        lockContentHeight();
                        showEmotionLayout();
                        unlockContentHeightDelayed();
                    } else {
                        hideFunctionLayout();
                        showEmotionLayout();//两者都没显示，直接显示表情布局
                    }
                }
            }
        });
        return this;
    }

    /**
     * 绑定功能按钮
     */
    public EmotionKeyboardManager bindToFunctionButton(View functionButton) {
        functionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFunctionView == null) throw new NullPointerException("FunctionView must not be null");
                if (mFunctionView.isShown()) {
                    lockContentHeight();//显示软件盘时，锁定内容高度，防止跳闪。
                    hideFunctionLayout();//隐藏表情布局，显示软件盘
                    showSoftInput();
                    unlockContentHeightDelayed();//软件盘显示后，释放内容高度
                } else {
                    if (isSoftInputShown()) {//同上
                        lockContentHeight();
                        showFunctionLayout();
                        unlockContentHeightDelayed();
                    } else {
                        hideEmotionLayout();
                        showFunctionLayout();//两者都没显示，直接显示表情布局
                    }
                }
            }
        });
        return this;
    }

    /**
     * 设置表情内容布局
     */
    public EmotionKeyboardManager setEmotionView(View emotionView) {
        mEmotionView = emotionView;
        return this;
    }

    /**
     * 设置功能键盘
     */
    public EmotionKeyboardManager setFunctionView(View functionView) {
        mFunctionView = functionView;
        return this;
    }

    public EmotionKeyboardManager build() {
        //设置软件盘的模式：SOFT_INPUT_ADJUST_RESIZE  这个属性表示Activity的主窗口总是会被调整大小，从而保证软键盘显示空间。
        //从而方便我们计算软件盘的高度
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //隐藏软件盘
        hideSoftInput();
        return this;
    }

    /**
     * 点击返回键时先隐藏表情布局
     */
    public boolean interceptBackPress() {
        if (mEmotionView.isShown()) {
            hideEmotionLayout();
            return true;
        }
        return false;
    }

    /**
     * 展示表情布局
     */
    private void showEmotionLayout() {
        if (mSoftInputHeight <= 0) {
            mSoftInputHeight = getSupportSoftInputHeight();
        }
        if (mSoftInputHeight <= 0) {
            mSoftInputHeight = mSp.getInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, 797);
        }
        hideSoftInput();
        mEmotionView.getLayoutParams().height = mSoftInputHeight;
        mEmotionView.setVisibility(View.VISIBLE);
    }

    /**
     * 展示功能布局
     */
    private void showFunctionLayout() {
        if (mSoftInputHeight <= 0) {
            mSoftInputHeight = getSupportSoftInputHeight();
        }
        if (mSoftInputHeight <= 0) {
            mSoftInputHeight = mSp.getInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, 797);
        }
        hideSoftInput();
        mFunctionView.getLayoutParams().height = mSoftInputHeight;
        mFunctionView.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏所有布局
     */
    private void hideAllView(){
        if (mEmotionView != null && mEmotionView.isShown())
            mEmotionView.setVisibility(View.GONE);
        if (mFunctionView != null && mFunctionView.isShown())
            mFunctionView.setVisibility(View.GONE);
    }

    /**
     * 隐藏表情布局
     */
    private void hideEmotionLayout() {
        if (mEmotionView != null && mEmotionView.isShown())
            mEmotionView.setVisibility(View.GONE);
    }

    /**
     * 隐藏功能布局
     */
    private void hideFunctionLayout() {
        if (mFunctionView != null && mFunctionView.isShown())
            mFunctionView.setVisibility(View.GONE);
    }

    /**
     * 锁定内容高度，防止跳闪
     */
    private void lockContentHeight() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
        params.height = mContentView.getHeight();
        params.weight = 0.0F;
    }

    /**
     * 释放被锁定的内容高度
     */
    private void unlockContentHeightDelayed() {
        mEtInput.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((LinearLayout.LayoutParams) mContentView.getLayoutParams()).weight = 1.0F;
            }
        }, 200L);
    }

    /**
     * 编辑框获取焦点，并显示软件盘
     */
    private void showSoftInput() {
        mEtInput.requestFocus();
        mEtInput.post(new Runnable() {
            @Override
            public void run() {
                mInputMethodManager.showSoftInput(mEtInput, 0);
            }
        });
    }

    /**
     * 隐藏软件盘
     */
    private void hideSoftInput() {
        mInputMethodManager.hideSoftInputFromWindow(mEtInput.getWindowToken(), 0);
    }

    /**
     * 是否显示软件盘
     */
    private boolean isSoftInputShown() {
        return getSupportSoftInputHeight() != 0;
    }

    /**
     * 获取软件盘的高度
     */
    private int getSupportSoftInputHeight() {
        Rect r = new Rect();
        /*
         * decorView是window中的最顶层view，可以从window中通过getDecorView获取到decorView。
         * 通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。
         */
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        //获取屏幕的高度
        int screenHeight = mActivity.getWindow().getDecorView().getRootView().getHeight();
        //计算软件盘的高度
        int softInputHeight = screenHeight - r.bottom;
        /*
         * 某些Android版本下，没有显示软键盘时减出来的高度总是144，而不是零，
         * 这是因为高度是包括了虚拟按键栏的(例如华为系列)，所以在API Level高于20时，
         * 我们需要减去底部虚拟按键栏的高度（如果有的话）
         */
        if (Build.VERSION.SDK_INT >= 20) {
            // When SDK Level >= 20 (Android L), the softInputHeight will contain the height of softButtonsBar (if has)
            softInputHeight = softInputHeight - getSoftButtonsBarHeight();
        }
        if (softInputHeight <= 0) {
            Log.w("emotion", "EmotionKeyboard--Warning: value of softInputHeight is below zero!");
        }
        //存一份到本地
        if (softInputHeight > 0) {
            mSp.edit().putInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, softInputHeight).apply();
        }
        return softInputHeight;
    }


    /**
     * 底部虚拟按键栏的高度
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        mActivity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }
}