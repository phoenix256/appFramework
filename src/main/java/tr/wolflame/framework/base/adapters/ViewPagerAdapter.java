package tr.wolflame.framework.base.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;

import tr.wolflame.framework.base.objects.FragmentPair;
import tr.wolflame.framework.base.util.LogApp;

public class ViewPagerAdapter extends FragmentAdvanceStatePagerAdapter {
    private final static String TAG = "ViewPagerAdapter";

    //private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
    //private final ArrayList<String> mFragmentTitleList = new ArrayList<>();
    private ArrayList<FragmentPair> fragmentPairs = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }


    @Override
    public int getCount() {
        return fragmentPairs.size();
    }

    public void addFragment(FragmentPair fragmentPair) {
        fragmentPairs.add(fragmentPair);
    }

    public void swapItems(ArrayList<FragmentPair> fragmentPairs) {
        this.fragmentPairs = fragmentPairs;
        notifyDataSetChanged();
    }

    public void swapItem(FragmentPair fragmentPair, int position) {
        try {
            fragmentPairs.set(position, fragmentPair);
            notifyDataSetChanged();
        } catch (Exception e) {
            LogApp.e(TAG, String.valueOf(e.toString()));
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentPairs.get(position).getTitle();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getFragmentItem(int position) {
        return fragmentPairs.get(position).getFragment();
    }

    @Override
    public void onFragmentItemUpdate(int position, Fragment fragment) {
    }
}