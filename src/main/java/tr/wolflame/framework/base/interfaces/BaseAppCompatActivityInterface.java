package tr.wolflame.framework.base.interfaces;

import android.support.annotation.LayoutRes;

/**
 * Created by SADIK on 05/02/16.
 */
public interface BaseAppCompatActivityInterface {

    @LayoutRes
    int initLayoutId();

    void onCreated();
}
