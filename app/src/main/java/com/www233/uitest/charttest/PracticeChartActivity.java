package com.www233.uitest.charttest;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.www233.uitest.R;

import java.util.ArrayList;
import java.util.List;

public class PracticeChartActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Practice";
    private static final int row_item_num = 3;   //每行显示数量
    List<TextView> view_text = new ArrayList<>();
    List<Fragment> fragment_list = new ArrayList<>();

    List<PracticeChartInfoItem> InfoList = new ArrayList<>();
    private PracticeChartRecyclerViewAdapter myAdapter;
    private RecyclerView mRecyclerView;
    ViewPager2 view_pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_practive_chart);

        setTableInfo();     // Recycler创建

        initListButton();   // 练习fragment

    }

    private void setTableInfo() {

        // 值赋给InfoList
        for (int i = 0; i < 8; i++) {
            PracticeChartInfoItem info = new PracticeChartInfoItem("该项的名字", "数值");
            InfoList.add(info);
        }
        PracticeChartInfoItem info = new PracticeChartInfoItem("发债企业", "美团", getResources().getColor(R.color.blue, getTheme()));
        InfoList.add(info);

        mRecyclerView = findViewById(R.id.recyclerView);
        myAdapter = new PracticeChartRecyclerViewAdapter(InfoList);
        Log.e(TAG, "创建Adapter");
        mRecyclerView.setAdapter(myAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(PracticeChartActivity.this, row_item_num);
        mRecyclerView.setLayoutManager(gridLayoutManager);
    }

    private void initListButton() {
        view_text.add(findViewById(R.id.view2_tv1));
        view_text.add(findViewById(R.id.view2_tv2));
        view_text.add(findViewById(R.id.view2_tv3));
        view_text.add(findViewById(R.id.view2_tv4));
        view_text.add(findViewById(R.id.view2_tv5));
        view_text.add(findViewById(R.id.view2_tv6));
        for (TextView i : view_text) {
            i.setOnClickListener(this);
        }
        refreshTextColor(0);

        Log.e(TAG, "initListButton: 创建fragment");

        PracticeFragment2 practiceFragment2 = new PracticeFragment2();
        practiceFragment2.setFragmentcallback(new IFragmentCallback() {
            @Override
            public void sendToActivity(String string) {
                Toast.makeText(PracticeChartActivity.this, string, Toast.LENGTH_SHORT).show();
            }

            @Override
            public String getFromActivity(String string) {
                return "传给fragment！！！";
            }
        });

        fragment_list.add(PracticeFragment.newInstance("第一个！！", getResources().getColor(R.color.orange, getTheme())));
        fragment_list.add(practiceFragment2);
        fragment_list.add(PracticeFragment.newInstance("第333个！！", getResources().getColor(R.color.green_light, getTheme())));
        fragment_list.add(PracticeFragment.newInstance("第4444个！！", getResources().getColor(R.color.white_approx, getTheme())));
        fragment_list.add(PracticeFragment.newInstance("第wwwww个！！", getResources().getColor(R.color.red_light, getTheme())));
        fragment_list.add(PracticeFragment.newInstance("第666666个！！", getResources().getColor(R.color.grey, getTheme())));


        view_pager = findViewById(R.id.viewpager);
        Log.e(TAG, "initListButton: 创建adapter");
        view_pager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(), getLifecycle(), fragment_list));
        Log.e(TAG, "initListButton: 创建结束");
        view_pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                refreshTextColor(position);
            }
        });


    }


    public void refreshTextColor(int index) {   // 选项颜色
        for (TextView i : view_text) {
            i.setTextColor(getResources().getColor(R.color.grey_heavy, getTheme()));
            i.setBackgroundColor(getResources().getColor(R.color.white, getTheme()));
        }
        view_text.get(index).setTextColor(getResources().getColor(R.color.blue, getTheme()));
        view_text.get(index).setBackgroundColor(getResources().getColor(R.color.grey, getTheme()));
    }

    // 学习fragment
    @Override
    public void onClick(View v) {
        int id = v.getId();
        int index = 0;

        if (id == R.id.view2_tv1) {
            index = 0;
        } else if (id == R.id.view2_tv2) {
            index = 1;
        } else if (id == R.id.view2_tv3) {
            index = 2;
        } else if (id == R.id.view2_tv4) {
            index = 3;
        } else if (id == R.id.view2_tv5) {
            index = 4;
        } else if (id == R.id.view2_tv6) {
            index = 5;
        }

        refreshTextColor(index);
        view_pager.setCurrentItem(index, true);


    }

    int cnt = 0;
    List<PracticeFragment> fragment_list_2 = new ArrayList<>();

    public void add_fragment(View view) {

        PracticeFragment fragment_dir = PracticeFragment.newInstance("创建fragment的" + (cnt++), getResources().getColor(R.color.green_light, getTheme()));
        fragment_list_2.add(fragment_dir);
        getSupportFragmentManager().beginTransaction().add(R.id.fl, fragment_dir).commit();
    }

    public void remove_fragment(View view) {
        if (cnt > 0) {
            getSupportFragmentManager().beginTransaction().remove(fragment_list_2.get(--cnt)).commit();
            fragment_list_2.remove(cnt);
        }
    }

    public void replace_fragment(View view) {
        cnt = 0;
        PracticeFragment fragment_dir = PracticeFragment.newInstance("重头再来的fragment!" + (cnt++), getResources().getColor(R.color.red_light, getTheme()));
        fragment_list_2.clear();
        fragment_list_2.add(fragment_dir);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl, fragment_dir).commit();
    }
}