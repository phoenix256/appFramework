package tr.wolflame.framework.base.util.helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import tr.wolflame.framework.R;

public class MultiStateFrameLayout extends FrameLayout {

    private static final int UNKNOWN_VIEW = -1;
    private static final int CONTENT_VIEW = 0;
    private static final int ERROR_VIEW = 1;
    private static final int LOADING_VIEW = 2;

    private LayoutInflater mInflater;
    private View mContentView;
    private View mLoadingView;
    private View mErrorView;
    private ViewState mViewState = ViewState.CONTENT;

    private TextView mErrorTextView;
    private ImageView mErrorImageView;
    private RetryListener mRetryListener;

    private int mLoadingViewResId;
    private int mErrorViewResId;

    private int mProgressBarColorResId;
    private int mErrorIconResId;
    private String mErrorText;
    private int mErrorTintColorResId;

    public MultiStateFrameLayout(Context context) {
        this(context, null);
    }

    public MultiStateFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiStateFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mInflater = LayoutInflater.from(getContext());
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MultiStateFrameLayout);

        mLoadingViewResId = a.getResourceId(R.styleable.MultiStateFrameLayout_loadingView, R.layout.layout_msfl_loading);
        mErrorViewResId = a.getResourceId(R.styleable.MultiStateFrameLayout_errorView, R.layout.layout_msfl_error);

        int viewState = a.getInt(R.styleable.MultiStateFrameLayout_viewState, UNKNOWN_VIEW);
        if (viewState != UNKNOWN_VIEW) {
            switch (viewState) {
                case CONTENT_VIEW:
                    mViewState = ViewState.CONTENT;
                    break;

                case ERROR_VIEW:
                    mViewState = ViewState.ERROR;
                    break;

                case LOADING_VIEW:
                    mViewState = ViewState.LOADING;
                    break;
            }
        }
        a.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mContentView == null) throw new IllegalArgumentException("Content view is not defined");
        setView();
    }

    /* All of the addView methods have been overridden so that it can obtain the content view via XML
     It is NOT recommended to add views into MultiStateView via the addView methods, but rather use
     any of the setViewForState methods to set views for their given ViewState accordingly */
    @Override
    public void addView(@NonNull View child) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child);
    }

    @Override
    public void addView(@NonNull  View child, int index) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child, index);
    }

    @Override
    public void addView(@NonNull  View child, int index, ViewGroup.LayoutParams params) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child, index, params);
    }

    @Override
    public void addView(@NonNull  View child, ViewGroup.LayoutParams params) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child, params);
    }

    @Override
    public void addView(@NonNull View child, int width, int height) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child, width, height);
    }

    @Override
    protected boolean addViewInLayout(@NonNull View child, int index, ViewGroup.LayoutParams params) {
        if (isValidContentView(child)) mContentView = child;
        return super.addViewInLayout(child, index, params);
    }

    @Override
    protected boolean addViewInLayout(@NonNull View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        if (isValidContentView(child)) mContentView = child;
        return super.addViewInLayout(child, index, params, preventRequestLayout);
    }

    /**
     * Returns the {@link View} associated with the {@link MultiStateFrameLayout.ViewState}
     *
     * @param state The {@link MultiStateFrameLayout.ViewState} with to return the view for
     * @return The {@link View} associated with the {@link MultiStateFrameLayout.ViewState}, null if no view is present
     */
    @Nullable
    public View getView(ViewState state) {
        switch (state) {
            case LOADING:
                return mLoadingView;

            case CONTENT:
                return mContentView;

            case ERROR:
                return mErrorView;

            default:
                return null;
        }
    }

    /**
     * Returns the current {@link MultiStateFrameLayout.ViewState}
     *
     * @return the current {@link MultiStateFrameLayout.ViewState}
     */
    public ViewState getViewState() {
        return mViewState;
    }

    /**
     * Sets the current {@link MultiStateFrameLayout.ViewState}
     *
     * @param state The {@link MultiStateFrameLayout.ViewState} to set {@link MultiStateFrameLayout} to
     */
    public void setViewState(ViewState state) {
        if (state != mViewState) {
            mViewState = state;
            setView();
        }
    }

    /**
     * Shows the {@link View} based on the {@link MultiStateFrameLayout.ViewState}
     */
    private void setView() {
        switch (mViewState) {
            case LOADING:
                if (mLoadingView == null) {
                    mLoadingView = inflateView(mLoadingViewResId);

                    ProgressBar mLoadingProgressBar = (ProgressBar) mLoadingView.findViewById(R.id.msfl_progress_bar);

                    if (mProgressBarColorResId != StaticFields.INVALID) {
                        int progressBarColor = getResources().getColor(mProgressBarColorResId);
                        mLoadingProgressBar.getIndeterminateDrawable().setColorFilter(progressBarColor, PorterDuff.Mode.SRC_IN);
                    }

                    addView(mLoadingView, mLoadingView.getLayoutParams());
                }

                if (mContentView != null) mContentView.setVisibility(View.GONE);
                if (mErrorView != null) mErrorView.setVisibility(View.GONE);
                mLoadingView.setVisibility(View.VISIBLE);
                break;

            case ERROR:
                if (mErrorView == null) {
                    mErrorView = inflateView(mErrorViewResId);

                    mErrorView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mRetryListener != null) {
                                mRetryListener.onRetry();
                            }
                        }
                    });

                    mErrorTextView = (TextView) mErrorView.findViewById(R.id.msfl_error_message);
                    mErrorImageView = (ImageView) mErrorView.findViewById(R.id.msfl_error_icon);

                    if (!TextUtils.isEmpty(mErrorText)) {
                        mErrorTextView.setText(mErrorText);
                    }

                    if (mErrorIconResId != StaticFields.INVALID) {
                        mErrorImageView.setImageResource(mErrorIconResId);
                    }

                    if (mErrorTintColorResId != StaticFields.INVALID) {
                        int errorTintColor = getResources().getColor(mErrorTintColorResId);
                        mErrorTextView.setTextColor(errorTintColor);
                        mErrorImageView.setColorFilter(errorTintColor, PorterDuff.Mode.SRC_IN);
                    }

                    addView(mErrorView, mErrorView.getLayoutParams());
                }

                if (mLoadingView != null) mLoadingView.setVisibility(View.GONE);
                if (mContentView != null) mContentView.setVisibility(View.GONE);
                mErrorView.setVisibility(View.VISIBLE);
                break;

            case CONTENT:
            default:
                if (mContentView == null) {
                    // Should never happen, the view should throw an exception if no content view is present upon creation
                    throw new NullPointerException("Content View");
                }

                if (mLoadingView != null) mLoadingView.setVisibility(View.GONE);
                if (mErrorView != null) mErrorView.setVisibility(View.GONE);
                mContentView.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setRetryListener(RetryListener retryListener) {
        mRetryListener = retryListener;
    }

    public void setProgressBarColorResId(@ColorInt int progressBarColorResId) {
        mProgressBarColorResId = progressBarColorResId;
    }

    public void setErrorTintColorResId(@ColorRes int errorTintColorResId) {
        mErrorTintColorResId = errorTintColorResId;
    }

    public void setErrorText(String error) {
        mErrorText = error;
        if (mErrorTextView != null) {
            mErrorTextView.setText(mErrorText);
        }
    }

    public void setErrorText(@StringRes int resId) {
        mErrorText = getContext().getString(resId);
        if (mErrorTextView != null) {
            mErrorTextView.setText(mErrorText);
        }
    }

    public void setErrorIcon(@DrawableRes int resId) {
        mErrorIconResId = resId;
    }

    public void setErrorIconVisibility(int visibility) {
        if (mErrorImageView != null) {
            mErrorImageView.setVisibility(visibility);
        }
    }

    private View inflateView(@LayoutRes int layoutRes) {
        if (mInflater == null) mInflater = LayoutInflater.from(getContext());
        return mInflater.inflate(layoutRes, this, false);
    }

    /**
     * Checks if the given {@link View} is valid for the Content View
     *
     * @param view The {@link View} to check
     * @return true if view is valid
     */
    private boolean isValidContentView(View view) {
        if (mContentView != null && mContentView != view) {
            return false;
        }

        return view != mLoadingView && view != mErrorView;
    }

    /**
     * Sets the view for the given view state
     *
     * @param view          The {@link View} to use
     * @param state         The {@link MultiStateFrameLayout.ViewState}to set
     * @param switchToState If the {@link MultiStateFrameLayout.ViewState} should be switched to
     */
    public void setViewForState(View view, ViewState state, boolean switchToState) {
        switch (state) {
            case LOADING:
                if (mLoadingView != null) removeView(mLoadingView);
                mLoadingView = view;
                addView(mLoadingView);
                break;

            case ERROR:
                if (mErrorView != null) removeView(mErrorView);
                mErrorView = view;
                addView(mErrorView);
                break;

            case CONTENT:
                if (mContentView != null) removeView(mContentView);
                mContentView = view;
                addView(mContentView);
                break;
        }

        if (switchToState) setViewState(state);
    }

    /**
     * Sets the {@link View} for the given {@link MultiStateFrameLayout.ViewState}
     *
     * @param view  The {@link View} to use
     * @param state The {@link MultiStateFrameLayout.ViewState} to set
     */
    public void setViewForState(View view, ViewState state) {
        setViewForState(view, state, false);
    }

    /**
     * Sets the {@link View} for the given {@link MultiStateFrameLayout.ViewState}
     *
     * @param layoutRes     Layout resource id
     * @param state         The {@link MultiStateFrameLayout.ViewState} to set
     * @param switchToState If the {@link MultiStateFrameLayout.ViewState} should be switched to
     */
    public void setViewForState(@LayoutRes int layoutRes, ViewState state, boolean switchToState) {
        if (mInflater == null) mInflater = LayoutInflater.from(getContext());
        View view = mInflater.inflate(layoutRes, this, false);
        setViewForState(view, state, switchToState);
    }

    /**
     * Sets the {@link View} for the given {@link MultiStateFrameLayout.ViewState}
     *
     * @param layoutRes Layout resource id
     * @param state     The {@link View} state to set
     */
    public void setViewForState(@LayoutRes int layoutRes, ViewState state) {
        setViewForState(layoutRes, state, false);
    }

    public enum ViewState {
        CONTENT,
        LOADING,
        ERROR
    }

    public interface RetryListener {
        void onRetry();
    }
}
