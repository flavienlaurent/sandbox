package com.fourmob.sandbox.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.RotateDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;

import com.fourmob.sandbox.R;

/**
 * Created by f.laurent on 27/11/13.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class RotateDrawablePost14Activity extends Activity {

    static final int MAX_LEVEL = 10000;
    static final int MID_MAX_LEVEL = MAX_LEVEL / 2;
    private boolean mExpanded;
    private Interpolator mSmoothInterpolator;

    private int mContentHeight;
    private TextView mTextView;
    private View mContent;

    private RotateDrawable mRotateDrawable;

    private static final com.nineoldandroids.util.Property<View, Integer> VIEW_LAYOUT_HEIGHT =
            new  com.nineoldandroids.util.Property<View, Integer>(Integer.class, "viewLayoutHeight") {

                @Override
                public void set(View object, Integer value) {
                    object.getLayoutParams().height = value.intValue();
                    object.requestLayout();
                }

                @Override
                public Integer get(View object) {
                    return object.getLayoutParams().height;
                }
            };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotatedrawable);

        mSmoothInterpolator = new AccelerateDecelerateInterpolator();

        mContentHeight = getResources().getDimensionPixelSize(R.dimen.content_height);
        mTextView = (TextView) findViewById(R.id.textView);
        mContent = findViewById(R.id.content);

        mRotateDrawable = (RotateDrawable) getResources().getDrawable(R.drawable.rotate_drawable);
        mTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, mRotateDrawable, null);

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextView.setEnabled(false);
                com.nineoldandroids.animation.ObjectAnimator expandCollapseAnimator =   com.nineoldandroids.animation.ObjectAnimator.ofInt(mContent, VIEW_LAYOUT_HEIGHT, mContent.getHeight(), !mExpanded ? mContentHeight : 0);
                expandCollapseAnimator.setInterpolator(mSmoothInterpolator);
                expandCollapseAnimator.addUpdateListener(new com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(com.nineoldandroids.animation.ValueAnimator valueAnimator) {
                        int level = (int) (MID_MAX_LEVEL * valueAnimator.getAnimatedFraction()) + (mExpanded ? MID_MAX_LEVEL : 0);
                        mRotateDrawable.setLevel(level);
                    }
                });

                expandCollapseAnimator.addListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {

                    @Override
                    public void onAnimationStart(com.nineoldandroids.animation.Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(com.nineoldandroids.animation.Animator animator) {
                        mExpanded = !mExpanded;
                        mTextView.setEnabled(true);
                    }

                    @Override
                    public void onAnimationCancel(com.nineoldandroids.animation.Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(com.nineoldandroids.animation.Animator animator) {

                    }
                });
                expandCollapseAnimator.start();
            }
        });
    }
}