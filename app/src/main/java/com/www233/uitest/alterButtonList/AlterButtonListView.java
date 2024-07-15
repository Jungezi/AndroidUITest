package com.www233.uitest.alterButtonList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.www233.uitest.R;

import java.util.ArrayList;
import java.util.List;

public class AlterButtonListView extends RadioGroup {
    private static final String TAG = "AlterButtonListView";

    private int button_size = 0, max_size, default_check = 0, text_size = 13, show_size, last_check = 0;
    private int button_style, button_style_others, other_list_style;
    private String more_hint = getResources().getString(R.string.more_hint_default);
    private List<RadioButton> radio_button = new ArrayList<>();
    private List<String> radio_button_others = new ArrayList<>();
    private List<String> all_button = new ArrayList<>();
    private Context context;
    private Point point;
    private AlertDialog dialog;
    private OnSelectedButtonChangedListener onSelectedButtonChangedListener;

    public AlterButtonListView(Context context) {
        this(context, (AttributeSet) null);
    }

    public AlterButtonListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBase(context);
        initAttr(attrs);
    }

    public AlterButtonListView(Context context, int max_size) {
        this(context, max_size, null);
    }

    public AlterButtonListView(Context context, int max_size, @Nullable List<String> list) {
        super(context);
        this.max_size = max_size;
        initBase(context);
        push(list);
    }

    /**
     * 设置最多的显示个数
     *
     * @param max_size 显示数量
     */
    public void setMax_size(int max_size) {
        try {
            if (max_size <= 0) throw new Exception("设置的显示个数太小");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.max_size = max_size;
        refresh();
    }

    public int getMax_size() {
        return max_size;
    }

    public int getDefault_check() {
        return default_check;
    }

    public void setDefault_check(int default_check) {
        this.default_check = default_check;
    }

    public int getText_size() {
        return text_size;
    }

    public void setText_size(int text_size) {
        try {
            if (max_size <= 0) throw new Exception("设置的字体大小太小");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.text_size = text_size;
        refresh();
    }

    public void setMore_hint(String more_hint) {
        this.more_hint = more_hint;
        setButtonText(-1, more_hint);
    }

    private void initBase(Context context) {
        this.context = context;
        WindowManager systemService = (WindowManager) (context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
        point = new Point();
        systemService.getDefaultDisplay().getSize(point);

        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setBackground(getResources().getDrawable(R.color.white, context.getTheme()));
        this.setPadding(0, 50, 0, 50);

        show_size = Math.min(button_size, max_size);

        // 主界面，space整理布局
        LayoutParams layoutParams_space = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f);
        Space space = getSpace(layoutParams_space);
        addView(space);

    }

    /**
     * 加入按钮list
     *
     * @param list 按钮名称的列表
     */
    public void push(List<String> list) {
        if (list == null || list.isEmpty()) return;
        int add_size = list.size();

        if (button_size + add_size < max_size) {
            addToViewFromList(list, add_size);
        } else if (button_size < max_size) {
            addToViewFromList(list, max_size - button_size);
        } else {
            addToViewFromList(list, 0);
        }
    }

    /**
     * 清空
     */
    public void clear() {
        button_size = 0;
        show_size = 0;
        last_check = default_check;
        all_button.clear();
        radio_button.clear();
        radio_button_others.clear();
        removeAllViewsInLayout();

        LayoutParams layoutParams_space = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f);
        Space space = getSpace(layoutParams_space);
        addView(space);
    }

    /**
     * list不变，更换使用当前的布局样式，选中选项不变
     */
    public void refresh() {
        int true_last_check = last_check;
        replace(new ArrayList<>(all_button));
        if (true_last_check < button_size && true_last_check != last_check)
            setCheck(true_last_check);  // 设置选中项为上一次选中的
    }

    /**
     * 用list，替换当前的按钮，并把当前选中置为默认选中的按钮
     *
     * @param list 按钮名称的列表
     */
    public void replace(@NonNull List<String> list) {
        clear();
        push(list);
    }

    /**
     * 把list中的前add_to_view个按钮加入主界面，剩下的加入更多界面
     *
     * @param list        按钮名称列表
     * @param add_to_view 加入主界面个数
     */
    private void addToViewFromList(@NonNull List<String> list, int add_to_view) {
        Log.e(TAG, "AlterButtonListView: addView");

        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LayoutParams layoutParams_space = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1f);

        all_button.addAll(list);
        int add_size = list.size();

        Space space;
        for (int i = 0; i < add_to_view; i++) {
            RadioButton rb = getRadioButton(layoutParams, list.get(i));
            radio_button.add(rb);
            int finalI = i;
            rb.setOnClickListener(v -> callSelectedButtonChangedListener(finalI));
            addView(rb);

            space = getSpace(layoutParams_space);
            addView(space);
        }

        if (add_size > add_to_view) {
            if (button_size <= max_size) { // 在此之前没有创建过更多按钮且需要创建

                RadioButton rb = getRadioButton(layoutParams, getResources().getString(R.string.more_hint_default));
                addView(rb);
                space = getSpace(layoutParams_space);
                addView(space);
                radio_button.add(rb);

                rb.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setCheck(last_check);   // 特殊：只有点击更多里面的按钮，才会改变当前选中项
                        Log.e(TAG, "setClick: " + last_check);
                        RecyclerView recyclerView = initDialog();   // 弹出更多按钮的对话框
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

            for (int i = add_to_view; i < add_size; i++) {
                radio_button_others.add(list.get(i));
            }

        }
        button_size += add_size;
        show_size = Math.min(button_size, max_size);

        if (button_size == add_size) {    // 说明是初始化view，设置当前选中项
            setCheck(default_check);
        }
    }


    private Space getSpace(LayoutParams layoutParamsSpace) {
        Space space = new Space(context);
        space.setLayoutParams(layoutParamsSpace);
        return space;
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

//        int id = getChildAt(Math.min(index, show_size)).getId();
//        if (id != -1)
//            check(id);
//        else {
        if (index >= button_size) {
            index = 0;
            Log.d(TAG, "setCheck: 设置index过大 调整为0");
        }
        last_check = index;
        if (index == -1 || index > show_size) index = show_size;    // 更多选项
        for (int i = 0; i < show_size; i++) {
            radio_button.get(i).setChecked(false);
        }
        Log.e(TAG, "setCheck: rb_len " + radio_button.size());
        radio_button.get(index).setChecked(true);
//        }
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
        last_check = position;
        Log.e(TAG, "callSelectedButtonChangedListener: " + last_check);
        onSelectedButtonChangedListener.changed(position);
    }

    private  <T> T getStyle(int style, T view){
        return view;
    }

    @NonNull
    private RadioButton getRadioButton(LayoutParams layoutParams, @Nullable String text) {
        RadioButton rb = new RadioButton(context);
        rb.setLayoutParams(layoutParams);
        rb.setBackground(getResources().getDrawable(R.drawable.alter_button_selected, null));
        rb.setTextColor(getResources().getColorStateList(R.color.alter_button_selected_color, null));
        rb.setTextSize(text_size);
        rb.setPadding(30, 0, 30, 0);
        rb.setGravity(Gravity.CENTER);
        if (text == null)
            rb.setText(getResources().getString(R.string.button_item_default));
        else
            rb.setText(text);
        rb.setButtonDrawable(null);
        return rb;
    }

    /**
     * 修改按钮上的文字
     *
     * @param index 按钮序号，-1为更多按钮
     * @param text  按钮文字
     * @return 此自定义view，可链式赋值
     */
    public AlterButtonListView setButtonText(int index, String text) {
        if (index >= button_size) try {
            throw new Exception("setButtonText: index过大");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (index == -1) {
            radio_button.get(show_size).setText(text);  // 更多按钮的提示词
        } else if (index < show_size)
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
        button_style = a.getResourceId(R.styleable.AlterButtonListView_button_style, R.style.alter_button_default);
        button_style_others = a.getResourceId(R.styleable.AlterButtonListView_button_style_others, R.style.alter_button_default);
        other_list_style = a.getResourceId(R.styleable.AlterButtonListView_other_list_style, R.style.other_list_default);

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
            /* 更多界面的TextView格式
             * */
            TextView textView = new TextView(context);
            textView.setGravity(Gravity.CENTER);
//            layoutParams.gravity = Gravity.CENTER;
//            layoutParams.topMargin = 10;
//            layoutParams.bottomMargin = 10;
            textView.setLayoutParams(layoutParams);
            textView.setTextSize(text_size);
            textView.setPadding(0, 30, 0, 30);
            if (viewType == 1) {    // 已选中选项的颜色变化
                textView.setTextColor(getResources().getColor(R.color.blue, null));
                textView.setBackground(getResources().getDrawable(R.color.grey, null));
            }

            return new AlterButtonListView.MyViewHolder(textView, viewType);

        }

        @Override
        public void onBindViewHolder(@NonNull AlterButtonListView.MyViewHolder holder, int position) {
            Log.e(TAG, position + ") onBindViewHolder: " + position);
            String tableItem = radio_button_others.get(position);
            holder.tv.setText(tableItem);
            holder.tv.setOnClickListener(v -> {
                Log.e(TAG, "onBindViewHolder: Clicked!" + max_size);
                setCheck(max_size);
                callSelectedButtonChangedListener(holder.getAdapterPosition() + max_size);
                dialog.dismiss();
            });
        }

        @Override
        public int getItemViewType(int position) {
            if (position == last_check - max_size) return 1;
            else return 0;
        }

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

    static public interface OnSelectedButtonChangedListener {
        void changed(int position);
    }
}
