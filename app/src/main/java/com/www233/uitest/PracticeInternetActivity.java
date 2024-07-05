package com.www233.uitest;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PracticeInternetActivity extends AppCompatActivity {
    private static final String TAG = "Practice";
    HttpConnect okHttpClient;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_practice_internet);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        uiInit();

        httpInit();
    }

    private void uiInit() {
        tv = findViewById(R.id.tv_wanandroid);
    }

    private void httpInit() {

        okHttpClient = new HttpConnect(new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String str = msg.obj.toString();
                tv.setText(str);
            }
        });
    }

    public void getSync(View view) {
        okHttpClient.getSync("https://httpbin.org/get?a=1&b=2");
    }

    public void getAsync(View view) {
        okHttpClient.getAsync("https://httpbin.org/get?a=1&b=2");
    }

    public void postSync(View view) {
        FormBody frombody = new FormBody.Builder().add("a", "2").add("b", "yes").build();
        okHttpClient.postSync("https://httpbin.org/post", frombody);
    }

    public void postAsync(View view) {
        FormBody frombody = new FormBody.Builder().add("a", "2").add("b", "yes").build();
        okHttpClient.postAsync("https://httpbin.org/post", frombody);

    }

    public void uploadFile(View view) {
        JSONObject json = new JSONObject();
        try {
            json.put("a", "111");
            json.put("b", "wewewewew");
            json.put("cdcd", "dsadwqsfas");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Log.e(TAG, "run: " + json.toString());
        RequestBody requestBody1 = RequestBody.create(json.toString().getBytes());
        okHttpClient.postAsync("https://httpbin.org/post", requestBody1);

    }

    public void loginTest(View view) {
        FormBody formBody = new FormBody.Builder().add("username", "Jungezi")
                .add("password", "123w654001").build();
        okHttpClient.postAsync("https://www.wanandroid.com/user/login", formBody);

    }

    public void accountList(View view) {
        okHttpClient.getAsync("https://www.wanandroid.com/lg/collect/list/0/json");
    }
}