package com.www233.uitest.ellipsizetest;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.www233.uitest.R;

public class EllipsizeTextView extends androidx.appcompat.widget.AppCompatTextView {
    private static final String TAG = "EllipsizeTextView";
    private final String ellipsisHintSign = "\u2026";   // "…"
    private String ellipsisHintToExpand;
    private String ellipsisHintToFold;
    private int initState, maxLine, ellipsisHintColor;
    private static final int FOLD = 0, EXPAND = 1, INHIBIT = 2;
    CharSequence originText;
    SpannableStringBuilder foldString = null;
    SpannableStringBuilder expandString = null;

    public EllipsizeTextView(@NonNull Context context) {
        this(context, null);
    }

    public EllipsizeTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EllipsizeTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        Log.d(TAG, "EllipsizeTextView: 初始化");

        originText = getText();

        final Resources.Theme theme = context.getTheme();
        TypedArray a = theme.obtainStyledAttributes(attrs, R.styleable.EllipsizeTextView, 0, 0);
        initState = a.getInt(R.styleable.EllipsizeTextView_state, FOLD);
        maxLine = a.getInt(R.styleable.EllipsizeTextView_max_lines, 5);

        String hintToExpand = a.getString(R.styleable.EllipsizeTextView_hint_text_toExpand);
        if (ellipsisHintToExpand == null) ellipsisHintToExpand = "  展开";
        else ellipsisHintToExpand = "  " + hintToExpand;

        String hintToFold = a.getString(R.styleable.EllipsizeTextView_hint_text_toFold);
        if (ellipsisHintToFold == null) ellipsisHintToFold = "  收起";
        else ellipsisHintToFold = "  " + hintToFold;

        ellipsisHintColor = a.getColor(R.styleable.EllipsizeTextView_hint_color, getResources().getColor(R.color.blue, theme));
        a.recycle();

        setMaxLines(maxLine);    // 初始化时测量准确


    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw: ");
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure: ");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private SpannableStringBuilder getFoldSpan() {
        if (foldString != null) return foldString;
        // 收起文字
        CharSequence foldText_last_line = ellipsisHintToExpand + originText.subSequence(getLayout().getLineStart(maxLine - 1), getLayout().getLineEnd(maxLine - 1));

        CharSequence foldText_last_line_elli = TextUtils.ellipsize(foldText_last_line, getPaint(), getWidth(), TextUtils.TruncateAt.END);
        CharSequence foldText = originText.subSequence(0, getLayout().getLineStart(maxLine - 1) - 1 + foldText_last_line_elli.length() - ellipsisHintToExpand.length() - ellipsisHintSign.length());

        Log.i(TAG, String.format("getFoldSpan: 原来len[%d]", foldText.length()));
//        foldText = foldText.subSequence(0, foldText.length() - ellipsisHintToExpand.length() - ellipsisHintSign.length());
        Log.i(TAG, String.format("getFoldSpan: [%s]", foldText));
        Log.i(TAG, String.format("getFoldSpan: 展开len[%d]", ellipsisHintToExpand.length()));
        Log.i(TAG, String.format("getFoldSpan: 标志len[%d]", ellipsisHintSign.length()));
        foldString = new SpannableStringBuilder();
        foldString.append(foldText);
        foldString.append(ellipsisHintSign);
        foldString.append(ellipsisHintToExpand);
        foldString.setSpan(new ClickableSpan() {
                               @Override
                               public void updateDrawState(@NonNull TextPaint ds) {
                                   super.updateDrawState(ds);
                                   ds.setColor(ellipsisHintColor);//设置颜色
                                   ds.setUnderlineText(false);//去掉下划线
                               }

                               @Override
                               public void onClick(@NonNull View widget) {
                                   setText(getExpandSpan());
                                   setMovementMethod(LinkMovementMethod.getInstance());
                               }
                           }, foldText.length() + ellipsisHintSign.length(),
                foldString.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return foldString;
    }

    private SpannableStringBuilder getExpandSpan() {
        if (expandString != null) return expandString;
        // 展开文字
        expandString = new SpannableStringBuilder();
        expandString.append(originText);
        expandString.append(ellipsisHintToFold);
        expandString.setSpan(new ClickableSpan() {
                                 @Override
                                 public void updateDrawState(@NonNull TextPaint ds) {
                                     super.updateDrawState(ds);
                                     ds.setColor(ellipsisHintColor);//设置颜色
                                     ds.setUnderlineText(false);//去掉下划线
                                 }

                                 @Override
                                 public void onClick(@NonNull View widget) {
                                     setText(getFoldSpan());
                                     setMovementMethod(LinkMovementMethod.getInstance());
                                 }
                             }, originText.length(),
                expandString.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return expandString;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d(TAG, "onLayout: ");
        super.onLayout(changed, left, top, right, bottom);

        if (initState != -1 && getLineCount() <= maxLine)    // 无需收起展开
        {
            initState = INHIBIT;
        } else
        {
            setMaxLines(Integer.MAX_VALUE);    // 无需再用到自带方法
            if (initState == FOLD)    // 初始化为收起状态
            {
                initState = -1;  // 已初始化完毕
                setText(getFoldSpan());
                setMovementMethod(LinkMovementMethod.getInstance());
                requestLayout();
            } else if (initState == EXPAND) {
                initState = -1;  // 已初始化完毕
                setText(getExpandSpan());
                setMovementMethod(LinkMovementMethod.getInstance());
                requestLayout();
            }
        }
        Log.d(TAG, "onLayout: done");
    }
}
