package com.fourmob.sandbox.activities;

import android.app.Activity;
import android.graphics.drawable.RotateDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;

import com.fourmob.sandbox.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * Created by f.laurent on 27/11/13.
 */
public class RotateDrawableActivity extends Activity {

    static final int MAX_LEVEL = 10000;
    static final int MID_MAX_LEVEL = MAX_LEVEL / 2;
    private boolean mExpanded;
    private Interpolator mSmoothInterpolator;

    private int mContentHeight;
    private TextView mTextView;
    private View mContent;

    private RotateDrawable mRotateDrawable;

    private ExpandCollapser mExpandCollapser;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotatedrawable);

        mSmoothInterpolator = new AccelerateDecelerateInterpolator();

        mContentHeight = getResources().getDimensionPixelSize(R.dimen.content_height);
        mTextView = (TextView) findViewById(R.id.textView);
        mContent = findViewById(R.id.content);
        mExpandCollapser = new ExpandCollapser(mContent);

        mRotateDrawable = (RotateDrawable) getResources().getDrawable(R.drawable.rotate_drawable);
        mTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, mRotateDrawable, null);

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextView.setEnabled(false);
                ObjectAnimator expandCollapseAnimator = ObjectAnimator.ofInt(mExpandCollapser, "height", mContent.getHeight(), !mExpanded ? mContentHeight : 0);
                expandCollapseAnimator.setInterpolator(mSmoothInterpolator);
                expandCollapseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    public void onAnimationUpdate(ValueAnimator animation) {
                        int level = (int) (MID_MAX_LEVEL * animation.getAnimatedFraction()) + (mExpanded ? MID_MAX_LEVEL : 0);
                        mRotateDrawable.setLevel(level);
                    }
                });
                expandCollapseAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mExpanded = !mExpanded;
                        mTextView.setEnabled(true);
                    }
                });
                expandCollapseAnimator.start();
            }
        });
    }

    private class ExpandCollapser {

        private View mView;

        ExpandCollapser(View view) {
            mView = view;
        }

        public void setHeight(int height) {
            mView.getLayoutParams().height = height;
            mView.requestLayout();
        }
    }
}