package com.www233.uitest.buttonpagescroll;

import static androidx.core.content.res.ResourcesCompat.getColor;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.www233.uitest.R;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ButtonPageScroll extends RecyclerView {
    private static final String TAG = "ButtonPageScroll";
    int type = 0;
    List<View> view_list;
    int page_limit = 10;
    int state = SCROLL_STATE_IDLE;
    int ROW = 2;   // 行数

    GridLayoutManager gridLayoutManager;

    public ButtonPageScroll(@NonNull Context context) {
        this(context, (AttributeSet) null);
    }

    public ButtonPageScroll(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ButtonPageScroll(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ButtonPageScroll(@NonNull Context context, List<View> view_list, int page_limit, int row, int type) {
        this(context);
        this.view_list = view_list;
        this.page_limit = page_limit;
        this.ROW = row;
        this.type = type;
        init();

    }

    private void init() {
        ButtonPageScrollAdapter myAdapter = new ButtonPageScrollAdapter();
        this.setAdapter(myAdapter);
        if (type == 0)
            gridLayoutManager = new GridLayoutManager(getContext(), ROW, HORIZONTAL, false);
        else if (type == 1)
            gridLayoutManager = new GridLayoutManager(getContext(), ROW, VERTICAL, false);

        this.setLayoutManager(gridLayoutManager);

        GridPageSnapHelper snapHelper = new GridPageSnapHelper(ROW, page_limit);
        snapHelper.attachToRecyclerView(this);

    }


    class ButtonPageScrollAdapter extends RecyclerView.Adapter<ButtonPageScrollViewHolder> {

        @NonNull
        @Override
        public ButtonPageScrollViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = new TextView(getContext());
            return new ButtonPageScrollViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ButtonPageScrollViewHolder holder, int position) {
            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(200, 200);
            lp2.setMargins(10, 10, 10, 10);
            if (position % page_limit == 0)
                holder.view.setBackgroundColor(getResources().getColor(R.color.red, getContext().getTheme()));
            else
                holder.view.setBackgroundColor(getResources().getColor(R.color.blue, getContext().getTheme()));
            holder.view.setLayoutParams(lp2);
            ((TextView) holder.view).setText(String.valueOf(position));
        }

        @Override
        public int getItemCount() {
            return view_list.size();
        }
    }

    static class ButtonPageScrollViewHolder extends RecyclerView.ViewHolder {
        View view;

        public ButtonPageScrollViewHolder(@NonNull View view) {
            super(view);
            this.view = view;
        }
    }


}
