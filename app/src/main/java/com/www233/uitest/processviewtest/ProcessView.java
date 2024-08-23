package com.www233.uitest.processviewtest;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.www233.uitest.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProcessView extends View {
    private static final String TAG = "ProcessView";

    public static class Part {
        float length;
        int colorRes;

        public Part(float length, int colorRes) {
            this.length = length;
            this.colorRes = colorRes;
        }
    }

    interface OnBarTouchListener {
        /**
         * @param position       当前点击处的百分占比
         * @param index          当前点击处位于第几个部分 (start from 0)
         * @param partProportion 当前点击处位于该部分的占比 in [0,1]
         */
        void onBarTouch(float position, int index, float partProportion);
    }

    final static int DEFAULT_TEXT_SIZE = 14, DEFAULT_BAR_HEIGHT = 10, DEFAULT_BAR_WIDTH = 100,
            DEFAULT_INDICATOR_SIZE = 10, DEFAULT_TEXT_MARGIN = 5;
    List<Part> processList = new ArrayList<>();
    float currentProcess;
    Paint paint;
    OnBarTouchListener onBarTouchListener;

    int barHeight;
    int textSize;
    int indicatorSize;
    int textMargin;

    int default_barHeight;
    int default_textSize;
    int default_indicatorSize;
    int default_textMargin;

    float barYStart = 0;

    public ProcessView(Context context) {
        this(context, null);
    }

    public ProcessView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProcessView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ProcessView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        final Resources.Theme theme = context.getTheme();
        TypedArray a = theme.obtainStyledAttributes(attrs, R.styleable.ProcessView, 0, 0);
        default_barHeight = (int) a.getDimension(R.styleable.ProcessView_barHeight, -1);
        default_textSize = (int) a.getDimension(R.styleable.ProcessView_numTextSize, -1);
        default_indicatorSize = (int) a.getDimension(R.styleable.ProcessView_barHeight, -1);
        default_textMargin = (int) a.getDimension(R.styleable.ProcessView_barHeight, -1);
        a.recycle();

        initPaint();

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (onBarTouchListener != null) {
                    Log.d(TAG, String.format("onTouchEvent: %s %s", event.getX(), event.getY()));
                    if (event.getX() >= 0 && event.getX() <= getMeasuredWidth()
                            && event.getY() >= 0 && event.getY() <= getMeasuredHeight())
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_MOVE:
                            case MotionEvent.ACTION_DOWN:
                            case MotionEvent.ACTION_UP:
                                float x = event.getX();
                                if (x < getPaddingStart()) x = getPaddingStart();
                                if (event.getX() > getMeasuredWidth() - getPaddingEnd())
                                    x = getMeasuredWidth() - getPaddingEnd();

                                List<Part> normList = normalize(processList);
                                float current_length = 0, index_position = 0;
                                float x_ori = projectInverse(x);
                                int index = 0;
                                for (Part part : normList) {
                                    // (start,end] 时绘制三角形和字体
                                    if (x_ori <= current_length + part.length) {
                                        index_position = (x_ori - current_length) / part.length;
                                        break;
                                    }
                                    current_length += part.length;
                                    index++;
                                }
                                onBarTouchListener.onBarTouch(x_ori, index, index_position);
                                return true;
                        }
                }

                return false;
            }
        });
    }

    private void initPaint() {
        paint = new Paint();
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
    }

    public List<Part> getProcessList() {
        return processList;
    }

    public void setProcessList(List<Part> processList) {
        this.processList = processList;
        invalidate();
    }

    public void addProcessList(List<Part> processList) {
        this.processList.addAll(processList);
        invalidate();
    }

    public void addProcess(Part process) {
        this.processList.add(process);
        invalidate();
    }

    public void removeProcess(int index) {
        this.processList.remove(index);
        invalidate();
    }


    public void setOnBarTouchListener(OnBarTouchListener onBarTouchListener) {
        this.onBarTouchListener = onBarTouchListener;
    }

    /**
     * 把list的length标准化，使总和=100
     *
     * @param processList 需要标准化的list
     * @return 标准化的list
     */
    private List<Part> normalize(List<Part> processList) {
        List<Part> newList = new ArrayList<>();
        float lengthAll = 0;
        for (Part p : processList) {
            lengthAll += p.length;
        }
        lengthAll /= 100;
        for (Part p : processList) {
            newList.add(new Part(p.length / lengthAll, p.colorRes));
        }
        return newList;

    }

    public float getCurrentProcess() {
        return currentProcess;
    }

    public void setCurrentProcess(float currentProcess) {
        this.currentProcess = currentProcess;
        invalidate();
    }

    public void setBarHeight(int barHeight) {
        this.default_barHeight = dp2px(barHeight);
        requestLayout();
    }

    public void setTextSize(int textSize) {
        this.default_textSize = sp2px(textSize);
        requestLayout();
    }

    public void setIndicatorSize(int indicatorSize) {
        this.default_indicatorSize = dp2px(indicatorSize);
        requestLayout();
    }

    public void setTextMargin(int textMargin) {
        this.default_textMargin = dp2px(textMargin);
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int widthsize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightsize = MeasureSpec.getSize(heightMeasureSpec);

        int height, width;

        width = widthMode == MeasureSpec.EXACTLY ? Math.max(widthsize, getPaddingLeft() + getPaddingRight()) :
                dp2px(DEFAULT_INDICATOR_SIZE) + getPaddingLeft() + getPaddingRight();

        if (heightMode == MeasureSpec.EXACTLY)   // size: 未指定具体大小的部分平分(除了margin)
        {
            textMargin = default_textMargin == -1 ? dp2px(DEFAULT_TEXT_MARGIN) : default_textMargin;

            int remainHeight = heightsize - textMargin - getPaddingTop() - getPaddingBottom();
            int notSet = 0;
            if (default_barHeight == -1)
                notSet += 1;
            else remainHeight -= default_barHeight;
            if (default_textSize == -1)
                notSet += 1;
            else remainHeight -= default_textSize;
            if (default_indicatorSize == -1)
                notSet += 1;
            else remainHeight -= default_indicatorSize;

            int height_even = 0;
            if (notSet != 0)
                height_even = remainHeight / notSet;

            barHeight = default_barHeight == -1 ? height_even : default_barHeight;
            textSize = default_textSize == -1 ? height_even : default_textSize;
            indicatorSize = default_indicatorSize == -1 ? height_even : default_indicatorSize;

            height = heightsize;
        } else {
            barHeight = default_barHeight == -1 ? dp2px(DEFAULT_BAR_HEIGHT) : default_barHeight;
            textSize = default_textSize == -1 ? sp2px(DEFAULT_TEXT_SIZE) : default_textSize;
            indicatorSize = default_indicatorSize == -1 ? dp2px(DEFAULT_INDICATOR_SIZE) : default_indicatorSize;
            textMargin = default_textMargin == -1 ? dp2px(DEFAULT_TEXT_MARGIN) : default_textMargin;

            height = getTextRect().height() + textMargin + indicatorSize + barHeight + getPaddingTop() + getPaddingBottom();
        }

        setMeasuredDimension(width, height);

    }

    /**
     * 把百分比的x转化为实际坐标
     *
     * @param x
     * @return
     */
    float project(float x) {
        return x / 100 * (getMeasuredWidth() - getPaddingLeft() - getPaddingRight()) + getPaddingStart();
    }

    float projectInverse(float x) {
        return (x - getPaddingStart()) * 100 / (getMeasuredWidth() - getPaddingLeft() - getPaddingRight());
    }


    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        int start = getPaddingStart(), end = getPaddingEnd();
        int top = getPaddingTop(), bottom = getPaddingBottom();

        // 百分比数字
        float text_center_x = project(currentProcess);
        String data = String.format(Locale.getDefault(), "%.2f%%", currentProcess);
        Rect rect = getTextRect();

        // 三角形路径
        Path triangle_path = new Path();
        float triangle_height = indicatorSize;
        float triangle_bottom_y = top + rect.height() + textMargin + indicatorSize;
        float triangle_side_div2 = triangle_height / (float) Math.sqrt(3);
        triangle_path.moveTo(text_center_x, triangle_bottom_y);
        triangle_path.lineTo(text_center_x - triangle_side_div2, triangle_bottom_y - triangle_height);
        triangle_path.lineTo(text_center_x + triangle_side_div2, triangle_bottom_y - triangle_height);

        // 防止字体出界
        if (text_center_x < (rect.width() >> 1)) text_center_x = (rect.width() >> 1);
        else if (text_center_x + (rect.width() >> 1) > getMeasuredWidth())
            text_center_x = getMeasuredWidth() - (rect.width() >> 1);

        // 颜色条和三角形
        float current_length = 0;
        float current_x = start;

        barYStart = triangle_bottom_y;

//        Log.i(TAG, String.format("onDraw: %s<>%s", triangle_height , barHeight));
        List<Part> normList = normalize(processList);

        for (Part part : normList) {
            final float next_x = project(part.length + current_length);
            Log.i(TAG, String.format("onDraw: [%d]%s %s -> %s", part.colorRes, part.length, current_x, next_x));

            paint.setColor(part.colorRes);
            canvas.drawRect(current_x, triangle_bottom_y, next_x, triangle_bottom_y + barHeight, paint);
            current_x = next_x;
            Log.i(TAG, String.format("绘制！ %s <> %s", currentProcess, current_length));

            // (start,end] 时绘制三角形和字体
            if (currentProcess > current_length) {
                canvas.drawPath(triangle_path, paint);
                canvas.drawText(data, text_center_x, top + rect.height(), paint);
            }
            current_length += part.length;
        }
        if (currentProcess == 0) {
            paint.setColor(processList.get(0).colorRes);    // 特殊情况: =0
            canvas.drawPath(triangle_path, paint);
            canvas.drawText(data, text_center_x, top + rect.height(), paint);
        }

    }

    private Rect getTextRect() {
        String data = String.format(Locale.getDefault(), "%.2f%%", currentProcess);   // 仅以文字为中心对齐
        Rect rect = new Rect();
        paint.setTextSize(textSize);
        paint.getTextBounds(data, 0, data.length(), rect);
        return rect;
    }

    public static int dp2px(int dp) {
        if (dp == -1) return -1;
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    public static int sp2px(int sp) {
        if (sp == -1) return -1;
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, Resources.getSystem().getDisplayMetrics());
    }

}
