package com.www233.uitest.plottest;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.www233.uitest.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlotActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_plot);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initLineChart(0);
    }

    private void initLineChart(int seed) {
        LineChart lineChart = findViewById(R.id.lineChart);
        List<Float> x = new ArrayList<>();
        List<Float> y = new ArrayList<>();
        Random rand = new Random(seed);
        for (int i = -20; i <= 120; i+=10) {
            x.add((float) i);
            y.add((rand.nextFloat())*100);
        }
        lineChart.setData(x, y, -20, 0, 120, 100);
    }

    Random rand = new Random(233);
    public void reset(View view) {
        initLineChart(rand.nextInt());
    }
}