package com.www233.uitest.httptest;

import androidx.annotation.NonNull;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

public class CookieUnitTest {
    Map<String, List<Cookie>> cookies = new HashMap<>();

    @Test
    public void cookieTest() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
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

        FormBody formBody = new FormBody.Builder().add("username", "Jungezi")
                .add("password", "123w654001").build();
        Request build = new Request.Builder().url("https://www.wanandroid.com/user/login").post(formBody).build();
        Call call = okHttpClient.newCall(build);

        Response response = null;
        try {
            response = call.execute();
            if(response.isSuccessful())
            {
                if (response.body() != null) {
                    System.out.println(response.body().string());
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        build = new Request.Builder().url("https://www.wanandroid.com/lg/collect/list/0/json").build();
        call = okHttpClient.newCall(build);

        response = null;
        try {
            response = call.execute();
            if(response.isSuccessful())
            {
                if (response.body() != null) {
                    System.out.println(response.body().string());
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
