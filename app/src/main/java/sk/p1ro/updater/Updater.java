package sk.p1ro.updater;

import android.content.Context;

import com.google.firebase.FirebaseApp;

import sk.p1ro.updater.util.UpdateException;
import sk.p1ro.updater.util.UpdateUtil;

public final class Updater {

    private Updater() {
    }


    public static void init(Context context) {
        checkSettings(context);
        FirebaseApp.initializeApp(context);
    }

    private static void checkSettings(Context context) {
        if (UpdateUtil.getApiKey(context).length() == 0){
            throw new UpdateException("There is no update_api_key string set");
        }

        if (UpdateUtil.getServerUrl(context).length() == 0){
            throw new UpdateException("There is no update_url string set");
        }
    }
}
