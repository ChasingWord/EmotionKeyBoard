package com.example.emotionkeyboard.adapter.recycleradaper;

import android.content.Context;
import android.view.View;

public abstract class RecyclerQuickAdapter<T> extends BaseRecyclerAdapter<T,RecyclerViewHelper> {

    public RecyclerQuickAdapter(Context context, /*BaseRecylerViewHolder recylerViewHolderHelper,*/int layoutId) {
        super(context, /*recylerViewHolderHelper*/layoutId, null);
    }

    public RecyclerQuickAdapter(Context context, BaseRecyclerMultitemTypeSupport baseRecyclerMultitemTypeSupport) {
        super(context,null, baseRecyclerMultitemTypeSupport);
    }


    @Override
    protected RecyclerViewHelper getAdapterHelper(int position, View itemView) {
        return RecyclerViewHelper.get(context, itemView, position);
    }
}
