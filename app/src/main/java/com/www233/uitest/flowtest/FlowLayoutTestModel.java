package com.www233.uitest.flowtest;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.MutableLiveData;

import com.www233.uitest.R;

import java.util.ArrayList;
import java.util.List;

public class FlowLayoutTestModel {
    TextDataDao textDataDao;

    public FlowLayoutTestModel(Context context) {

        textDataDao = TextDatabase.getInstance(context).textDataDao();

    }

    public List<TextDataItem> getData_list() {

        return textDataDao.getTextDataItem();
    }

    public void setData_list(List<TextDataItem> data_list) {
        textDataDao.deleteTextDataItem();
        textDataDao.insertTextDataItems(data_list);
    }

    public void addData_list(List<TextDataItem> data_list) {
        textDataDao.insertTextDataItems(data_list);
    }

    public void addData_list(TextDataItem data_list) {
        textDataDao.insertTextDataItem(data_list);
    }

    public void delete() {
        textDataDao.deleteTextDataItem();
    }
}
