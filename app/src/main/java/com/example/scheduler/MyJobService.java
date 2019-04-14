package com.example.scheduler;


import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;

import org.greenrobot.eventbus.EventBus;

public class MyJobService extends JobService {

    public static final String MY_PREFS_NAME = "MyPrefsFile";

    String s;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("text", "");
        if (restoredText != null) {
            s = prefs.getString("s", "I'm here");

        }

        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("s", s + "-I'm here");
        editor.apply();
//        EventBus.getDefault().post(new NotifyUpdateEvent(s));
        sendMessage();
        return false;
    }

    private void sendMessage() {
        Intent intent = new Intent("custom-event-name");
        intent.putExtra("message", s);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }
}
