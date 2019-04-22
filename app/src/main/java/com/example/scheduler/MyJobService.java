package com.example.scheduler;


import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

//import com.firebase.jobdispatcher.JobService;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;

public class MyJobService extends JobService {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private static final String TAG = "MyJobService";
    private String s;
    private String t;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Date currentTime = Calendar.getInstance().getTime();
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("text", "");
        String time = prefs.getString("time", "");
        if (restoredText != null && time != null) {
            s = prefs.getString("s", "I'm here at ");
            t = prefs.getString("t", currentTime.toString());
        }
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("s", s + "-I'm here at " );
        editor.putString("t", t + "-" + currentTime.toString() );
        editor.apply();
        Log.i(TAG, "onStartJob: I'm here");
//        EventBus.getDefault().post(new NotifyUpdateEvent(s));
        sendMessage();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i(TAG, "onStopJob: job stopped");
        return true;
    }

    private void sendMessage() {
        Intent intent = new Intent("custom-event-name");
        intent.putExtra("message", s);
        intent.putExtra("time", t);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
