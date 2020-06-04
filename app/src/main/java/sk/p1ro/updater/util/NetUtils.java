package sk.p1ro.updater.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class NetUtils {

    public static boolean isWifiOn(Context context) {
        boolean result = false;
        ApplicationInfo info = context.getApplicationInfo();
        boolean isDebuggable =  ( 0 != ( info.flags & ApplicationInfo.FLAG_DEBUGGABLE ) );
        if (isDebuggable) {
            result = true;
        } else {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connManager != null) {
                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (mWifi != null) {
                    result = mWifi.isConnected();
                }
            }
        }
        return result;
    }

}
