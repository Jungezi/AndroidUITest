package com.www233.uitest.viewtest;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.www233.uitest.R;

public class MyButtonView extends FrameLayout {
    Drawable picture;
    float textsize;
    CharSequence text;
    int mLayout;
    LinearLayout linearLayout;

    public MyButtonView(Context context) {
        this(context, null);
    }

    public MyButtonView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyButtonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MyButtonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        initAttr(context, attrs, defStyleAttr, 0);
        initView(context);

    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.my_view_button, this, true);
        ImageView iv = findViewById(R.id.iv);
        TextView tv_up = findViewById(R.id.tv_up);
        TextView tv_down = findViewById(R.id.tv_down);
        linearLayout = findViewById(R.id.button);


        if(mLayout == 1)
        {
            tv_down.setVisibility(View.GONE);
            tv_up.setTextSize(TypedValue.COMPLEX_UNIT_PX, textsize);
            tv_up.setText(text);
        }
        else
        {
            tv_up.setVisibility(View.GONE);
            tv_down.setTextSize(TypedValue.COMPLEX_UNIT_PX, textsize);
            tv_down.setText(text);
        }

    }

    private void initAttr(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        final Resources.Theme theme = context.getTheme();
        TypedArray a = theme.obtainStyledAttributes(attrs,
                R.styleable.MyButtonView, defStyleAttr, defStyleRes);
        picture = a.getDrawable(R.styleable.MyButtonView_BVpicture);
        textsize = a.getDimension(R.styleable.MyButtonView_BVtextsize, 10);
        text = a.getText(R.styleable.MyButtonView_BVtext);
        mLayout = a.getInt(R.styleable.MyButtonView_BVlayout, 1);
        a.recycle();

        Log.e("myView", "initAttr: " + textsize );
        Log.e("myView", "initAttr: " + text );

    }

    public void setOnClickListener(OnClickListener onclicklistener)
    {
        linearLayout.setOnClickListener(onclicklistener);
    }

}
