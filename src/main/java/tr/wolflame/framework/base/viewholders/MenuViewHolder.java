package tr.wolflame.framework.base.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import tr.wolflame.framework.R;
import tr.wolflame.framework.base.objects.LeftMenuItem;

public class MenuViewHolder extends RecyclerView.ViewHolder {

    public int typeId;

    public TextView textView;
    public ImageView imageView;
    public ImageView profile;
    public TextView name;
    public TextView email;


    public MenuViewHolder(View itemView, int viewType) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
        super(itemView);

        // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created
        typeId = viewType;  // Setting holder id = 0 as the object being populated are of type header view  // setting holder id as 1 as the object being populated are of type item row

        if (viewType == LeftMenuItem.TYPE_ITEM) {
            textView = (TextView) itemView.findViewById(R.id.rowText); // Creating TextView object with the id of textView from item_row.xml
            imageView = (ImageView) itemView.findViewById(R.id.rowIcon);// Creating ImageView object with the id of ImageView from item_row.xml

        } else if (viewType == LeftMenuItem.TYPE_HEADER) {
            name = (TextView) itemView.findViewById(R.id.name);         // Creating Text View object from header.xml for name
            email = (TextView) itemView.findViewById(R.id.email);       // Creating Text View object from header.xml for email
            profile = (ImageView) itemView.findViewById(R.id.circleView);// Creating Image view object from header.xml for profile pic
        } else if (viewType == LeftMenuItem.TYPE_FOOTER) {
            name = (TextView) itemView.findViewById(R.id.name);         // Creating Text View object from header.xml for name
            email = (TextView) itemView.findViewById(R.id.email);       // Creating Text View object from header.xml for email
            profile = (ImageView) itemView.findViewById(R.id.circleView);// Creating Image view object from header.xml for profile pic
        } else {
            name = (TextView) itemView.findViewById(R.id.name);         // Creating Text View object from header.xml for name
            email = (TextView) itemView.findViewById(R.id.email);       // Creating Text View object from header.xml for email
            profile = (ImageView) itemView.findViewById(R.id.circleView);// Creating Image view object from header.xml for profile pic
        }
    }
}


