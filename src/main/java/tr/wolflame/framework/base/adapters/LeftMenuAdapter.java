package tr.wolflame.framework.base.adapters;

import android.content.Context;

import java.util.ArrayList;

import tr.wolflame.framework.base.objects.LeftMenuItem;
import tr.wolflame.framework.base.util.LogApp;
import tr.wolflame.framework.base.viewholders.MenuViewHolder;

/**
 * Created by SADIK on 22/02/16.
 */
public class LeftMenuAdapter extends BaseMenuAdapter<LeftMenuItem> {

    public LeftMenuAdapter(Context context, ArrayList<LeftMenuItem> itemList) {
        super(context, itemList);
    }

    @Override
    protected void selectItem(MenuViewHolder mViewHolder, LeftMenuItem leftMenuItem, int position) {
        super.selectItem(mViewHolder, leftMenuItem, position);
        LogApp.d(TAG, String.valueOf(position));

        //((BaseAppCompatActivity) context).selectLeftMenuItem(position);
    }
}
