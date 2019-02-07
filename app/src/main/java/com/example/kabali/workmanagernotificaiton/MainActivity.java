package com.example.kabali.workmanagernotificaiton;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

public class MainActivity extends AppCompatActivity {

    public static final String key_value = "data_send";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Passing the data to worker
        Data data = new Data.Builder().putString(key_value,"This is my work data").build();

        // This is the constraints to set the notification when the device is charging.
        Constraints constraints = new Constraints.Builder().setRequiresCharging(true).build();


        final OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setInputData(data)
                .setConstraints(constraints)
                .addTag("test_work")
                .build();

        
        // To set the peroidicwork request

//        final PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(MyWorker.class, 1, TimeUnit.MINUTES)
//                .addTag("test_work")
//                .build();
//
//        PeriodicWorkRequest.Builder myWorkBuilder =
//                new PeriodicWorkRequest.Builder(MyWorker.class, 2, TimeUnit.MINUTES);
//
//        final PeriodicWorkRequest myWork = myWorkBuilder.build();
//        WorkManager.getInstance()
//                .enqueueUniquePeriodicWork("jobTag", ExistingPeriodicWorkPolicy.REPLACE, myWork);

       final UUID workId = request.getId();


        findViewById(R.id.perform_work).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WorkManager.getInstance().enqueue(request);

                //This is the peridoicwork request

//                WorkManager.getInstance().enqueueUniquePeriodicWork("jobTag", ExistingPeriodicWorkPolicy.KEEP, myWork);

            }
        });

        // To cancel the work order enqueue

        findViewById(R.id.cancel_work).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // WorkManager.getInstance().cancelWorkById(workId);

                WorkManager.getInstance().cancelAllWorkByTag("test_work");
            }
        });


        final TextView textView = findViewById(R.id.textView);

        WorkManager.getInstance().getWorkInfoByIdLiveData(request.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {

                        //UpdatDownloaded executables cannot execute on hoste the status of the work manager into textview
                        String status = workInfo.getState().name();

                        textView.append(status + "\n");


                    }
                });

    }
}
