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
import android.widget.LinearLayout;

import com.example.emotionkeyboard.EmotionKeyboardLayout;
import com.example.emotionkeyboard.util.EmotionKeyboardManager;
import com.example.emotionkeyboard.util.EmotionUtil;
import com.example.emotionkeyboard.view.EmotionFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends FragmentActivity {

    private RecyclerView mRcvContent;
    private ImageButton mIbEmotion;
    private ImageButton mIbFunction;
    private EditText mEtInput;
    private Button mSend;

    private EmotionKeyboardLayout mEmotionKeyboardLayout;
    private LinearLayout mFunctionKeyboardLayout;

    private ChatListAdapter mChatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmotionKeyboardLayout = (EmotionKeyboardLayout) findViewById(R.id.emotion_keyboard_layout);
        mFunctionKeyboardLayout = (LinearLayout) findViewById(R.id.function_keyboard_layout);
        mRcvContent = (RecyclerView) findViewById(R.id.rl_content);
        mIbEmotion = (ImageButton) findViewById(R.id.ib_emotion);
        mIbFunction = (ImageButton) findViewById(R.id.ib_function);
        mEtInput = (EditText) findViewById(R.id.et_input);
        mSend = (Button) findViewById(R.id.send);

        // 一、初始化键盘管理类
        EmotionKeyboardManager.with(this)
                .setEmotionView(mEmotionKeyboardLayout)
//                .setFunctionView(mFunctionKeyboardLayout)
                .bindToContent(mRcvContent)
                .bindToEditText(mEtInput)
                .bindToEmotionButton(mIbEmotion)
                .bindToFunctionButton(mIbFunction)
                .build();

        // 二、初始化数据源
        mEmotionKeyboardLayout.bindEditText(mEtInput);// 绑定输入控件
        EmotionFragment.OnClickPicListener onClickPicListener = new EmotionFragment.OnClickPicListener() {
            @Override
            public void onClickPic(String theme, int resId, String picFilePath) {
                Map<String,String> map = new HashMap<>();
                map.put(ChatListAdapter.RES_ID, String.valueOf(resId));
                map.put(ChatListAdapter.PIC_FILE_PATH, picFilePath);
                mChatListAdapter.add(map);
                mRcvContent.scrollToPosition(mChatListAdapter.getItemCount() - 1);
            }
        };
        mEmotionKeyboardLayout.createEmotionFragment(false, "theme1", -1, EmotionUtil.getEmotionMap(EmotionUtil.EMOTION_CLASSIC_TYPE1), onClickPicListener);
        mEmotionKeyboardLayout.createEmotionFragment(false, "theme2", R.mipmap.theme2, EmotionUtil.getEmotionMap(EmotionUtil.EMOTION_CLASSIC_TYPE2), onClickPicListener);
        mEmotionKeyboardLayout.createEmotionFragment(true, "theme3", R.mipmap.theme3, EmotionUtil.getEmotionMap(EmotionUtil.EMOTION_CLASSIC_TYPE3), onClickPicListener);
        mEmotionKeyboardLayout.createEmotionFragment(false, "theme4", R.mipmap.theme4, EmotionUtil.getEmotionMap(EmotionUtil.EMOTION_CLASSIC_TYPE4), onClickPicListener);
        mEmotionKeyboardLayout.initLocalFileEmotion(onClickPicListener);
        mEmotionKeyboardLayout.loadEmotionVp(getSupportFragmentManager());

        mChatListAdapter = new ChatListAdapter(this, R.layout.item_chat);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRcvContent.setLayoutManager(layoutManager);
        mRcvContent.setAdapter(mChatListAdapter);
        mChatListAdapter.addAll(new ArrayList<Map<String, String>>());

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> map = new HashMap<>();
                map.put(ChatListAdapter.CONTENT, mEtInput.getText().toString());
                mChatListAdapter.add(map);
                mEtInput.setText("");
                mRcvContent.scrollToPosition(mChatListAdapter.getItemCount() - 1);
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
