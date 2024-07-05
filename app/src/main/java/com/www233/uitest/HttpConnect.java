package com.www233.uitest;

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
import okhttp3.Response;

public class HttpConnect extends OkHttpClient {
    OkHttpClient httpClient;
    Map<String, List<Cookie>> cookies = new HashMap<>();
    public HttpConnect() {

        httpClient = new OkHttpClient.Builder()
                .cache(new Cache(new File("W:\\code\\test"), 1024*1024))
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

}
