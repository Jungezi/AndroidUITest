package com.www233.uitest.flowtest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {
    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    List<Integer> line_first_index = new ArrayList<>();
    List<Integer> line_height = new ArrayList<>();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        line_first_index.clear();
        line_height.clear();

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int widthsize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightsize = MeasureSpec.getSize(heightMeasureSpec);

        int cnt = getChildCount();

        int line_width = 0, width_max = 0, height_max = 0, height_all = 0;


        line_first_index.add(0);
        for (int i = 0; i < cnt; i++) {

            final View child = getChildAt(i);
            if (child == null || child.getVisibility() == View.GONE) {
                continue;
            }
            final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            int child_padding_horizontal = getPaddingStart() + getPaddingEnd();
            int child_padding_oriental = getPaddingStart() + getPaddingEnd();

            final int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                    child_padding_horizontal, lp.width);
            final int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
                    child_padding_oriental, lp.height);
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            final int child_widthsize = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            final int child_heightsize = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if (line_width + child_widthsize <= widthsize) {
                line_width += child_widthsize;
                height_max = Math.max(child_heightsize, height_max);
                if(i == cnt - 1)
                {
                    width_max = Math.max(line_width, width_max);
                    height_all += height_max;
                    line_height.add(height_max);    // 最后一行
                }
            } else {
                width_max = Math.max(line_width, width_max);
                height_max = Math.max(child_heightsize, height_max);
                height_all += height_max;
                line_first_index.add(i);
                line_height.add(height_max);
                if(i == cnt - 1)
                {
                    line_width = child_widthsize;
                    height_max = child_heightsize;
                    width_max = Math.max(line_width, width_max);
                    height_all += height_max;
                    line_height.add(height_max);    // 最后一行
                }
                line_width = 0; // 进入新的一行
                height_max = 0;
            }
        }

        line_first_index.add(cnt);  // 标记结束
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthsize : width_max,
                heightMode == MeasureSpec.EXACTLY ? heightsize : height_all);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int l_start = getPaddingStart();;
        int index = 0;
        int t_start = 0;
        for (int line = 0, all_line = line_first_index.size() - 1; line < all_line; line++) {    // line_last_index 记录第line行最后一个child的索引
            int l_line = l_start;
            for(int last_index = line_first_index.get(line + 1); index < last_index; index++){
                View child = getChildAt(index);
                if (child == null || child.getVisibility() == View.GONE) {
                    continue;
                }
                final MarginLayoutParams child_lp = (MarginLayoutParams) child.getLayoutParams();
                l_line += child_lp.leftMargin;
                final int child_widthsize = child.getMeasuredWidth();
                final int child_heightsize = child.getMeasuredHeight();
                child.layout(l_line , t_start + child_lp.topMargin, l_line + child_widthsize, t_start + child_lp.topMargin + child_heightsize);
                l_line += child_widthsize + child_lp.rightMargin;
            }
            t_start += line_height.get(line);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
