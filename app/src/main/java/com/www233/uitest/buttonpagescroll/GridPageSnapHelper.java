package com.www233.uitest.buttonpagescroll;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import java.util.Objects;

public class GridPageSnapHelper extends SnapHelper {
    private static final String TAG = "GridPageSnapHelper";
    private int currentPosition = 0;
    private int ROW, page_limit, all_item = 0;
    private OrientationHelper mHorizontalHelper;

    public GridPageSnapHelper(int ROW, int page_limit) {
        this.ROW = ROW;
        this.page_limit = page_limit;
    }

    @Override
    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) throws IllegalStateException {
        super.attachToRecyclerView(recyclerView);
        if (recyclerView != null) {
            all_item = Objects.requireNonNull(recyclerView.getAdapter()).getItemCount();
            recyclerView.addItemDecoration(new ButtonPageScrollDecoration());
        }
    }

    @NonNull
    private OrientationHelper getHorizontalHelper(
            @NonNull RecyclerView.LayoutManager layoutManager) {
        if (mHorizontalHelper == null || mHorizontalHelper.getLayoutManager() != layoutManager) {
            mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }
        return mHorizontalHelper;
    }
    @Nullable
    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {
        int[] out = new int[2];


        int position = layoutManager.getPosition(targetView);
        OrientationHelper helper = getHorizontalHelper(layoutManager);

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
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
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
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {

        Log.i(TAG, String.format("findTargetSnapPosition: 当前index为%d", currentPosition));
        Log.d(TAG, "findTargetSnapPosition: 速度 " + velocityX);
        if (velocityX > 0)   // 向右滑动
        {
            if (currentPosition + page_limit > all_item)
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


    class ButtonPageScrollDecoration extends RecyclerView.ItemDecoration {
        int last_line_st = all_item - ((all_item - 1) % ROW) - 1;

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int position = parent.getChildAdapterPosition(view);
            if (position >= last_line_st) { // 最后一列边距到末尾
                int line = (position % page_limit) / ROW + 1;
                int all_line = page_limit / ROW;
                outRect.set(0, 0, parent.getWidth() / all_line * (all_line - line), 0);
            }

        }
    }
}
