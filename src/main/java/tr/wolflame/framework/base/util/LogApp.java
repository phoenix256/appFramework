package tr.wolflame.framework.base.util;


import tr.wolflame.framework.BuildConfig;

/**
 * Created by SADIK on 05/02/16.
 */
public class LogApp {

    //:TODO DEBUG
    public static void d(String TAG, String message) {
        if (BuildConfig.IS_TEST)
            android.util.Log.d(TAG, String.valueOf(message));
    }

    public static void d(String TAG, String parameter, String message) {
        if (BuildConfig.IS_TEST)
            android.util.Log.d(TAG, String.valueOf(parameter) + ": " + String.valueOf(message));
    }


    //:TODO ERROR
    public static void e(String TAG, String message) {
        if (BuildConfig.IS_TEST)
            android.util.Log.e(TAG, String.valueOf(message));
    }

    public static void e(String TAG, String parameter, String message) {
        if (BuildConfig.IS_TEST)
            android.util.Log.e(TAG, String.valueOf(parameter) + ": " + String.valueOf(message));
    }


}


