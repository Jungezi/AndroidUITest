package com.www233.uitest.multithreadtest;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
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
    TextInputEditText te_st;
    TextInputEditText te_ed;
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
        initNotify();
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
                }

            }
        };
//        handler = new Handler();
    }

    private void initWidget() {
        tv = findViewById(R.id.tv_show);
        tv2 = findViewById(R.id.tv_show2);
        pb = findViewById(R.id.progressBar);
        te_st = findViewById(R.id.te_st);
        te_ed = findViewById(R.id.te_ed);

    }

    Notification notification;
    NotificationManager notificationManager;

    private void initNotify() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notifChannel = new NotificationChannel("nof", "nof", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notifChannel);
        }
    }

    public void sendNotif(View view) {
        notification = new NotificationCompat.Builder(this, "nof")
                .setContentTitle("标题")
                .setContentText("内容")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .build();
        notificationManager.notify(1, notification);
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
        int MAX = Integer.parseInt(te_ed.getText().toString());
        int MIN = Integer.parseInt(te_st.getText().toString());
        int len = MAX - MIN + 1;
        List<Integer> list = new ArrayList<>();
        for (int i = MIN; i <= MAX; i++) list.add(i);
        pb.setMax(len);
        new MultiThreadCalTask(list, 10, handler,
                num -> {
                    pb.setProgress(num, false);
                    tv.setText(String.format("%.2f%% %d/%d", (float) num / MAX * 100, num, len));
                }, ans -> tv.setText(String.format("[done]100%%:ans=%d", ans))
        ).start();

    }


    public void serviceAct(View view) {
        if (service_state) {
            Intent serve = new Intent(this, MyFirstService.class);
            startService(serve);
        } else {
            Intent serve = new Intent(this, MyFirstService.class);
            stopService(serve);

        }
        service_state = !service_state;
    }

    boolean service_state = false;
    int service_num;

    public void lockTest(View view) {
        LockTest lockTest = new LockTest();
        try {
            lockTest.LockForTest();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    class MyServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mb = (MyFirstService.MyBind) service;
            int MAX = Integer.parseInt(te_ed.getText().toString());
            int MIN = Integer.parseInt(te_st.getText().toString());
            mb.startRun(MIN, MAX);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    MyFirstService.MyBind mb;

    public void taskRequest(View view) {

        Toast.makeText(this, "num: " + mb.getState(), Toast.LENGTH_SHORT).show();
    }

    public void taskRun(View view) {
        Intent serve = new Intent(this, MyFirstService.class);
        bindService(serve, new MyServiceConnection(), Context.BIND_AUTO_CREATE);
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

    private void initThread() {
        MyHandlerThread handlerThread = new MyHandlerThread("ThreadName");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };
    }

    class MyHandlerThread extends HandlerThread{

        public MyHandlerThread(String name) {
            super(name);
        }

        public MyHandlerThread(String name, int priority) {
            super(name, priority);
        }

        @Override
        protected void onLooperPrepared() {
            super.onLooperPrepared();
        }


    }

}