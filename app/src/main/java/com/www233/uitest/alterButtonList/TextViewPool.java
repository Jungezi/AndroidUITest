package com.www233.uitest.alterButtonList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pools;

public class TextViewPool {

    private static final String TAG = "ButtonPageScroll";
    public TextView tv;
    private static final Pools.SynchronizedPool<TextViewPool> sPool =
            new Pools.SynchronizedPool<>(30);
    public TextViewPool(Context context){
        tv = new TextView(context);
    }
    public static TextViewPool obtain(Context context) {
        TextViewPool instance = sPool.acquire();
        if(instance !=null) Log.d(TAG, "obtain: 复用！");
        else Log.d(TAG, "obtain: 新的！");
        return (instance != null) ? instance : new TextViewPool(context);
    }

    public void recycle() {
        Log.i(TAG, "finalize: 组件销毁！");
        sPool.release(this);
    }

}
