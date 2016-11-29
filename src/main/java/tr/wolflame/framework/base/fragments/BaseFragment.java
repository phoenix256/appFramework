package tr.wolflame.framework.base.fragments;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import tr.wolflame.framework.R;
import tr.wolflame.framework.base.activities.BaseAppCompatActivity;
import tr.wolflame.framework.base.adapters.BaseItemAdapter;
import tr.wolflame.framework.base.adapters.ViewPagerAdapter;
import tr.wolflame.framework.base.interfaces.BaseFragmentInterface;
import tr.wolflame.framework.base.network.RestBuilderGenerator;
import tr.wolflame.framework.base.objects.FragmentPair;
import tr.wolflame.framework.base.util.LogApp;
import tr.wolflame.framework.base.util.helper.MultiStateFrameLayout;
import tr.wolflame.framework.base.util.helper.StaticFields;

/**
 * Created by SADIK on 10/02/16.
 */
public abstract class BaseFragment extends RootFragment implements BaseFragmentInterface, SwipeRefreshLayout.OnRefreshListener, MultiStateFrameLayout.RetryListener {

    protected TabLayout tabLayout;
    protected ViewPager viewPager;

    protected MultiStateFrameLayout multiStateFrameLayout;

    protected SwipeRefreshLayout swipeRefreshLayout;

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return View of the fragment You can make your changes inside of this method like {@link #onCreateView(LayoutInflater inflater, ViewGroup container,
     * Bundle savedInstanceState)}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogApp.d(TAG, "onCreateView");

        // Inflate the layout for this fragment
        rootView = inflater.inflate(initLayoutId(), container, false);

        if (rootView != null) {
            initMultiState();

            initSwipeRefreshLayout();

            initViewPager();

            initList();
        } else {
            LogApp.e(TAG, "rootView is Null");
        }


        return onInternalCreateView(rootView, inflater, container, savedInstanceState);
    }

    protected BaseAppCompatActivity getBaseActivity() {
        try {
            return ((BaseAppCompatActivity) getActivity());
        } catch (ClassCastException e) {
            return null;
        }
    }

    private void initViewPager() {
        viewPager = (ViewPager) rootView.findViewById(initViewPagerId());

        if (viewPager != null) {
            setupViewPager();
            initTabLayout();
        }
    }

    private void setupViewPager() {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());

        for (FragmentPair fragmentPair : initFragmentPairList()) {
            adapter.addFragment(fragmentPair);
        }

        if (viewPager != null)
            viewPager.setAdapter(adapter);
    }

    @Nullable
    public ViewPagerAdapter getViewPagerAdapter() {
        if (viewPager != null)
            return ((ViewPagerAdapter) viewPager.getAdapter());
        return null;
    }

    private void initTabLayout() {

        if (rootView != null)
            tabLayout = (TabLayout) rootView.findViewById(initTabLayoutId());

        if (tabLayout != null && viewPager != null)
            tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * @return viewpagerId initialization if it is wanted.
     * {@link #initTabLayoutId()} must be also overridden.
     */
    @IdRes
    protected int initViewPagerId() {
        return R.id.viewpager;
    }

    /**
     * @return tabLayoutId initialization if it is wanted.
     * {@link #initViewPagerId()} must be also overridden.
     */
    @IdRes
    protected int initTabLayoutId() {
        return R.id.tabs;
    }

    protected ArrayList<FragmentPair> initFragmentPairList() {
        return new ArrayList<>();
    }

    private void initList() {

        recyclerView = (RecyclerView) rootView.findViewById(initRecyclerViewId());

        if (recyclerView != null) {

            recyclerView.setHasFixedSize(isRecyclerViewHasFixedSize());

            layoutManager = initLayoutManager();

            if (layoutManager != null) {
                recyclerView.setLayoutManager(layoutManager);

                //recyclerView.addItemDecoration(new VerticalSpaceItemDecoration((int) recyclerView.getContext().getResources().getDimension(R.dimen.tab_indicator_height)));
                //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.divider));

                adapter = initRecyclerViewAdapter();

                if (adapter != null) {
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(adapter);
                } else
                    recyclerView.setVisibility(View.GONE);
            }
        }
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(initSwipeRefreshLayoutId());

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(this);
            swipeRefreshLayout.setNestedScrollingEnabled(true);
        }
    }

    @IdRes
    protected int initSwipeRefreshLayoutId() {
        return StaticFields.INVALID;
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onRetry() {

    }

    protected int initRecyclerViewId() {
        return R.id.recyclerView_list;
    }

    protected boolean isRecyclerViewHasFixedSize() {
        return true;
    }

    protected RecyclerView.LayoutManager initLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    protected BaseItemAdapter initRecyclerViewAdapter() {
        return null;
    }


    public void onNetworkError(String error) {
        LogApp.e(TAG, String.valueOf(error));

        if (!TextUtils.isEmpty(error) && error.contains(RestBuilderGenerator.RETROFIT_TOO_MUCH_REQUEST_CODE))
            return;

        if (multiStateFrameLayout != null && isAdded()) {
            multiStateFrameLayout.setViewState(MultiStateFrameLayout.ViewState.ERROR);
            multiStateFrameLayout.setErrorText(error);
        }
    }

    public void onSuccessfulResponse() {
        if (multiStateFrameLayout != null && isAdded()) {
            multiStateFrameLayout.setViewState(MultiStateFrameLayout.ViewState.CONTENT);
        }
    }

    @IdRes
    protected int initMultiStateFrameLayoutId() {
        return StaticFields.INVALID;
    }

    private void initMultiState() {
        if (rootView != null)
            multiStateFrameLayout = (MultiStateFrameLayout) rootView.findViewById(initMultiStateFrameLayoutId());

        if (multiStateFrameLayout != null) {
            multiStateFrameLayout.setErrorTintColorResId(this.getErrorTintColorResId());
            multiStateFrameLayout.setViewState(MultiStateFrameLayout.ViewState.CONTENT);
            multiStateFrameLayout.setRetryListener(this);
        }
    }

    public MultiStateFrameLayout getMultiStateFrameLayout() {
        return multiStateFrameLayout;
    }

    @ColorRes
    protected int getErrorTintColorResId() {
        return R.color.colorAccent;
    }

    protected void setBackgroundColor(@ColorInt int colorRes) {
        final View view = getRootView();
        if (view != null) {
            view.setBackgroundColor(colorRes);
        }
    }

    protected void setBackgroundDrawable(@DrawableRes int drawableRes) {
        final View view = getRootView();
        if (view != null) {
            view.setBackgroundResource(drawableRes);
        }
    }

    @Nullable
    private View getRootView() {
        return rootView;
    }
}
