package tr.wolflame.framework.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import tr.wolflame.framework.R;


public class SlipperLinearLayout extends LinearLayout {
    private static final int INVALID_ID = -1;
    private static final int INVALID_RES_ID = 0;
    private static final String TAG = "SlipperLinearLayout";
    private Animation inAnimation;
    private Animation outAnimation;

    private int visibilityState = INVALID_ID;

    public SlipperLinearLayout(Context context) {
        super(context);

        init(context, null, INVALID_RES_ID);
    }

    public SlipperLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        init(context, attributeSet, INVALID_RES_ID);
    }

    public SlipperLinearLayout(Context context, AttributeSet attributeSet, int defaultValue) {
        super(context, attributeSet, defaultValue);

        init(context, attributeSet, defaultValue);
    }


    void init(Context context, AttributeSet attributeSet, int defaultValue) {

        if (attributeSet != null) {
            TypedArray attr = context.obtainStyledAttributes(attributeSet, R.styleable.SlipperLinearLayout, INVALID_RES_ID, INVALID_RES_ID);
            int inAnimationResId = attr.getResourceId(R.styleable.SlipperLinearLayout_in_animation, INVALID_RES_ID);
            int outAnimationResId = attr.getResourceId(R.styleable.SlipperLinearLayout_out_animation, INVALID_RES_ID);

            if (inAnimationResId != INVALID_RES_ID)
                setInAnimation(AnimationUtils.loadAnimation(context, inAnimationResId));

            if (outAnimationResId != INVALID_RES_ID)
                setOutAnimation(AnimationUtils.loadAnimation(context, outAnimationResId));

            attr.recycle();
        }
    }


    public void setInAnimation(Animation inAnimation) {
        this.inAnimation = inAnimation;
    }

    public void setOutAnimation(Animation outAnimation) {
        this.outAnimation = outAnimation;
    }

    @Override
    public void setVisibility(final int visibility) {
        if (visibilityState == INVALID_ID)
            visibilityState = super.getVisibility();

        if (visibilityState != visibility) {
            visibilityState = visibility;
            if (visibility == VISIBLE) {
                if (inAnimation != null) {
                    startAnimation(inAnimation);
                }
                super.setVisibility(visibility);
            } else if ((visibility == INVISIBLE) || (visibility == GONE)) {
                if (outAnimation != null) {
                    initAnimationListener(outAnimation, visibility);
                    startAnimation(outAnimation);
                }
            }
        }
    }

    @Override
    public int getVisibility() {
        if (visibilityState == INVALID_ID)
            visibilityState = super.getVisibility();

        return visibilityState;
    }

    private void initAnimationListener(Animation animation, final int visibility) {
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d(TAG, "onAnimationStart() called with: animation = [" + animation + "]");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG, "onAnimationEnd() called with: animation = [" + animation + "]");
                SlipperLinearLayout.super.setVisibility(visibility);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.d(TAG, "onAnimationRepeat() called with: animation = [" + animation + "]");
            }
        });
    }
}
