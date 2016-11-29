package tr.wolflame.framework.base.adapters;

import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import tr.wolflame.framework.R;
import tr.wolflame.framework.base.objects.LeftMenuItem;
import tr.wolflame.framework.base.util.helper.StaticFields;
import tr.wolflame.framework.base.viewholders.MenuViewHolder;

/**
 * Created by SADIK on 11/02/16.
 */
public class LeftMenuExampleAdapter extends RecyclerView.Adapter<MenuViewHolder> {

    private String navTitles[] = new String[StaticFields.INVALID]; // String Array to store the passed titles Value from MainActivity.java

    @DrawableRes
    private int menuIcons[] = new int[StaticFields.INVALID];        // Int Array to store the passed icons resource value from MainActivity.java

    private String name;        //String Resource for header View name
    private int profilePhoto;        //int Resource for header view profile picture
    private String email;       //String Resource for header view email


    // MyAdapter Constructor with titles and icons parameter
    // titles, icons, name, email, profile pic are passed from the main activity as we have seen earlier
    public LeftMenuExampleAdapter(String navTitles[], int menuIcons[], String name, String email, int profilePhoto) {

        //here we assign those passed values to the values we declared here in adapter
        if (navTitles != null)
            this.navTitles = navTitles;

        if (menuIcons != null)
            this.menuIcons = menuIcons;

        this.name = name;
        this.email = email;
        this.profilePhoto = profilePhoto;

    }


    //Below first we override the method onCreateViewHolder which is called when the ViewHolder is
    //Created, In this method we inflate the item_row.xml layout if the viewType is Type_ITEM or else we inflate header.xml
    // if the viewType is TYPE_HEADER
    // and pass it to the view holder
    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int layoutId;

        if (viewType == LeftMenuItem.TYPE_HEADER) {
            layoutId = R.layout.item_header_drawer;
        } else {
            layoutId = R.layout.item_menu_row;
        }
        return new MenuViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false), viewType); //Creating ViewHolder and passing the object of type view
    }

    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(MenuViewHolder holder, int position) {
        if (holder.typeId == LeftMenuItem.TYPE_ITEM) {                              // as the list view is going to be called after the header view so we decrement the
            // position by 1 and pass it to the holder while setting the text and image
            holder.textView.setText(navTitles[position - 1]); // Setting the Text with the array of our Titles
            holder.imageView.setImageResource(menuIcons[position - 1]);// Setting the image with array of our icons
        } else {

            holder.profile.setImageResource(profilePhoto);           // Similarly we set the resources for header view
            holder.name.setText(name);
            holder.email.setText(email);
        }
    }

    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return navTitles.length + 1; // the number of items in the list will be +1 the titles including the header view.
    }


    // Witht the following method we check what type of view is being passed
    @Override
    public int getItemViewType(int position) {

        if (isPositionItem(position))
            return LeftMenuItem.TYPE_ITEM;

        if (isPositionHeader(position))
            return LeftMenuItem.TYPE_HEADER;

        if (isPositionOther(position))
            return LeftMenuItem.TYPE_OTHER;

        return LeftMenuItem.TYPE_FOOTER;
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

}