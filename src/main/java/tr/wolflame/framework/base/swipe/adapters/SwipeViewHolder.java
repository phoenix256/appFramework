package tr.wolflame.framework.base.swipe.adapters;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SwipeViewHolder {
    public FrameLayout background, rootView;
    public TextView title, overView, year, rating, genres;
    public ImageView cardImage, skipIv, watchedIv;
    public LinearLayout trailerLl;
}