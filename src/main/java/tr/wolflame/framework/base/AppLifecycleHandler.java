package tr.wolflame.framework.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import tr.wolflame.framework.base.util.LogApp;


public class AppLifecycleHandler implements Application.ActivityLifecycleCallbacks {

    final private String TAG = this.getClass().getSimpleName();

    public void onActivityCreated(final Activity activity, Bundle bundle) {
        LogApp.e(TAG, "onActivityCreated: " + activity.getLocalClassName());

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                //inform yourself that the activity unexpectedly stopped
                //or activity is finished

                LogApp.e(TAG, String.valueOf(thread.getName()) + " :" + String.valueOf(ex.toString()));

            }
        });
    }

    public void onActivityDestroyed(Activity activity) {
        LogApp.e(TAG, "onActivityDestroyed: " + activity.getLocalClassName());

//        if(activity.getLocalClassName().contains("MainActivity"))
//        	clearObserverOnDestroy();


    }

    public void onActivityPaused(Activity activity) {
        LogApp.e(TAG, "onActivityPaused: " + activity.getLocalClassName());
    }

    public void onActivityResumed(Activity activity) {
        LogApp.e(TAG, "onActivityResumed: " + activity.getLocalClassName());
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        LogApp.e(TAG, "onActivitySaveInstanceState: " + activity.getLocalClassName());
    }

    public void onActivityStarted(Activity activity) {
        LogApp.e(TAG, "onActivityStarted: " + activity.getLocalClassName());

        // ApplicationState.getInstance().setConnectionType(NetworkUtilities.getConnectivityStatus(activity));
        // VolleySingleton.getInstance(activity).getNetworkManager().detectLoginType();

    }

    public void onActivityStopped(Activity activity) {
        LogApp.e(TAG, "onActivityStopped: " + activity.getLocalClassName());

    }
}

