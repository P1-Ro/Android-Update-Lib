package sk.p1ro.updater.service;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import sk.p1ro.updater.util.UpdateUtil;

public class UpdateService extends IntentService {

    public UpdateService() {
        super("UpdateService");
    }

    public UpdateService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        UpdateUtil.installUpdate(this);
    }
}
