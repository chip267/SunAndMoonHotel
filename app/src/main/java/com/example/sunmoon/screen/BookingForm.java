package com.example.sunmoon.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sunmoon.R;
import com.example.sunmoon.models.Booking;
import com.example.sunmoon.models.Guest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookingForm extends AppCompatActivity {
    EditText CheckIn, Checkout, Room, Name, DOB, Gender, Phone,TypeOfRenting;
    ImageView Back;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Booking booking;
    Guest guest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.booking_form);
        super.onCreate(savedInstanceState);

        CheckIn = findViewById(R.id.boxStart1);
        Checkout = findViewById(R.id.boxEnd1);
        Room = findViewById(R.id.boxRoom);
        TypeOfRenting = findViewById(R.id.boxRent1);

        Name = findViewById(R.id.boxNameCus1);
        DOB = findViewById(R.id.boxDateCus1);
        Gender = findViewById(R.id.boxGenderCus1);
        Phone = findViewById(R.id.boxEndNumCus1);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("BookingInfo");

        booking = new Booking();
        guest = new Guest();

        Back = findViewById(R.id.imageView14);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkin = CheckIn.getText().toString();
                String checkout = Checkout.getText().toString();
                String room = Room.getText().toString();
                String typeofrenting = TypeOfRenting.getText().toString();

                String name = Name.getText().toString();
                String dob = DOB.getText().toString();
                String gender = Gender.getText().toString();
                String phone = Phone.getText().toString();

                addDatatoFireBase(checkin,checkout,room,typeofrenting,name,dob,gender,phone);
            }
        });
    }
    //add data
    private void addDatatoFireBase(String checkin, String checkout, String room, String typeofrenting, String name, String dob, String gender, String phone)
    {
        booking.setCheckinDate(checkin);
        booking.setCheckoutDate(checkout);
        booking.setRoomID(room);
        booking.setBookingType(typeofrenting);

        guest.setgName(name);
        guest.setgBirthday(dob);
        guest.setGender(gender);
        guest.setgPhone(phone);

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