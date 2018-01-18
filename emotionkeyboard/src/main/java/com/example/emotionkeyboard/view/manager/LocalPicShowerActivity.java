package com.example.emotionkeyboard.view.manager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.emotionkeyboard.R;
import com.example.emotionkeyboard.adapter.recycleradaper.BaseRecyclerAdapter;
import com.example.emotionkeyboard.decoration.FlexibleDividerDecoration;
import com.example.emotionkeyboard.decoration.VerticalDividerItemDecoration;
import com.example.emotionkeyboard.util.EmotionUtil;
import com.example.emotionkeyboard.util.GenericTools;
import com.example.emotionkeyboard.view.BaseActivity;
import com.example.emotionkeyboard.view.EmotionTitleAdapter;
import com.example.emotionkeyboard.view.FixedSpeedScroller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chasing on 2018/1/16.
 * 展示所有的表情（apk包含的mipmap资源图）
 */
public class LocalPicShowerActivity extends BaseActivity {
    private ViewPager mVp;
    private RecyclerView mRcvTitle;

    private EmotionTitleAdapter mTitleAdapter;
    private LocalPicShowerVpAdapter mVpAdapter;

    private List<RecyclerView> mEmotionRcvList;
    private List<Map<String, String>> mVpTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_local_pic_shower);

        initData();
        initView();
    }

    private void initView() {
        initRcvTitle();
        initVp();
    }

    private void initRcvTitle() {
        mRcvTitle = (RecyclerView) findViewById(R.id.rcv_local_pic_shower_title);
        mTitleAdapter = new EmotionTitleAdapter(this, R.layout.item_emotion_title);
        mRcvTitle.setAdapter(mTitleAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRcvTitle.setLayoutManager(linearLayoutManager);
        mRcvTitle.addItemDecoration(new VerticalDividerItemDecoration.Builder(this)
                .visibilityProvider(new FlexibleDividerDecoration.VisibilityProvider() {
                    @Override
                    public boolean shouldHideDivider(int position, RecyclerView parent) {
                        return false;
                    }
                }).colorResId(android.R.color.darker_gray).build());

        mTitleAdapter.setItemClickListener(new BaseRecyclerAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                mVp.setCurrentItem(position);
            }
        });
        mTitleAdapter.addAll(mVpTitle);
    }

    private void initVp() {
        mVp = (ViewPager) findViewById(R.id.vp_local_pic_shower);
        mVpAdapter = new LocalPicShowerVpAdapter(mEmotionRcvList);
        mVp.setAdapter(mVpAdapter);
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTitleAdapter.setSelectPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        try {
            //设置ViewPager的页面切换速度为0，避免间隔较大的页面跳转产生问题
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(this);
            mScroller.set(mVp, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        mEmotionRcvList = new ArrayList<>();
        mVpTitle = new ArrayList<>();

        mEmotionRcvList.add(createSingleRcv("", R.mipmap.ic_launcher_round, EmotionUtil.getEmotionMap(EmotionUtil.EMOTION_CLASSIC_TYPE2)));
        mEmotionRcvList.add(createSingleRcv("", R.mipmap.add_pic_bg_n, EmotionUtil.getEmotionMap(EmotionUtil.EMOTION_CLASSIC_TYPE3)));
    }

    private RecyclerView createSingleRcv(String title, int themeIconResId, LinkedHashMap<String, Integer> singleEmotionPageData) {
        HashMap<String, String> titleMap = new HashMap<>();
        titleMap.put(EmotionTitleAdapter.TITLE, title);
        titleMap.put(EmotionTitleAdapter.ICON, String.valueOf(themeIconResId));
        mVpTitle.add(titleMap);

        RecyclerView recyclerView = new RecyclerView(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        recyclerView.setLayoutParams(layoutParams);
        ManagerAdapter managerAdapter = new ManagerAdapter(this, R.layout.item_emotion, GenericTools.getScreenWidth(this) / 4);
        recyclerView.setAdapter(managerAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(gridLayoutManager);
        //recyclerView.addItemDecoration(new DividerGridItemDecoration(this).colorResId(android.R.color.darker_gray));

        ArrayList<String> emotionData = new ArrayList<>();
        for (Map.Entry<String, Integer> map : singleEmotionPageData.entrySet()) {
            emotionData.add(String.valueOf(map.getValue()));
        }
        managerAdapter.addAll(emotionData);
        return recyclerView;
    }
}