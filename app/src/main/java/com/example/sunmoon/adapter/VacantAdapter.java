package com.example.sunmoon.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunmoon.R;
import com.example.sunmoon.models.Booking;
import com.example.sunmoon.models.Conditions;
import com.example.sunmoon.models.Guest;
import com.example.sunmoon.models.Room;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VacantAdapter extends RecyclerView.Adapter<VacantAdapter.ViewHolder> {
    private List<Room> rooms = new ArrayList<>();
    public interface OnButtonClickListener {
        void onBookNowButtonClick(String roomId);
    }

    private OnButtonClickListener onButtonClickListener;

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.onButtonClickListener = listener;
    }
    public void setData(List<Room> rooms) {
        this.rooms = rooms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_vacant_item_view, parent, false);
        return new VacantAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Room room = rooms.get(position);

        holder.rid.setText("Room "+room.getRoomID());
        holder.rType.setText(room.getRoomType());
        holder.priceofDay.setText((int) room.getPricebyDay());

        holder.btnBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onButtonClickListener != null) {
                    String roomId = room.getRoomID();
                    onButtonClickListener.onBookNowButtonClick(roomId);
                }
            }
        });

        // Set the click listener for the "Delete" button

    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View itemview;
        public TextView rid;
        public TextView rType;
        public TextView priceofDay;
        public TextView note;

        AppCompatButton btnBookNow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemview = itemView;
            rid = itemView.findViewById(R.id.tv_room);
            rType = itemView.findViewById(R.id.tv_kind);
            priceofDay = itemView.findViewById(R.id.tv_money);
            note = itemView.findViewById(R.id.tv_note);
        }
    }
}


