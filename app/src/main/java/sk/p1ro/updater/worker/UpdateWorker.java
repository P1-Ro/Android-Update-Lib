package sk.p1ro.updater.worker;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import sk.p1ro.updater.R;
import sk.p1ro.updater.service.UpdateService;
import sk.p1ro.updater.util.NetUtils;
import sk.p1ro.updater.util.NotificationUtil;
import sk.p1ro.updater.util.UpdateUtil;

public class UpdateWorker extends Worker {

    private String version;
    private Context context;
    private static final String CHANNEL_ID = "Update";

    public UpdateWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        this.context = context;
        version = getInputData().getString("version");

    }

    @NonNull
    @Override
    public Result doWork() {

        if (NetUtils.isWifiOn(context)) {
            UpdateUtil.downloadUpdate(context, success ->
                    showUpdateNotification(version)
            );

            return Result.success();
        }

        return Result.retry();
    }

    private void showUpdateNotification(String version) {

        Intent intent = new Intent(context, UpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        String msg = context.getString(R.string.update_version, version);
        String title = context.getString(R.string.update_title);

        NotificationUtil.createNotification(context, CHANNEL_ID, title, msg, pendingIntent);
    }
}
