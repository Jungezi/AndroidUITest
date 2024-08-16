package com.www233.uitest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.www233.uitest.charttest.PracticeChartActivity;
import com.www233.uitest.ellipsizetest.EllipsizeTestActivity;
import com.www233.uitest.flowtest.FlowLayoutTestActivity;
import com.www233.uitest.httptest.PracticeInternetActivity;
import com.www233.uitest.multithreadtest.MultiThreadTestActivity;
import com.www233.uitest.mvvmtest.MvvmTestActivity;
import com.www233.uitest.plottest.PlotActivity;
import com.www233.uitest.viewtest.MyViewActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    public void toImitate(View view) {
        Log.e(TAG, "toImitate: UI1" );
        Intent intent = new Intent(this, ImitateActivity.class);
        Log.e(TAG, "toImitate: UI1 done." );
        startActivity(intent);
    }

    public void toPractice1(View view) {
        Intent intent = new Intent(this, PracticeChartActivity.class);
        startActivity(intent);
    }

    public void toPractice2(View view) {
        Intent intent = new Intent(this, PracticeInternetActivity.class);
        startActivity(intent);
    }

    public void myView(View view) {
        Intent intent = new Intent(this, MyViewActivity.class);
        startActivity(intent);

    }

    public void toMvvm(View view) {
        Intent intent = new Intent(this, MvvmTestActivity.class);
        startActivity(intent);
    }

    public void toFlowLayout(View view) {
        Intent intent = new Intent(this, FlowLayoutTestActivity.class);
        startActivity(intent);
    }

    public void toMulti(View view) {
        Intent intent = new Intent(this, MultiThreadTestActivity.class);
        startActivity(intent);

    }

    public void toEllipsizeView(View view) {
        Intent intent = new Intent(this, EllipsizeTestActivity.class);
        startActivity(intent);
    }

    public void toPlotTest(View view) {
        Intent intent = new Intent(this, PlotActivity.class);
        startActivity(intent);
    }
}