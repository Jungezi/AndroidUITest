package com.www233.uitest.ellipsizetest;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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
        if (ellipsisHintToExpand == null) ellipsisHintToExpand = "展开";
        else ellipsisHintToExpand = hintToExpand;

        String hintToFold = a.getString(R.styleable.EllipsizeTextView_hint_text_toFold);
        if (ellipsisHintToFold == null) ellipsisHintToFold = "收起";
        else ellipsisHintToFold = hintToFold;

        ellipsisHintColor = a.getColor(R.styleable.EllipsizeTextView_hint_color, getResources().getColor(R.color.blue, theme));
        a.recycle();

        setMaxLines(maxLine);    // 初始化时测量准确
//        setHighlightColor(getResources().getColor(R.color.blue_bg, theme));


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

    int findMaxEllipsize(String str, float maxWidth) {
        float width_elli = getPaint().measureText(str);
        int len = str.length();
        Log.i(TAG, String.format("findMaxEllipsize: all:%s max:%s", width_elli, maxWidth));
        while (width_elli > maxWidth) {
            width_elli -= getPaint().measureText(str, len - 1, len);
            Log.i(TAG, String.format("findMaxEllipsize: all:%s max:%s", width_elli, maxWidth));
            len -= 1;
        }
        while (str.charAt(len - 1) == '\n') len--;
        return len;
    }

    private SpannableStringBuilder getFoldSpan() {
        if (foldString != null) return foldString;
        // 展开文字
        CharSequence foldText_last_line = originText.subSequence(getLayout().getLineStart(maxLine - 1),
                getLayout().getLineEnd(maxLine - 1));
        CharSequence foldText = originText.subSequence(0, getLayout().getLineStart(maxLine - 1)
                + findMaxEllipsize(foldText_last_line.toString(),
                getWidth() - getPaint().measureText(ellipsisHintSign + ellipsisHintToExpand + ellipsisHintSign)));
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
                           }, foldString.length() - ellipsisHintToExpand.length(),
                foldString.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return foldString;
    }

    private SpannableStringBuilder getExpandSpan() {
        if (expandString != null) return expandString;
        // 收起文字
        expandString = new SpannableStringBuilder();
        expandString.append(originText);
//        expandString.append("\n");
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
                             }, expandString.length() - ellipsisHintToFold.length(),
                expandString.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return expandString;
    }

    private void refreash() {
        foldString = null;
        expandString = null;
        initState = FOLD;
        setMaxLines(maxLine);    // 初始化时测量准确
        setText(originText);
    }

    public void setMaxLine(int maxLines) {
        this.maxLine = maxLines;
        refreash();
    }

    public void setProfile(CharSequence text) {
        originText = text;
        refreash();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d(TAG, "onLayout: ");
        super.onLayout(changed, left, top, right, bottom);

        if (initState != -1 && getLineCount() <= maxLine)    // 无需收起展开
        {
            initState = INHIBIT;
        } else {
            setMaxLines(Integer.MAX_VALUE);    // 无需再用到自带方法
            getExpandSpan();
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
