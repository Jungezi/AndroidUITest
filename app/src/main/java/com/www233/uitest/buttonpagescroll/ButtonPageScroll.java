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
    List<View> view_list;
    int page_limit = 10;
    int state = SCROLL_STATE_IDLE;
    private static final int ROW = 2;   // 行数

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

    public ButtonPageScroll(@NonNull Context context, List<View> view_list) {
        this(context);
        this.view_list = view_list;
        init();

    }

    public ButtonPageScroll(@NonNull Context context, List<View> view_list, int page_limit) {
        this(context);
        this.view_list = view_list;
        this.page_limit = page_limit;
        init();

    }

    private void init() {
        ButtonPageScrollAdapter myAdapter = new ButtonPageScrollAdapter();
        this.setAdapter(myAdapter);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        gridLayoutManager = new GridLayoutManager(getContext(), ROW, HORIZONTAL, false);

        this.setLayoutManager(gridLayoutManager);

        ButtonPageScrollSnapHelper snapHelper = new ButtonPageScrollSnapHelper();
        snapHelper.attachToRecyclerView(this);

        addItemDecoration(new ButtonPageScrollDecoration());
    }

    class ButtonPageScrollDecoration extends ItemDecoration {
        int last_line_st = view_list.size() - ((view_list.size() - 1) % ROW) - 1;

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int position = parent.getChildAdapterPosition(view);
            if (position >= last_line_st) {
                int line = (position % page_limit) / ROW + 1;
                int all_line = page_limit / ROW;
                outRect.set(0, 0, getWidth() / all_line * (all_line - line), 0);
            }

        }
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
            lp2.setMargins(20, 5, 20, 5);
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

    private OrientationHelper mHorizontalHelper;

    @NonNull
    private OrientationHelper getHorizontalHelper(
            @NonNull RecyclerView.LayoutManager layoutManager) {
        if (mHorizontalHelper == null || mHorizontalHelper.getLayoutManager() != layoutManager) {
            mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }
        return mHorizontalHelper;
    }

    class ButtonPageScrollSnapHelper extends SnapHelper {
        private int currentPosition = 0;

        @Nullable
        @Override
        public int[] calculateDistanceToFinalSnap(@NonNull LayoutManager layoutManager, @NonNull View targetView) {
            int[] out = new int[2];


            int position = layoutManager.getPosition(targetView);
            OrientationHelper helper = getHorizontalHelper(layoutManager);

//            Log.e(TAG, String.format("全页|开始%d 中间%d 结尾%d ", helper.getStartAfterPadding(), helper.getTotalSpace(), helper.getEndAfterPadding()));
//            Log.e(TAG, String.format("全页|Recycler view %d ", getWidth()));
//            Log.e(TAG, String.format("组件%d|开始%d 中间%d 结尾%d ", layoutManager.getPosition(targetView), helper.getDecoratedStart(targetView), helper.getDecoratedMeasurement(targetView), helper.getDecoratedEnd(targetView)));

            int currentPageStart = currentPosition;

            Log.e(TAG, String.format("calculateDistanceToFinalSnap: %d %d", position, currentPosition));
            if (Math.abs(position - currentPosition) >= page_limit) {   // 目标view已加载出来，不用再计算
                out[0] = helper.getDecoratedStart(targetView) - helper.getStartAfterPadding();
                currentPageStart = position;
            } else {    // 移动半块及以上时会滑动页面
                int dis = Math.abs(helper.getDecoratedStart(targetView) - helper.getStartAfterPadding());
                if (position < currentPosition - ROW
                        || dis <= (helper.getDecoratedMeasurement(targetView) / 2) && position < currentPosition) {  // 左移
                    currentPageStart = currentPosition - page_limit;

                } else if (position >= currentPosition + ROW
                        || dis >= (helper.getDecoratedMeasurement(targetView) / 2) && position >= currentPosition) {    // 右移
                    currentPageStart = currentPosition + page_limit;
                }

                int columnWidth = helper.getDecoratedMeasurement(targetView);
                int distance = ((position - currentPageStart) / ROW) * columnWidth;
                final int childStart = helper.getDecoratedStart(targetView);

                out[0] = childStart - distance;
            }

            out[1] = 0; // 不竖向移动
            currentPosition = currentPageStart;
            Log.i(TAG, String.format("calculateDistanceToFinalSnap: 最左%d移动至[%d]%d", position, currentPageStart, out[0]));

            return out;
        }

        @Nullable
        @Override
        public View findSnapView(LayoutManager layoutManager) {
            final int childCount = layoutManager.getChildCount();
            OrientationHelper helper = getHorizontalHelper(layoutManager);
            if (childCount == 0) {
                return null;
            }

            View closestChild = null;
            final int center = helper.getStartAfterPadding();
            int closest = Integer.MAX_VALUE;

            for (int i = 0; i < childCount; i++) {
                final View child = layoutManager.getChildAt(i);
                if (child == null) continue;

                int childCenter = helper.getDecoratedStart(child)
                        + (helper.getDecoratedMeasurement(child));
                int distance = Math.abs(childCenter - center);

                if (distance < closest) {
                    closest = distance;
                    closestChild = child;
                }

            }
            Log.i(TAG, String.format("findSnapView 现在在 %d", currentPosition));
            return closestChild;
        }

        @Override
        public int findTargetSnapPosition(LayoutManager layoutManager, int velocityX, int velocityY) {

            Log.i(TAG, String.format("findTargetSnapPosition: 当前index为%d", currentPosition));
            Log.d(TAG, "findTargetSnapPosition: 速度 " + velocityX);
            if (velocityX > 0)   // 向右滑动
            {
                if (currentPosition + page_limit > view_list.size())
                    return currentPosition;
                else
                    return currentPosition + page_limit;

            } else if (velocityX < 0) // 向左滑动
            {
                if (currentPosition - page_limit < 0)
                    return currentPosition;
                else
                    return currentPosition - page_limit;
            }
            return RecyclerView.NO_POSITION;
        }
    }

}
