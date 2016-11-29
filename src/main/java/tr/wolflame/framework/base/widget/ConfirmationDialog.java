package tr.wolflame.framework.base.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import tr.wolflame.framework.R;

import static android.content.ContentValues.TAG;

/**
 * Created by sadikaltintoprak on 28/11/2016.
 */

public class ConfirmationDialog extends DialogFragment {

    private static final String ARG_MESSAGE = "message";

    private DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            Log.d(TAG, "onClick() called with: dialogInterface = [" + dialogInterface + "], i = [" + i + "]");
        }
    };
    private DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            Log.d(TAG, "onClick() called with: dialogInterface = [" + dialogInterface + "], i = [" + i + "]");
        }
    };

    public static ConfirmationDialog newInstance(String message) {
        final ConfirmationDialog confirmationDialog = new ConfirmationDialog();
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        confirmationDialog.setArguments(args);
        return confirmationDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setMessage(getArguments().getString(ARG_MESSAGE, getResources().getString(R.string.confirmation_message)))
                .setPositiveButton(android.R.string.ok, positiveListener)
                .setNegativeButton(android.R.string.cancel, negativeListener)
                .create();
    }

    public void setPositiveListener(DialogInterface.OnClickListener positiveListener) {
        this.positiveListener = positiveListener;
    }

    public void setNegativeListener(DialogInterface.OnClickListener negativeListener) {
        this.negativeListener = negativeListener;
    }
}