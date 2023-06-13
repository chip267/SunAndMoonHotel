package com.example.sunmoon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunmoon.R;
import com.example.sunmoon.models.Booking;

import java.util.List;

public class BookedRoomAdapter extends RecyclerView.Adapter<BookedRoomAdapter.ViewHolder>{
    private List bookedRoom;
    private Context room_Context;

    public BookedRoomAdapter(List _bookedRoom, Context room_Context) {
        this.bookedRoom = _bookedRoom;
        this.room_Context = room_Context;
    }

    @NonNull
    @Override
    public BookedRoomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Nạp layout cho View biểu diễn phần tử room
        View roomView = inflater.inflate(R.layout.room_item_view, parent, false);

        ViewHolder viewHolder = new ViewHolder(roomView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Booking booked_room = (Booking) bookedRoom.get(position);

        holder.checkInDate.setText(booked_room.getCheckinDate());
        holder.checkOutDate.setText(booked_room.getCheckoutDate());
        holder.roomID.setText("Room " + booked_room.getRid());
        //holder.guestName.setText(booked_room.getGuestName());
        //holder.guestPhone.setText(booked_room.getGuestPhone());
    }

    @Override
    public int getItemCount() {
        return bookedRoom.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View itemview;
        public TextView checkInDate;
        public TextView checkOutDate;
        public TextView roomID;
        public TextView guestName;
        public TextView guestPhone;

        public ViewHolder(View itemView) {
            super(itemView);
            itemview = itemView;
            checkInDate = itemView.findViewById(R.id.tvStart1);
            checkOutDate = itemView.findViewById(R.id.tvEnd1);
            roomID = itemView.findViewById(R.id.tvRoom110);
            guestName = itemView.findViewById(R.id.tvName1);
            guestPhone = itemView.findViewById(R.id.tvPhone1);
        }
    }
}
