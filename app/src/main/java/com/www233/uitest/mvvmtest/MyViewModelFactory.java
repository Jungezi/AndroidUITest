package com.www233.uitest.mvvmtest;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MyViewModelFactory implements ViewModelProvider.Factory{
    int num;

    public MyViewModelFactory(int num) {
        this.num = num;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)(new MyViewModel(num));
    }
}
