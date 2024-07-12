package com.www233.uitest.mvvmtest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.www233.uitest.R;
import com.www233.uitest.alterButtonList.AlterButtonListView;
import com.www233.uitest.alterButtonList.OnSelectedButtonChangedListener;
import com.www233.uitest.databinding.ActivityMvvmTestBinding;

public class MvvmTestActivity extends AppCompatActivity {
    private static final String TAG = "myView";
    MyViewModel myViewModel;
    SharedPreferences preferences;
    ActivityMvvmTestBinding viewDataBinding;
    MyObservable myObservable;

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
        AlterButtonListView alterButtonListView = findViewById(R.id.blv);
        alterButtonListView.setButtonText(0, "天地")
                .setButtonText(1, "银行")
                .setButtonText(2, "地府");

        alterButtonListView.setCheck(1);
        alterButtonListView.setCheck(3);

        alterButtonListView.setOnSelectedButtonChangedListener(new OnSelectedButtonChangedListener() {
            @Override
            public void changed(int position) {
                Toast.makeText(MvvmTestActivity.this,String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViewModel() {
        preferences = getPreferences(Context.MODE_PRIVATE);
        int num = preferences.getInt("CNT", 0);
        Log.e(TAG, "initViewModel: ");
        myViewModel = new ViewModelProvider(this,new MyViewModelFactory(num)).get(MyViewModel.class);
        myObservable = new MyObservable(num);

        Log.e(TAG, "initViewModel: vm1" );
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