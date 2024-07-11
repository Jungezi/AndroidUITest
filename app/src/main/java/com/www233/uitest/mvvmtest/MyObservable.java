package com.www233.uitest.mvvmtest;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.www233.uitest.BR;


public class MyObservable extends BaseObservable {
    private int cnt;

    public MyObservable(int cnt) {
        this.cnt = cnt;
    }

    @Bindable
    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
        notifyPropertyChanged(BR.cnt);
    }
    public void clear()
    {
        this.cnt = 0;
        notifyPropertyChanged(BR.cnt);
    }

    public void plusone()
    {
        cnt = cnt + 1;
        notifyPropertyChanged(BR.cnt);
    }

    public void minusone()
    {
        cnt = cnt - 1;
        notifyPropertyChanged(BR.cnt);
    }
}
