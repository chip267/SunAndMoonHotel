package com.example.sunmoon.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.sunmoon.models.Conditions;
import com.example.sunmoon.models.Guest;
import com.example.sunmoon.models.Room;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BookingForm extends AppCompatActivity {
    public EditText Room, Name, DOB, Phone, Idcard;
    public Button Confirm;
    public String TypeOfBooking ="";
    public String Gender ="";
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference databaseReference;
    private List<Booking> bookinglist = new ArrayList<>();
    private RecyclerView recyclerView;
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
                String rid = Room.getText().toString();
                String typeofbooking = TypeOfBooking;

                String name = Name.getText().toString();
                String dob = DOB.getText().toString();
                String gender = Gender;
                String phone = Phone.getText().toString();
                String gid = Idcard.getText().toString();


                generatebookingId(new bookingIdCallback() {

                    @Override
                    public void onbookingIdGenerated(String bookingId) {
                        int avail = 1;
                        String checkoutDate = "00/00/0000";
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy");
                        String checkinHour = timeFormat.format(new Date());
                        String checkinDate = dateFormat.format(new Date());
                        String checkoutHour = "";
                        String rid = Room.getText().toString();
                        int total = 0;
                        String status = "Check in";

                        Booking booking = new Booking(bookingId,checkinDate,checkoutDate,checkinHour,checkoutHour, typeofbooking, rid,total,status,gid);
                        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Booking");
                        databaseRef.child(bookingId).setValue(booking);
                        FirebaseDatabase.getInstance().getReference("Room").child(rid).child("rAvail").setValue(1);
                    }
                    @Override
                    public void onbookingIdGenerationFailed(String errorMessage) {
                        // Handle the report ID generation failure
                        // You can display an error message or perform error handling logic here
                    }
                });

                generateguestId(new guestIdCallback(){

                    @Override
                    public void onguestIdGenerated(String guestId) {
                        boolean gAvail = true;
                        String gName = Name.getText().toString();
                        String gDOB = DOB.getText().toString();
                        String gGender = Gender;
                        String gPhone = Phone.getText().toString();
                        String gId = Idcard.getText().toString();

                        Guest guest = new Guest(gId, gName,gGender, gPhone,gDOB,gAvail);
                        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Guest");
                        databaseRef.child(guestId).setValue(guest);
                    }
                    @Override
                    public void onguestIdGenerationFailed(String errorMessage) {

                    }
                });

                addDatatoFireBase(rid, typeofbooking, name, dob, gender, phone, gid);
                Intent intent = new Intent(getApplicationContext(),AllRoom.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();

            }
        });

    }
    private void generatebookingId(final BookingForm.bookingIdCallback callback) {
        final DatabaseReference conditionsRef = FirebaseDatabase.getInstance().getReference().child("Booking");
        conditionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int highestNumber = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String bookingId = snapshot.getKey();
                    String gid = Idcard.getText().toString();
                    if (bookingId.startsWith("b")) {
                        try {
                            int number = Integer.parseInt(bookingId.substring(1));
                            if (number > highestNumber) {
                                highestNumber = number;
                            }
                        } catch (NumberFormatException e) {
                            // Ignore invalid report IDs
                        }
                    }
                }
                String newReportId = String.format("b%03d", highestNumber + 1);
                callback.onbookingIdGenerated(newReportId);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
                callback.onbookingIdGenerationFailed(databaseError.getMessage());
            }
        });
    }


    interface bookingIdCallback {
        void onbookingIdGenerated(String bookingId);
        void onbookingIdGenerationFailed(String errorMessage);
    }

    private void generateguestId(final BookingForm.guestIdCallback callback) {
        final DatabaseReference conditionsRef = FirebaseDatabase.getInstance().getReference().child("Guest");
        conditionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String guestId = Idcard.getText().toString();
                callback.onguestIdGenerated(guestId);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
                callback.onguestIdGenerationFailed(databaseError.getMessage());
            }
        });
    }


    interface guestIdCallback {
        void onguestIdGenerated(String guessId);
        void onguestIdGenerationFailed(String errorMessage);
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