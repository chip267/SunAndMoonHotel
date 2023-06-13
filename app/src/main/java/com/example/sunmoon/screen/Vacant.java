package com.example.sunmoon.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sunmoon.R;
import com.example.sunmoon.adapter.BookedRoomAdapter;
import com.example.sunmoon.adapter.GuestAdapter;
import com.example.sunmoon.adapter.RecyclerViewAdapter;
import com.example.sunmoon.adapter.VacantAdapter;
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

public class Vacant extends AppCompatActivity {

    List<Room> listroom = new ArrayList<>();
    RecyclerView vacantRecyclerView;
    VacantAdapter Adapter;
    ImageView btn_back;

    Button allroom, booked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_vacant);

        btn_back = findViewById(R.id.imageViewBackhome);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
                finish();
            }
        });

        allroom = findViewById(R.id.btn_AllRooms);
        allroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AllRoom.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        booked = findViewById(R.id.btn_Booked);
        booked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Booked.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        vacantRecyclerView = findViewById(R.id.list_vacant_room);//recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        vacantRecyclerView.setLayoutManager(layoutManager);
        Adapter = new VacantAdapter();
        vacantRecyclerView.setAdapter(Adapter);
        Adapter.setOnButtonClickListener((VacantAdapter.OnButtonClickListener) this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference conditionsRef = database.getReference("Conditions");
        conditionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    List<Room> filteredConditions = new ArrayList<>();
                    for (DataSnapshot Snapshot : snapshot.getChildren()) {
                        Room room = Snapshot.getValue(Room.class);
                        if (room != null && room.isrAvail() == 0) {
                            // Check if the condition's date falls within the current week

                                filteredConditions.add(room);

                        }
                    }
                    Adapter.setData(filteredConditions);
                    Adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Vacant.this, "Error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}