package com.www233.uitest.multithreadtest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MultiThreadTestActivity extends AppCompatActivity {

    private static final String TAG = "MultiThreadTestActivity";

    TextView tv;
    TextView tv2;
    ProgressBar pb;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_multi_thread_test);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initWidget();
        initHandler();
    }

    List<Integer> cal_result = new ArrayList<>();
    int cnt = 0, ans = 0, done_num = 0;

    void handle2(int index, int process){
        cnt = cnt - cal_result.get(index) + process;
        cal_result.set(index, process);
        pb.setProgress(cnt, false);
        tv.setText(String.format("%.2f%% -> process = %d(%d)", cnt / 100., process, index));
    }

    void handle3(int obj){
        ans += obj;
        done_num ++;
        if(done_num == 5) {
            tv.setText(String.format("[done]100%%:%d", ans));
            Log.d(TAG, "handle3: done");
        }
    }


    private void initHandler() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                int type = msg.what;
                Object obj = msg.obj;
                switch (type) {
                    case 1: // startTask
                        Log.e(TAG, "handleMessage: 收到！");
                        if (obj instanceof String) {
                            String text = (String) tv2.getText();
                            tv2.setText(String.format("%s %s", text, obj));
                        }
                        break;
                    case 2: // 进度条
                        int index = ((CalVal) obj).num;
                        int process = ((CalVal) obj).process;
                        cnt = cnt - cal_result.get(index) + process;
                        cal_result.set(index, process);
                        pb.setProgress(process, true);
                        tv.setText(String.format("%d%% %d", process / 10000, process));
                        break;
                    case 3: // 结束进度条计算
                        ans += (int) obj;
                        done_num ++;
                        if(done_num == 5)tv.setText(String.format("[done]100%%:%d", ans));
                        break;
                }

            }
        };
//        handler = new Handler();
    }

    private void initWidget() {
        tv = findViewById(R.id.tv_show);
        tv2 = findViewById(R.id.tv_show2);
        pb = findViewById(R.id.progressBar);

    }

    public void startTask(View view) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 4, 1, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(2),
                new ThreadPoolExecutor.DiscardPolicy()
        );
        List<MyRunnable> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            list.add(new MyRunnable("线程池执行子线程 " + i));
        }
        for (int i = 0; i < 8; i++) {
            threadPoolExecutor.execute(list.get(i));
            Log.i(TAG, "startTask: for " + i);
        }
        threadPoolExecutor.shutdown();
        Log.d(TAG, "startTask: 结束执行");
    }

    public void startTaskBar(View view) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(6, 16, 10, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(16),
                new ThreadPoolExecutor.AbortPolicy()
        );
        List<CalRunnable> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new CalRunnable(i * 2000, (i + 1) * 2000, i));
            cal_result.add(0);
        }
        for (int i = 0; i < 5; i++) {
            threadPoolExecutor.execute(list.get(i));
        }

    }

    static class CalVal {
        public CalVal(int num, int process) {
            this.num = num;
            this.process = process;
        }

        int num;

        public void setProcess(int process) {
            this.process = process;
        }

        int process;
    }

    class CalRunnable implements Runnable {
        int st, ed;
        int num;

        public CalRunnable(int st, int ed, int num) {
            this.st = st;
            this.ed = ed;
            this.num = num;
        }



        @Override
        public void run() {
            int ans = 0;
            Log.i(TAG, num + ")run");
            Message msg = new Message();
            msg.what = 2;
            CalVal cv = new CalVal(num, 0);
            for (int i = st; i < ed; i++) {
                ans += i;

                if((i+1)%5 == 0)
                {
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    cv.setProcess(i);
                    msg.obj = cv;
//                    handler.sendMessage(msg);
                    int finalI = i;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            handle2(num, finalI - st);
                        }
                    });
                }


            }
            msg.what = 3;
            msg.obj = ans;
//            handler.sendMessage(msg);
            int finalAns = ans;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    handle3(finalAns);
                }
            });
            Log.i(TAG, num + ")done");
        }
    }


    class MyRunnable implements Runnable {

        private String msg;

        public MyRunnable(String msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            Log.d(TAG, "run: 计算msg长度:" + msg);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Message mss = new Message();
            mss.obj = msg;
            mss.what = 1;

            Log.d(TAG, "run: 计算msg长度成功!" + this.msg);
            handler.sendMessage(mss);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String text = (String) tv2.getText();
                    tv2.setText(String.format("%s (%s)", text, msg));
                }
            });
        }
    }

}