package tr.wolflame.framework.base.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Filter;

import java.util.ArrayList;

import tr.wolflame.framework.base.interfaces.BaseAdapterInterface;
import tr.wolflame.framework.base.objects.BaseItem;
import tr.wolflame.framework.base.util.LogApp;


/**
 * Created by SADIK on 25/06/15.
 */
public abstract class BaseItemAdapter<T extends BaseItem, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements BaseAdapterInterface<VH> {

    protected final String TAG = this.getClass().getSimpleName();

    protected ArrayList<T> itemList;
    protected ArrayList<T> wholeList = new ArrayList<>();

    protected Context context;

    protected ArrayList<VH> viewHolderList;

    public BaseItemAdapter(Context context, ArrayList<T> itemList) {
        this.context = context;

        initWholeSearchList(itemList);

        generateItems(itemList);
    }

    private void generateItems(ArrayList<T> itemList) {

        this.itemList = itemList;

        this.viewHolderList = new ArrayList<>();

        for (T item : itemList) {
            this.viewHolderList.add(null);
        }

    }

    private void initWholeSearchList(ArrayList<T> itemList) {
        this.wholeList.clear();
        this.wholeList.addAll(itemList);
    }

    private void refreshItems(ArrayList<T> itemList) {

        generateItems(itemList);

        notifyDataSetChanged();
    }


    public void swapItems(ArrayList<T> itemList) {

        initWholeSearchList(itemList);

        refreshItems(itemList);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    @Override
    public void onBindViewHolder(final VH baseViewHolder, final int position) {

        if (isOnItemClickEnabled()) {
            baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isOnItemCheckEnabled())
                        checkAll(position);

                    selectItem(baseViewHolder, itemList.get(position), position);
                }
            });
        }

        // This is filled with dynamic class data
        viewHolderList.set(position, initOnBindViewHolder(baseViewHolder, position));
    }


    public VH getChildAt(int position) {
        //position = position + ObjectHolder.DUMMY_NUMBER_COLUMNS;  //for dummy items because of the adapter view bug

        return (viewHolderList.size() > position) ? viewHolderList.get(position) : null;
    }


    protected boolean isOnItemClickEnabled() {
        return true;
    }

    protected boolean isOnItemCheckEnabled() {
        return false;
    }

    protected void selectItem(VH viewHolder, T item, int position) {

    }

    protected synchronized void checkAll(int position) {

        try {
            for (int i = 0; i < itemList.size(); i++) {
                itemList.get(i).setSelected(false);
            }

            itemList.get(position).setSelected(true);

            notifyDataSetChanged();

        } catch (Exception e) {
            LogApp.e(TAG, String.valueOf(e.toString()));
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                constraint = constraint.toString().toLowerCase();

                final FilterResults result = new FilterResults();

                if (!TextUtils.isEmpty(constraint.toString())) {

                    final ArrayList<T> founded = new ArrayList<>();
                    for (T item : wholeList) {
                        final String title = item.getTitle();
                        if (title.toLowerCase().contains(constraint)) {
                            founded.add(item);
                        }
                    }
                    result.values = founded;
                    result.count = founded.size();
                } else {
                    result.values = wholeList;
                    result.count = wholeList.size();
                }
                return result;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                /*temList.clear();
                for (T item : (ArrayList<T>) results.values) {
                    itemList.add(item);
                }
                notifyDataSetChanged();*/
                refreshItems((ArrayList<T>) results.values);
            }
        };
    }

}