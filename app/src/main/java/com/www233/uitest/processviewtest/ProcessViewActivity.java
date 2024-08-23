package com.www233.uitest.processviewtest;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.eekidu.dev.devlayout.DevLayout;
import com.github.eekidu.dev.devlayout.widget.EditorTextLayout;
import com.github.eekidu.dev.devlayout.widget.RadioGroupLayout;
import com.github.eekidu.dev.devlayout.widget.SeekBarLayout;
import com.www233.uitest.R;

import java.util.ArrayList;
import java.util.List;

public class ProcessViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_process_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initProcessView();
        initDevLayout();
    }

    ProcessView processView;
    private void initDevLayout() {
        DevLayout devLayout = findViewById(R.id.devLayout);
        devLayout.addSeekBar("条儿高度", new SeekBarLayout.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(int i) {
                processView.setBarHeight(i);
            }
        }).setMin(5).setMax(30);
        devLayout.addSeekBar("三角形高度", new SeekBarLayout.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(int i) {
                processView.setIndicatorSize(i);
            }
        }).setMin(5).setMax(30);
        devLayout.addSeekBar("字体大小", new SeekBarLayout.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(int i) {
                processView.setTextSize(i);
            }
        }).setMin(5).setMax(30);
        devLayout.addSeekBar("字体下方间距", new SeekBarLayout.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(int i) {
                processView.setTextMargin(i);
            }
        }).setMin(1).setMax(10);
        devLayout.addSeekBar("当前进度", new SeekBarLayout.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(int i) {
                processView.setCurrentProcess(i/100f);
            }
        }).setMin(0).setMax(10000);
        devLayout.addSeekBar("设置高度", new SeekBarLayout.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(int i) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) processView.getLayoutParams();
                lp.height = i;
                processView.setLayoutParams(lp);
            }
        }).setMin(100).setMax(300);
        devLayout.addSeekBar("设置宽度", new SeekBarLayout.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(int i) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) processView.getLayoutParams();
                lp.width = i;
                processView.setLayoutParams(lp);
            }
        }).setMin(300).setMax(900);
        devLayout.addButton("条儿高度自动", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processView.setBarHeight(-1);
            }
        });
        devLayout.addButton("三角形高度自动", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processView.setIndicatorSize(-1);
            }
        });
        devLayout.addButton("字体大小自动", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processView.setTextSize(-1);
            }
        });
        devLayout.addButton("字体间距默认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processView.setTextMargin(-1);
            }
        });
        devLayout.addButton("适应高度", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) processView.getLayoutParams();
                lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                processView.setLayoutParams(lp);
            }
        });
        devLayout.addButton("适应宽度", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) processView.getLayoutParams();
                lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
                processView.setLayoutParams(lp);
            }
        });

        devLayout.hr();
        devLayout.addSeekBar("新增的长度", new SeekBarLayout.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(int i) {
                length = i;
            }
        }).setMin(10).setMax(100);
        devLayout.addRadioGroup("新增的颜色")
                        .addItem("红色", new RadioGroupLayout.OnItemCheckListener() {
                            @Override
                            public void onSelect() {
                                colorRes = getColor(R.color.red);
                            }
                        })
                        .addItem("蓝色", new RadioGroupLayout.OnItemCheckListener() {
                            @Override
                            public void onSelect() {
                                colorRes = getColor(R.color.blue);
                            }
                        })
                        .addItem("橘色", new RadioGroupLayout.OnItemCheckListener() {
                            @Override
                            public void onSelect() {
                                colorRes = getColor(R.color.orange);
                            }
                        })
                        .addItem("灰色", new RadioGroupLayout.OnItemCheckListener() {
                            @Override
                            public void onSelect() {
                                colorRes = getColor(R.color.grey_heavy);
                            }
                        }).setChecked(0);
        devLayout.addButton("添加", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processView.addProcess(new ProcessView.Part(length, colorRes));
            }
        });
        devLayout.addButton("移除", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processView.removeProcess(processView.getProcessList().size() - 1);
            }
        });

    }

    int length = 0;
    int colorRes = 0;
    private void initProcessView() {
        processView = findViewById(R.id.processView);
        List<ProcessView.Part> process = new ArrayList<>();
        process.add(new ProcessView.Part(30,getColor(R.color.red)));
        process.add(new ProcessView.Part(40,getColor(R.color.blue)));
        process.add(new ProcessView.Part(30,getColor(R.color.orange)));
        processView.setProcessList(process);
        processView.setCurrentProcess(20);
    }



}