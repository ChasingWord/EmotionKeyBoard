package com.example.testinput.keyboard;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.testinput.R;
import com.example.testinput.adapter.recycleradaper.BaseRecylerAdapter;
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
    private EmotionEntity mEmotionEntity;

    private EditText mEtInput;

    private RecyclerView mEmotionRcv;
    private EmotionAdapter mEmotionAdapter;

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
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.frag_emotion, null);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mEtInput = getActivity().findViewById(R.id.et_input);
    }

    private void initView(View view) {
        mEmotionAdapter = new EmotionAdapter(getActivity(), R.layout.item_emotion);
        mEmotionRcv = view.findViewById(R.id.rcv_emotion);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 7);
        mEmotionRcv.setLayoutManager(gridLayoutManager);
        mEmotionRcv.setAdapter(mEmotionAdapter);
        mEmotionAdapter.setItemClickListener(new BaseRecylerAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                if (mEmotionEntity.isEmotionIcon()) {
                    //是表情
                    if (position == 21){
                        mEtInput.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                    } else {
                        String msg = mEtInput.getText().toString() + mEmotionAdapter.getItem(position).getEmotionName();
                        EmotionUtil.getEmotionContent(EmotionUtil.EMOTION_CLASSIC_TYPE, getActivity(), mEtInput, msg);
                        mEtInput.setSelection(msg.length());
                    }
                } else {
                    //是图片
                }
            }
        });

        mEmotionEntity = (EmotionEntity) getArguments().getSerializable(INTENT_DATA);
        if (mEmotionEntity != null) {
            HashMap<String, Integer> emotionMap = mEmotionEntity.getEmotionMap();
            ArrayList<SingleEmotion> allEmotion = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : emotionMap.entrySet()) {
                SingleEmotion emotion = new SingleEmotion();
                emotion.setEmotionName(entry.getKey())
                        .setEmotionResId(entry.getValue());
                allEmotion.add(emotion);
            }
            mEmotionAdapter.addAll(allEmotion);
        }
    }
}
