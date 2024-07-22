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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.www233.uitest.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FlowLayoutTestActivity extends AppCompatActivity {

    FlowLayout fl;
    FlowLayoutTestViewModel flowLayoutTestViewModel;

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
        initViewModel();
    }

    private void initViewModel() {
        flowLayoutTestViewModel = new ViewModelProvider(this, new FlowLayoutTestModelFactory(this))
                .get(FlowLayoutTestViewModel.class);

        flowLayoutTestViewModel.getData_list().observe(this, new Observer<List<TextDataItem>>() {
            @Override
            public void onChanged(List<TextDataItem> textDataItems) {

                fl.removeAllItems();
                List<View> views = new ArrayList<>();
                for (TextDataItem item : textDataItems) {
                    views.add(createTextView(item.data_size, item.data_content));
                }
                fl.addItemList(views);
            }
        });

        flowLayoutTestViewModel.getAlign_type().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                fl.setLayout_gravity(integer);
            }
        });

        flowLayoutTestViewModel.getLine_limit().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                fl.setMax_line(integer);
            }
        });
    }

    char alpha = 'A';
    int size = 10;

    public void addToView(View view) {

        flowLayoutTestViewModel.addData("友人" + alpha, size);
        alpha++;
        size++;
        if (alpha > 'Z') alpha = 'A';
        if (size > 25) size = 10;
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

    public void removeAllView(View view) {
        flowLayoutTestViewModel.clear();
    }

    public void addToViewAll(View view) {
        List<TextDataItem> data_list = new ArrayList<>();
        data_list.add(new TextDataItem("烂橘子", 25));
        data_list.add(new TextDataItem("黎明", 18));
        data_list.add(new TextDataItem("在白纸上写了答案的费尔马定理", 14));
        data_list.add(new TextDataItem("BlingBlingBling", 22));
        data_list.add(new TextDataItem("环球旅行1天", 10));
        flowLayoutTestViewModel.addData(data_list);
    }

    int gravity = 0;

    public void changeAlign(View view) {
        flowLayoutTestViewModel.setAlign_type(gravity);
        gravity = (gravity + 1) % 3;
    }

    public void limitMinus(View view) {
        flowLayoutTestViewModel.setLine_limit(flowLayoutTestViewModel.getLine_limit().getValue() - 1);
    }

    public void limitPlus(View view) {
        flowLayoutTestViewModel.setLine_limit(flowLayoutTestViewModel.getLine_limit().getValue() + 1);
    }
}