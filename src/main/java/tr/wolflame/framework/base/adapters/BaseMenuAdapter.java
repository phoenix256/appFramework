package tr.wolflame.framework.base.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import tr.wolflame.framework.R;
import tr.wolflame.framework.base.objects.LeftMenuItem;
import tr.wolflame.framework.base.util.Utils;
import tr.wolflame.framework.base.util.helper.StaticFields;
import tr.wolflame.framework.base.viewholders.MenuViewHolder;

/**
 * Created by SADIK on 22/02/16.
 */
public abstract class BaseMenuAdapter<MI extends LeftMenuItem> extends BaseItemAdapter<MI, MenuViewHolder> {

    public BaseMenuAdapter(Context context, ArrayList<MI> itemList) {
        super(context, itemList);
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final int layoutId;
        if (viewType == LeftMenuItem.TYPE_ITEM) {
            layoutId = initItemLayout();
        } else if (viewType == LeftMenuItem.TYPE_HEADER) {
            layoutId = initHeaderLayout();
        } else if (viewType == LeftMenuItem.TYPE_FOOTER) {
            layoutId = initFooterLayout();
        } else {
            layoutId = initOtherLayout();
        }
        return new MenuViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false), viewType); //Creating ViewHolder and passing the object of type view
    }

    @Override
    public MenuViewHolder initOnBindViewHolder(final MenuViewHolder mViewHolder, final int position) {

        final MI menuItem = itemList.get(position);
        final int icon = menuItem.getIcon();
        final String title = menuItem.getTitle();
        final String subTitle = menuItem.getSubTitle();

        final int typeId = mViewHolder.typeId;

        if (isPositionItem(typeId)) {                              // as the list view is going to be called after the header view so we decrement the
            // position by 1 and pass it to the holder while setting the text and image
            mViewHolder.textView.setText(menuItem.getTitle()); // Setting the Text with the array of our Titles

            mViewHolder.textView.setSelected(menuItem.isSelected());

            if (icon != StaticFields.INVALID)
                mViewHolder.imageView.setImageResource(icon);// Setting the image with array of our icons
            else
                mViewHolder.imageView.setVisibility(View.GONE);

        } else if (isPositionHeader(typeId)) {  //HEADER

            if (menuItem.getIcon() != StaticFields.INVALID)
                mViewHolder.profile.setImageResource(menuItem.getIcon());           // Similarly we set the resources for header view

            if (Utils.isStringNotEmpty(title))
                mViewHolder.name.setText(title);

            if (Utils.isStringNotEmpty(subTitle))
                mViewHolder.email.setText(subTitle);

        } else if (isPositionFooter(typeId)) {  //FOOTER

            if (menuItem.getIcon() != StaticFields.INVALID)
                mViewHolder.profile.setImageResource(menuItem.getIcon());           // Similarly we set the resources for footer view

            if (Utils.isStringNotEmpty(title))
                mViewHolder.name.setText(title);

            if (Utils.isStringNotEmpty(subTitle))
                mViewHolder.email.setText(subTitle);

        } else {   //OTHER

            if (menuItem.getIcon() != StaticFields.INVALID)
                mViewHolder.profile.setImageResource(menuItem.getIcon());           // Similarly we set the resources for header view

            if (Utils.isStringNotEmpty(title))
                mViewHolder.name.setText(title);

            if (Utils.isStringNotEmpty(subTitle))
                mViewHolder.email.setText(subTitle);
        }

        return mViewHolder;
    }

    // With the following method we check what type of view is being passed
    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).getItemType();
    }

    private boolean isPositionHeader(int position) {
        return position == LeftMenuItem.TYPE_HEADER;
    }

    private boolean isPositionFooter(int position) {
        return position == LeftMenuItem.TYPE_FOOTER;
    }

    private boolean isPositionOther(int position) {
        return position == LeftMenuItem.TYPE_OTHER;
    }

    private boolean isPositionItem(int position) {
        return position == LeftMenuItem.TYPE_ITEM;
    }

    @LayoutRes
    protected int initItemLayout() {
        return R.layout.item_menu_row;
    }

    @LayoutRes
    protected int initHeaderLayout() {
        return R.layout.item_header_drawer;
    }

    @LayoutRes
    protected int initFooterLayout() {
        return R.layout.item_footer_drawer;
    }

    @LayoutRes
    protected int initOtherLayout() {
        return R.layout.item_other_drawer;
    }

}
