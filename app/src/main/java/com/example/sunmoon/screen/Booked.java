package com.example.sunmoon.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sunmoon.R;
import com.example.sunmoon.adapter.BookedRoomAdapter;
import com.example.sunmoon.adapter.GuestAdapter;

import com.example.sunmoon.adapter.RecyclerViewAdapter;
import com.example.sunmoon.models.Booking;
import com.example.sunmoon.models.Guest;
import com.example.sunmoon.models.Room;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Booked extends AppCompatActivity implements BookedRoomAdapter.OnButtonClickListener{
    List<Booking> bookedRooms;
    RecyclerView bookedRoomRecyclerView;
    BookedRoomAdapter roomAdapter;
    ImageView btnBack;

    AppCompatButton btnNavAllRoom, btnNavVacant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rooms_booked);

        btnNavAllRoom = findViewById(R.id.btn_AllRooms);
        btnNavAllRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Booked.this, AllRoom.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        btnNavVacant = findViewById(R.id.btn_Occupied);
        btnNavVacant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Booked.this, Vacant.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        bookedRoomRecyclerView = findViewById(R.id.booked_room);
        bookedRooms = new ArrayList<Booking>();

        roomAdapter = new BookedRoomAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        bookedRoomRecyclerView.setAdapter(roomAdapter);
        bookedRoomRecyclerView.setLayoutManager(linearLayoutManager);
        roomAdapter.setOnButtonClickListener(this);
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
                                        }
                                    }
                                    roomAdapter.setData(bookedRooms);
                                    roomAdapter.notifyDataSetChanged();
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
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void onCheckOutButtonClick(String bookedId, String roomID) {
        FirebaseDatabase.getInstance().getReference("Booking").child(bookedId).child("status").setValue("checkout");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy");
        String time = timeFormat.format(new Date());
        String date = dateFormat.format(new Date());
        FirebaseDatabase.getInstance().getReference("Booking").child(bookedId).child("checkoutDate").setValue(date);
        FirebaseDatabase.getInstance().getReference("Booking").child(bookedId).child("checkoutHour").setValue(time);

        FirebaseDatabase.getInstance().getReference("Room").orderByChild("roomID").equalTo(roomID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
            public void onDataChange(@NonNull DataSnapshot roomDataSnapshot) {
                if (roomDataSnapshot.exists()) {
                    FirebaseDatabase.getInstance().getReference("Room").child(roomID).child("rAvail").setValue(0);
                    for (int i = 0; i < bookedRooms.size(); i++){
                        if(bookedRooms.get(i).getBookingID() == bookedId){
                            bookedRooms.remove(i);
                            roomAdapter.setData(bookedRooms);
                            roomAdapter.notifyItemRemoved(i);
                            break;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur during the query
            }
        });
        FirebaseDatabase.getInstance().getReference("Booking").child(bookedId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot bookingDataSnapshot) {
                        if (bookingDataSnapshot.exists()) {
                            String checkinDate = bookingDataSnapshot.child("checkinDate").getValue(String.class);
                            String checkinHour = bookingDataSnapshot.child("checkinHour").getValue(String.class);
                                if (checkinDate.equals(date)) {
                                    try {
                                        Date checkin = timeFormat.parse(checkinHour);
                                        Date checkout = timeFormat.parse(time);
                                        long differenceMillis = checkout.getTime() - checkin.getTime();
                                        float differenceHoursFloat = (float) differenceMillis / (60 * 60 * 1000);
                                        int differenceHours = (int) Math.ceil(differenceHoursFloat);
                                        FirebaseDatabase.getInstance().getReference("Room").orderByChild("roomID").equalTo(roomID).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot roomDataSnapshot) {
                                                        if (roomDataSnapshot.exists()) {
                                                            for (DataSnapshot snapshot : roomDataSnapshot.getChildren()) {
                                                                int pricePerHour = snapshot.child("pricebyHour").getValue(Integer.class);
                                                                int total = differenceHours * pricePerHour;
                                                                FirebaseDatabase.getInstance().getReference("Booking").child(bookedId).child("total").setValue(total);
                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        // Handle any errors that occur during the query
                                                    }
                                                });
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    try {
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                        Date checkin = dateFormat.parse(checkinDate);
                                        Date checkout = dateFormat.parse(date);
                                        long differenceMillis = checkout.getTime() - checkin.getTime();
                                        long differenceDays = TimeUnit.MILLISECONDS.toDays(differenceMillis);
                                        FirebaseDatabase.getInstance().getReference("Room").orderByChild("roomID").equalTo(roomID).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot roomDataSnapshot) {
                                                if (roomDataSnapshot.exists()) {
                                                    for (DataSnapshot snapshot : roomDataSnapshot.getChildren()) {
                                                        int pricePerDay = snapshot.child("pricebyDay").getValue(Integer.class);
                                                        int total = (int) (differenceDays * pricePerDay);
                                                        FirebaseDatabase.getInstance().getReference("Booking").child(bookedId).child("total").setValue(total);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                // Handle any errors that occur during the query
                                            }
                                        });
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle any errors that occur during the query
                    }
                });
    }
}