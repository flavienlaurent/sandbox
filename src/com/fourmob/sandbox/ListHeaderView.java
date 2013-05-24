package com.fourmob.sandbox;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fourmob.sandbox.R;


public class ListHeaderView extends LinearLayout implements AbsListView.OnScrollListener {

	private boolean mAlreadyInflated = false;

	private ImageView imageView1;

	public ListHeaderView(Context context) {
		super(context);
	}

	public ListHeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	private void afterSetContent() {
		imageView1 = ((ImageView)findViewById(R.id.imageView1));
	}

	/**
	 * The mAlreadyInflated_ hack is needed because of an Android bug which leads to infinite calls of onFinishInflate()
	 * when inflating a layout with a parent and using the <merge /> tag.
	 * 
	 */
	@Override
	public void onFinishInflate() {
		if( ! mAlreadyInflated) {
			mAlreadyInflated = true;
			inflate(getContext(), R.layout.view_list_header, this);
			afterSetContent();
		}
		super.onFinishInflate();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		View localView = view.getChildAt(0);
		if(localView == this) {
			int i = - localView.getTop() / 2;
			this.imageView1.setTranslationY(i);
		}

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {}

	/**
	 * @param context
	 * @return
	 */
	public static ListHeaderView build(Context context) {
		ListHeaderView instance = new ListHeaderView(context);
		instance.onFinishInflate();
		return instance;
	}

}
