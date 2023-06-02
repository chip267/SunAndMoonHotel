package com.example.sunmoon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sunmoon.screen.Home;
import com.example.sunmoon.models.Floor;
import com.example.sunmoon.models.Account;
import com.example.sunmoon.models.Booking;
import com.example.sunmoon.models.Conditions;
import com.example.sunmoon.models.Guest;
import com.example.sunmoon.models.Employee;
import com.example.sunmoon.models.Room;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    DatabaseReference accountData = FirebaseDatabase.getInstance().getReference("Account");
    DatabaseReference bookingData = FirebaseDatabase.getInstance().getReference("Booking");
    DatabaseReference conditionData = FirebaseDatabase.getInstance().getReference("Condition");
    DatabaseReference employeeData = FirebaseDatabase.getInstance().getReference("Employee");
    DatabaseReference floorData = FirebaseDatabase.getInstance().getReference("Floor");
    DatabaseReference guestData = FirebaseDatabase.getInstance().getReference("Guest");
    DatabaseReference roomData = FirebaseDatabase.getInstance().getReference("Room");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    /* code nay de test do du lieu len firebase, muon test thi uncomment roi sua vai
    xong chay app la dc, test xong thi len Firebase xoa thu cong nhe!!!
        Floor floor = new Floor("2",true);
        floorData.setValue(floor);
        Account acc = new Account("admin","admin","1234");
        accountData.setValue(acc, "aUsername");
        Guest guest = new Guest("01234","customer1","678910",
                "2003",true);
        guestData.setValue(guest);
        Room room = new Room("101","Vacant","Standard",
                100000,300000,true);
        roomData.setValue(room);*/


        Button buttonLogIn = findViewById(R.id.buttonlogin);
        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Home.class);
                startActivity(intent);

            }
        });
    }
}