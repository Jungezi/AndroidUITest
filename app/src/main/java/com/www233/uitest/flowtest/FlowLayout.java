package com.www233.uitest.flowtest;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.RoomDatabase;
import androidx.room.Update;

import com.www233.uitest.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {

    public FlowLayout(Context context) {
        super(context);
        attrInit(context, null);
    }


    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        attrInit(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        attrInit(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        attrInit(context, attrs);
    }

    private void attrInit(Context context, AttributeSet attrs) {
        final Resources.Theme theme = context.getTheme();
        TypedArray a = theme.obtainStyledAttributes(attrs, R.styleable.FlowLayout, 0, 0);
        layout_gravity = a.getInt(R.styleable.FlowLayout_layout_gravity, 1);
        max_line = a.getInt(R.styleable.FlowLayout_max_line, 10);
        a.recycle();

    }

    List<Integer> line_last_index = new ArrayList<>();
    List<Integer> line_height = new ArrayList<>();
    static final int text_size_default = 15;

    public int getLayout_gravity() {
        return layout_gravity;
    }

    public void setLayout_gravity(int layout_gravity) {
        this.layout_gravity = layout_gravity;
        requestLayout();
    }

    public int getMax_line() {
        return max_line;
    }

    public void setMax_line(int max_line) {
        this.max_line = max_line;
        requestLayout();
    }

    int layout_gravity, max_line;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        line_last_index.clear();
        line_height.clear();

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int widthsize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightsize = MeasureSpec.getSize(heightMeasureSpec);

        int cnt = getChildCount();

        int line_width = 0, width_max = 0, height_max = 0, height_all = 0;

        int child_padding_horizontal = getPaddingStart() + getPaddingEnd();
        int child_padding_vertical = getPaddingStart() + getPaddingEnd();

        for (int i = 0; i < cnt; i++) {

            final View child = getChildAt(i);
            if (child == null || child.getVisibility() == View.GONE) {
                continue;
            }
            final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();


            final int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                    child_padding_horizontal, lp.width);
            final int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
                    child_padding_vertical, lp.height);
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            final int child_widthsize = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            final int child_heightsize = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if (line_width + child_widthsize > widthsize) {
                if(line_last_index.size() < max_line)
                    height_all += height_max;
                line_last_index.add(i - 1);
                line_height.add(height_max);
                line_width = 0; // 进入新的一行
                height_max = 0;
            }
            width_max = Math.max(line_width, width_max);
            height_max = Math.max(child_heightsize, height_max);
            line_width += child_widthsize;
            height_max = Math.max(child_heightsize, height_max);
        }

        width_max = Math.max(line_width, width_max);
        if(line_last_index.size()< max_line)
        {
            height_all += height_max;
        }
        line_height.add(height_max);    // 最后一行
        line_last_index.add(cnt - 1);
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthsize : Math.min(width_max, widthsize),
                heightMode == MeasureSpec.EXACTLY ? heightsize : Math.min(height_all, heightsize));

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int l_start = getPaddingStart();
        ;
        int index = 0;
        int t_start = 0;
        int t_bias;
        for (int line = 0, all_line = Math.min(line_last_index.size(), max_line) ; line < all_line; line++) {    // line_last_index 记录第line行最后一个child的索引
            int l_line = l_start;
            for (int last_index = line_last_index.get(line); index <= last_index; index++) {
                View child = getChildAt(index);
                if (child == null || child.getVisibility() == View.GONE) {
                    continue;
                }
                final MarginLayoutParams child_lp = (MarginLayoutParams) child.getLayoutParams();
                l_line += child_lp.leftMargin;
                final int child_widthsize = child.getMeasuredWidth();
                final int child_heightsize = child.getMeasuredHeight();

                switch (layout_gravity) {
                    case 0:
                        t_bias = child_lp.topMargin;
                        break;
                    case 2:
                        t_bias = line_height.get(line) - child_heightsize - child_lp.bottomMargin;
                        break;
                    case 1:
                    default:
                        t_bias = (line_height.get(line) - child_heightsize - child_lp.bottomMargin + child_lp.topMargin) >> 1;
                        break;
                }
                child.layout(l_line, t_start + t_bias, l_line + child_widthsize, t_start + t_bias + child_heightsize);
                l_line += child_widthsize + child_lp.rightMargin;
            }
            t_start += line_height.get(line);
        }
    }


    private void createItem(View view) {

        view.setPadding(dp2px(5), dp2px(2), dp2px(5), dp2px(2));
        MarginLayoutParams lp = new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(dp2px(3), dp2px(4), dp2px(3), dp2px(4));
        view.setLayoutParams(lp);

        addView(view);
    }

    public void addItem(View view) {

        createItem(view);
        requestLayout();
    }

    public void addItemList(List<View> list) {

        for (View v : list)
            createItem(v);
        requestLayout();
    }


    public void removeAllItems() {
        removeAllViews();
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);

    }

    private static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    private static int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, Resources.getSystem().getDisplayMetrics());
    }





}
