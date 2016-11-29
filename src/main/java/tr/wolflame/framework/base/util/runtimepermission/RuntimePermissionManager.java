package tr.wolflame.framework.base.util.runtimepermission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import tr.wolflame.framework.R;
import tr.wolflame.framework.base.util.LogApp;


/**
 * Created by SADIK on 18/01/16.
 */
public class RuntimePermissionManager {

    private static final int RESULT_PERMS_MANAGER = 1339;
    private static final int REQUEST_APP_SETTINGS = 1340;

    private static final String PERMISSION_GRANTED = "PERMISSION_GRANTED";
    private static final String PERMISSION_DENIED = "PERMISSION_DENIED";

    final private String TAG = this.getClass().getSimpleName();

    private Activity mActivity;

    private ArrayList<PermissionObject> mPermissionList = new ArrayList<>();

    public RuntimePermissionManager(Activity mActivity) {
        this.mActivity = mActivity;
        this.mPermissionList = new ArrayList<>();
    }

    public static boolean useRuntimePermissions() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    /**
     * @param permissionKeys      has to be new String[]{ Manifest.permission.INTERNET,
     *                            Manifest.permission.WAKE_LOCK,
     *                            Manifest.permission.ACCESS_FINE_LOCATION,
     *                            Manifest.permission.ACCESS_WIFI_STATE,
     *                            Manifest.permission.ACCESS_NETWORK_STATE,
     *                            Manifest.permission.WRITE_EXTERNAL_STORAGE }
     * @param mPermissionCallback
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermission(String[] permissionKeys, PermissionCallback mPermissionCallback) {

        if (useRuntimePermissions()) {

            LogApp.d(TAG, "useRuntimePermissions: " + String.valueOf("true"));

            final ArrayList<String> mKeyList = new ArrayList<>();
            final ArrayList<String> mPreGrantedKeyList = new ArrayList<>();

            for (String permissionKey : permissionKeys) {

                if (ContextCompat.checkSelfPermission(mActivity, permissionKey) != PackageManager.PERMISSION_GRANTED) {

                    final PermissionObject mPermissionObject = new PermissionObject(permissionKey, mPermissionCallback);
                    mPermissionList.add(mPermissionObject);

                    mKeyList.add(permissionKey);

                } else {
                    LogApp.d(TAG, "requestPermission: " + String.valueOf(permissionKey) + " already " + PERMISSION_GRANTED);
                    mPreGrantedKeyList.add(permissionKey);
                }
            }

            if (!mPreGrantedKeyList.isEmpty()) {
                mPermissionCallback.listOfPreGrantedPermissions(mPreGrantedKeyList);
            }

            if (!mKeyList.isEmpty()) {

                String[] mPermissionArray = new String[mKeyList.size()];
                mPermissionArray = mKeyList.toArray(mPermissionArray);

                mActivity.requestPermissions(mPermissionArray, RESULT_PERMS_MANAGER);
            } else {

                final ArrayList<String> grantedList = new ArrayList<>();

                for (int i = 0; i < permissionKeys.length; i++) {
                    grantedList.add(permissionKeys[i]);
                }

                mPermissionCallback.onPermissionGranted(grantedList);
            }

        } else {

            LogApp.d(TAG, "useRuntimePermissions: " + String.valueOf("false") + " " + PERMISSION_GRANTED);
            mPermissionCallback.onRunTimePermissionNotNeeded();
        }

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        LogApp.d(TAG, "requestCode: " + String.valueOf(requestCode) + " | RESULT_PERMS_MANAGER: " + String.valueOf(RESULT_PERMS_MANAGER));
        switch (requestCode) {
            case RESULT_PERMS_MANAGER:

                callPermissionValue(permissions, grantResults);

                break;
            default:
                LogApp.d(TAG, "requestCode isNot Equal");
                break;
        }
    }

    private synchronized void callPermissionValue(String[] permissions, int[] grantResults) {

        try {
            final int permissionSize = permissions.length;

            final boolean isSingleItem = (permissionSize <= 1);

            boolean isDenied = false;
            boolean isAllDenialsNoMore = true;

            final ArrayList<String> granted = new ArrayList<>();
            final ArrayList<String> mAllDeniedList = new ArrayList<>();

            final ArrayList<String> denied = new ArrayList<>();
            final ArrayList<String> deniedWithNoMore = new ArrayList<>();


            PermissionCallback mPermissionCallback = null;

            for (int k = 0; k < permissionSize; k++) {

                final String tempPermissionKey = permissions[k];
                final int tempGrantResult = grantResults[k];

                for (int i = 0; i < mPermissionList.size(); i++) {

                    final PermissionObject mPermissionObject = mPermissionList.get(i);

                    if (mPermissionObject.getPermissionKey().equals(tempPermissionKey)) {

                        mPermissionCallback = mPermissionObject.getmPermissionCallback();

                        if (tempGrantResult == PackageManager.PERMISSION_DENIED) {
                            LogApp.d(TAG, "requestPermission: " + String.valueOf(tempPermissionKey) + " " + PERMISSION_DENIED);

                            final boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(mActivity, tempPermissionKey);
                            if (!showRationale) {
                                LogApp.d(TAG, "FLAGGED NEVER ASK AGAIN");
                                // user denied flagging NEVER ASK AGAIN
                                // you can either enable some fall back,
                                // disable features of your app
                                // or open another dialog explaining
                                // again the permission and directing to
                                // the app setting
                                deniedWithNoMore.add(tempPermissionKey);
                            } else {
                                LogApp.d(TAG, "WITHOUT NEVER ASK AGAIN");
                                // user denied WITHOUT never ask again
                                // this is a good place to explain the user
                                // why you need the permission and ask if he want
                                // to accept it (the rationale)
                                denied.add(tempPermissionKey);
                            }
                            isDenied = true;
                        } else {
                            LogApp.d(TAG, "requestPermission: " + String.valueOf(tempPermissionKey) + " " + PERMISSION_GRANTED);
                            granted.add(tempPermissionKey);
                        }


                        mPermissionList.remove(i);

                        if (isSingleItem)
                            break;
                    }
                }

                if (k == permissionSize - 1) {

                    if (isDenied) {

                        if (!deniedWithNoMore.isEmpty())
                            mPermissionCallback.onPermissionDeniedWithNoMore(deniedWithNoMore);

                        if (!denied.isEmpty()) {
                            isAllDenialsNoMore = false;
                            mPermissionCallback.onPermissionDenied(denied);
                        }


                        mAllDeniedList.addAll(deniedWithNoMore);
                        mAllDeniedList.addAll(denied);

                        if (!mAllDeniedList.isEmpty())
                            mPermissionCallback.listOfDeniedPermissions(mAllDeniedList, isAllDenialsNoMore);


                    } else {
                        mPermissionCallback.onPermissionGranted(granted);
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private AlertDialog mDialog;

    public void showPermissionAlert(final ArrayList<String> permissionKeyInput,
                                    boolean optionalThird, boolean isAllDenialsNoMore, DialogInterface.OnClickListener positiveButtonListener,
                                    DialogInterface.OnClickListener negativeBtnClickListener) {

        String[] buttonTexts = {mActivity.getResources().getString(R.string.try_again), mActivity.getResources().getString(R.string.dont_allow), mActivity.getResources().getString(R.string.go_to_settings)};
        AlertDialog.Builder d = new AlertDialog.Builder(mActivity);

        final String chars = mActivity.getResources().getString(R.string.info);
        final SpannableString mTitle = new SpannableString(chars);
        mTitle.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.generic_color)), 0, chars.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        d.setTitle(mTitle)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setCancelable(false)
                .setMessage(String.format(mActivity.getResources().getString(R.string.permissionListWarning), getPermissionsText(permissionKeyInput)));

        d.setPositiveButton(buttonTexts[0], positiveButtonListener);

        d.setNegativeButton(buttonTexts[1], negativeBtnClickListener);

        if (optionalThird)
            d.setNeutralButton(buttonTexts[2], new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    goToSettings(getStringArray(permissionKeyInput));
                    mDialog.dismiss();
                }
            });

        mDialog = d.create();

        mDialog.show();

        if (isAllDenialsNoMore)
            mDialog.getButton(DialogInterface.BUTTON_POSITIVE).setVisibility(View.GONE);

        try {
            Resources resources = mDialog.getContext().getResources();
            final int color = resources.getColor(R.color.generic_color); // your color here

            int alertTitleId = resources.getIdentifier("alertTitle", "id", "android");
            TextView alertTitle = (TextView) mDialog.getWindow().getDecorView().findViewById(alertTitleId);
            alertTitle.setTextColor(color); // change title text color

            int titleDividerId = resources.getIdentifier("titleDivider", "id", "android");
            View titleDivider = mDialog.getWindow().getDecorView().findViewById(titleDividerId);
            titleDivider.setBackgroundColor(color); // change divider color
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String[] permissionKeys;

    private void goToSettings(String[] permissionKeys) {
        this.permissionKeys = permissionKeys;
        final Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + mActivity.getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        //myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.startActivityForResult(myAppSettings, REQUEST_APP_SETTINGS);
    }

    final private static String LOCATION = "LOCATION",
            STORAGE = "STORAGE",
            INTERNET = "INTERNET",
            WAKE_LOCK = "WAKE_LOCK",
            ACCESS_NETWORK_STATE = "ACCESS_NETWORK_STATE",
            ACCESS_WIFI_STATE = "ACCESS_WIFI_STATE",
            READ_PHONE_STATE = "READ_PHONE_STATE";

    public String getPermissionsText(ArrayList<String> permissionKeys) {
        final StringBuilder mStringBuilder = new StringBuilder("");

        final int permissionSize = permissionKeys.size();
        for (int i = 0; i < permissionSize; i++) {

            final String permissionKey = permissionKeys.get(i);

            if (i != 0)
                mStringBuilder.append(", ");

            if (permissionKey.contains(LOCATION)) {
                mStringBuilder.append("Konumuna");

            } else if (permissionKey.contains(STORAGE)) {
                mStringBuilder.append("Dosyalarına");

            } else if (permissionKey.contains(INTERNET)) {
                mStringBuilder.append("İnternetine");

            } else if (permissionKey.contains(WAKE_LOCK)) {
                mStringBuilder.append("Telefon Durumuna");

            } else if (permissionKey.contains(ACCESS_NETWORK_STATE)) {
                mStringBuilder.append("Bağlantı Durumuna");

            } else if (permissionKey.contains(ACCESS_WIFI_STATE)) {
                mStringBuilder.append("İnternet Durumuna");

            } else if (permissionKey.contains(READ_PHONE_STATE)) {
                mStringBuilder.append("Arama Durumuna");
            }
        }


        return mStringBuilder.toString();

    }

    public boolean hasPermissions(@NonNull String... permissions) {
        for (String permission : permissions)
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(mActivity, permission))
                return false;
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_APP_SETTINGS) {

            LogApp.d(TAG, "permissionKeys: " + String.valueOf(permissionKeys));

            if (permissionKeys != null && hasPermissions(permissionKeys)) {
                permissionKeys = null;

                LogApp.d(TAG, String.valueOf("All permissions granted!"));
                if (mDialog != null) {
                    mDialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
                    mDialog.dismiss();
                }
            } else {

                LogApp.d(TAG, String.valueOf("Permissions not granted."));
                if (mDialog != null) {
                    mDialog.show();
                }
            }
        }
    }

    public static String[] getStringArray(ArrayList<String> inputArray) {

        String[] outputArray = new String[inputArray.size()];
        outputArray = inputArray.toArray(outputArray);

        return outputArray;
    }

}
