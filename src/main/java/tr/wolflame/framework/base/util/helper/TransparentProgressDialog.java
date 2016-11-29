package tr.wolflame.framework.base.util.helper;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import tr.wolflame.framework.R;


/**
 * Created by SADIK on 15/07/15.
 */
public class TransparentProgressDialog extends Dialog {

    private Context context;

    public TransparentProgressDialog(Context context) {
        super(context, R.style.TransparentProgressDialog);
        this.context = context;
    }

    public void showCustomDrawable(@DrawableRes int resOfDrawable) {
        final WindowManager.LayoutParams wlmp = getWindow().getAttributes();
        wlmp.gravity = Gravity.CENTER_HORIZONTAL;

        getWindow().setAttributes(wlmp);

        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);

        final LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        final ImageView customIv = new ImageView(getContext());
        customIv.setImageResource(resOfDrawable);

        layout.addView(customIv, params);

        addContentView(layout, params);

        final RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(3000);

        customIv.setAnimation(anim);
        customIv.startAnimation(anim);

        show();
    }

    public void showCustomLayout(@LayoutRes int resOfLayout) {
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);

        final RelativeLayout.LayoutParams mParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View view = inflater.inflate(resOfLayout, viewGroup);

        view.setBackgroundColor(Color.TRANSPARENT);

        setContentView(view, mParams);
        show();
    }

    public static TransparentProgressDialog show(Context context) {
        return show(context, "", "", false);
    }

    public static TransparentProgressDialog show(Context context, CharSequence title, CharSequence message) {
        return show(context, title, message, false);
    }

    public static TransparentProgressDialog show(Context context, CharSequence title, CharSequence message, boolean indeterminate) {
        return show(context, title, message, indeterminate, false, null);
    }

    public static TransparentProgressDialog show(Context context, CharSequence title, CharSequence message, boolean indeterminate, boolean cancelable) {
        return show(context, title, message, indeterminate, cancelable, null);
    }

    public static TransparentProgressDialog show(Context context, CharSequence title, CharSequence message, boolean indeterminate, boolean cancelable, OnCancelListener cancelListener) {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(context);
        dialog.setTitle(title);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        /* The next line will add the ProgressBar to the dialog. */
        dialog.addContentView(new ProgressBar(context), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dialog.show();

        return dialog;
    }

}

