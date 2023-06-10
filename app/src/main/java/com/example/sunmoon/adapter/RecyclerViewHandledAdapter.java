package com.example.sunmoon.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunmoon.R;
import com.example.sunmoon.models.Conditions;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewHandledAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<Conditions> conditions = new ArrayList<>();
    public interface OnButtonClickListener {
        void onDeleteButtonClick(String reportId);
    }

    private RecyclerViewHandledAdapter.OnButtonClickListener onButtonClickListener;

    public void setOnButtonClickListener(RecyclerViewHandledAdapter.OnButtonClickListener listener) {
        this.onButtonClickListener = listener;
    }
    public void setData(List<Conditions> conditions) {
        this.conditions = conditions;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkroom_item_handled_view, parent, false);
        return new RecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Conditions condition = conditions.get(position);
        holder.tvStatus.setText(condition.getState());
        holder.tvBy.setText(condition.getName());
        holder.tvTimeDate.setText(condition.getDate());
        holder.tvRoomNum.setText("Room "+condition.getRoomNo());
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onButtonClickListener != null) {
                    String reportId = condition.getReportID();
                    onButtonClickListener.onDeleteButtonClick(reportId);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return conditions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStatus;
        TextView tvBy;
        TextView tvTimeDate;
        TextView tvRoomNum;
        AppCompatButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tv_status_content);
            tvBy = itemView.findViewById(R.id.tv_by_content);
            tvTimeDate = itemView.findViewById(R.id.tv_time_date);
            tvRoomNum = itemView.findViewById(R.id.tv_roomnum);
            btnDelete = itemView.findViewById(R.id.btn_Delete);
        }
    }
}
