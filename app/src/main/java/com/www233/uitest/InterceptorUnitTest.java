package com.www233.uitest;

import android.util.Log;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.TestOnly;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InterceptorUnitTest {
    @Test
    public void InterceptorTest() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(new Cache(new File("W:\\code\\test"), 1024*1024)).addInterceptor(new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request build = chain.request().newBuilder().addHeader("os", "android")
                        .addHeader("version", "1.0").build();

                return chain.proceed(build);
            }
        }).build();

        Request build = new Request.Builder().url("https://httpbin.org/get?a=1&b=2").build();
        Call call = okHttpClient.newCall(build);
        try {
            Response execute = call.execute();
            if (execute.body() != null) {
                System.out.println(execute.body().string());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
