package com.www233.uitest.multithreadtest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyFirstService extends Service {
    private static final String TAG = "MyFirstService";
    MyBind mybind = new MyBind();
    public MyFirstService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
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

        int state = 0;
        void startRun(){
            Log.i(TAG, "Start MyBind. ");
            state = 0;
            run();
        }

        void run() {
            for(;state < 100; state ++)
            {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {

                }
            }
        }

        int getState(){
            return state;
        }

    }
}