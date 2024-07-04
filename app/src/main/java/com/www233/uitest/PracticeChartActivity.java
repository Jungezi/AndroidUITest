package com.www233.uitest;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class PracticeChartActivity extends AppCompatActivity {

    private static final String TAG = "Practice";
    List<TextView> view_text = new ArrayList<>();
    List<Fragment> fragment_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_practive_chart);


        initListButton();

    }

    private void initListButton() {
        view_text.add(findViewById(R.id.view2_tv1));
        view_text.add(findViewById(R.id.view2_tv2));
        view_text.add(findViewById(R.id.view2_tv3));
        view_text.add(findViewById(R.id.view2_tv4));
        view_text.add(findViewById(R.id.view2_tv5));
        view_text.add(findViewById(R.id.view2_tv6));
        for (TextView i : view_text) {
//            i.setOnClickListener(this);
            i.setTextColor(getResources().getColor(R.color.grey_heavy, getTheme()));
            i.setBackgroundColor(getResources().getColor(R.color.white, getTheme()));
        }
//        replaceFragment(new PracticeFragment1());
        view_text.get(0).setTextColor(getResources().getColor(R.color.blue, getTheme()));
        view_text.get(0).setBackgroundColor(getResources().getColor(R.color.grey, getTheme()));

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

        fragment_list.add(new PracticeFragment1());
        fragment_list.add(practiceFragment2);
        fragment_list.add(new PracticeFragment1());
        fragment_list.add(new PracticeFragment1());
        fragment_list.add(new PracticeFragment1());
        fragment_list.add(new PracticeFragment1());


        ViewPager2 view_pager = findViewById(R.id.viewpager);
        Log.e(TAG, "initListButton: 创建adapter");
        view_pager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(), getLifecycle(), fragment_list));
        Log.e(TAG, "initListButton: 创建结束");


    }

//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//        for(TextView i : view_text)
//        {
//            i.setTextColor(getResources().getColor(R.color.grey_heavy, getTheme()));
//            i.setBackgroundColor(getResources().getColor(R.color.white, getTheme()));
//        }
//        if (id == R.id.view2_tv1) {
//            Bundle bundle = new Bundle();
//            bundle.putString("text", "传递参数！");
//            PracticeFragment1 practiceFragment1 = new PracticeFragment1();
//            practiceFragment1.setArguments(bundle);
//            replaceFragment(practiceFragment1);
//            view_text.get(0).setTextColor(getResources().getColor(R.color.blue, getTheme()));
//            view_text.get(0).setBackgroundColor(getResources().getColor(R.color.grey, getTheme()));
//        } else if (id == R.id.view2_tv2) {
//            PracticeFragment2 practiceFragment2 = new PracticeFragment2();
//            practiceFragment2.setFragmentcallback(new IFragmentCallback() {
//                @Override
//                public void sendToActivity(String string) {
//                    Toast.makeText(PracticeChartActivity.this, string, Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public String getFromActivity(String string) {
//
//                    return "传给fragment！！！";
//                }
//            });
//
//            replaceFragment(practiceFragment2);
//            view_text.get(1).setTextColor(getResources().getColor(R.color.blue, getTheme()));
//            view_text.get(1).setBackgroundColor(getResources().getColor(R.color.grey, getTheme()));
//        }
//    }
//
//    private void replaceFragment(Fragment fragment) {
//        FragmentManager supportFragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.viewpager, fragment);
////        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
//    }
}