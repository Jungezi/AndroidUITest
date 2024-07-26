package com.www233.uitest.viewtest;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.www233.uitest.R;
import com.www233.uitest.buttonpagescroll.ButtonPageScroll;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MyViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initButton();
        initRecycler();

    }

    private void initRecycler() {
        List<View> list = new ArrayList<>();
        TextView tv;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(20, 30);
        lp2.setMargins(5,5,5,5);
        for(int i = 0;i<43 ;i++)
        {
            tv = new TextView(this);
            tv.setText("text");
            tv.setBackgroundColor(getColor(R.color.blue));
            tv.setLayoutParams(lp2);
            list.add(tv);
        }
        ButtonPageScroll buttonPageScroll = new ButtonPageScroll(this,list);
        buttonPageScroll.setLayoutParams(lp);
        buttonPageScroll.setBackgroundColor(getColor(R.color.green_light));

        LinearLayout view = findViewById(R.id.main);
        view.addView(buttonPageScroll);
    }

    private void initButton() {
        MyButtonView mbv = findViewById(R.id.mbv1);
        mbv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyViewActivity.this, "click button1!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}