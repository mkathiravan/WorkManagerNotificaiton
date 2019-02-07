package com.example.kabali.workmanagernotificaiton;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {

    public static final String key_task_output = "data_outsend";

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        Data data = getInputData();
        String desc = data.getString(MainActivity.key_value);

        try
        {
            Thread.sleep(5000);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        display_notification("This is work manager",desc);


        Data data1 = new Data.Builder().putString(key_task_output,"Task completed").build();


        return Result.success();
    }

    private void display_notification(String task, String desc)
    {
        NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel("workmanagersimple","workmanagersimple",NotificationManager.IMPORTANCE_DEFAULT);

            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),"workmanagersimple")
                .setContentTitle(task)
                .setContentText(desc)
                .setSmallIcon(R.mipmap.ic_launcher);

        notificationManager.notify(1,builder.build());


    }
}
