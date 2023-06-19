package com.example.sunmoon.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunmoon.R;
import com.example.sunmoon.models.Conditions;
import com.example.sunmoon.screen.CheckRoomHandled;
import com.example.sunmoon.screen.CheckRoomPending;
import com.example.sunmoon.screen.PersonelDetails;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewHandledAdapter extends RecyclerView.Adapter<RecyclerViewHandledAdapter.ViewHolder> {
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
    public RecyclerViewHandledAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkroom_item_handled_view, parent, false);
        return new RecyclerViewHandledAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHandledAdapter.ViewHolder holder, int position) {
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(holder.itemView.getContext());
                dialog.setContentView(R.layout.chechroom_done_popup);
                dialog.show();
                AppCompatButton close = dialog.findViewById(R.id.btn_close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                TextView room = dialog.findViewById(R.id.tv_room);
                TextView status = dialog.findViewById(R.id.tv_status1);
                TextView name = dialog.findViewById(R.id.tv_createby1);
                TextView time = dialog.findViewById(R.id.tv_timecreate1);
                TextView statusUpdate = dialog.findViewById(R.id.tv_updatestatus1);
                TextView nameUpdate = dialog.findViewById(R.id.tv_updateby1);
                TextView timeUpdate = dialog.findViewById(R.id.tv_timeupdate1);
                room.setText("Room "+condition.getRoomNo());
                status.setText(condition.getState());
                name.setText(condition.getName());
                time.setText(condition.getDate());
                statusUpdate.setText(condition.getStatusUpdate());
                nameUpdate.setText(condition.getNameUpdate());
                timeUpdate.setText(condition.getDateUpdate());
                ImageView image = dialog.findViewById(R.id.imageView15);
                String imageURL = condition.getImageURL();
                Picasso.get().load(imageURL).into(image);
                AppCompatButton delete = dialog.findViewById(R.id.btn_Delete);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String reportID = condition.getReportID();
                        DatabaseReference conditionsRef = FirebaseDatabase.getInstance().getReference("Conditions");
                        conditionsRef.child(reportID).child("avail").setValue(0)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(holder.itemView.getContext(), "Đã xóa thành công", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.itemView.getContext(), "Failed to set avail value: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                        dialog.dismiss();
                    }
                });
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
