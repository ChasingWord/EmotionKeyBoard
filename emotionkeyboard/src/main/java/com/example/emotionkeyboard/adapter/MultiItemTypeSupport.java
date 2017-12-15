package com.example.emotionkeyboard.adapter;

public interface MultiItemTypeSupport<T> {
	
	int getLayoutId(int position, T t);
	int getViewTypeCount();
	int getItemViewType(int postion, T t);
}