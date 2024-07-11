package com.www233.uitest.mvvmtest;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.Closeable;

public class MyViewModel extends ViewModel {

    private MutableLiveData<Integer> cnt = new MutableLiveData<>();
    public LiveData<Integer> getCnt() {
        return cnt;
    }
    public Integer getCntValue() {
        return cnt.getValue();
    }


    public MyViewModel( int cnt) {
        this.cnt.setValue(cnt);
    }

    public MyViewModel(int cnt, @NonNull Closeable... closeables) {
        super(closeables);
        this.cnt.setValue(cnt);
    }

    public void clear()
    {
        this.cnt.postValue(0);
    }

    public void plusone()
    {
        int num = cnt.getValue();
        this.cnt.postValue(num + 1);
    }

    public void minusone()
    {
        int num = cnt.getValue();
        this.cnt.postValue(num - 1);
    }


}
