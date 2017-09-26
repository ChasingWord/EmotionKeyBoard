package com.example.testinput.keyboard;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;

import com.example.testinput.R;
import com.example.testinput.adapter.BaseQuickAdapter;
import com.example.testinput.keyboard.entity.EmotionEntity;
import com.example.testinput.keyboard.entity.SingleEmotion;
import com.example.testinput.keyboard.util.EmotionUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chasing on 2017/9/15.
 * 一页表情
 */
public class EmotionFragment extends Fragment {
    public static final String INTENT_DATA = "data";

    //表情的行列数
    private static final int COLUMN_COUNT_EMOTION = 7;
    private static final int ROW_COUNT_EMOTION = 3;
    //图片的行列数
    private static final int COLUMN_COUNT_IMG = 3;
    private static final int ROW_COUNT_IMG = 2;

    private View mRootView;
    private GridView mGvEmotion;
    private EmotionGvAdapter mEmotionGvAdapter;

    /**
     * 注意：
     *      传入进来的表情/图片的数量不能超过设置的行列数积，否则多余的部分将不会显示
     *      表情的总数需要在行列数积的基础上-1，因为最后一个需要添加"删除"按钮
     */
    private EmotionEntity mEmotionEntity;
    private int mPageTotalCount = 1;
    private boolean initOnce = true;

    private EditText mEtInput;


    public EmotionFragment() {

    }

    public static EmotionFragment getInstance(EmotionEntity emotionEntity) {
        EmotionFragment emotionFragment = new EmotionFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(INTENT_DATA, emotionEntity);
        emotionFragment.setArguments(bundle);
        return emotionFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null){//vp切换时，会重新调用onCreateView，添加判断避免重复初始化
            mRootView = inflater.inflate(R.layout.frag_emotion, null);
            parseBundle();
            initView(mRootView);
            initData();
        }
        return mRootView;
    }

    private void parseBundle() {
        Bundle arguments = getArguments();
        mEmotionEntity = (EmotionEntity) arguments.getSerializable(INTENT_DATA);
    }

    private void initView(View rootView) {
        mGvEmotion = rootView.findViewById(R.id.grid_emotion);
        if (mEmotionEntity.isEmotionIcon()) {
            mGvEmotion.setNumColumns(COLUMN_COUNT_EMOTION);
            mPageTotalCount *= COLUMN_COUNT_EMOTION;
        } else {
            mGvEmotion.setNumColumns(COLUMN_COUNT_IMG);
            mPageTotalCount *= COLUMN_COUNT_IMG;
        }
        int rowCount;
        if (mEmotionEntity.isEmotionIcon()) {
            rowCount = ROW_COUNT_EMOTION;
            mPageTotalCount *= ROW_COUNT_EMOTION;
        } else {
            rowCount = ROW_COUNT_IMG;
            mPageTotalCount *= ROW_COUNT_IMG;
        }
        mEmotionGvAdapter = new EmotionGvAdapter(mGvEmotion, getActivity(), R.layout.item_emotion, rowCount);
        mGvEmotion.setAdapter(mEmotionGvAdapter);
        mEmotionGvAdapter.setItemClickListener(new BaseQuickAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                if (mEmotionEntity.isEmotionIcon()){
                    //是表情则显示在EditText
                    if (position == mPageTotalCount - 1) {
                        //最后一个图标是删除
                        mEtInput.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                    } else {
                        SingleEmotion singleEmotion = mEmotionGvAdapter.getItem(position);
                        if (singleEmotion.getEmotionResId() == -1) return;
                        String emotionString = mEtInput.getText().toString() + singleEmotion.getEmotionName();
                        EmotionUtil.setEmotionContent(getActivity(), mEtInput, emotionString);
                        mEtInput.setSelection(emotionString.length());
                    }
                } else {
                    //是图片则直接进行显示，不在EditText显示
                    //Todo 获取Activity上面显示图片的UI，将图片直接显示上去
                }
            }
        });
    }

    private void initData() {
        HashMap<String, Integer> emotionMap = mEmotionEntity.getEmotionMap();
        ArrayList<SingleEmotion> emotionList = new ArrayList<>();
        for (Map.Entry<String, Integer> map : emotionMap.entrySet()) {
            SingleEmotion singleEmotion = new SingleEmotion();
            singleEmotion.setEmotionName(map.getKey());
            singleEmotion.setEmotionResId(map.getValue());
            emotionList.add(singleEmotion);
        }
        if (mEmotionEntity.isEmotionIcon()){
            //是表情，则页面的最后一个是“删除”按钮，是图片则不需要处理
            for (int i = emotionList.size(); i >= mPageTotalCount; i--) {
                emotionList.remove(i - 1);
            }
            for (int i = emotionList.size(); i < mPageTotalCount; i++) {
                SingleEmotion singleEmotion = new SingleEmotion();
                if (i == mPageTotalCount - 1) {
                    singleEmotion.setEmotionName("删除")
                            .setEmotionResId(R.mipmap.ic_launcher_round);
                    emotionList.add(singleEmotion);
                } else {
                    singleEmotion.setEmotionName("")
                            .setEmotionResId(-1);
                    emotionList.add(singleEmotion);
                }
            }
        }
        mEmotionGvAdapter.clear();
        mEmotionGvAdapter.addAll(emotionList);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mEtInput = getActivity().findViewById(R.id.et_input);
    }
}
