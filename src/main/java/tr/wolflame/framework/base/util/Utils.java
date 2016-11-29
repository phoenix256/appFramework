package tr.wolflame.framework.base.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import tr.wolflame.framework.R;
import tr.wolflame.framework.base.objects.ExtraPair;
import tr.wolflame.framework.base.util.helper.StaticFields;
import tr.wolflame.framework.base.util.helper.TextViewPlus;

/**
 * Created by SADIK on 05/02/16.
 */
public class Utils {

    private final static String TAG = "Utils";

    public static void startActivity(Context context, Class<?> cls, boolean willBeFinished) {
        final Intent intent = new Intent(context, cls);
        context.startActivity(intent);

        if (willBeFinished)
            ((Activity) context).finish();
    }

    public static void startActivityForResult(Context context, Class<?> cls, int requestCode, boolean willBeFinished) {
        final Intent intent = new Intent(context, cls);
        ((Activity) context).startActivityForResult(intent, requestCode);

        if (willBeFinished)
            ((Activity) context).finish();
    }

    public static void startActivity(Context context, Class<?> cls, ExtraPair extraPair, boolean willBeFinished) {
        final Intent intent = new Intent(context, cls);

        if (extraPair != null) {

            /*for (ExtraPair extraPair : extraPairs) {
                final String key = extraPair.getKey();
                if (key != null) {

                    final Serializable serializable = extraPair.getSerializable();
                    if (serializable != null)
                        intent.putExtra(key, serializable);

                    final ArrayList<Serializable> serializables = extraPair.getSerializableList();
                    if (serializables != null)
                        intent.putExtra(key, serializables);

                    final Parcelable parcelable = extraPair.getParcelableObj();
                    if (parcelable != null)
                        intent.putExtra(key, parcelable);

                    final ArrayList<Parcelable> parcelables = extraPair.getParcelables();
                    if (parcelables != null)
                        intent.putParcelableArrayListExtra(key, parcelables);


                }
            }*/

            intent.putExtra(StaticFields.KEY_EXTRA_PAIR, extraPair);

        }

        context.startActivity(intent);

        if (willBeFinished)
            ((Activity) context).finish();
    }


    public static boolean isStringNotEmpty(String input) {
        return input != null && !TextUtils.isEmpty(input);
    }

    public static synchronized <T> ArrayList<T> removeDuplicatedItems(ArrayList<T> inputList) {
        // add elements to al, including duplicates
        final Set<T> hs = new HashSet<>(inputList);
        inputList.clear();
        inputList.addAll(hs);
        return inputList;
    }

    public static synchronized <T> ArrayList<T> removeDuplicates(ArrayList<T> inputList) {
        final ArrayList<T> resultList = new ArrayList<>();

        for (T item : inputList) {
            if (!resultList.contains(item))
                resultList.add(item);
        }

        return resultList;
    }

    public static synchronized <T> ArrayList<T> getDifferentItems(ArrayList<T> originalList, ArrayList<T> inputList) {
        final ArrayList<T> resultList = new ArrayList<>();

        for (T item : inputList) {
            if (!originalList.contains(item))
                resultList.add(item);
        }

        return resultList;
    }

    public static void checkAndSetTextGone(TextViewPlus inputTv, String inputText) {
        if (!TextUtils.isEmpty(inputText)) {
            inputTv.setText(inputText);
        } else {
            inputTv.setVisibility(View.GONE);
        }
    }

    public static void startYoutubeVideo(Context context, String urlId) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + urlId)).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
    }

    public static <T> boolean isListNotEmpty(ArrayList<T> empty) {
        return empty != null && !empty.isEmpty();
    }

    public static boolean isFragmentAdded(Fragment fragment) {
        return fragment != null && fragment.isAdded();
    }

    public static void setVisibilityAnimated(final View view, final boolean isVisible) {

        final int startAlpha = isVisible ? 0 : 1;
        final int endAlpha = isVisible ? 1 : 0;

        final AlphaAnimation fadeOutAnimation = new AlphaAnimation(startAlpha, endAlpha);

        fadeOutAnimation.setDuration(400); // time for animation in milliseconds
        fadeOutAnimation.setFillAfter(true); // make the transformation persist
        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {

                if (view != null)
                    view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        });

        view.startAnimation(fadeOutAnimation);
    }


    private static final float ROTATE_FROM = 0.0f;
    private static final float ROTATE_TO = -10.0f * 360.0f;// 3.141592654f * 32.0f;
    private final static long DURATION = 2 * 1500;

    public static void rotateAnimation(View view, Animation.AnimationListener animationListener) {
        final RotateAnimation rotateAnimation; // = new RotateAnimation(ROTATE_FROM, ROTATE_TO);
        rotateAnimation = new RotateAnimation(ROTATE_FROM, ROTATE_TO, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(DURATION);
        rotateAnimation.setRepeatCount(0);

        rotateAnimation.setAnimationListener(animationListener);

        if (view != null)
            view.startAnimation(rotateAnimation);
    }

    public static void shareText(Context context, String shareText) {
        final Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, context.getResources().getText(R.string.share)));
    }

    public static boolean isActivityValid(Activity activity) {
        return activity != null && !activity.isFinishing();
    }
}
