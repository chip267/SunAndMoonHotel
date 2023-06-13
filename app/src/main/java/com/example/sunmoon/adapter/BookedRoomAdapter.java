package com.example.sunmoon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunmoon.MainActivity;
import com.example.sunmoon.R;
import com.example.sunmoon.models.Booking;
import com.example.sunmoon.models.Guest;
import com.example.sunmoon.screen.Booked;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    public void onBindViewHolder(@NonNull BookedRoomAdapter.ViewHolder holder, int position) {
        Booking booked_room = (Booking) bookedRoom.get(position);
        String gidCard = booked_room.getGid();
        DatabaseReference guestRef = FirebaseDatabase.getInstance().getReference("Guest");
        guestRef.orderByChild("gIDCard").equalTo(gidCard).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot guestDataSnapshot) {
                if (guestDataSnapshot.exists()) {
                    for (DataSnapshot guestSnapshot : guestDataSnapshot.getChildren()) {
                        String gName = guestSnapshot.child("gName").getValue(String.class);
                        String gPhone = guestSnapshot.child("gPhone").getValue(String.class);
                        holder.guestName.setText(gName);
                        holder.guestPhone.setText(gPhone);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur during the query
            }
        });

        holder.checkInDate.setText(booked_room.getCheckinDate());
        holder.checkOutDate.setText(booked_room.getCheckoutDate());
        holder.roomID.setText("Room " + booked_room.getRid());
        holder.room = booked_room;
        
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
        //public String rID;

        public Booking room;

        public ViewHolder(View itemView) {
            super(itemView);
            itemview = itemView;
            checkInDate = itemView.findViewById(R.id.tvStart1);
            checkOutDate = itemView.findViewById(R.id.tvEnd1);
            roomID = itemView.findViewById(R.id.tvRoom110);
            guestName = itemView.findViewById(R.id.tvName1);
            guestPhone = itemView.findViewById(R.id.tvPhone1);

            itemView.findViewById(R.id.btn_Checkout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase.getInstance().getReference("Booking").child(room.getBookingID()).child("status").setValue("checkout");
                    FirebaseDatabase.getInstance().getReference("Room").orderByChild("roomID").equalTo(room.getRid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot roomDataSnapshot) {
                            if (roomDataSnapshot.exists()) {
                                FirebaseDatabase.getInstance().getReference("Room").child(room.getRid()).child("rAvail").setValue(0);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle any errors that occur during the query
                        }
                    });
                    //FirebaseDatabase.getInstance().getReference("Room").child(checkRoomID).child("rAvail").setValue(0);
                }
            });
        }
    }
}
