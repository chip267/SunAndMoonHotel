package com.example.sunmoon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sunmoon.screen.Home;
import com.example.sunmoon.models.Floor;
import com.example.sunmoon.models.Account;
import com.example.sunmoon.models.Booking;
import com.example.sunmoon.models.Conditions;
import com.example.sunmoon.models.Guest;
import com.example.sunmoon.models.Employee;
import com.example.sunmoon.models.Room;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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
        inputUsrName = findViewById(R.id.et_input_usrname);
        inputPassword = findViewById(R.id.et_input_password);



        /*String username1 = "hien";
        String passCode = "1234";
        String password = "12345";

        Account helperClass = new Account(username1, password, passCode);

        accountData.child(username1).setValue(helperClass);*/
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


        btnLogin = findViewById(R.id.buttonlogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
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

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}