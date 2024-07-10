package com.www233.uitest.charttest;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.www233.uitest.R;

public class PracticeChartRecyclerViewHolder extends RecyclerView.ViewHolder {
    TextView tv_key;
    TextView tv_value;

    public PracticeChartRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_key = itemView.findViewById(R.id.tv_key);
        tv_value = itemView.findViewById(R.id.tv_value);
    }
}
