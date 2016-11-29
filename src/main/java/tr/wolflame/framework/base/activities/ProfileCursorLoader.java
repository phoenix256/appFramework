package tr.wolflame.framework.base.activities;


import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

import tr.wolflame.framework.base.util.LogApp;
import tr.wolflame.framework.base.util.helper.OnTaskCompleted;

public class ProfileCursorLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "ProfileCursorLoader";

    private Activity activity;

    private OnTaskCompleted onTaskCompleted;

    public ProfileCursorLoader(Activity activity) {
        this.activity = activity;
    }


    public void initLoader(OnTaskCompleted onTaskCompleted) {
        this.onTaskCompleted = onTaskCompleted;
        initLoader(this);
    }

    public void initLoader() {
        initLoader(this);
    }

    public void initLoader(LoaderManager.LoaderCallbacks loaderCallback) {
        activity.getLoaderManager().initLoader(0, null, loaderCallback);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle arguments) {
        return new CursorLoader(activity,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(
                        ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY),
                ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE + " = ?",
                new String[]{ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            // Potentially filter on ProfileQuery.IS_PRIMARY
            cursor.moveToNext();
        }

        for (int i = 0; i < emails.size(); i++) {
            final String email = emails.get(i);
            LogApp.d(TAG, String.valueOf(email));

            if (i == 0 && onTaskCompleted != null) {
                onTaskCompleted.onTaskCompleted(email);
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }
}