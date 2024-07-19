package com.www233.uitest.multithreadtest;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.www233.uitest.MainActivity;
import com.www233.uitest.R;

import java.util.ArrayList;
import java.util.List;


public class MyFirstService extends Service {
    private static final String TAG = "ServiceTask";
    MyBind mybind = new MyBind();
    Handler handler = new Handler();
    public MyFirstService() {
    }

    NotificationManager notificationManager;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notifChannel = new NotificationChannel("nofore", "nof", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notifChannel);
        }

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent activity = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);
        Notification notification = new NotificationCompat.Builder(this, "nofore")
                .setContentTitle("前台服务标题")
                .setContentText("前台服务内容")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(activity)
                .build();
        startForeground(2, notification);
        Log.i(TAG, "onCreate: 前台服务");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }



    @Override
    public IBinder onBind(Intent intent) {
        return mybind;
    }

    class MyBind extends Binder{

        int ans = 0;
        void startRun(int MIN, int MAX){
            Log.i(TAG, "Start MyBind. ");
            List<Integer> list = new ArrayList<>();
            for(int i = MIN;i<=MAX;i++)list.add(i);
            new MultiThreadCalTask(list, 10, handler, this::setAns, this::show).start();
        }

        private void setAns(int ans){
            this.ans = ans;
        }

        private void show(int ans) {
            this.ans = ans;
            Notification notification = new NotificationCompat.Builder(MyFirstService.this, "nof")
                    .setContentTitle("后台任务")
                    .setContentText("执行结束！！！ans:" + ans)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .build();
            notificationManager.notify(1, notification);
        }


        int getState(){
            return ans;
        }

    }


}