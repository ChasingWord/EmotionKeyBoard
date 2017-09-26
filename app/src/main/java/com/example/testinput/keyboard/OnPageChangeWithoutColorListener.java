package com.example.testinput.keyboard;

import android.support.v4.view.ViewPager;
import android.widget.EdgeEffect;

import java.lang.reflect.Field;

/**
 * Created by chasing on 2017/9/26.
 */

public abstract class OnPageChangeWithoutColorListener implements ViewPager.OnPageChangeListener {

    private EdgeEffect leftEdge;
    private EdgeEffect rightEdge;

    public OnPageChangeWithoutColorListener(ViewPager viewPager) {
        try {
            Field leftEdgeField = viewPager.getClass().getDeclaredField("mLeftEdge");
            Field rightEdgeField = viewPager.getClass().getDeclaredField("mRightEdge");
            if (leftEdgeField != null && rightEdgeField != null) {
                leftEdgeField.setAccessible(true);
                rightEdgeField.setAccessible(true);
                leftEdge = (EdgeEffect) leftEdgeField.get(viewPager);
                rightEdge = (EdgeEffect) rightEdgeField.get(viewPager);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (leftEdge != null && rightEdge != null) {
            leftEdge.finish();
            rightEdge.finish();
            leftEdge.setSize(0, 0);
            rightEdge.setSize(0, 0);
        }
    }
}
