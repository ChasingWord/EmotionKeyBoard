package com.example.testinput.keyboard;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testinput.R;
import com.example.testinput.adapter.recycleradaper.BaseRecylerAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by chasing on 2017/9/15.
 * 一页表情
 */
public class EmotionFragment extends Fragment {
    public static final String INTENT_DATA = "data";
    private EmotionEntity mEmotionEntity;

    private RecyclerView mEmotionRcv;
    private EmotionAdapter mEmotionAdapter;

    public EmotionFragment(){

    }

    public static EmotionFragment getInstance(EmotionEntity emotionEntity){
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

    private void initView(View view) {
        mEmotionAdapter = new EmotionAdapter(getActivity(), R.layout.item_emotion);
        mEmotionRcv = view.findViewById(R.id.rcv_emotion);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 7);
        mEmotionRcv.setLayoutManager(gridLayoutManager);
        mEmotionRcv.setAdapter(mEmotionAdapter);
        mEmotionAdapter.setItemClickListener(new BaseRecylerAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {

            }
        });

        EmotionEntity emotionEntity = (EmotionEntity) getArguments().getSerializable(INTENT_DATA);
        if (emotionEntity != null) {
            HashMap<String, Integer> emotionMap = emotionEntity.getEmotionMap();
            Collection<Integer> values = emotionMap.values();
            ArrayList<Integer> array = new ArrayList<>(values);
            mEmotionAdapter.addAll(array);
        }
    }
}
