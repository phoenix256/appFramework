package tr.wolflame.framework.base.swipe.adapters;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import tr.wolflame.framework.base.objects.BaseItem;

public abstract class BaseSwipeAdapter<VH extends SwipeViewHolder, D extends BaseItem> extends BaseAdapter {

    protected final String TAG = this.getClass().getSimpleName();

    protected ArrayList<D> itemList;
    public Context context;

    public ArrayList<VH> swipeViewHolderList;

    public BaseSwipeAdapter(ArrayList<D> apps, Context context) {
        this.context = context;

        generateItems(apps);
    }

    private void generateItems(ArrayList<D> itemList) {
        this.itemList = itemList;
        this.swipeViewHolderList = new ArrayList<>();

        for (D item : itemList) {
            this.swipeViewHolderList.add(null);
        }
    }

    public void swapItems(ArrayList<D> itemList) {

        generateItems(itemList);

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public SwipeViewHolder getChildAt(int position) {
        //position = position + ObjectHolder.DUMMY_NUMBER_COLUMNS;  //for dummy items because of the adapter view bug
        return (swipeViewHolderList.size() > position) ? swipeViewHolderList.get(position) : null;
    }
}
