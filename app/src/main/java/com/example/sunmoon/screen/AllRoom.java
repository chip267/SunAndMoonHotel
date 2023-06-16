package com.example.sunmoon.screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.sunmoon.R;
import com.example.sunmoon.adapter.CustomAdapter;
import com.example.sunmoon.adapter.ReportAdapter;
import com.example.sunmoon.models.Room;

public class AllRoom extends AppCompatActivity {
    private ImageView imageViewBack;
    private ImageView imageViewAddRoom;
    private AppCompatButton bookedButton;
    private AppCompatButton vacantButton;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_rooms);
        imageViewBack = findViewById(R.id.imageViewBackhome);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllRoom.this, Home.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        imageViewAddRoom = findViewById(R.id.imageViewAddRoom);
        imageViewAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllRoom.this, AllRoomAdd.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        bookedButton = findViewById(R.id.btn_Booked);
        bookedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllRoom.this, Booked.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        vacantButton = findViewById(R.id.btn_Vacant);
        vacantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllRoom.this, Vacant.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        recyclerView = findViewById(R.id.list_room);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new CustomAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new CustomAdapter.OnItemClickListener() {
            @Override
            public void onVacantItemClick(Room room) {
                String roomID = room.getRoomID();
                Intent intent = new Intent(AllRoom.this, BookingForm.class);
                intent.putExtra("roomID", roomID);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
            @Override
            public void onBookedItemClick(Room room) {
                Intent intent = new Intent(AllRoom.this, Booked.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        adapter.fetchData();
    }
}