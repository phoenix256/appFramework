package tr.wolflame.framework.base.objects;


import android.support.v4.app.Fragment;

/**
 * Created by SADIK on 09/02/16.
 */
public class FragmentPair {

    private Fragment fragment;

    private String title;

    public FragmentPair() {
    }

    public FragmentPair(Fragment fragment, String title) {
        this.fragment = fragment;
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
