package com.www233.uitest.viewtest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.www233.uitest.R;

public class MyView extends View {
    CharSequence title = "";
    CharSequence content = "";
    int color;

    public MyView(Context context) {
        this(context, null);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("myView", "onClick: ??");
                ((Activity)context).finish();
            }
        });

        final Resources.Theme theme = context.getTheme();
        TypedArray a = theme.obtainStyledAttributes(attrs,
                R.styleable.MyView, defStyleAttr, defStyleRes);
        int n = a.getIndexCount();

        for(int i = 0;i<n;i++)
        {
            int attr = a.getIndex(i);
            if (attr == R.styleable.MyView_myTitle) {
                title = a.getText(attr);
            } else if (attr == R.styleable.MyView_myContent) {
                content = a.getText(attr);
            } else if (attr == R.styleable.MyView_myBackground) {
                color = a.getColor(attr, defStyleAttr);
            }
        }
        a.recycle();

        Log.e("MyView", "MyView:(title) " + title );
        Log.e("MyView", "MyView:(content) " + content );

    }

    @SuppressLint({"DrawAllocation", "ResourceAsColor"})
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        @SuppressLint("DrawAllocation") Paint paint = new Paint();
        Path path = new Path();

        float half = getHeight() / 2f;
        float len = getHeight()/4f;
        paint.setStrokeWidth(8);

        paint.setAntiAlias(true);//抗锯齿

//        Paint textPaint=new Paint();
//        textPaint.setStyle(Paint.Style.FILL);
//        textPaint.setStrokeWidth(8);
//        textPaint.setTextSize(len);
//
//        Paint.FontMetrics fontMetrics=textPaint.getFontMetrics();
//        float distance=(fontMetrics.bottom - fontMetrics.top)/2;

        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(half);
        canvas.drawText(title.toString(), len*4, half + half/2 , paint);     // 文字
        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setTextSize(len);
        canvas.drawText(content.toString(), getWidth() - len, half + len/2, paint);


        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        path.moveTo(len, half);
        path.lineTo(len*3, half - len);
        path.lineTo(len*3,half + len);
        path.close();
        canvas.drawPath(path,paint);    // 三角形


        paint.setColor(R.color.green_light);
        paint.setStrokeWidth(3);
        canvas.drawLine(20f, getHeight() - 3f, getWidth() - 20f, getHeight() - 3f, paint);// 线


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        switch (widthMode)
        {
            case MeasureSpec.EXACTLY:   // match_parent
                break;
            case MeasureSpec.AT_MOST:   // wrap_content
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }
    }

}
