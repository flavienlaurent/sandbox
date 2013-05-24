package com.fourmob.sandbox;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class PlayMusicActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playmusic);

		final Drawable actionBarDrawable = getResources().getDrawable(R.drawable.ab_solid_sbx);
		actionBarDrawable.setAlpha(0);
		getActionBar().setBackgroundDrawable(actionBarDrawable);

		final ListView listView1 = (ListView)findViewById(R.id.listView1);
		final ListHeaderView listHeaderView = ListHeaderView.build(this);
		listView1.addHeaderView(listHeaderView);
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

			final float maxTop = getResources().getDimensionPixelSize(R.dimen.imgheaderheight);

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				listHeaderView.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);

				final int headerTop = Math.abs(listHeaderView.getTop());
				float ratio = headerTop / (maxTop - getActionBar().getHeight());
				int alpha = Math.min(255, (int)(255 * ratio));

				actionBarDrawable.setAlpha(alpha);
			}
		});
	}

}
