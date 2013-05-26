package com.fourmob.sandbox.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fourmob.sandbox.R;

public class GreyjoyActivity extends Activity {

	private View stickyView;
	private StickyListHeaderView stickyListHeaderView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_greyjoy);

		stickyView = findViewById(R.id.stickyView);
		stickyListHeaderView = buildStickyListHeaderView(this);

		final ListView listView1 = (ListView) findViewById(R.id.listView1);
		listView1.addHeaderView(stickyListHeaderView);
		listView1.setAdapter(new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView = getLayoutInflater().inflate(
							android.R.layout.simple_list_item_1, null);
				}
				((TextView) convertView.findViewById(android.R.id.text1))
						.setText(getItem(position));
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
				return 45;
			}
		});

		listView1.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				stickyListHeaderView.onScroll(view, firstVisibleItem,
						visibleItemCount, totalItemCount);

			}

		});

		listView1.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						stickyListHeaderView.stickHeader(listView1);
					}
				});
	}

	private class StickyListHeaderView extends LinearLayout implements
			AbsListView.OnScrollListener {

		private boolean mAlreadyInflated = false;

		private View imageView1;
		private View placeholderView;

		public StickyListHeaderView(Context context) {
			super(context);
		}

		private void afterSetContent() {
			imageView1 = findViewById(R.id.imageView1);
			placeholderView = findViewById(R.id.placeholderView);
		}

		@Override
		public void onFinishInflate() {
			if (!mAlreadyInflated) {
				mAlreadyInflated = true;
				inflate(getContext(), R.layout.view_sticky_list_header, this);
				afterSetContent();
			}
			super.onFinishInflate();
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			View localView = view.getChildAt(0);
			if (localView == this) {
				int i = -localView.getTop() / 2;
				this.imageView1.setTranslationY(i);
			}

			stickHeader(view);
		}

		private void stickHeader(AbsListView view) {
			int scrollY = getScrollY(view);
			stickyView.setTranslationY(Math.max(0, placeholderView.getTop()
					- scrollY));
		}

		public int getScrollY(AbsListView view) {
			View c = view.getChildAt(0);
			if (c == null) {
				return 0;
			}

			int firstVisiblePosition = view.getFirstVisiblePosition();
			int top = c.getTop();

			int headerHeight = 0;
			if (firstVisiblePosition >= 1) {
				headerHeight = stickyListHeaderView.getHeight();
			}

			return -top + firstVisiblePosition * c.getHeight() + headerHeight;
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}

	}

	public StickyListHeaderView buildStickyListHeaderView(Context context) {
		StickyListHeaderView instance = new StickyListHeaderView(context);
		instance.onFinishInflate();
		return instance;
	}
}
