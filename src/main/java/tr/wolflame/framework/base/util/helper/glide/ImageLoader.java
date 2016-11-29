package tr.wolflame.framework.base.util.helper.glide;

import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by SADIK on 02/03/16.
 */
public class ImageLoader {

    public static void loadIntoImageView(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .crossFade()
                .into(imageView);

    }


    public static void loadIntoImageView(ImageView imageView, @DrawableRes int resourceId, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .crossFade()
                .error(resourceId)
                .into(imageView);
    }

}
