package com.www233.uitest.alterButtonList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.www233.uitest.R;
import com.www233.uitest.httptest.PracticeInternetActivity;
import com.www233.uitest.tabletest.TableActivity;
import com.www233.uitest.tabletest.TableItem;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AlterButtonListView extends RadioGroup {
    private static final String TAG = "AlterButtonListView";
    private int button_size, max_size, default_check, text_size, show_size;
    String more_hint;
    List<RadioButton> radio_button = new ArrayList<>();
    List<String> radio_button_others = new ArrayList<>();
    Context context;
    Point point;
    AlertDialog dialog;
    private OnSelectedButtonChangedListener onSelectedButtonChangedListener;

    public AlterButtonListView(Context context) {
        this(context, null);
    }

    public AlterButtonListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        WindowManager systemService = (WindowManager) (context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE));

        point = new Point();
        systemService.getDefaultDisplay().getSize(point);


        Log.e(TAG, "AlterButtonListView: initAttr");
        initAttr(attrs);
        Log.e(TAG, "AlterButtonListView: View");
        initView();
    }

    private void initView() {
        this.setOrientation(LinearLayout.HORIZONTAL);
        Log.e(TAG, "AlterButtonListView: addView");

        LayoutParams layoutParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f);


        for (int i = 0; i < show_size; i++) {
            RadioButton rb = getRadioButton(layoutParams);
            rb.setChecked(i == default_check);
            radio_button.add(rb);
            int finalI = i;
            rb.setOnClickListener(v -> callSelectedButtonChangedListener(finalI));
            addView(rb);
            Log.e(TAG, "initView: " + rb.toString());
        }

        if (button_size > max_size) {

            RadioButton rb = getRadioButton(layoutParams);
            rb.setText(getResources().getString(R.string.more_hint_default));
            rb.setChecked(default_check >= show_size);  // 选中更多
            addView(rb);

            for (int i = max_size; i < button_size; i++) {
                radio_button_others.add(getResources().getString(R.string.button_item_default));
            }

            rb.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecyclerView recyclerView = initDialog();
                    dialog = new AlertDialog.Builder(context)
                            .setView(recyclerView)
                            .create();

                    WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
                    dialog.show();

                    layoutParams.gravity = Gravity.CENTER | Gravity.BOTTOM;
//                    layoutParams.height = (int) (point.y*0.6);
                    dialog.getWindow().setAttributes(layoutParams);
                }
            });
        }


    }

    private RecyclerView initDialog() {


        RecyclerView recyclerView;

        recyclerView = new RecyclerView(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        recyclerView.setLayoutParams(layoutParams);

        MyAdapter myAdapter = new MyAdapter();
        Log.e(TAG, "创建Adapter");
        recyclerView.setAdapter(myAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        return recyclerView;

    }

    public void setCheck(int index) {

        if (index >= show_size) index = show_size - 1;
        for (int i = 0; i < show_size; i++) {
            radio_button.get(i).setChecked(false);
        }
        radio_button.get(index).setChecked(true);
    }

    public int getCheck() {
        for (int i = 0; i < show_size; i++) {
            if (radio_button.get(i).isChecked())
                return i;
        }
        return -1;
    }

    public void setOnSelectedButtonChangedListener(OnSelectedButtonChangedListener onSelectedButtonChangedListener) {
        this.onSelectedButtonChangedListener = onSelectedButtonChangedListener;
    }

    private void callSelectedButtonChangedListener(int position) {
        onSelectedButtonChangedListener.changed(position);
    }

    @NonNull
    private RadioButton getRadioButton(LayoutParams layoutParams) {
        RadioButton rb = new RadioButton(context);
        rb.setLayoutParams(layoutParams);
        rb.setBackground(getResources().getDrawable(R.drawable.alter_button_selected, null));
        rb.setTextColor(getResources().getColorStateList(R.color.alter_button_selected_color, null));
        rb.setTextSize(text_size);
        rb.setGravity(Gravity.CENTER);
        rb.setText(getResources().getString(R.string.button_item_default));
        rb.setButtonDrawable(null);
        return rb;
    }

    public AlterButtonListView setButtonText(int index, String text) {
        if (index >= button_size) try {
            throw new Exception("setButtonText: index过大");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (index < show_size)
            radio_button.get(index).setText(text);
        else
            radio_button_others.set(index - show_size, text);
        return this;
    }


    private void initAttr(@Nullable AttributeSet attrs) {

        final Resources.Theme theme = context.getTheme();
        TypedArray a = theme.obtainStyledAttributes(attrs, R.styleable.AlterButtonListView, 0, 0);

        button_size = a.getInt(R.styleable.AlterButtonListView_button_size, 0);
        max_size = a.getInt(R.styleable.AlterButtonListView_max_size, 0);
        default_check = a.getInt(R.styleable.AlterButtonListView_default_check, 0); // 从0开始给按钮编号
        text_size = a.getInt(R.styleable.AlterButtonListView_text_size, 13);
        show_size = Math.min(button_size, max_size);


        more_hint = a.getString(R.styleable.AlterButtonListView_more_hint);
        if (more_hint == null) more_hint = getResources().getString(R.string.more_hint_default);

        a.recycle();

    }


    class MyAdapter extends RecyclerView.Adapter<AlterButtonListView.MyViewHolder> {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//        int index_selected = getCheck() - max_size;

        @NonNull
        @Override
        public AlterButtonListView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView textView = new TextView(context);
            textView.setGravity(Gravity.CENTER);
//            layoutParams.gravity = Gravity.CENTER;
//            layoutParams.topMargin = 10;
//            layoutParams.bottomMargin = 10;
            textView.setLayoutParams(layoutParams);
            textView.setTextSize(20);
            textView.setPadding(0, 10, 0, 10);
//            if (viewType == 1) {
//                textView.setTextColor(getResources().getColor(R.color.blue, null));
//                textView.setBackground(getResources().getDrawable(R.color.grey_heavy, null));
//            }

            return new AlterButtonListView.MyViewHolder(textView, viewType);

        }

        @Override
        public void onBindViewHolder(@NonNull AlterButtonListView.MyViewHolder holder, int position) {
            Log.e(TAG, position + ") onBindViewHolder: " + position);
            String tableItem = radio_button_others.get(position);
            holder.tv.setText(tableItem);
            holder.tv.setOnClickListener(v -> {
                callSelectedButtonChangedListener(holder.getAdapterPosition() + max_size);
                dialog.dismiss();
            });
        }

//        @Override
//        public int getItemViewType(int position) {
//            if(position == index_selected - 1) return 1;
//            else return 0;
//        }

        @Override
        public int getItemCount() {
            return radio_button_others.size();
        }


    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public MyViewHolder(@NonNull TextView itemView, int viewType) {
            super(itemView);
            tv = itemView;
        }
    }
}
