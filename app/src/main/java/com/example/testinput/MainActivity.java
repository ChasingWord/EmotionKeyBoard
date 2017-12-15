package com.example.testinput;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.emotionkeyboard.EmotionKeyboardLayout;
import com.example.emotionkeyboard.util.EmotionKeyboardManager;
import com.example.emotionkeyboard.util.EmotionUtil;
import com.example.emotionkeyboard.view.EmotionFragment;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

    private RecyclerView mRlContent;
    private ImageButton mIbChangeEmotion;
    private EditText mEtInput;
    private Button mSend;

    private EmotionKeyboardLayout mEmotionKeyboardLayout;

    private ChatListAdapter mChatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmotionKeyboardLayout = (EmotionKeyboardLayout) findViewById(R.id.emotion_keyboard_layout);
        mRlContent = (RecyclerView) findViewById(R.id.rl_content);
        mIbChangeEmotion = (ImageButton) findViewById(R.id.ib);
        mEtInput = (EditText) findViewById(R.id.et_input);
        mSend = (Button) findViewById(R.id.send);

        // 一、初始化键盘管理类
        EmotionKeyboardManager.with(this)
                .setEmotionView(mEmotionKeyboardLayout)
                .bindToContent(mRlContent)
                .bindToEditText(mEtInput)
                .bindToEmotionButton(mIbChangeEmotion)
                .build();

        // 二、初始化数据源
        mEmotionKeyboardLayout.bindEditText(mEtInput);// 绑定输入控件
        EmotionFragment.OnClickPicListener onClickPicListener = new EmotionFragment.OnClickPicListener() {
            @Override
            public void onClickPic(String theme, int resId) {
                mChatListAdapter.add(resId + "");
            }
        };
        mEmotionKeyboardLayout.createEmotionFragment(true, "theme1", -1, EmotionUtil.getEmotionMap(EmotionUtil.EMOTION_CLASSIC_TYPE1), null);
        mEmotionKeyboardLayout.createEmotionFragment(false, "theme2", R.mipmap.theme2, EmotionUtil.getEmotionMap(EmotionUtil.EMOTION_CLASSIC_TYPE2), onClickPicListener);
        mEmotionKeyboardLayout.createEmotionFragment(true, "theme3", R.mipmap.theme3, EmotionUtil.getEmotionMap(EmotionUtil.EMOTION_CLASSIC_TYPE3), null);
        mEmotionKeyboardLayout.createEmotionFragment(false, "theme4", R.mipmap.theme4, EmotionUtil.getEmotionMap(EmotionUtil.EMOTION_CLASSIC_TYPE4), onClickPicListener);
        mEmotionKeyboardLayout.loadEmotionVp(getSupportFragmentManager());

        mChatListAdapter = new ChatListAdapter(this, R.layout.item_chat);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRlContent.setLayoutManager(layoutManager);
        mRlContent.setAdapter(mChatListAdapter);
        mChatListAdapter.addAll(new ArrayList<String>());

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChatListAdapter.add(mEtInput.getText().toString());
                mEtInput.setText("");
            }
        });
    }

    private void showSoftInput() {
        mEtInput.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEtInput, InputMethodManager.SHOW_FORCED);
    }

    private void hideSoftInput() {
        mEtInput.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) { //输入法打开状态
            inputMethodManager.hideSoftInputFromWindow(mEtInput.getWindowToken()
                    , InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }
}
