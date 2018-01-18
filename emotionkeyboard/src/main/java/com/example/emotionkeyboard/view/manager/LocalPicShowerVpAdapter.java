package com.example.emotionkeyboard.view.manager;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by chasing on 2018/1/16.
 */

public class LocalPicShowerVpAdapter extends PagerAdapter {
    private List<RecyclerView> data;

    public LocalPicShowerVpAdapter(List<RecyclerView> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(data.get(position), 0);//添加页卡
        return data.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(data.get(position));//删除页卡
    }
}
