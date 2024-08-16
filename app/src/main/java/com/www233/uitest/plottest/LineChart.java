package com.www233.uitest.plottest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.internal.ViewUtils;
import com.www233.uitest.R;

import java.util.ArrayList;
import java.util.List;

public class LineChart extends FrameLayout {
    private static final String TAG = "LineChart";
    List<PointF> pointFList = new ArrayList<>(), pointFOrigin = new ArrayList<>();
    float x_start = 0, y_start = 0;
    float x_end = 0, y_end = 0;
    Paint paint, paint_cover, paint_point, paint_back;

    private float startX, startY;
    Bitmap line, cover, backGrid;

    public LineChart(Context context) {
        this(context, null);
    }

    public LineChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public LineChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        paint = new Paint();
        paint.setStrokeWidth(3);
        paint.setColor(getResources().getColor(R.color.blue_light100, getContext().getTheme()));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        paint_back = new Paint();
        paint_back.setStrokeWidth(5);
        paint_back.setColor(getResources().getColor(R.color.grey, getContext().getTheme()));
        paint_back.setAntiAlias(true);
        paint_back.setTextSize(30);
        paint_back.setTextAlign(Paint.Align.RIGHT);
        paint_back.setStyle(Paint.Style.STROKE);

        paint_cover = new Paint();
        paint_cover.setStrokeWidth(2);
        paint_cover.setColor(Color.GRAY);
        paint_cover.setAntiAlias(true);
        paint_cover.setTextAlign(Paint.Align.CENTER);
        paint_cover.setTextSize(30);

        paint_point = new Paint();
        paint_point.setStrokeWidth(5);
        paint_point.setColor(Color.RED);
        paint_point.setAntiAlias(true);
        paint_point.setTextSize(30);
        paint_point.setTextAlign(Paint.Align.LEFT);

        PathEffect pathEffectTwo = new DashPathEffect(new float[]{10, 5},0);


        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (cover == null) {

                    cover = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
//                    Log.i(TAG, String.format("onTouchDown: [%d %d]", getWidth(), getHeight()));
                }

                int start = getPaddingStart(), end = getPaddingEnd();
                int top = getPaddingTop(), bottom = getPaddingBottom();
                Canvas canvas = new Canvas(cover);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_DOWN:
                        paint_cover.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                        canvas.drawPaint(paint_cover);  // 清空
                        startX = event.getX();
                        startY = event.getY();
//                        Log.i(TAG, String.format("onTouchDown: %.2f %.2f", startX, startY));
                        paint_cover.setXfermode(null);
                        if (!(startX < start || startX > getWidth()-end)) {
                            paint_cover.setPathEffect(null);
                            canvas.drawLine(startX, top, startX, getHeight() - bottom, paint_cover);
                            int index = latestPoint(startX);

                            if (index != -1) {

                                PointF[] pointInterval = new PointF[2];
                                PointF[] pointIntervalOrigin = new PointF[2];
                                pointInterval[0] = pointFList.get(index);
                                pointInterval[1] = pointFList.get(index + 1);
                                pointIntervalOrigin[0] = pointFOrigin.get(index);
                                pointIntervalOrigin[1] = pointFOrigin.get(index + 1);

                                canvas.drawCircle(pointInterval[0].x, pointInterval[0].y, 5, paint_point);
                                canvas.drawCircle(pointInterval[1].x, pointInterval[1].y, 5, paint_point);

//                                String hint1 = String.format("(%.2f,%.2f)", pointIntervalOrigin[0].x, pointIntervalOrigin[0].y);
//                                String hint2 = String.format("(%.2f,%.2f)", pointIntervalOrigin[1].x, pointIntervalOrigin[1].y);

                                String hint1_y = String.format("%.2f", pointIntervalOrigin[0].y);
                                String hint2_y = String.format("%.2f", pointIntervalOrigin[1].y);
                                PointF p1_y = getShowPositionBound(pointInterval[0], hint1_y);
                                PointF p2_y = getShowPositionBound(pointInterval[1], hint2_y);

                                canvas.drawText(hint1_y, p1_y.x, p1_y.y, paint_point);
                                canvas.drawText(hint2_y, p2_y.x, p2_y.y, paint_point);

                                String hint1_x = String.format("%.0f", pointIntervalOrigin[0].x);
                                String hint2_x = String.format("%.0f", pointIntervalOrigin[1].x);
                                PointF p1_x = getShowPositionX(pointInterval[0], hint1_x);
                                PointF p2_x = getShowPositionX(pointInterval[1], hint2_x);
                                canvas.drawText(hint1_x, p1_x.x, p1_x.y, paint_cover);
                                canvas.drawText(hint2_x, p2_x.x, p2_x.y, paint_cover);

                                paint_cover.setPathEffect(pathEffectTwo);
                                canvas.drawLine(pointInterval[0].x,getHeight() - bottom,pointInterval[0].x,pointInterval[0].y,paint_cover);
                                canvas.drawLine(pointInterval[1].x,getHeight() - bottom,pointInterval[1].x,pointInterval[1].y,paint_cover);
                            }


                        }
                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
//                        Log.i(TAG, "onTouchUp: ");
                        cover.recycle();
                        cover = null;
                        invalidate();
                        break;
                }
                return true;
            }
        });
    }

    private PointF getShowPositionBound(PointF pointF, String string) {

        Rect rect = new Rect();
        PointF p = new PointF(pointF.x + 20, pointF.y);
        paint_point.getTextBounds(string, 0, string.length(), rect);
        if (p.x + rect.width() > getWidth()) p.x -= rect.width() + 40;
        if (p.y + rect.height() > getHeight()) p.y -= rect.height();
        return p;
    }

    private PointF getShowPositionX(PointF pointF, String string) {

        Rect rect = new Rect();
        PointF p = new PointF(pointF.x, pointF.y);
        paint_point.getTextBounds(string, 0, string.length(), rect);
        Log.i(TAG, "getShowPositionX: " + rect.width());
        p.y = getHeight() - getPaddingBottom() + rect.height() + 10;
        return p;
    }

    public void setData(List<Float> x, List<Float> y) {
        pointFOrigin.clear();
        pointFList.clear();
        for (int i = 0, size = Math.min(x.size(), y.size()); i < size; i++) {
            pointFOrigin.add(new PointF(x.get(i), y.get(i)));
            pointFList.add(projection(x.get(i), y.get(i)));
//            Log.d(TAG, String.format("setData: (%.2f,%.2f)(%.2f,%.2f)->(%.2f,%.2f)",x.get(i),y.get(i),
//                    pointFOrigin.get(i).x, pointFOrigin.get(i).y,
//                    pointFList.get(i).x, pointFList.get(i).y));
        }
//        creatformat(pointFList);
//        pointFList.sort(null);  // TODO: 未验证是否实现comparable方法
    }

    public void setData(List<Float> x, List<Float> y, float x_start, float y_start) {
        setData(x, y);
        this.x_start = x_start;
        this.y_start = y_start;
    }

    public void setData(List<Float> x, List<Float> y, float x_start, float y_start, float x_end, float y_end) {
        setData(x, y, x_start, y_start);
        this.x_end = x_end;
        this.y_end = y_end;

        invalidate();

    }

    public int latestPoint(float x) {
        for (int i = 0, size = pointFList.size() - 1; i < size; i++) {
            if (pointFList.get(i).x <= x && pointFList.get(i + 1).x > x) {
                return i;   // i & i + 1
            }
        }
        return -1;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    private void setLineBitmap() {
        if (line != null) {
            line.recycle();
            line = null;
        }
        line = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(line);

//        float last_y, y;
//        last_y = (float) Nn(pointFList, x_start);
//        for (float i = x_start; i < x_end; i += 1) {
//            y = (float) Nn(pointFList, i);
//            Log.e(TAG, String.format("setLineBitmap: (%.2f,%.2f)", i, y));
//            canvas.drawLine(i - 1f, last_y, i, y, paint);
//            last_y = y;
//        }

        LinearGradient linearGradient = new LinearGradient(0, 0, 0, getHeight(),
                getResources().getColor(R.color.blue_light40, getContext().getTheme()),
                Color.TRANSPARENT, Shader.TileMode.REPEAT);
        paint.setShader(linearGradient);
        Path path = new Path();
        path.moveTo(pointFList.get(0).x, getHeight());
        for (int i = 0, size = pointFList.size(); i < size; i++) {
            path.lineTo(pointFList.get(i).x, pointFList.get(i).y);
        }
        path.lineTo(pointFList.get(pointFList.size() - 1).x, getHeight());
        path.close();
        canvas.drawPath(path, paint);

        paint.setShader(null);
        for (int i = 1, size = pointFList.size(); i < size; i++) {
            canvas.drawLine(pointFList.get(i - 1).x, pointFList.get(i - 1).y,
                    pointFList.get(i).x, pointFList.get(i).y, paint);
        }
        for (int i = 0, size = pointFList.size(); i < size; i++) {
//            Log.d(TAG, String.format("setLineBitmap: (%.2f,%.2f)->(%.2f,%.2f)",
//                    pointFOrigin.get(i).x, pointFOrigin.get(i).y,
//                    pointFList.get(i).x, pointFList.get(i).y));
        }

    }

    PointF projection(float x, float y) {
        int start = getPaddingStart(), end = getPaddingEnd();
        int top = getPaddingTop(), bottom = getPaddingBottom();
        float x_len = getWidth(), y_len = getHeight();
        float xp, yp;
        xp = (x - x_start) / (x_end - x_start) * (x_len - start - end) + start;
        yp = (y_end - y) / (y_end - y_start) * (y_len - top - bottom) + top;
//        Log.d(TAG, String.format("projection: (%.2f,%.2f)->(%.2f,%.2f)", x, y, xp, yp));
        return new PointF(xp, yp);
    }


    @Override
    protected void onDraw(@NonNull Canvas canvas) {
//        Log.d(TAG, "onDraw: ");
        super.onDraw(canvas);

        setBackGrid();
        canvas.drawBitmap(backGrid, 0, 0, paint_back);

        setLineBitmap();
        canvas.drawBitmap(line, 0, 0, paint);

        if (cover != null)
            canvas.drawBitmap(cover, 0, 0, paint_cover);

    }

    private void setBackGrid() {
        int start = getPaddingStart(), end = getPaddingEnd();
        int top = getPaddingTop(), bottom = getPaddingBottom();
        if (backGrid != null) {
            backGrid.recycle();
            backGrid = null;
        }
        backGrid = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(backGrid);

        paint_back.setColor(getResources().getColor(R.color.grey, getContext().getTheme()));
        paint_back.setStyle(Paint.Style.STROKE);

        // 边框
        canvas.drawRect(start, top, getWidth() - end, getHeight() - bottom, paint_back);

        PointF projection1 = projection(0, 20);
        PointF projection2 = projection(0, 0);
        float interval = projection2.y - projection1.y;
        for (int i = getHeight() - bottom, i_ori = (int) y_start; i_ori <= (int) y_end; i -= (int) interval, i_ori += 20) {
            canvas.drawLine(start, i, getWidth() - end, i, paint_back);
        }

        // 坐标轴数值
        paint_back.setColor(getResources().getColor(R.color.grey_heavy, getContext().getTheme()));
        paint_back.setStyle(Paint.Style.FILL);
        paint_back.setTextAlign(Paint.Align.RIGHT);
        Rect rect = new Rect();
        String str;
        for (int i = getHeight() - bottom, i_ori = (int) y_start; i_ori <= (int) y_end; i -= (int) interval, i_ori += 20) {
            str = String.valueOf(i_ori);
            paint_back.getTextBounds(str, 0, str.length(), rect);
            canvas.drawText(str, start - 10, i + (rect.height() >> 1), paint_back);
        }

        paint_back.setTextAlign(Paint.Align.LEFT);
        str = String.valueOf((int) x_start);
        paint_back.getTextBounds(str, 0, str.length(), rect);
        canvas.drawText(str, start + 10 - (rect.width() >> 1),
                getHeight() - bottom + rect.height() + 10, paint_back);

        str = String.valueOf((int) x_end);
        paint_back.getTextBounds(str, 0, str.length(), rect);
        canvas.drawText(str, getWidth() - end - (rect.width() >> 1),
                getHeight() - bottom + rect.height() + 10, paint_back);
    }


}
