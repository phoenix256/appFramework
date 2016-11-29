package tr.wolflame.framework.base.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import tr.wolflame.framework.base.interfaces.ItemValueEventListener;
import tr.wolflame.framework.base.util.helper.FirebaseAuthHelper;

/**
 * Created by SADIK on 08/06/16.
 */
public abstract class AppSettings {
    private static final String TAG = "AppSettings";

    private static final String KEY_USER_DATABASE = "userDatabase";
    public static final String KEY_EMAIL = "eMail";
    public static final String KEY_TIMESTAMP = "Timestamp";

    private final String APP_PREFERENCES = this.getClass().getSimpleName();

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private SharedPreferences getSharedPreferences(Context context) {
        if (sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences(APP_PREFERENCES, 0);
        return sharedPreferences;
    }

    private SharedPreferences.Editor getEditor(Context context) {
        if (editor == null) {
            editor = getSharedPreferences(context).edit();
            editor.apply();
        }
        return editor;
    }

    public void clearAppPrefs(Context context) {
        getEditor(context).clear().commit();
    }

    public <T> void saveItems(Context context, ArrayList<T> userList, String KEY) {
        saveItems(context, userList, KEY, true);
    }

    public <T> void saveItems(Context context, ArrayList<T> userList, String KEY, boolean isAddOnline) {
        Log.d(TAG, "saveItems() called with: context = [" + context + "], userList = [" + userList + "], KEY = [" + KEY + "]");

        getEditor(context).putString(KEY, new Gson().toJson(userList)).apply();

        if (isAddOnline) {
            final String currentTimeMillis = String.valueOf(System.currentTimeMillis());

            writeList(userList, KEY, currentTimeMillis);

            saveItem(context, KEY + KEY_TIMESTAMP, currentTimeMillis);
        }
    }

    private void saveItem(Context context, String KEY, String result) {
        getEditor(context).putString(KEY, result).apply();
    }

    public void clearItems(Context context, String KEY) {
        clearItems(context, KEY, true);
    }

    public void clearItems(Context context, String KEY, boolean isAddOnline) {
        saveItems(context, new ArrayList<>(), KEY, isAddOnline);
    }

    public <T> void addItem(Context context, T item, Class<T[]> classOfArray, String KEY) {
        addItem(context, item, classOfArray, KEY, true);
    }

    public <T> void addItem(Context context, T item, Class<T[]> classOfArray, String KEY, boolean isAddOnline) {
        ArrayList<T> itemList = getItems(context, classOfArray, KEY);

        if (itemList == null)
            itemList = new ArrayList<>();

        if (!itemList.contains(item)) {
            itemList.add(item);
        }

        saveItems(context, itemList, KEY, isAddOnline);
    }

    public <T> void removeItem(Context context, T item, Class<T[]> classOfArray, String KEY) {
        removeItem(context, item, classOfArray, KEY, true);
    }

    public <T> void removeItem(Context context, T item, Class<T[]> classOfArray, String KEY, boolean isAddOnline) {
        final ArrayList<T> itemList = getItems(context, classOfArray, KEY);
        if (itemList != null && !itemList.isEmpty()) {
            itemList.remove(item);

            saveItems(context, itemList, KEY, isAddOnline);
        }
    }

    public String getStringItem(Context context, String KEY) {
        return getSharedPreferences(context).getString(KEY, "");
    }

    public <T> ArrayList<T> getItems(Context context, Class<T[]> classOfArray, String KEY) {
        ArrayList<T> itemList = new ArrayList<>();
        if (getSharedPreferences(context).contains(KEY)) {
            final String jsonUsers = getSharedPreferences(context).getString(KEY, null);

            if (jsonUsers != null && !jsonUsers.isEmpty()) {
                itemList.addAll(Arrays.asList(new Gson().fromJson(jsonUsers, classOfArray)));
            }
        }
        return itemList;
    }

    public <T> boolean isItemContain(Context context, T item, Class<T[]> classOfArray, String KEY) {
        final ArrayList<T> itemList = getItems(context, classOfArray, KEY);
        if (itemList != null && !itemList.isEmpty()) {
            return itemList.contains(item);
        } else {
            return false;
        }
    }

    public static void writeObjectUnderUser(Object object, String resultKey) {
        if (FirebaseAuthHelper.isUserSignedIn()) {
            final String currentTimeMillis = String.valueOf(System.currentTimeMillis());

            final FirebaseUser firebaseUser = FirebaseAuthHelper.getCurrentFirebaseUser();

            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

            if (firebaseUser != null) {
                final String uid = firebaseUser.getUid();

                if (!TextUtils.isEmpty(uid)) {
                    databaseReference.child(KEY_USER_DATABASE).child(uid).child(resultKey).setValue(object);

                    databaseReference.child(KEY_USER_DATABASE).child(uid).child(resultKey + KEY_TIMESTAMP).setValue(currentTimeMillis);
                }
            }
        }
    }

    public void writeCurrentUser(Context context) {
        if (FirebaseAuthHelper.isUserSignedIn()) {
            final String currentTimeMillis = String.valueOf(System.currentTimeMillis());

            final FirebaseUser firebaseUser = FirebaseAuthHelper.getCurrentFirebaseUser();

            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

            if (firebaseUser != null) {
                final String uid = firebaseUser.getUid();

                final String email = firebaseUser.getEmail();

                if (!TextUtils.isEmpty(uid) && !TextUtils.isEmpty(email)) {
                    databaseReference.child(KEY_USER_DATABASE).child(uid).child(KEY_EMAIL).setValue(email);

                    databaseReference.child(KEY_USER_DATABASE).child(uid).child(KEY_EMAIL + KEY_TIMESTAMP).setValue(currentTimeMillis);

                    saveItem(context, KEY_EMAIL + KEY_TIMESTAMP, currentTimeMillis);
                }
            }
        }
    }

    private static <T> void writeList(ArrayList<T> objectList, String keyOfResultList, String currentTimeMillis) {
        if (FirebaseAuthHelper.isUserSignedIn()) {
            final FirebaseUser firebaseUser = FirebaseAuthHelper.getCurrentFirebaseUser();

            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

            if (firebaseUser != null) {
                final String uid = firebaseUser.getUid();

                if (!TextUtils.isEmpty(uid)) {
                    databaseReference.child(KEY_USER_DATABASE).child(uid).child(keyOfResultList).setValue(objectList);

                    databaseReference.child(KEY_USER_DATABASE).child(uid).child(keyOfResultList + KEY_TIMESTAMP).setValue(currentTimeMillis);
                }
            }
        }
    }

    protected static <T> void getValueList(String keyOfResultList, final GenericTypeIndicator<ArrayList<T>> genericTypeIndicator, final ItemValueEventListener<T> itemValueEventListener) {
        getValueList(keyOfResultList, genericTypeIndicator, true, itemValueEventListener);
    }

    protected static <T> void getValueList(String keyOfResultList, final GenericTypeIndicator<ArrayList<T>> genericTypeIndicator, boolean keepSynced, final ItemValueEventListener<T> itemValueEventListener) {
        Log.d(TAG, "getValueList() called with: keyOfResultList = [" + keyOfResultList + "], genericTypeIndicator = [" + genericTypeIndicator + "], keepSynced = [" + keepSynced + "], itemValueEventListener = [" + itemValueEventListener + "]");

        if (FirebaseAuthHelper.isUserSignedIn()) {
            final FirebaseUser firebaseUser = FirebaseAuthHelper.getCurrentFirebaseUser();

            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.keepSynced(keepSynced);

            if (firebaseUser != null) {
                final String uid = firebaseUser.getUid();

                if (!TextUtils.isEmpty(uid))
                    databaseReference.child(KEY_USER_DATABASE).child(uid).child(keyOfResultList).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d(TAG, "onDataChange() called with: dataSnapshot = [" + dataSnapshot + "]");
                            try {
                                itemValueEventListener.onArrayListFilled(dataSnapshot.getValue(genericTypeIndicator));
                            } catch (Exception e) {
                                e.printStackTrace();
                                FirebaseCrash.report(e);
                                itemValueEventListener.onCancelled();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d(TAG, "onCancelled() called with: databaseError = [" + databaseError + "]");
                            itemValueEventListener.onCancelled();
                        }
                    });
            }
        }
    }

    public static <T> void getValue(final Class<T> classOfT, final ItemValueEventListener<T> itemValueEventListener, String... resultKeyList) {
        getValue(classOfT, true, itemValueEventListener, resultKeyList);
    }

    public static <T> void getValue(final Class<T> classOfT, boolean keepSynced, final ItemValueEventListener<T> itemValueEventListener, String... resultKeyList) {
        Log.d(TAG, "getValue() called with: classOfT = [" + classOfT + "], keepSynced = [" + keepSynced + "], itemValueEventListener = [" + itemValueEventListener + "], resultKeyList = [" + resultKeyList + "]");

        if (FirebaseAuthHelper.isUserSignedIn() && resultKeyList != null && resultKeyList.length > 0) {

            final FirebaseUser firebaseUser = FirebaseAuthHelper.getCurrentFirebaseUser();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.keepSynced(keepSynced);

            if (firebaseUser != null) {
                final String uid = firebaseUser.getUid();

                if (!TextUtils.isEmpty(uid)) {
                    databaseReference = databaseReference.child(KEY_USER_DATABASE).child(uid);

                    for (String child : resultKeyList) {
                        databaseReference = databaseReference.child(child);
                    }

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d(TAG, "onDataChange() called with: dataSnapshot = [" + dataSnapshot + "]");
                            try {
                                itemValueEventListener.onObjectFilled(dataSnapshot.getValue(classOfT));
                            } catch (Exception e) {
                                e.printStackTrace();
                                FirebaseCrash.report(e);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d(TAG, "onCancelled() called with: databaseError = [" + databaseError + "]");
                            itemValueEventListener.onCancelled();
                        }
                    });
                }

            }
        }
    }
}
