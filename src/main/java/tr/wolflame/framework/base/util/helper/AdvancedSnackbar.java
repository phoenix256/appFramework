package tr.wolflame.framework.base.util.helper;

import android.support.annotation.ColorRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import tr.wolflame.framework.R;

/**
 * Created by SADIK on 20/04/16.
 */
public class AdvancedSnackbar {

    private Snackbar snackbar;

    private AdvancedSnackbar(Snackbar snackbar) {
        this.snackbar = snackbar;
    }

    public Snackbar getSnackbar() {
        return snackbar;
    }

    public static class Builder {

        private final static int EMPTY_RES = 0;

        private String message = "";
        private int backgroundColor = EMPTY_RES;
        private int textColor = EMPTY_RES;

        private int actionTextColor = EMPTY_RES;
        private View view = null;
        private int showLength = Snackbar.LENGTH_LONG;
        private int gravity = Gravity.LEFT;
        private String actionText = "";
        private View.OnClickListener actionClickListener = null;

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setBackgroundColor(@ColorRes int colorRes) {
            backgroundColor = colorRes;
            return this;
        }

        public Builder setView(View view) {
            this.view = view;
            return this;
        }

        /**
         * @param showLength {@link Snackbar LENGTH}
         * @return Builder
         */
        public Builder setShowLength(int showLength) {
            this.showLength = showLength;
            return this;
        }

        @ColorRes
        public Builder setTextColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setActionText(String actionText) {
            this.actionText = actionText;
            return this;
        }

        @ColorRes
        public Builder setActionTextColor(int actionTextColor) {
            this.actionTextColor = actionTextColor;
            return this;
        }

        public Builder setActionClickListener(View.OnClickListener actionClickListener) {
            this.actionClickListener = actionClickListener;
            return this;
        }

        public AdvancedSnackbar build() {
            final Snackbar snackbar = Snackbar.make(view, message, showLength);

            final View snackBarView = snackbar.getView();

            if (backgroundColor != EMPTY_RES)
                snackBarView.setBackgroundColor(backgroundColor);

            if (!TextUtils.isEmpty(actionText) && actionClickListener != null) {
                snackbar.setAction(actionText, actionClickListener);

                if (actionTextColor != EMPTY_RES)
                    snackbar.setActionTextColor(ContextCompat.getColor(view.getContext(), actionTextColor));
            }

            final TextView textTv = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);

            if (textTv != null) {
                if (textColor != EMPTY_RES)
                    textTv.setTextColor(ContextCompat.getColor(view.getContext(), textColor));

                textTv.setGravity(gravity);
            }

            return new AdvancedSnackbar(snackbar);
        }
    }

    public static void showSnackBar(View view, String message) {
        if (view != null) {
            showSnackBar(view, message, R.color.colorAccent);
        }
    }


    public static void showSnackBar(View view, String message, @ColorRes int actionTextColor) {
        if (view != null) {
            new AdvancedSnackbar.Builder().setView(view).setMessage(message).setShowLength(Snackbar.LENGTH_LONG).setGravity(Gravity.START).setTextColor(actionTextColor).build().getSnackbar().show();
        }
    }

}
