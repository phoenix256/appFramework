package tr.wolflame.framework.base;

import android.support.multidex.MultiDexApplication;

public class FrameApplication extends MultiDexApplication {

    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onCreate() {
        //MultiDex.install(getApplicationContext());
        super.onCreate();
        // Fabric.with(this, new Crashlytics());

        //FacebookSdk.sdkInitialize(getApplicationContext());

        //registerActivityLifecycleCallbacks(new AppLifecycleHandler());
    }


}
