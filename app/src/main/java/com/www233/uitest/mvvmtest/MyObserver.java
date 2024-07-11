package com.www233.uitest.mvvmtest;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

public class MyObserver implements DefaultLifecycleObserver {

    private static final String TAG = "myView";
    Lifecycle lifecycle;

    public MyObserver(Lifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onCreate(owner);
        Log.e(TAG, "(observer)onCreate: ");
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onStop(owner);
        Log.e(TAG, "(observer)onStop: " );
    }
}
