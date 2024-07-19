package com.www233.uitest.multithreadtest;

import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MultiThreadCalTask {
    private static final String TAG = "ServiceTask";
    int st, ed, thread;
    List<Integer> cal_list;
    int thread_finish = 0;
    Handler handler;
    List<Integer> cal_result = new ArrayList<>();
    List<Integer> cal_progress = new ArrayList<>();

    public MultiThreadCalTask(List<Integer> cal_list, int thread, Handler handler, CallbackSendMessage task_progress, CallbackSendMessage task_result) {
        this.cal_list = cal_list;
        this.thread = thread;
        this.handler = handler;
        this.task_progress = task_progress;
        this.task_result = task_result;
    }

    private CallbackSendMessage task_progress, task_result;

    public void start() {
        st = 0;
        ed = cal_list.size() - 1;
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(Math.min(thread, 8),
                thread, 600, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(thread),
                new ThreadPoolExecutor.AbortPolicy()
        );
        List<CalRunnable> list = new ArrayList<>();
        int interval = (ed - st + 1) / thread;
        int index_st = st;
        CountDownLatch countDownLatch = new CountDownLatch(thread);
        for (int i = 0; i < thread - 1; i++) {
            list.add(new CalRunnable(index_st, index_st + interval, i, countDownLatch));
            Log.d(TAG, String.format("%d:%d->%d", i, index_st, index_st + interval));
            cal_result.add(0);
            cal_progress.add(0);
            index_st += interval;
        }
        list.add(new CalRunnable(index_st, ed + 1, thread - 1, countDownLatch));
        Log.d(TAG, String.format("%d:%d->%d", thread - 1, index_st, ed + 1));
        cal_result.add(0);
        cal_progress.add(0);
        for (int i = 0; i < thread; i++) {
            threadPoolExecutor.execute(list.get(i));
        }
    }

    public interface CallbackSendMessage {
        void callback(int num);
    }

    class CalRunnable implements Runnable {
        int st, ed;
        int index;
        CountDownLatch countDownLatch;

        public CalRunnable(int st, int ed, int index, CountDownLatch countDownLatch) {
            this.st = st;
            this.ed = ed;
            this.index = index;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            int ans = 0;
            Log.i(TAG, index + ")run");
            for (int i = st; i < ed; i++) {
                ans += cal_list.get(i);

                if ((ed - i - 1) % 10 == 0) {
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    cal_progress.set(index, i - st + 1);
                    int progress = 0;
                    for (int j = 0; j < thread; j++) {
                        progress += cal_progress.get(j);
                    }
                    int finalProgress = progress;
//                    Log.e(TAG, "run: + callback run prev");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
//                            Log.e(TAG, index + ")run: + callback run " + finalProgress);
                            task_progress.callback(finalProgress);
                        }
                    });
                }
            }
            countDownLatch.countDown();
            cal_result.set(index, ans);
            Log.d(TAG, index + ")run: done " + ans);
            if (countDownLatch.getCount() == 0) {
                int all_result = 0;
                for (int j = 0; j < thread; j++) {
                    all_result += cal_result.get(j);
                }
                int finalAll_result = all_result;
                Log.e(TAG, "run: + callback done prev");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, index + ")run: + callback done");
                        task_result.callback(finalAll_result);
                    }
                });
            }
        }
    }
}
