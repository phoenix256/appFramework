package tr.wolflame.framework.base.activities;

import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import tr.wolflame.framework.R;

/**
 * Created by SADIK on 26/05/16.
 */
public abstract class BaseDetailActivity extends BaseAppCompatActivity {

    protected ImageView appBarIv;
    protected FloatingActionButton fab;

    @Override
    public int initLayoutId() {
        return R.layout.layout_base_detail;
    }

    @Override
    public void onCreated() {

        appBarIv = (ImageView) findViewById(initCollapsingIvId());
        fab = (FloatingActionButton) findViewById(initFabId());

        replaceFragment(R.id.container, initFragment());
    }

    public ImageView getAppBarIv() {
        return appBarIv;
    }

    public FloatingActionButton getFab() {
        return fab;
    }

    protected abstract Fragment initFragment();

    @Override
    protected boolean isHomeAsUpEnabled() {
        return true;
    }

    @Override
    protected int initAppBarLayoutId() {
        return R.id.appBarLayout;
    }

    @Override
    protected float initAppBarImageAspectRatio() {
        return 0.56f;
    }

    @IdRes
    protected int initCollapsingIvId() {
        return R.id.imageViewCollapsingToolbar;
    }

    @IdRes
    protected int initFabId() {
        return R.id.fab;
    }

}
