package com.www233.uitest.multithreadtest;

import android.util.Log;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {
    private static final String TAG = "LockTest";
    static ReentrantLock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();

    @Test
    public void LockForTest() throws InterruptedException {
        // t2线程 因为某条件不满足 进入等待队列
        Thread thread = new Thread(() -> {
            try {
                lock.lock();
                Log.d(TAG, "因为某些条件无法满足，进入等待");
                condition.await();//等待signal唤醒，然后进入等待队列
                Log.d(TAG,"条件满足了被唤醒，开始工作");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Log.d(TAG, "释放锁");
                lock.unlock();
            }
        }, "t2");
        thread.start();
        TimeUnit.MILLISECONDS.sleep(1000);
        lock.lock();
        Log.i(TAG, "LockForTest: 主线程获得锁");
        // 创建5个线程，因为拿不到锁都进入阻塞队列
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    Log.d(TAG,"t" + (finalI + 3) + "线程拿不到锁 进入阻塞队列");
                    lock.lock();
                    Log.d(TAG,"t" + (finalI + 3) + "线程拿到锁，开始工作");
                    TimeUnit.SECONDS.sleep(1);//模拟工作时间2秒
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }, "t" + (i + 3)).start();
            if(i == 3)
                condition.signal();
        }
        TimeUnit.MILLISECONDS.sleep(100);//确保t3 - t7 5个线程都进入阻塞队列
        Log.i(TAG, "LockForTest: 主线程解锁");
        lock.unlock();


    }


}
