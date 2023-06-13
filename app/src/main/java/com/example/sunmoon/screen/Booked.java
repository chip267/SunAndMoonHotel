package com.example.sunmoon.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.sunmoon.R;
import com.example.sunmoon.adapter.BookedRoomAdapter;
import com.example.sunmoon.adapter.GuestAdapter;

import com.example.sunmoon.models.Booking;
import com.example.sunmoon.models.Guest;
import com.example.sunmoon.models.Room;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Booked extends AppCompatActivity {
    List<Booking> bookedRooms;
    List<Guest> guests;
    List<Room> rooms;
    RecyclerView bookedRoomRecyclerView;
    BookedRoomAdapter roomAdapter;

    Guest guestInfor;
    Room roomInfor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rooms_booked);

        bookedRoomRecyclerView = findViewById(R.id.booked_room);
        bookedRooms = new ArrayList<Booking>();
        /*for (int i = 1; i <= 50; i++) {
            bookedRooms.add(new Booking("12/5/2020", "14/5/2020", "10"+i , "Nguyen" + i, "090836335" + i));
        }*/

        roomAdapter = new BookedRoomAdapter(bookedRooms, this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        bookedRoomRecyclerView.setAdapter(roomAdapter);
        bookedRoomRecyclerView.setLayoutManager(linearLayoutManager);

        FirebaseDatabase.getInstance().getReference("Booking").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot bookingDataSnapshot) {
                if (bookingDataSnapshot.exists()){
                    for (DataSnapshot bookingSnapshot:bookingDataSnapshot.getChildren()){
                        Booking booking = bookingSnapshot.getValue(Booking.class);
                        booking.setBookingID(bookingSnapshot.getKey());


                        FirebaseDatabase.getInstance().getReference("Room").orderByChild("roomID").equalTo(booking.getRid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot roomDataSnapshot) {
                                if (roomDataSnapshot.exists()){
                                    for (DataSnapshot roomSnapshot : roomDataSnapshot.getChildren()){
                                        int isAvail = roomSnapshot.child("rAvail").getValue(Integer.class);
                                        if (isAvail == 1){
                                            bookedRooms.add(booking);
                                            roomAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }
                else {
                    Toast.makeText(Booked.this, "Not exist!!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}