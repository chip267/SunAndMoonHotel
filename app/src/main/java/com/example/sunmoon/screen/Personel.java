package com.example.sunmoon.screen;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunmoon.R;
import com.example.sunmoon.adapter.RecyclerViewHandledAdapter;
import com.example.sunmoon.adapter.RecyclerViewPersonnel;
import com.example.sunmoon.models.Conditions;
import com.example.sunmoon.models.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.sunmoon.R;
import com.example.sunmoon.models.Room;
import com.example.sunmoon.models.UserSingleton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Personel extends AppCompatActivity {
    Button btnAdd;
    private DatabaseReference databaseRef;
    private List<Employee> employeeList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerViewPersonnel adapter;
    private Dialog passcodeDialog;
    private String passCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personel_overview);

        ImageView imageViewBack;
        imageViewBack = findViewById(R.id.imageView12);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Personel.this, Home.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passcodeDialog = new Dialog(Personel.this);
                passcodeDialog.setContentView(R.layout.passcode_adding_popup);
                passcodeDialog.show();
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
                                        Toast.makeText(Personel.this, "Successfully",
                                                Toast.LENGTH_SHORT).show();
                                        passcodeDialog.dismiss();
                                        Intent intent = new Intent(Personel.this, PersonelAdd.class);
                                        startActivity(intent);
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    }
                                    else{
                                        inputPassCode.requestFocus();
                                        inputPassCode.setError("Passcode is invalid!");
                                    }
                                }
                                else{
                                    Toast.makeText(Personel.this, "Failed",
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
        recyclerView = findViewById(R.id.personnelRecycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewPersonnel();
        recyclerView.setAdapter(adapter);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference employeeRef = database.getReference("Employee");
        employeeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    List<Employee> filteredConditions = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Employee employee = snapshot.getValue(Employee.class);
                        if (employee != null && employee.getEmpAvail() == 1) {
                            filteredConditions.add(employee);
                        }
                    }
                    adapter.setData(filteredConditions);
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Personel.this, "Error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
