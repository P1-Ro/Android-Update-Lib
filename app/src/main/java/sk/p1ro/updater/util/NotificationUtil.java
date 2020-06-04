package sk.p1ro.updater.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static sk.p1ro.updater.util.UpdateUtil.getCurrentAppInfo;

public class NotificationUtil {

    private static boolean initialized = false;

    public static void createNotification(Context context, String channelId, String title, String message, PendingIntent pendingIntent) {

        PackageInfo info = getCurrentAppInfo(context);

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (info.versionCode >= Build.VERSION_CODES.O && !initialized) {
            createNotificationChannel(context, channelId);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(info.applicationInfo.icon)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (pendingIntent != null) {
            notification.setContentIntent(pendingIntent);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(123456781, notification.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static synchronized void createNotificationChannel(Context context, String channelId) {
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(channelId, channelId, importance);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
            initialized = true;
        }
    }
}