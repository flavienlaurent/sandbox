package com.fourmob.sandbox.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.fourmob.sandbox.R;


public class BaratheonActivity extends Activity {
	
	private static final int SCROLL_TO_TOP = -1;
	private static final int SCROLL_TO_BOTTOM = 1;
	
	private static final int CHANGE_SCROLL_THRESHOLD = 5;
	
	private View poppyBottomView;
	private View imageView;
	
	private	int poppyBottomHeight = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baratheon);
		
		poppyBottomView = findViewById(R.id.poppyBottomView);
		imageView = findViewById(R.id.imageView);
		
		poppyBottomView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(BaratheonActivity.this, "Poppy click!", Toast.LENGTH_SHORT).show();
			}
		});

		final ListView listView1 = (ListView)findViewById(R.id.listView1);
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
            void onScrollDirectionChanged(int scrollDirection) {
            	translateYPoppyBottomView(scrollDirection);
			}

			private void translateYPoppyBottomView(int scrollDirection) {
				if(poppyBottomHeight <=0 ) {
            		poppyBottomHeight = poppyBottomView.getHeight();
            	}
				poppyBottomView.animate().translationY(scrollDirection == SCROLL_TO_TOP ? 0 : poppyBottomHeight);
			}


			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

            int mScrollDirection = 0;
            int mLastScrollY;

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int newScrollY = getScrollY();
                int newScrollDirection;
                
                if(newScrollY == mLastScrollY || Math.abs(newScrollY-mLastScrollY) < CHANGE_SCROLL_THRESHOLD) {
                	return;
                }
                if (newScrollY > mLastScrollY) {
                	newScrollDirection = SCROLL_TO_BOTTOM;
                } else  {
                	newScrollDirection = SCROLL_TO_TOP;
                }

                if(newScrollDirection != mScrollDirection) {
                    onScrollDirectionChanged(newScrollDirection);
                }
                
                mLastScrollY = newScrollY;
                mScrollDirection = newScrollDirection;
            }

			public int getScrollY() {
                View c = listView1.getChildAt(0);
                if(c == null) {
                	return 0;
                }
                return -c.getTop() + listView1.getFirstVisiblePosition() * c.getHeight();
            }
		});
	}
}
