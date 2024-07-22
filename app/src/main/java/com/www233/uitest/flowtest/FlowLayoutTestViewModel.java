package com.www233.uitest.flowtest;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.www233.uitest.R;

import java.util.ArrayList;
import java.util.List;

public class FlowLayoutTestViewModel extends ViewModel {
    private MutableLiveData<List<TextDataItem>> data_list;
    private MutableLiveData<Integer> line_limit = new MutableLiveData<>(5);
    private MutableLiveData<Integer> align_type = new MutableLiveData<>(1);
    FlowLayoutTestModel flowLayoutTestModel;

    public FlowLayoutTestViewModel(Context context) {
        flowLayoutTestModel = new FlowLayoutTestModel(context);
        data_list = new MutableLiveData<>(flowLayoutTestModel.getData_list());
    }


    public LiveData<Integer> getLine_limit() {
        return line_limit;
    }

    public LiveData<Integer> getAlign_type() {
        return align_type;
    }

    public void setLine_limit(int line_limit) {
        this.line_limit.postValue(line_limit);
    }

    public void setAlign_type(int align_type) {
        this.align_type.postValue(align_type);
    }

    public LiveData<List<TextDataItem>> getData_list() {
        return data_list;
    }

    private void setData_list(List<TextDataItem> data_list) {
        this.data_list.postValue(data_list);
    }

    public void addData(String add_data, int add_size)
    {
        List<TextDataItem> data_prev = data_list.getValue();
        TextDataItem textDataItem = new TextDataItem(add_data, add_size);
        data_prev.add(textDataItem);
        flowLayoutTestModel.addData_list(textDataItem);
        setData_list(data_prev);
    }
    public void addData(List<TextDataItem> add_data)
    {
        List<TextDataItem> data_prev = data_list.getValue();
        data_prev.addAll(add_data);
        flowLayoutTestModel.addData_list(add_data);
        setData_list(data_prev);
    }

    public void clear()
    {
        flowLayoutTestModel.delete();
        setData_list(new ArrayList<>());
    }
}
