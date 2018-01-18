package com.example.emotionkeyboard.adapter.recycleradaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

public class RecyclerViewHelper {

	private final Context context;
	public View itemView;
	public int position;

	protected RecyclerViewHelper(Context context, View itemView, int position) {
		this.context = context;
		this.itemView = itemView;
		this.position = position;
	}

	public static RecyclerViewHelper get(Context context,  View itemView,int position) {

 		return new RecyclerViewHelper(context, itemView,position);

	}

	public <T extends View> T getView(int viewId) {
		return retrieveView(viewId);
	}

	@SuppressWarnings("unchecked")
	protected <T extends View> T retrieveView(int viewId) {
		return (T) itemView.findViewById(viewId);
	}

	public RecyclerViewHelper setText(int viewId, String value) {
		TextView view = retrieveView(viewId);
		view.setText(value);
		return this;
	}

	public RecyclerViewHelper setImageResource(int viewId, int imageResId) {
		ImageView view = retrieveView(viewId);
		view.setImageResource(imageResId);
		return this;
	}

	public RecyclerViewHelper setBackgroundColor(int viewId, int color) {
		View view = retrieveView(viewId);
		view.setBackgroundColor(color);
		return this;
	}

	public RecyclerViewHelper setBackgroundRes(int viewId, int backgroundRes) {
		View view = retrieveView(viewId);
		view.setBackgroundResource(backgroundRes);
		return this;
	}

	public RecyclerViewHelper setTextColor(int viewId, int textColor) {
		TextView view = retrieveView(viewId);
		view.setTextColor(textColor);
		return this;
	}

	public RecyclerViewHelper setTextColorRes(int viewId, int textColorRes) {
		TextView view = retrieveView(viewId);
		view.setTextColor(context.getResources().getColor(textColorRes));
		return this;
	}

	public RecyclerViewHelper setImageDrawable(int viewId, Drawable drawable) {
		ImageView view = retrieveView(viewId);
		view.setImageDrawable(drawable);
		return this;
	}


	public RecyclerViewHelper setImageBitmap(int viewId, Bitmap bitmap) {
		ImageView view = retrieveView(viewId);
		view.setImageBitmap(bitmap);
		return this;
	}

	@SuppressLint("NewApi")
	public RecyclerViewHelper setAlpha(int viewId, float value) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			retrieveView(viewId).setAlpha(value);
		} else {
			AlphaAnimation alpha = new AlphaAnimation(value, value);
			alpha.setDuration(0);
			alpha.setFillAfter(true);
			retrieveView(viewId).startAnimation(alpha);
		}
		return this;
	}

	public RecyclerViewHelper setVisible(int viewId, boolean visible) {
		View view = retrieveView(viewId);
		view.setVisibility(visible ? View.VISIBLE : View.GONE);
		return this;
	}

	public RecyclerViewHelper linkify(int viewId) {
		TextView view = retrieveView(viewId);
		Linkify.addLinks(view, Linkify.ALL);
		return this;
	}

	public RecyclerViewHelper setTypeface(int viewId, Typeface typeface) {
		TextView view = retrieveView(viewId);
		view.setTypeface(typeface);
		view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
		return this;
	}

	public RecyclerViewHelper setTypeface(Typeface typeface, int... viewIds) {
		for (int viewId : viewIds) {
			TextView view = retrieveView(viewId);
			view.setTypeface(typeface);
			view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
		}
		return this;
	}

	public RecyclerViewHelper setProgress(int viewId, int progress) {
		ProgressBar view = retrieveView(viewId);
		view.setProgress(progress);
		return this;
	}

	public RecyclerViewHelper setProgress(int viewId, int progress, int max) {
		ProgressBar view = retrieveView(viewId);
		view.setMax(max);
		view.setProgress(progress);
		return this;
	}

	public RecyclerViewHelper setMax(int viewId, int max) {
		ProgressBar view = retrieveView(viewId);
		view.setMax(max);
		return this;
	}

	public RecyclerViewHelper setRating(int viewId, float rating) {
		RatingBar view = retrieveView(viewId);
		view.setRating(rating);
		return this;
	}
	public RecyclerViewHelper setRating(int viewId, float rating, int max) {
		RatingBar view = retrieveView(viewId);
		view.setMax(max);
		view.setRating(rating);
		return this;
	}

	public RecyclerViewHelper setTag(int viewId, Object tag) {
		View view = retrieveView(viewId);
		view.setTag(tag);
		return this;
	}

	public RecyclerViewHelper setTag(int viewId, int key, Object tag) {
		View view = retrieveView(viewId);
		view.setTag(key, tag);
		return this;
	}

	public RecyclerViewHelper setChecked(int viewId, boolean checked) {
		Checkable view = (Checkable) retrieveView(viewId);
		view.setChecked(checked);
		return this;
	}

	public RecyclerViewHelper setAdapter(int viewId, Adapter adapter) {
		AdapterView view = retrieveView(viewId);
		view.setAdapter(adapter);
		return this;
	}

	public RecyclerViewHelper setOnClickListener(int viewId, View.OnClickListener listener) {
		View view = retrieveView(viewId);
		view.setOnClickListener(listener);
		return this;
	}

	public RecyclerViewHelper setOnCheckedListener(int viewId, CompoundButton.OnCheckedChangeListener listener){
		CheckBox view = retrieveView(viewId);
		view.setOnCheckedChangeListener(listener);
		return this;
	}

	public RecyclerViewHelper setOnTouchListener(int viewId, View.OnTouchListener listener) {
		View view = retrieveView(viewId);
		view.setOnTouchListener(listener);
		return this;
	}

	public RecyclerViewHelper setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
		View view = retrieveView(viewId);
		view.setOnLongClickListener(listener);
		return this;
	}

	public RecyclerViewHelper setTVMaxEms(int viewId, int length) {
		TextView view = retrieveView(viewId);
		view.setMaxEms(length);
		return this;
	}

	public RecyclerViewHelper setMarginTop(int viewId, int margin) {
		View v = retrieveView(viewId);
		if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
			ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
			layoutParams.topMargin = margin;
			v.setLayoutParams(layoutParams);
		} else {
			Log.e("RecyclerHelper", "This LayoutParams is not instanceof ViewGroup.MarginLayoutParams");
		}
		return this;
	}

	public RecyclerViewHelper setPaddingLeft(int viewId, int paddingLeft) {
		View v = retrieveView(viewId);
		int paddingTop = v.getPaddingTop();
		int paddingRight = v.getPaddingRight();
		int paddingBottom = v.getPaddingBottom();
		v.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
		return this;
	}

	public RecyclerViewHelper setPaddingTop(int viewId, int paddingTop) {
		View v = retrieveView(viewId);
		int paddingLeft = v.getPaddingLeft();
		int paddingRight = v.getPaddingRight();
		int paddingBottom = v.getPaddingBottom();
		v.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
		return this;
	}

	public RecyclerViewHelper setPaddingRight(int viewId, int paddingRight) {
		View v = retrieveView(viewId);
		int paddingLeft = v.getPaddingLeft();
		int paddingTop = v.getPaddingTop();
		int paddingBottom = v.getPaddingBottom();
		v.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
		return this;
	}

	public RecyclerViewHelper setPaddingBottom(int viewId, int paddingBottom) {
		View v = retrieveView(viewId);
		int paddingLeft = v.getPaddingLeft();
		int paddingTop = v.getPaddingTop();
		int paddingRight = v.getPaddingRight();
		v.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
		return this;
	}

	public int getHeight(int viewId) {
		View v = retrieveView(viewId);
		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		v.measure(w, h);
		return v.getMeasuredHeight();
	}

	public View getView() {
		return itemView;
	}

	public int getPosition() {
		return position;
	}

	public RecyclerViewHelper setViewHeight(int viewId, int viewHeight) {
		View view = retrieveView(viewId);
		ViewGroup.LayoutParams lp = view.getLayoutParams();
		lp.height = viewHeight;
		view.setLayoutParams(lp);
		return this;
	}

	public RecyclerViewHelper setViewWidth(int viewId, int width){
		View view = retrieveView(viewId);
		ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
		layoutParams.width = width;
		view.setLayoutParams(layoutParams);
		return this;
	}
}
