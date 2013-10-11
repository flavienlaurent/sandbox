package com.fourmob.sandbox.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.fourmob.sandbox.R;
import com.nineoldandroids.view.ViewHelper;

public class StickyHeaderViewPre14Activity extends Activity {

	private View stickyView;
	private ViewGroup mainView;

	private ViewGroup stickyListHeaderView;
	private View imageView1;
	private View placeholderView;

	private ViewGroup.LayoutParams stickedToTopLayoutParams;
	private ViewGroup.LayoutParams listViewHeaderLayoutParams;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pre14_stickyheaderview);

		mainView = (ViewGroup) findViewById(R.id.mainView);

		stickyView = findViewById(R.id.stickyView);

		stickyListHeaderView = (ViewGroup) getLayoutInflater().inflate(R.layout.view_sticky_list_header, null);

		imageView1 = stickyListHeaderView.findViewById(R.id.imageView1);
		placeholderView = stickyListHeaderView.findViewById(R.id.placeholderView);

		listViewHeaderLayoutParams = placeholderView.getLayoutParams();
		stickedToTopLayoutParams = stickyView.getLayoutParams();

		Log.d("Sticky", "listViewHeaderLayoutParams=" + listViewHeaderLayoutParams);
		Log.d("Sticky", "stickedToTopLayoutParams=" + stickedToTopLayoutParams);

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

		listView1.post(new Runnable() {
			@Override
			public void run() {
				listView1.setOnScrollListener(new AbsListView.OnScrollListener() {

					@Override
					public void onScrollStateChanged(AbsListView view, int scrollState) {

					}

					@Override
					public void onScroll(AbsListView view, int firstVisibleItem,
										 int visibleItemCount, int totalItemCount) {

						View localView = view.getChildAt(0);
						if (localView == stickyListHeaderView) {
							int newY = -localView.getTop() / 2;
							ViewHelper.setTranslationY(imageView1, newY);
						}

						stickHeader(view);
					}
				});
			}
		});
	}

	boolean isStickedToTop = true;

	private void stickHeader(AbsListView view) {
		int scrollY = getScrollY(view);
		int translateY = placeholderView.getTop() - scrollY;

		Log.d("Sticky", "placeholderView.getTop()=" + placeholderView.getTop());
		Log.d("Sticky", "scrollY=" + scrollY);
		Log.d("Sticky", "translateY=" + translateY);

		if(!isStickedToTop && translateY <= 0) {
			//stickyview is sticked to top
			Log.d("Sticky", "stickyview is sticked to top");
			isStickedToTop = true;
			removeStickViewFromItsParent();
			mainView.addView(stickyView, stickedToTopLayoutParams);
		} else if(isStickedToTop && translateY > 0) {
			Log.d("Sticky", "stickview gets back to listview header");
			//stickview gets back to listview header
			isStickedToTop = false;
			removeStickViewFromItsParent();
			stickyListHeaderView.addView(stickyView, listViewHeaderLayoutParams);
		}
	}

	private void removeStickViewFromItsParent() {
		ViewGroup parent = (ViewGroup) stickyView.getParent();
		if(parent == null) {
			return;
		}
		parent.removeView(stickyView);
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
}
