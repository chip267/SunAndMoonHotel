package com.example.sunmoon.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sunmoon.R;
import com.example.sunmoon.models.Employee;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PersonelAdd extends AppCompatActivity {
    private EditText addFirstName, addLastName, addIDCard, addBirth, addGender, addPhone, addAddress, addEmail, addPosition;
    private List<Employee> EmployeeList = new ArrayList<>();
    AppCompatButton btnCancel;
    AppCompatButton btnConfirm;
    ImageView imageViewBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_employee);

        addFirstName = findViewById(R.id.box_firstname);
        addLastName = findViewById(R.id.box_lastname);
        addIDCard = findViewById(R.id.box_IdCard);
        addBirth = findViewById(R.id.box_dateofbirth);
        addGender = findViewById(R.id.box_gender);
        addPhone = findViewById(R.id.box_endphone);
        addAddress = findViewById(R.id.box_address);
        addEmail = findViewById(R.id.box_email);
        addPosition = findViewById(R.id.box_position);
        btnConfirm = findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = addFirstName.getText().toString();
                String lastName = addLastName.getText().toString();
                String idCard = addIDCard.getText().toString();
                String birth = addBirth.getText().toString();
                String gender = addGender.getText().toString();
                String phone = addPhone.getText().toString();
                String address = addAddress.getText().toString();
                String email = addEmail.getText().toString();
                String position = addPosition.getText().toString();
                generateEmployeeId(new EmployeeIdCallback() {
                    @Override
                    public void onEmployeeIdGenerated(String EmployeeId) {
                        int avail = 1;
                        Employee employee = new Employee(EmployeeId,idCard,firstName,lastName,phone,gender,birth,email,address,position,avail);
                        DatabaseReference employeeRef = FirebaseDatabase.getInstance().getReference("Employee");
                        employeeRef.child(EmployeeId).setValue(employee);

                    }
                    @Override
                    public void onEmployeeIdGenerationFailed(String errorMessage) {

                    }
                });

                Toast.makeText(PersonelAdd.this, "NEW EMPLOYEE ADDED SUCCESSFULLY",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),Personel.class);
                startActivity(intent);

            };

        });

        btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonelAdd.this, Personel.class);
                Toast.makeText(PersonelAdd.this, "Cancelled",
                        Toast.LENGTH_SHORT).show();
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    private void generateEmployeeId(final EmployeeIdCallback callback) {
        final DatabaseReference employeeRef = FirebaseDatabase.getInstance().getReference().child("Employee");

        employeeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int highestNumber = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String reportId = snapshot.getKey();
                    assert reportId != null;
                    if (reportId.startsWith("ID")) {
                        try {
                            int number = Integer.parseInt(reportId.substring(2));
                            if (number > highestNumber) {
                                highestNumber = number;
                            }
                        } catch (NumberFormatException e) {
                            // Ignore invalid report IDs
                        }
                    }
                }

                String newEmployeeId = String.format("ID%03d", highestNumber + 1);
                callback.onEmployeeIdGenerated(newEmployeeId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onEmployeeIdGenerationFailed(error.getMessage());
            }

        });
    }
    interface EmployeeIdCallback {
        void onEmployeeIdGenerated(String EmployeeId);
        void onEmployeeIdGenerationFailed(String errorMessage);
    }

}