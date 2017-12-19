package com.example.emotionkeyboard.decoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.State;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

public class DividerGridItemDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Drawable mDivider;
    private boolean hasFooter;
    private Paint mPaint;
    private int mColorResId;
    private int preHorizontalBottom;

    public DividerGridItemDecoration(Context context) {
        this(context, false);
    }

    /**
     * @param context
     * @param hasFooter 是否有脚部
     */
    public DividerGridItemDecoration(Context context, boolean hasFooter) {
        mContext = context;
        this.hasFooter = hasFooter;
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        mPaint = new Paint();
    }

    public DividerGridItemDecoration colorResId(int resId){
        mColorResId = resId;
        return this;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, State state) {
        drawHorizontal(c, parent);
        drawVertical(c, parent);
    }

    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {

            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager)
                    .getSpanCount();
        }
        return spanCount;
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            // 有脚部时，最后一条不画
            if (hasFooter &&
                    parent.getChildLayoutPosition(child) == parent.getLayoutManager().getItemCount() - 1) {
                continue;
            }

            if (mColorResId > 0){
                int lineWidth = 1;
                mPaint.setColor(ContextCompat.getColor(mContext, mColorResId));
                mPaint.setStrokeWidth(lineWidth);
                if (preHorizontalBottom - child.getBottom() != 0 && Math.abs(preHorizontalBottom - child.getBottom()) < 10){
                    // 一行的最后一个的bottom莫名小了3px
                    c.drawLine(child.getLeft() - lineWidth, preHorizontalBottom, child.getRight() + lineWidth, preHorizontalBottom, mPaint);
                } else {
                    preHorizontalBottom = child.getBottom();
                    c.drawLine(child.getLeft() - lineWidth, child.getBottom(), child.getRight() + lineWidth, child.getBottom(), mPaint);
                }
            } else {
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams(); int left = child.getLeft() - params.leftMargin;
                int right = child.getRight() + params.rightMargin
                        + mDivider.getIntrinsicWidth();
                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicWidth();

            if (mColorResId > 0){
                int lineWidth = 1;
                mPaint.setColor(ContextCompat.getColor(mContext, mColorResId));
                mPaint.setStrokeWidth(lineWidth);
                c.drawLine(child.getRight(), child.getTop() - lineWidth, child.getRight(), child.getBottom() + lineWidth, mPaint);
            } else {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

    private boolean isLastColum(RecyclerView parent, int pos, int spanCount,
                                int childCount) {
        LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
            {
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager manager = ((StaggeredGridLayoutManager) layoutManager);
            if (manager.getOrientation() == StaggeredGridLayoutManager.VERTICAL) {
                View view = parent.getChildAt(pos);
                if (view != null && ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams())
                        .getSpanIndex() == ((StaggeredGridLayoutManager) layoutManager).getSpanCount()) {
                    // 如果是最后一列，则不需要绘制右边
                    return true;
                }
            } else {
                childCount = childCount - childCount % spanCount;
                if (pos >= childCount)// 如果是最后一列，则不需要绘制右边
                    return true;
            }
        }
        return false;
    }

    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount,
                              int childCount) {
        LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            childCount = childCount - childCount % spanCount;
            if (pos >= childCount)// 如果是最后一行，则不需要绘制底部
                return true;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                childCount = childCount - childCount % spanCount + 1;
                // 如果是最后一行，则不需要绘制底部
                if (pos >= childCount)
                    return true;
            } else
            // StaggeredGridLayoutManager 且横向滚动
            {
                // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition,
                               RecyclerView parent) {
        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();
        if (isLastRaw(parent, itemPosition, spanCount, childCount))// 如果是最后一行，则不需要绘制底部
        {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        } else if (isLastColum(parent, itemPosition, spanCount, childCount))// 如果是最后一列，则不需要绘制右边
        {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(),
                    mDivider.getIntrinsicHeight());
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
        getItemOffsets(outRect, ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition(),
                parent);
    }
}