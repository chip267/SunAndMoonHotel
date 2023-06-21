package com.example.sunmoon.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunmoon.R;
import com.example.sunmoon.adapter.CustomAdapter;
import com.example.sunmoon.adapter.ReportAdapter;
import com.example.sunmoon.models.Room;
import com.example.sunmoon.models.UserSingleton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AllRoom extends AppCompatActivity {
    private ImageView imageViewBack;
    private ImageView imageViewAddRoom;
    private AppCompatButton bookedButton;
    private AppCompatButton vacantButton;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private Dialog passcodeDialog;
    private String passCode;

    private ImageButton buttonclose;
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
                passcodeDialog = new Dialog(AllRoom.this);
                passcodeDialog.setContentView(R.layout.passcode_adding_popup);
                passcodeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                passcodeDialog.show();
                buttonclose = passcodeDialog.findViewById(R.id.imageButtonClose);
                buttonclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        passcodeDialog.dismiss();
                    }
                });
                AppCompatButton doneCodeBtn = passcodeDialog.findViewById(R.id.btn_Done);
                TextView inputPassCode = passcodeDialog.findViewById(R.id.box_room);
                doneCodeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference accountData = FirebaseDatabase.getInstance().getReference("Account");
                        passCode = inputPassCode.getText().toString().trim();
                        if (TextUtils.isEmpty(passCode)){
                            inputPassCode.requestFocus();
                            inputPassCode.setError("Please enter passcode!");
                            return;
                        }
                        String usr = UserSingleton.getInstance().getUserName();
                        Query checkUserDatabase = accountData.orderByChild("aUsername").equalTo(usr);
                        String finalPasscode = passCode;
                        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    String getPasscode = snapshot.child(usr).child("aPasscode").getValue(String.class);
                                    if (getPasscode.equals(finalPasscode)){
                                        Toast.makeText(AllRoom.this, "Successfully",
                                                Toast.LENGTH_SHORT).show();
                                        passcodeDialog.dismiss();
                                        Intent intent = new Intent(AllRoom.this, AllRoomAdd.class);
                                        startActivity(intent);
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    }
                                    else{
                                        inputPassCode.requestFocus();
                                        inputPassCode.setError("Passcode is invalid!");
                                    }
                                }
                                else{
                                    Toast.makeText(AllRoom.this, "Failed",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
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