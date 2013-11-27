package com.fourmob.sandbox.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.RotateDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Property;
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

    private static final Property<View, Integer> VIEW_LAYOUT_HEIGHT =
            new Property<View, Integer>(Integer.class, "viewLayoutHeight") {

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
                ObjectAnimator expandCollapseAnimator = ObjectAnimator.ofInt(mContent, VIEW_LAYOUT_HEIGHT, mContent.getHeight(), !mExpanded ? mContentHeight : 0);
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
}