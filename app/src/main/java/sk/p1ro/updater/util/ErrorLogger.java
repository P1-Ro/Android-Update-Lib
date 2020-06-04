package sk.p1ro.updater.util;

import android.util.Log;

public final class ErrorLogger {

    private ErrorLogger() {
    }

    public static void log(String msg, Throwable throwable) {

        String finalMsg = msg != null ? msg + throwable.getMessage() : (throwable.getMessage() != null ? throwable.getMessage() : "Volley time out");
        Log.e(ErrorLogger.class.getName(), finalMsg, throwable);
    }

    public static void log(Throwable throwable) {
        log(null, throwable);
    }
}
