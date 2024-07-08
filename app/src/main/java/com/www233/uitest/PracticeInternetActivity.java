package com.www233.uitest;

import android.icu.text.IDNA;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

public class PracticeInternetActivity extends AppCompatActivity {
    private static final String TAG = "Practice";
    HttpConnect okHttpClient;
    TextView tv;
    private RecyclerView mRecyclerView;
    private PracticeChartRecyclerViewAdapter myAdapter;
    GridLayoutManager gridLayoutManager;
    LinearLayoutManager linearLayoutManager;

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
        mRecyclerView = findViewById(R.id.recyclerView);
        gridLayoutManager = new GridLayoutManager(PracticeInternetActivity.this, 1);
        linearLayoutManager = new LinearLayoutManager(PracticeInternetActivity.this);

    }

    List<PracticeChartInfoItem> InfoList = new ArrayList<>();
    List<TableItem> mItemList = new ArrayList<>();
    private void getvalue(Map map)
    {
        for(Object key: map.keySet())
        {
            Object value = map.get(key);
            if(value instanceof Map)
            {
                getvalue((Map) value);
            }
            else
            {
                if(map.get(key)!=null)
                    InfoList.add(new PracticeChartInfoItem(key.toString(), map.get(key).toString()));
            }
        }
    }
    private void httpInit() {

        okHttpClient = new HttpConnect(new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {

                if(msg.what ==0)
                {
                        Log.d(TAG, "handleMessage1: " + msg.obj.toString());
                        Gson gson = new Gson();
    //                HttpTable json = gson.fromJson(msg.obj.toString(), HttpTable.class);
                        Map map = gson.fromJson(msg.obj.toString(), Map.class);

                        InfoList.clear();
                        getvalue(map);
                        mRecyclerView.setLayoutManager(gridLayoutManager);
                        myAdapter = new PracticeChartRecyclerViewAdapter(InfoList);
                        mRecyclerView.setAdapter(myAdapter);
                }
                else
                {
                    Gson gson = new Gson();
                    HttpTable json = gson.fromJson(msg.obj.toString(), HttpTable.class);
                    mItemList.clear();
                    for(HttpTable.Bean1 bean : json.data.datas){
                        ArrayList<String> objects = new ArrayList<>();
                        objects.add(bean.chapterName);
                        objects.add(bean.niceDate);
                        mItemList.add(new TableItem(bean.title, objects , bean.id , bean.publishTime));
                    }

                    mRecyclerView.setLayoutManager(linearLayoutManager);
                    MyAdapter mAdapter = new MyAdapter();
                    mRecyclerView.setAdapter(mAdapter);
//                    List<Map<String, String>> json = (List<Map<String, String>>) ((Map)map.get("data")).get("datas");
//
//                    for(Map<String, String> bean : json){
//                        ArrayList<String> objects = new ArrayList<>();
//                        objects.add(bean.get("title"));
//                        objects.add("");
//                        mItemList.add(new TableItem(bean.get("chapterName"), objects , new Double( bean.get("publishTime")), new Double(bean.get("niceDate"))));
//                    }
//                    mRecyclerView.setLayoutManager(linearLayoutManager);
//                    MyAdapter mAdapter = new MyAdapter();
//                    mRecyclerView.setAdapter(mAdapter);

                }



                return false;
            }
        }));
    }

    public void getSync(View view) {
        okHttpClient.getSync("https://httpbin.org/get?a=1&b=2");
    }

    public void getAsync(View view) {
        okHttpClient.getAsync("https://httpbin.org/get?a=1&b=2", 0);
    }

    public void postSync(View view) {
        FormBody frombody = new FormBody.Builder().add("a", "2").add("b", "yes").build();
        okHttpClient.postSync("https://httpbin.org/post", frombody);
    }

    public void postAsync(View view) {
        FormBody frombody = new FormBody.Builder().add("a", "2").add("b", "yes").build();
        okHttpClient.postAsync("https://httpbin.org/post", frombody);

    }


    public void loginTest(View view) {
        FormBody formBody = new FormBody.Builder().add("username", "Jungezi")
                .add("password", "123w654001").build();
        okHttpClient.postAsync("https://www.wanandroid.com/user/login", formBody);

    }

    public void accountList(View view) {
        okHttpClient.getAsync("https://www.wanandroid.com/lg/collect/list/0/json", 1);
    }


    class MyAdapter extends RecyclerView.Adapter<TableActivity.MyViewHolder> {

        @NonNull
        @Override
        public TableActivity.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //View view = View.inflate(TableActivity.this, R.layout.list_item, null);
            View holder_view;

            switch (viewType) {
                case 0:
                    holder_view = LayoutInflater.from(PracticeInternetActivity.this).inflate(R.layout.list_item, parent, false);
                    break;
                default: // cm
                    holder_view = LayoutInflater.from(PracticeInternetActivity.this).inflate(R.layout.list_item_cm, parent, false);
                    break;
            }

            return new TableActivity.MyViewHolder(holder_view, viewType);

        }

        @Override
        public void onBindViewHolder(@NonNull TableActivity.MyViewHolder holder, int position) {
            Log.e(TAG, position + ") onBindViewHolder: " + position);
            TableItem tableItem = mItemList.get(position);
            holder.tv_title.setText(tableItem.title);
            holder.tv_tag1.setText(tableItem.tag.get(0));
            holder.tv_tag2.setText(tableItem.tag.get(1));
            holder.tv_number.setText(String.format("%s", tableItem.number));
            holder.tv_number_sub.setText(String.format("%s%%", tableItem.number_sub / 100));

        }


        @Override
        public int getItemCount() {
            return mItemList.size();
        }


    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_tag1;
        TextView tv_tag2;
        TextView tv_number;
        TextView tv_number_sub;

        public MyViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            Log.e(TAG, "MyViewHolder: 创建中");
                    tv_title = itemView.findViewById(R.id.title);
                    tv_tag1 = itemView.findViewById(R.id.tag1);
                    tv_tag2 = itemView.findViewById(R.id.tag2);
                    tv_number = itemView.findViewById(R.id.number);
                    tv_number_sub = itemView.findViewById(R.id.number_sub);


        }
    }


}