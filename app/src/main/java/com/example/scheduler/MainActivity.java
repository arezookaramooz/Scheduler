package com.example.scheduler;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        ComponentName componentName = new ComponentName(this, MyJobService.class);
        JobInfo info = new JobInfo.Builder(1, componentName)
                .setPeriodic(5000)
                .build();
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.schedule(info);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "JOb Scheduled");
        } else {
            Log.d(TAG, "Job Scheduling fail");
        }

//        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
//        Job job = createJob(dispatcher);
//        dispatcher.mustSchedule(job);
    }

//    public static Job createJob(FirebaseJobDispatcher dispatcher){
//
//        Log.d(TAG, "createJob: I'm here");
//        Job job = dispatcher.newJobBuilder()
//                .setLifetime(Lifetime.FOREVER)
//                .setService(MyJobService.class)
//                .setTag("UniqueTagForYourJob")
//                .setReplaceCurrent(false)
//                .setRecurring(false)
//                .setTrigger(Trigger.executionWindow(5, 15))  //Trigger.NOW
//                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
////                .setConstraints(Constraint.ON_ANY_NETWORK, Constraint.DEVICE_CHARGING)
//                .build();
//        return job;
//    }





//    @Override
//    public void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        EventBus.getDefault().unregister(this);
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(NotifyUpdateEvent event) {
//        setUpRecyclerView(event.getMessage());
//    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            String time = intent.getStringExtra("time");
            setUpRecyclerView(message, time);
        }
    };

    private void initViews() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        toolbar = findViewById(R.id.tool_bar);

        setSupportActionBar(toolbar);
        Button clearButton = findViewById(R.id.clear_butoon);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                prefs.edit().clear().commit();
            }
        });
    }

    private void setUpRecyclerView(String message, String time) {

        Adapter adapter = new Adapter(this, getArrayFromString(message), getArrayFromString(time));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged();
    }

    private ArrayList<String> getArrayFromString(String s) {
        ArrayList<String> members = new ArrayList<>();

        String[] array = s.split("-");

        for (int i = 0; i < array.length; i++) {
            members.add(array[i]);
        }
        return members;
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy is called");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}