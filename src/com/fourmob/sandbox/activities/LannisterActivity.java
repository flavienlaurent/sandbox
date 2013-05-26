package com.fourmob.sandbox.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.fourmob.sandbox.R;


public class LannisterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lannister);

		final Drawable actionBarDrawable = getResources().getDrawable(R.drawable.ab_solid_sbx);
		actionBarDrawable.setAlpha(0);
		getActionBar().setBackgroundDrawable(actionBarDrawable);

		final ListView listView1 = (ListView)findViewById(R.id.listView1);
		final SmoothListHeaderView smoothListHeaderView = SmoothListHeaderView.build(this);
		listView1.addHeaderView(smoothListHeaderView);
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

			float maxTop = -1;

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				smoothListHeaderView.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
				
				if(maxTop <= 0) {
					maxTop = smoothListHeaderView.getHeight();
				}

				final int headerTop = Math.abs(smoothListHeaderView.getTop());
				float ratio = headerTop / (maxTop - getActionBar().getHeight());
				int alpha = Math.min(255, (int)(255 * ratio));

				actionBarDrawable.setAlpha(alpha);
			}
		});
	}

    private static class SmoothListHeaderView extends LinearLayout implements AbsListView.OnScrollListener {

        private boolean mAlreadyInflated = false;

        private ImageView imageView1;

        public SmoothListHeaderView(Context context) {
            super(context);
        }

        private void afterSetContent() {
            imageView1 = ((ImageView)findViewById(R.id.imageView1));
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
                this.imageView1.setTranslationY(i);
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
}
