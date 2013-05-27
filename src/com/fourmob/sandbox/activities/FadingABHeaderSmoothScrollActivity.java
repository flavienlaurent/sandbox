package com.fourmob.sandbox.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fourmob.sandbox.R;


public class FadingABHeaderSmoothScrollActivity extends Activity {

	private float maxTop = - 1;

	private Drawable mActionBarDrawable;

	private SmoothListHeaderView mSmoothListHeaderView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_fadingab_headersmoothscroll);

		mActionBarDrawable = getResources().getDrawable(R.drawable.ab_solid_sbx);

		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
			mActionBarDrawable.setCallback(mDrawableCallback);
		}

		getActionBar().setBackgroundDrawable(mActionBarDrawable);

		final ListView listView1 = (ListView)findViewById(R.id.listView1);
		mSmoothListHeaderView = SmoothListHeaderView.build(this);
		listView1.addHeaderView(mSmoothListHeaderView);
		listView1.setAdapter(new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if(convertView == null) {
					convertView = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
				}
				((TextView)convertView.findViewById(android.R.id.text1)).setText(getItem(position));
				return convertView;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public String getItem(int position) {
				return "text" + position;
			}

			@Override
			public int getCount() {
				return 15;
			}
		});

		listView1.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				mSmoothListHeaderView.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
				updateActionBarAlpha();
			}
		});

	}

	private void updateActionBarAlpha() {
		if(maxTop <= 0) {
			maxTop = mSmoothListHeaderView.getHeight();
		}

		final int headerTop = Math.abs(mSmoothListHeaderView.getTop());
		float ratio = headerTop / (maxTop - getActionBar().getHeight());
		int actionBarDrawableAlpha = Math.min(255, (int)(255 * ratio));

		mActionBarDrawable.setAlpha(actionBarDrawableAlpha);
	}

	private static class SmoothListHeaderView extends LinearLayout implements AbsListView.OnScrollListener {

		private boolean mAlreadyInflated = false;

		private ImageView mImageView1;

		public SmoothListHeaderView(Context context) {
			super(context);
		}

		private void afterSetContent() {
			mImageView1 = ((ImageView)findViewById(R.id.imageView1));
		}

		@Override
		public void onFinishInflate() {
			if( ! mAlreadyInflated) {
				mAlreadyInflated = true;
				inflate(getContext(), R.layout.view_smooth_list_header, this);
				afterSetContent();
			}
			super.onFinishInflate();
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			View localView = view.getChildAt(0);
			if(localView == this) {
				int i = - localView.getTop() / 2;
				this.mImageView1.setTranslationY(i);
			}

		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {}

		public static SmoothListHeaderView build(Context context) {
			SmoothListHeaderView instance = new SmoothListHeaderView(context);
			instance.onFinishInflate();
			return instance;
		}

	}

	private Drawable.Callback mDrawableCallback = new Drawable.Callback() {

		@Override
		public void invalidateDrawable(Drawable who) {
			getActionBar().setBackgroundDrawable(who);
		}

		@Override
		public void scheduleDrawable(Drawable who, Runnable what, long when) {}

		@Override
		public void unscheduleDrawable(Drawable who, Runnable what) {}
	};
}
