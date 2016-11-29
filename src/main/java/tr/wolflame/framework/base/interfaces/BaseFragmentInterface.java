package tr.wolflame.framework.base.interfaces;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by SADIK on 10/02/16.
 */
public interface BaseFragmentInterface {

    @LayoutRes
    int initLayoutId();

    View onInternalCreateView(View rootView, LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState);

}
