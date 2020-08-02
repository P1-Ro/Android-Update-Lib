package sk.p1ro.updater.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;

import sk.p1ro.updater.BuildConfig;
import sk.p1ro.updater.R;


public final class UpdateUtil {

    public static String getServerUrl(Context context) {
        return context.getString(R.string.update_url);
    }

    public static String getApiKey(Context context) {
        return context.getString(R.string.update_api_key);
    }

    public static void checkAndInstallUpdate(Context context) {
        checkUpdate(context, shouldInstall -> {
            if (shouldInstall) {
                downloadUpdate(context, callback -> {
                    installUpdate(context);
                });
            } else {
                Toast.makeText(context, R.string.update_latest_version, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void checkUpdate(Context context, Listener<Boolean> callback) throws UpdateException {

        ProgressDialog dialog = ProgressDialog.show(
                context,
                context.getString(R.string.update_check),
                context.getString(R.string.update_wait_please),
                true
        );

        PackageInfo info = getCurrentAppInfo(context);

        if (dialog != null) {
            dialog.setCancelable(true);
        }

        new Thread(() -> {
            try {
                URL u = new URL(getServerUrl(context) + "latest/" + info.packageName);
                HttpURLConnection c = (HttpURLConnection) u.openConnection();
                c.setRequestProperty("apiKey", getApiKey(context));
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();

                try (BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()))) {
                    String line;
                    StringBuilder sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }

                    JSONObject result = new JSONObject(sb.toString());
                    int versionCode = result.getInt("versionCode");
                    boolean shouldUpdate = versionCode > info.versionCode;
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                        callback.onResponse(shouldUpdate);
                    }

                } catch (Exception e) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    throw new UpdateException(e);
                }

                callback.onResponse(true);
            } catch (Exception e) {
                throw new UpdateException(e);
            }
        }).start();
    }

    static PackageInfo getCurrentAppInfo(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = packageManager.getPackageInfo(context.getApplicationContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            ErrorLogger.log(e);
        }
        return info;
    }

    public static void downloadUpdate(Context context, Listener<Boolean> callback) throws UpdateException {
        PackageInfo info = getCurrentAppInfo(context);
        new Thread(() -> {
            try {
                URL u = new URL(getServerUrl(context) + "download/" + info.packageName);
                HttpURLConnection c = (HttpURLConnection) u.openConnection();
                c.setRequestProperty("apiKey", getApiKey(context));
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();

                File directory = context.getFilesDir();
                FileOutputStream f = new FileOutputStream(new File(directory, info.packageName + ".apk"));

                InputStream in = c.getInputStream();

                byte[] buffer = new byte[1024];
                int len1;
                while ((len1 = in.read(buffer)) > 0) {
                    f.write(buffer, 0, len1);
                }
                f.close();
                callback.onResponse(true);
            } catch (Exception e) {
                throw new UpdateException(e);
            }
        }).start();
    }

    public static void installUpdate(Context context) {
        PackageInfo info = getCurrentAppInfo(context);
        File directory = context.getFilesDir();
        File file = new File(directory, info.packageName + ".apk");
        Uri fileUri = Uri.fromFile(file);

        if (Build.VERSION.SDK_INT >= 24) {
            fileUri = FileProvider.getUriForFile(context, context.getPackageName() + ".apks", file);
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
        intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
        intent.setDataAndType(fileUri, "application/vnd.android" + ".package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);

    }
}
