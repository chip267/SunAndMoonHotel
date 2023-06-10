package com.example.sunmoon.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunmoon.R;
import com.example.sunmoon.models.Conditions;

import java.util.ArrayList;
import java.util.List;

public class HouseKeepingAdapter extends RecyclerView.Adapter<HouseKeepingAdapter.ViewHolder> {
    private List<Conditions> conditions = new ArrayList<>();

    public void setData(List<Conditions> conditions) {
        this.conditions = conditions;
    }

    @NonNull
    @Override
    public HouseKeepingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.housekeeping_item, parent, false);
        return new HouseKeepingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HouseKeepingAdapter.ViewHolder holder, int position) {
        Conditions condition = conditions.get(position);
        holder.tvStatus.setText(condition.getState());
        String dateTime = condition.getDate();
        String time = dateTime.substring(0, 5);
        holder.tvTimeDate.setText(time);
        holder.tvRoomNum.setText(condition.getRoomNo());
    }

    @Override
    public int getItemCount() {
        return conditions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStatus;
        TextView tvTimeDate;
        TextView tvRoomNum;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.textViewState);
            tvTimeDate = itemView.findViewById(R.id.textViewDate);
            tvRoomNum = itemView.findViewById(R.id.textviewRoom);
        }
    }
}
