package com.example.emotionkeyboard.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by chasing on 2017/9/15.
 * ViewPager切换的Adapter
 */
public class EmotionVpAdapter extends FragmentPagerAdapter {
    private List<Fragment> datas;

    public EmotionVpAdapter(FragmentManager fm, List<Fragment> datas) {
        super(fm);
        this.datas = datas;
    }

    @Override
    public Fragment getItem(int position) {
        return datas.get(position);
    }

    @Override
    public int getCount() {
        return datas.size();
    }
}
