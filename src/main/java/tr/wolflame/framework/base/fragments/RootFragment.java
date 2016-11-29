package tr.wolflame.framework.base.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import tr.wolflame.framework.base.adapters.BaseItemAdapter;
import tr.wolflame.framework.base.objects.ExtraPair;
import tr.wolflame.framework.base.util.LogApp;
import tr.wolflame.framework.base.util.helper.StaticFields;

public class RootFragment extends Fragment {

    protected final String TAG = this.getClass().getSimpleName();

    protected RecyclerView recyclerView;
    protected BaseItemAdapter adapter;
    protected RecyclerView.LayoutManager layoutManager;
    protected Parcelable recyclerState;
    protected final String LIST_STATE_KEY = TAG + "RecyclerState";

    private ExtraPair extraPair;

    protected View rootView;

    public RootFragment() {
    }

    public static Fragment newInstance(ExtraPair extraPair) {
        final RootFragment rootFragment = new RootFragment();
        // Supply index input as an argument.
        final Bundle args = new Bundle();
        args.putSerializable(StaticFields.KEY_EXTRA_PAIR, extraPair);
        rootFragment.setArguments(args);

        return rootFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogApp.d(TAG, "onCreate");

        //setRetainInstance(true);

        initExtraPair();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    private void initExtraPair() {
        final Bundle args = getArguments();

        if (args != null)
            extraPair = (ExtraPair) args.getSerializable(StaticFields.KEY_EXTRA_PAIR);
    }

    protected ExtraPair getExtraPair() {
        return extraPair;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (layoutManager != null) {
            recyclerState = layoutManager.onSaveInstanceState();

            if (recyclerState != null)
                outState.putParcelable(LIST_STATE_KEY, recyclerState);
        }

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            // Retrieve list state and list/item positions
            recyclerState = savedInstanceState.getParcelable(LIST_STATE_KEY);
        }
    }

    protected void restoreState(SwipeRefreshLayout mSwipeLayout) {

        if (recyclerState != null) {
            if (mSwipeLayout != null)
                mSwipeLayout.setEnabled(false);

            layoutManager.onRestoreInstanceState(recyclerState);

            recyclerState = null;

        }
    }


    protected void replaceFragment(int resId, Fragment fragment) {
        replaceFragment(resId, fragment, false);
    }

    protected void replaceFragment(int resId, Fragment fragment, boolean addToBackstack) {

        try {
            final FragmentManager fragmentManager = getFragmentManager();

            if (fragmentManager != null) {
                if (addToBackstack) {
                    getFragmentManager().beginTransaction()
                            .replace(resId, fragment)
                            .addToBackStack(fragment.getClass().getSimpleName())
                            .commit();
                } else {
                    getFragmentManager().beginTransaction()
                            .replace(resId, fragment)
                            .commit();
                }
            } else {

                LogApp.e(TAG, "fragmentManager is null");
            }

        } catch (Exception e) {
            LogApp.e(TAG, String.valueOf(e.toString()));

        }


    }

    protected void restoreState() {
        if (recyclerState != null) {
            layoutManager.onRestoreInstanceState(recyclerState);

            recyclerState = null;
        }
    }

    public void clearState() {
        this.recyclerState = null;
    }

    public BaseItemAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}