package com.www233.uitest;

import android.os.Build;
import android.os.Bundle;
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
    OkHttpClient okHttpClient;
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

        okHttpClient = new HttpConnect().httpClient;
    }

    public void getSync(View view) {
        new Thread() {
            @Override
            public void run() {
                Request build = new Request.Builder().url("https://httpbin.org/get?a=1&b=2").build();
                Call call = okHttpClient.newCall(build);
                try {
                    Response execute = call.execute();
                    if (execute.body() != null) {
                        Log.e(TAG, "getSync: " + execute.body().string());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();

    }

    public void getAsync(View view) {
        new Thread() {
            @Override
            public void run() {
                Request build = new Request.Builder().url("https://httpbin.org/get?a=1&b=2").build();
                Call call = okHttpClient.newCall(build);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            Log.e(TAG, "onResponse: Success\n" + response.body().string());
                        } else {
                            Log.e(TAG, "onResponse: failed");
                        }
                    }
                });
            }
        }.start();
    }

    public void postSync(View view) {
        new Thread() {
            @Override
            public void run() {
                FormBody frombody = new FormBody.Builder().add("a", "2").add("b", "yes").build();
                Request request = new Request.Builder().url("https://httpbin.org/post").post(frombody).build();
                Call call = okHttpClient.newCall(request);
                try {
                    Response execute = call.execute();
                    if (execute.body() != null) {
                        String string = execute.body().string();
                        Log.e(TAG, "postSync run: " + string);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();
    }

    public void postAsync(View view) {
        FormBody frombody = new FormBody.Builder().add("a", "2").add("b", "yes").build();
        Request request = new Request.Builder().url("https://httpbin.org/post").post(frombody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.e(TAG, "postAsync onResponse: " + response.body().string());
                    }
                }
            }
        });

    }

    public void uploadFile(View view) {
        new Thread() {
            @Override
            public void run() {
                File file = new File("W:\\code\\test.txt");
                Log.e(TAG, "uploadFile: new file");
                RequestBody requestBody = RequestBody.create(file, MediaType.parse("text/plain"));

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

                Log.e(TAG, "uploadFile: new RequestBody");
//                MultipartBody multipartBody = new MultipartBody.Builder().addFormDataPart("file", file.getName(), requestBody).build();
                Log.e(TAG, "uploadFile: new MultipartBody");
                Request build = new Request.Builder().url("https://httpbin.org/post").post(requestBody1).build();
                Call call = okHttpClient.newCall(build);
                try {
                    Log.e(TAG, "uploadFile: start execute");
                    Response execute = call.execute();
                    Log.e(TAG, "uploadFile: execute done.");
                    if (execute.body() != null) {
                        Log.e(TAG, "uploadFile: " + execute.body().string());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();


    }

    public void loginTest(View view) {

        new Thread() {
            @Override
            public void run() {
                FormBody formBody = new FormBody.Builder().add("username", "Jungezi")
                        .add("password", "123w654001").build();
                Request build = new Request.Builder().url("https://www.wanandroid.com/user/login").post(formBody).build();
                Call call = okHttpClient.newCall(build);

                Response response = null;
                try {
                    response = call.execute();
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            String str = response.body().string();
                            System.out.println(str);
                            runOnUiThread(() -> tv.setText(str));
                        }

                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();


    }

    public void accountList(View view) {
        new Thread() {
            @Override
            public void run() {
                Request build = new Request.Builder().url("https://www.wanandroid.com/lg/collect/list/0/json").build();
                Call call = okHttpClient.newCall(build);

                Response response = null;
                try {
                    response = call.execute();
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            String str = response.body().string();
                            System.out.println(str);
                            runOnUiThread(() -> tv.setText(str));
                        }

                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();
    }
}