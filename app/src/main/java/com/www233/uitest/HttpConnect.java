package com.www233.uitest;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HttpConnect extends OkHttpClient {
    OkHttpClient httpClient;
    Map<String, List<Cookie>> cookies = new HashMap<>();
    private Handler mHandler ;

    public HttpConnect(Handler mHandler) {
        this.mHandler = mHandler;

        httpClient = new OkHttpClient.Builder()
                .cache(new Cache(new File("W:\\code\\test.txt"), 1024*1024))
                .addInterceptor(new Interceptor() {
                    @NonNull
                    @Override
                    public Response intercept(@NonNull Chain chain) throws IOException {
                        Request build = chain.request().newBuilder().addHeader("os", "android")
                                .addHeader("version", "1.0").build();

                        return chain.proceed(build);
                    }
                }).cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(@NonNull HttpUrl httpUrl, @NonNull List<Cookie> list) {
                        cookies.put(httpUrl.host(), list);
                    }

                    @NonNull
                    @Override
                    public List<Cookie> loadForRequest(@NonNull HttpUrl httpUrl) {
                        List<Cookie> List = cookies.get(httpUrl.host());
                        if(List != null)
                            return List;
                        else
                            return new ArrayList<>();
                    }
                }).build();
    }


    public void getSync(String url) {
        new Thread() {
            @Override
            public void run() {
                Request build = new Request.Builder().url(url).build();
                Call call = httpClient.newCall(build);
                try {
                    Response execute = call.execute();
                    if (execute.body() != null) {
                        String str = execute.body().string();
                        System.out.println("getSync: " + str);

                        Message message = new Message();
                        message.what = 0;
                        message.obj = str;

                        mHandler.sendMessage(message);

                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();

    }


    public void getAsync(String url, int type) {
        new Thread() {
            @Override
            public void run() {
                Request build = new Request.Builder().url(url).build();
                Call call = httpClient.newCall(build);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String str = null;
                            if (response.body() != null) {
                                str = response.body().string();
                                System.out.println("getAsync: " + str);

                                Message message = new Message();
                                message.what = type;
                                message.obj = str;

                                mHandler.sendMessage(message);
                            }
                        }
                    }
                });
            }
        }.start();
    }

    public void postSync(String url, FormBody frombody) {
        new Thread() {
            @Override
            public void run() {
                Request request = new Request.Builder().url(url).post(frombody).build();
                Call call = httpClient.newCall(request);
                try {
                    Response execute = call.execute();
                    if (execute.body() != null) {
                        String str = execute.body().string();

                        Message message = new Message();
                        message.what = 0;
                        message.obj = str;

                        mHandler.sendMessage(message);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();
    }


    public void postAsync(String url, RequestBody frombody) {
        Request request = new Request.Builder().url(url).post(frombody).build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String str = response.body().string();

                        Message message = new Message();
                        message.what = 0;
                        message.obj = str;

                        mHandler.sendMessage(message);
                    }
                }
            }
        });

    }

}
