package sk.p1ro.updater.service;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import sk.p1ro.updater.worker.UpdateWorker;

public class UpdateNotificationService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        String version = data.get("version");
        buildUpdateWorker(version);
    }

    private void buildUpdateWorker(String version) {
        Data inputData = new Data.Builder()
                .putString("version", version)
                .build();

        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED).build();

        OneTimeWorkRequest updateRequest = new OneTimeWorkRequest.Builder(UpdateWorker.class)
                .setInputData(inputData)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(this).enqueue(updateRequest);
    }
}
