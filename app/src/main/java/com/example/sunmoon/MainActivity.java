package com.example.sunmoon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sunmoon.screen.Home;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Console;

/*import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;*/

public class MainActivity extends AppCompatActivity {
    DatabaseReference accountData = FirebaseDatabase.getInstance().getReference("Account");
    DatabaseReference bookingData = FirebaseDatabase.getInstance().getReference("Booking");
    DatabaseReference conditionData = FirebaseDatabase.getInstance().getReference("Condition");
    DatabaseReference employeeData = FirebaseDatabase.getInstance().getReference("Employee");
    DatabaseReference floorData = FirebaseDatabase.getInstance().getReference("Floor");
    DatabaseReference guestData = FirebaseDatabase.getInstance().getReference("Guest");
    DatabaseReference roomData = FirebaseDatabase.getInstance().getReference("Room");
    EditText inputUsrName, inputPassword;
    Button btnLogin;

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


                String username, password;
                username = inputUsrName.getText().toString().trim();
                password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(username)){
                    Toast.makeText(MainActivity.this, "Enter username",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    Toast.makeText(MainActivity.this, "Enter password",Toast.LENGTH_SHORT).show();
                    return;
                }

                Query checkUserDatabase = accountData.orderByChild("aUsername").equalTo(username);

                checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String getPassword = snapshot.child(username).child("aPassword").getValue(String.class);
                            if (getPassword.equals(password)){
                                Toast.makeText(MainActivity.this, "Login successfully",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Home.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Wrong password.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                Intent intent = new Intent(MainActivity.this, SalesOverview.class);
                startActivity(intent);

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}