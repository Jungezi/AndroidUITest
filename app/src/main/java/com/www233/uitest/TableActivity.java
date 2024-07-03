package com.www233.uitest;

import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class TableActivity extends AppCompatActivity {

    private static final String TAG = "TableActivity";
    RecyclerView mRecyclerView;
    MyAdapter myAdapter;
    List<TableItem> mItemList = new ArrayList<>();

    static final int item_size = 105;
    List<Integer> cm_num = new ArrayList<Integer>();
    List<Integer> cm_is = new ArrayList<Integer>();
    int cm_cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_table);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        item_init();
    }

    private void item_init() {
        mRecyclerView = findViewById(R.id.recyclerView1);

        Random rand = new Random(111);
        for (int i = 0; i < item_size; i++) {
            String title = "标题" + i;
            String tag1 = i + "_1";
            String tag2 = i + "_2";
            int num = rand.nextInt(10000) + 10000;
            int num_sub = rand.nextInt(10000);
            ArrayList<String> tag = new ArrayList<>();
            tag.add(tag1);
            tag.add(tag2);
            TableItem item = new TableItem(title, tag, num, num_sub);
            mItemList.add(item);
            cm_num.add(0);
            cm_is.add(0);

            Log.e(TAG, i + ") item_init: " + num);
        }
        for (int i = rand.nextInt(10) + 5; i < item_size; i += rand.nextInt(10) + 5) {
            cm_num.set(i, 1);   // 随机设置cm位置
            cm_is.set(i, 1);    // 随机设置cm位置
            cm_cnt++;
        }
        for (int i = 1; i < item_size; i++) {
            cm_num.set(i, cm_num.get(i - 1) + cm_num.get(i)); // 记录该位置及之前的cm数量
        }
        myAdapter = new MyAdapter();
        Log.e(TAG, "创建Adapter");
        mRecyclerView.setAdapter(myAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TableActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //View view = View.inflate(TableActivity.this, R.layout.list_item, null);
            View holder_view;

            switch (viewType) {
                case 0:
                    holder_view = LayoutInflater.from(TableActivity.this).inflate(R.layout.list_item, parent, false);
                    break;
                default: // cm
                    holder_view = LayoutInflater.from(TableActivity.this).inflate(R.layout.list_item_cm, parent, false);
                    break;
            }

            return new MyViewHolder(holder_view, viewType);

        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            int index = getActualPosition(position);
            int viewType = getItemViewType(position);
            switch (viewType) {
                case 0:
                    Log.e(TAG, position + ") onBindViewHolder: " + index);
                    TableItem tableItem = mItemList.get(index);
                    holder.tv_title.setText(tableItem.title);
                    holder.tv_tag1.setText(tableItem.tag.get(0));
                    holder.tv_tag2.setText(tableItem.tag.get(1));
                    holder.tv_number.setText(String.format("%s", tableItem.number));
                    holder.tv_number_sub.setText(String.format("%s%%", tableItem.number_sub / 100));
                    break;
                default:
                    holder.cv_delete.setOnClickListener(new MyOnClickListener(position, index));

                    break;
            }

        }


        @Override
        public int getItemCount() {
            return mItemList.size() + cm_cnt;
        }

        @Override
        public int getItemViewType(int position) {
            int index = getActualPosition(position);
            if (cm_is.get(index) == 1 && index + cm_num.get(index) == position) return 1;  //广告
            else return 0;
        }

        public int getActualPosition(int position) {

            for (int ed = position >= item_size ? item_size - 1 : position; ed >= 0; ed--) {
                Log.e(TAG, position + ") getActualPosition: " + ed + " " + cm_num.get(ed) + " " + cm_is.get(ed));
                if (cm_is.get(ed) == 1 && ed + cm_num.get(ed) == position) return ed;    // 广告
                if (ed + cm_num.get(ed) - cm_is.get(ed) == position) return ed;    // 一般
            }
            return 0;
        }


    }

    class MyOnClickListener implements View.OnClickListener {

        public int position;
        public int index;

        public MyOnClickListener(int position, int index) {
            this.position = position;
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            Log.e(TAG, "onClick: 删除广告");
            cm_cnt--;
            cm_is.set(index, 0);
            for (int i = index; i < item_size; i++) {
                cm_num.set(i, cm_num.get(i) - 1);
            }
            myAdapter.notifyItemRemoved(position);

        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_tag1;
        TextView tv_tag2;
        TextView tv_number;
        TextView tv_number_sub;
        CardView cv_delete;

        public MyViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            Log.e(TAG, "MyViewHolder: 创建中");

            switch (viewType) {
                case 0:
                    tv_title = itemView.findViewById(R.id.title);
                    tv_tag1 = itemView.findViewById(R.id.tag1);
                    tv_tag2 = itemView.findViewById(R.id.tag2);
                    tv_number = itemView.findViewById(R.id.number);
                    tv_number_sub = itemView.findViewById(R.id.number_sub);
                    break;

                default:
                    cv_delete = itemView.findViewById(R.id.cv_cm_delete);
                    break;
            }


        }
    }
}