package tr.wolflame.framework.base.interfaces;

import android.support.v7.widget.RecyclerView;

/**
 * Created by SADIK on 03/12/15.
 */

public interface BaseAdapterInterface<VH extends RecyclerView.ViewHolder> {

    VH initOnBindViewHolder(VH mViewHolder, int position);
}
