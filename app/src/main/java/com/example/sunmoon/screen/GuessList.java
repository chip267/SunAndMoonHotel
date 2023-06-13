package com.example.sunmoon.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sunmoon.MainActivity;
import com.example.sunmoon.R;
import com.example.sunmoon.adapter.GuestAdapter;
import com.example.sunmoon.models.Guest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import com.example.sunmoon.R;

public class GuessList extends AppCompatActivity {
    List<Guest> guests;
    RecyclerView guestRecyclerView;
    GuestAdapter gAdapter;
    private ImageView imageViewBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guest_list);
        imageViewBack = findViewById(R.id.imageViewBackGuest);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuessList.this, Home.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        guestRecyclerView = findViewById(R.id.guestList);
        guests = new ArrayList<Guest>();

        gAdapter = new GuestAdapter(guests, this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        guestRecyclerView.setAdapter(gAdapter);
        guestRecyclerView.setLayoutManager(linearLayoutManager);

        FirebaseDatabase.getInstance().getReference("Guest").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot guestSnapshot:snapshot.getChildren()){
                        Guest guest = guestSnapshot.getValue(Guest.class);
                        guest.setgIDCard(guestSnapshot.getKey());
                        guests.add(guest);
                    }
                    gAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(GuessList.this, "Not exist!!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}