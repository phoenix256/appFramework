package tr.wolflame.framework.base.util.helper;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {

    private Context mContext;

    public static boolean isConnectingToInternet(Context mContext) {

        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }

        ((Activity) mContext).runOnUiThread(new Runnable() {
            public void run() {

                //Toast.makeText(mContext, mContext.getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
            }
        });


        return false;
    }

    public static ConnectionType getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return ConnectionType.TYPE_WIFI;
            }

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){

                return ConnectionType.TYPE_MOBILE;
            }
        }

        return ConnectionType.TYPE_NOT_CONNECTED;

    }

}
