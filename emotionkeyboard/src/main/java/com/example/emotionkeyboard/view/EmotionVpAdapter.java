package com.example.emotionkeyboard.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.emotionkeyboard.adapter.FragmentPageAdapter;

import java.util.List;

/**
 * Created by chasing on 2017/9/15.
 * ViewPager切换的Adapter
 */
public class EmotionVpAdapter extends FragmentPageAdapter {
    private List<Fragment> datas;
    private FragmentManager fm;
    private int mChildCount = 0;

    public EmotionVpAdapter(FragmentManager fm, List<Fragment> datas) {
        super(fm);
        this.fm = fm;
        this.datas = datas;
    }

    public void setDatas(List<Fragment> datas) {
        mChildCount = getCount();
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        // 最简单解决 notifyDataSetChanged() 页面不刷新问题的方法
        if ( mChildCount > 0) {
            mChildCount --;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
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
