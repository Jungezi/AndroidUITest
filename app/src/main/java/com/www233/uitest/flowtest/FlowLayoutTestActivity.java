package com.www233.uitest.flowtest;

import android.app.ActionBar;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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

        TextView tv = createTextView(size, "友人" + alpha);
        fl.addItem(tv);
        alpha ++;
        size ++;
        if(alpha>'Z')alpha = 'A';
        if(size > 25) size = 10;
    }

    @NonNull
    private TextView createTextView(int size, String text) {
        TextView tv = new TextView(FlowLayoutTestActivity.this);
        tv.setTextSize(size);
        tv.setText(text);
        tv.setBackground(getResources().getDrawable(R.color.grey_heavy, getTheme()));
        tv.setTextColor(getResources().getColor(R.color.white_approx, getTheme()));
        return tv;
    }

    public void removeFromView(View view) {
        fl.removeAllItems();
    }

    public void addToViewAll(View view) {
        List<String> list = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        List<View> tv = new ArrayList<>();
        tv.add(createTextView(25,"烂橘子"));
        tv.add(createTextView(18,"黎明"));
        tv.add(createTextView(14,"在白纸上写了答案的费尔马定理"));
        tv.add(createTextView(22,"BlingBlingBling"));
        tv.add(createTextView(10,"环球旅行1天"));
        fl.addItemList(tv);
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