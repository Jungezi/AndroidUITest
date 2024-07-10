package com.www233.uitest.charttest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.www233.uitest.R;

import java.util.List;

public class PracticeChartRecyclerViewAdapter extends RecyclerView.Adapter<PracticeChartRecyclerViewHolder> {

    List<PracticeChartInfoItem> InfoList;

    public PracticeChartRecyclerViewAdapter(List<PracticeChartInfoItem> InfoList) {
        this.InfoList = InfoList;
    }

    @NonNull
    @Override
    public PracticeChartRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holder_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.practive_chart_info_item, parent, false);


        return new PracticeChartRecyclerViewHolder(holder_view);
    }

    @Override
    public void onBindViewHolder(@NonNull PracticeChartRecyclerViewHolder holder, int position) {
        holder.tv_key.setText(InfoList.get(position).key);
        holder.tv_value.setText(InfoList.get(position).value);
        int color = InfoList.get(position).value_color;
        if(color != -1)
            holder.tv_value.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        return InfoList.size();
    }
}
