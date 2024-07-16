package com.www233.uitest.flowtest;

import android.app.ActionBar;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.www233.uitest.R;

import java.util.ArrayList;
import java.util.List;

public class FlowLayoutTestActivity extends AppCompatActivity {

    FlowLayout fl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_flow_layout_test);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fl = findViewById(R.id.fl);
    }

    char alpha = 'A';
    int size = 10;
    public void addToView(View view) {

        fl.addItem("友人" + alpha, size);
        alpha ++;
        size ++;
        if(alpha>'Z')alpha = 'A';
        if(size > 25) size = 10;
    }
    public void removeFromView(View view) {
        fl.removeAllItems();
    }

    public void addToViewAll(View view) {
        List<String> list = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        list.add("烂橘子");
        list.add("黎明");
        list.add("在白纸上写了答案的费尔马定理");
        list.add("BlingBlingBling");
        list.add("环球旅行1天");
        list2.add(25);
        list2.add(18);
        list2.add(14);
        list2.add(22);
        list2.add(10);
        fl.addItemList(list, list2);
    }

    int gravity = 0;
    public void changeAlign(View view) {
        fl.setLayout_gravity(gravity);
        gravity = (gravity + 1 )%3;
    }

    public void limitMinus(View view) {
        fl.setMax_line(fl.getMax_line() - 1);
    }

    public void limitPlus(View view) {
        fl.setMax_line(fl.getMax_line() + 1);
    }
}