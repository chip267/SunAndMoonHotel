package com.example.sunmoon.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.sunmoon.R;
import com.example.sunmoon.models.Booking;
import com.example.sunmoon.models.Guest;
import com.example.sunmoon.models.Room;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookingForm extends AppCompatActivity {
    public EditText Room, Name, DOB, Phone, Idcard;
    public Button Confirm;
    public String TypeOfBooking ="";
    public String Gender ="";
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference databaseReference;
    public Booking booking;
    public Room groom;
    public Guest guest;
    public ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_form);

        Room = findViewById(R.id.box_room);

        Name = findViewById(R.id.box_name);
        DOB = findViewById(R.id.box_dateofbirth);

        Phone = findViewById(R.id.box_phone);
        Idcard = findViewById(R.id.box_idcard);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Booking");

        booking = new Booking();
        guest = new Guest();
        back = findViewById(R.id.imageViewBackhome);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Home.class);
                startActivity(intent);
                finish();
            }
        });

        Confirm = findViewById(R.id.btn_confirm);

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String room = Room.getText().toString();
                String typeofbooking = TypeOfBooking;

                String name = Name.getText().toString();
                String dob = DOB.getText().toString();
                String gender = Gender;
                String phone = Phone.getText().toString();
                String idcard = Idcard.getText().toString();

                addDatatoFireBase(room, typeofbooking, name, dob, gender, phone, idcard);
                Intent intent = new Intent(getApplicationContext(),Home.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void RadioButtonTypeClicked(View view)
    {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radioButton2:
                if (checked)
                    TypeOfBooking = "Day";
                break;
            case R.id.radioButton3:
                if (checked)
                    TypeOfBooking = "Hour";
                break;
        }
    }
    public void RadioButtonGenderClicked(View view)
    {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radioButtonFemale:
                if (checked)
                    Gender = "Female";
                break;
            case R.id.radioButtonMale:
                if (checked)
                    Gender = "Male";
                break;
        }
    }
    //add data
    private void addDatatoFireBase(String room, String typeofbooking, String name, String dob,String gender, String phone, String idcard)
    {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = rootRef.child("Room");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> list = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String uid = ds.getKey();
                    list.add(uid);
                }
                for(String idroom: list)
                {
                    if(idroom == room)
                    {
                        booking.setBookingType(typeofbooking);
                        guest.setgName(name);
                        guest.setgBirthday(dob);
                        guest.setGender(gender);
                        guest.setgPhone(phone);
                        guest.setgIDCard(idcard);

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                databaseReference.setValue(booking);
                                databaseReference.setValue(guest);
                                Toast.makeText(BookingForm.this, "data added", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(BookingForm.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

    }
}