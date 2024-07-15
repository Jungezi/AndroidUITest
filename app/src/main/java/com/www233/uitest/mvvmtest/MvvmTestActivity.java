package com.www233.uitest.mvvmtest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.www233.uitest.R;
import com.www233.uitest.alterButtonList.AlterButtonListView;
import com.www233.uitest.databinding.ActivityMvvmTestBinding;

import java.util.ArrayList;
import java.util.List;

public class MvvmTestActivity extends AppCompatActivity {
    private static final String TAG = "myView";
    MyViewModel myViewModel;
    SharedPreferences preferences;
    ActivityMvvmTestBinding viewDataBinding;
    MyObservable myObservable;
    int button_cnt = 0, op_time = 0;
    LinearLayout linearLayout;
    AlterButtonListView alterButtonListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_mvvm_test);
//        setContentView(R.layout.activity_mvvm_test);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getLifecycle().addObserver(new MyObserver(getLifecycle()));
        initViewModel();

        initButtonList();

//        initButton();
//        initText();


    }


    private void initButtonList() {
        linearLayout = findViewById(R.id.main);
        List<String> list = new ArrayList<>();
        for(;button_cnt < 3; button_cnt++)
        {
            list.add("按钮" + button_cnt);
//            list.add("按钮" );
        }
        alterButtonListView = new AlterButtonListView(this, 4, list);
        linearLayout.addView(alterButtonListView);
        alterButtonListView.setCheck(2);
        alterButtonListView.setOnSelectedButtonChangedListener(new AlterButtonListView.OnSelectedButtonChangedListener() {
            @Override
            public void changed(int position) {
                Log.e(TAG, "changed: 按钮" + position );
            }
        });
    }

    private void initViewModel() {
        preferences = getPreferences(Context.MODE_PRIVATE);
        int num = preferences.getInt("CNT", 0);
        Log.e(TAG, "initViewModel: ");
        myViewModel = new ViewModelProvider(this, new MyViewModelFactory(num)).get(MyViewModel.class);
        myObservable = new MyObservable(num);

        Log.e(TAG, "initViewModel: vm1");
        myViewModel.getCnt().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                TextView tv = findViewById(R.id.tv_cnt);
                tv.setText(String.valueOf(integer));
            }
        }); // viewmodel + livedata

        Log.e(TAG, "initViewModel: vm2");
        viewDataBinding.setVm2(myObservable);       // dataBinding
        viewDataBinding.setListener(new Listener());
//        viewDataBinding.setLifecycleOwner(this);
        Log.e(TAG, "initViewModel: done");
    }

    public void addButton(View view) {
        op_time ++;
        List<String> list = new ArrayList<>();
        list.add(op_time + "按钮" + ++button_cnt);
        alterButtonListView.push(list);

    }

    public void replaceButton(View view) {
        op_time ++;
        button_cnt = 0;
        List<String> list = new ArrayList<>();
        for(;button_cnt < 4; button_cnt++)
        {
            list.add(op_time + "按钮" + button_cnt);
        }
        alterButtonListView.replace(list);
    }

    public void clearButton(View view) {
        op_time ++;
        button_cnt = 0;
        alterButtonListView.clear();
    }

    public void fontplus(View view) {
        alterButtonListView.setText_size(alterButtonListView.getText_size() + 1);
    }

    public void fontminus(View view) {
        alterButtonListView.setText_size(alterButtonListView.getText_size() - 1);
    }

    public void maxplus(View view) {
        alterButtonListView.setMax_size(alterButtonListView.getMax_size() + 1);
    }

    public void maxminus(View view) {
        alterButtonListView.setMax_size(alterButtonListView.getMax_size() - 1);
    }


    public class Listener {
        public void clear() {
            myViewModel.clear();
            myObservable.clear();
        }

        public void plusone() {
            myViewModel.plusone();
            myObservable.plusone();
        }

        public void minusone() {
            myViewModel.minusone();
            myObservable.minusone();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        preferences.edit().putInt("CNT", myObservable.getCnt()).apply();  // 存储计数次数
    }
}