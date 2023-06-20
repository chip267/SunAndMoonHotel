package com.example.sunmoon.screen;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunmoon.R;
import com.example.sunmoon.adapter.CustomAdapter;
import com.example.sunmoon.adapter.CustomAddAdapter;
import com.example.sunmoon.models.Conditions;
import com.example.sunmoon.models.Room;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AllRoomAdd extends AppCompatActivity {
    private AppCompatButton doneButton;
    private AppCompatButton addButton;
    private EditText hourEditText, dayEditText;
    public String roomType ="";
    private Dialog dialog;
    private RecyclerView recyclerView;
    private CustomAddAdapter adapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_rooms_2);
        recyclerView = findViewById(R.id.list_room);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new CustomAddAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new CustomAddAdapter.OnItemClickListener() {
            @Override
            public void onVacantItemClick(Room room) {
                String roomID = room.getRoomID();
                Dialog dialog1 = new Dialog(AllRoomAdd.this);
                dialog1.setContentView(R.layout.edit_room_popup);
                dialog1.show();
                AppCompatButton cancelButton = dialog1.findViewById(R.id.btn_cancelroomedit);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });
                TextView roomNum = dialog1.findViewById(R.id.tv_roomEdit);
                EditText hourEdit = dialog1.findViewById(R.id.box_houredit);
                EditText dayEdit = dialog1.findViewById(R.id.box_dayedit);
                RadioGroup radioGroup = dialog1.findViewById(R.id.radiogroupEdit);
                roomNum.setText("Room " + roomID);
                AppCompatButton doneButton = dialog1.findViewById(R.id.btn_doneroomedit);
                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String hourEditText, dayEditText;
                        hourEditText = hourEdit.getText().toString().trim();
                        dayEditText = dayEdit.getText().toString().trim();
                        if (TextUtils.isEmpty(hourEditText)){
                            Toast.makeText(AllRoomAdd.this, "Enter Price Hour",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(dayEditText)){
                            Toast.makeText(AllRoomAdd.this, "Enter Price Day",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                        if (checkedRadioButtonId == -1) {
                            Toast.makeText(AllRoomAdd.this, "Select a room type", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String hourPrice = hourEdit.getText().toString();
                        Double hour = Double.valueOf(hourPrice);
                        String dayPrice = dayEdit.getText().toString();
                        Double day = Double.valueOf(dayPrice);
                        checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                        if (checkedRadioButtonId == R.id.radioButtonStandardEdit) {
                            roomType = "Standard";
                        } else if (checkedRadioButtonId == R.id.radioButtonDeluxeEdit) {
                            roomType = "Deluxe";
                        }
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference roomRef = database.getReference("Room").child(roomID);
                        roomRef.child("roomType").setValue(roomType);
                        roomRef.child("pricebyHour").setValue(hour);
                        roomRef.child("pricebyDay").setValue(day);
                        dialog1.dismiss();
                    }
                });
            }
            @Override
            public void onBookedItemClick(Room room) {
                String roomID = room.getRoomID();
                Dialog dialog1 = new Dialog(AllRoomAdd.this);
                dialog1.setContentView(R.layout.edit_room_popup);
                dialog1.show();
                AppCompatButton cancelButton = dialog1.findViewById(R.id.btn_cancelroomedit);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });
                TextView roomNum = dialog1.findViewById(R.id.tv_roomEdit);
                EditText hourEdit = dialog1.findViewById(R.id.box_houredit);
                EditText dayEdit = dialog1.findViewById(R.id.box_dayedit);
                RadioGroup radioGroup = dialog1.findViewById(R.id.radiogroupEdit);
                roomNum.setText("Room " + roomID);
                AppCompatButton doneButton = dialog1.findViewById(R.id.btn_doneroomedit);
                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String hourEditText, dayEditText;
                        hourEditText = hourEdit.getText().toString().trim();
                        dayEditText = dayEdit.getText().toString().trim();
                        if (TextUtils.isEmpty(hourEditText)){
                            Toast.makeText(AllRoomAdd.this, "Enter Price Hour",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(dayEditText)){
                            Toast.makeText(AllRoomAdd.this, "Enter Price Day",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                        if (checkedRadioButtonId == -1) {
                            Toast.makeText(AllRoomAdd.this, "Select a room type", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String hourPrice = hourEdit.getText().toString();
                        Double hour = Double.valueOf(hourPrice);
                        String dayPrice = dayEdit.getText().toString();
                        Double day = Double.valueOf(dayPrice);
                        checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                        if (checkedRadioButtonId == R.id.radioButtonStandardEdit) {
                            roomType = "Standard";
                        } else if (checkedRadioButtonId == R.id.radioButtonDeluxeEdit) {
                            roomType = "Deluxe";
                        }
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference roomRef = database.getReference("Room").child(roomID);
                        roomRef.child("roomType").setValue(roomType);
                        roomRef.child("pricebyHour").setValue(hour);
                        roomRef.child("pricebyDay").setValue(day);
                        dialog1.dismiss();
                    }
                });
            }
        });
        adapter.fetchData();
        doneButton = findViewById(R.id.btn_doneRoom);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllRoomAdd.this, AllRoom.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        addButton = findViewById(R.id.btn_addroom);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(AllRoomAdd.this);
                dialog.setContentView(R.layout.addrooms_popup);
                dialog.show();
                AppCompatButton cancelButton = dialog.findViewById(R.id.btn_cancelroomadd);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                hourEditText = dialog.findViewById(R.id.box_hour);
                dayEditText = dialog.findViewById(R.id.box_day);
                TextView roomAddTextView = dialog.findViewById(R.id.tv_roomAdd);
                DatabaseReference roomRef = FirebaseDatabase.getInstance().getReference("Room");
                roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int highestNumber = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String roomId = snapshot.getKey();
                            try {
                                int number = Integer.parseInt(roomId);
                                if (number > highestNumber) {
                                    highestNumber = number;
                                }
                            } catch (NumberFormatException e) {
                                // Ignore invalid room IDs
                            }
                        }
                        int nextRoomNumber = highestNumber + 1;
                        String nextRoomId = String.format(Locale.US, "%03d", nextRoomNumber);
                        roomAddTextView.setText("Room " + nextRoomId);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle database error
                    }
                });

                AppCompatButton doneButton = dialog.findViewById(R.id.btn_doneroomadd);
                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String hourPrice = hourEditText.getText().toString();
                        Double hour = Double.valueOf(hourPrice);
                        String dayPrice = dayEditText.getText().toString();
                        Double day = Double.valueOf(dayPrice);
                        RadioGroup radioGroup = dialog.findViewById(R.id.radiogroupType);
                        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                        if (checkedRadioButtonId == R.id.radioButtonStandard) {
                            roomType = "Standard";
                        } else if (checkedRadioButtonId == R.id.radioButtonDeluxe) {
                            roomType = "Deluxe";
                        }
                        generateroomId(new RoomIdCallback() {
                            @Override
                            public void onRoomIdGenerated(String roomId) {
                                int avail = 0;
                                Room rooms = new Room(roomId, roomType, hour, day, avail);
                                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Room");
                                databaseRef.child(roomId).setValue(rooms);
                                dialog.dismiss();
                            }
                            @Override
                            public void onRoomIdGenerationFailed(String errorMessage) {
                                // Handle the report ID generation failure
                                // You can display an error message or perform error handling logic here
                            }
                        });
                    }
                });
            }
        });
    }
    private void generateroomId(final RoomIdCallback callback) {
        final DatabaseReference roomRef = FirebaseDatabase.getInstance().getReference().child("Room");
        roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int highestNumber = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String roomId = snapshot.getKey();
                        try {
                            int number = Integer.parseInt(roomId);
                            if (number > highestNumber) {
                                highestNumber = number;
                            }
                        } catch (NumberFormatException e) {
                            // Ignore invalid report IDs
                        }
                }
                String newRoomId = String.format("%03d", highestNumber + 1);
                callback.onRoomIdGenerated(newRoomId);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
                callback.onRoomIdGenerationFailed(databaseError.getMessage());
            }
        });
    }
    interface RoomIdCallback {
        void onRoomIdGenerated(String roomId);
        void onRoomIdGenerationFailed(String errorMessage);
    }
}
