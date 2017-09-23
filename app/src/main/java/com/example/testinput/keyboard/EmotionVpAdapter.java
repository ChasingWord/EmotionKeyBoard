package com.example.testinput.keyboard;

import android.app.Fragment;
import android.app.FragmentManager;

import com.example.testinput.adapter.FragmentPageAdapter;

import java.util.List;

/**
 * Created by chasing on 2017/9/15.
 */

public class EmotionVpAdapter extends FragmentPageAdapter {
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
