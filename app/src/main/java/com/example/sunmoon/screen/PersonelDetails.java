package com.example.sunmoon.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunmoon.R;
import com.example.sunmoon.models.Employee;
import com.example.sunmoon.models.UserSingleton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class PersonelDetails extends AppCompatActivity implements Serializable {
    private TextView tvId1, tv_id1, tvName1, tv_name1, tv_dateofbirth1, tv_phoneno1, tv_address1, tv_email1, tv_position1;
    private Dialog passcodeDialog;
    private String passCode;
    private ImageButton buttonclose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personel_details);
        ImageView imageViewBack;
        imageViewBack = findViewById(R.id.imageView14);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonelDetails.this, Personel.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        tvId1 = findViewById(R.id.tvId1);
        tv_id1 = findViewById(R.id.tv_id1);
        tvName1 = findViewById(R.id.tvName1);
        tv_name1 = findViewById(R.id.tv_name1);
        tv_dateofbirth1 = findViewById(R.id.tv_dateofbirth1);
        tv_phoneno1 = findViewById(R.id.tv_phoneno1);
        tv_address1 = findViewById(R.id.tv_address1);
        tv_email1 = findViewById(R.id.tv_email1);
        tv_position1 = findViewById(R.id.tv_position1);
        Intent i =getIntent();
        String employeeId = i.getStringExtra("employeeId");
        String empName = i.getStringExtra("empName");
        String empPhone = i.getStringExtra("empPhone");
        String empPosition = i.getStringExtra("empPosition");
        String empBirthday = i.getStringExtra("empBirthday");
        String empMail = i.getStringExtra("empMail");
        String empAddress = i.getStringExtra("empAddress");
        String empID = i.getStringExtra("empID");
        tvId1.setText(empID);
        tv_id1.setText(employeeId);
        tvName1.setText(empName);
        tv_name1.setText(empName);
        tv_dateofbirth1.setText(empBirthday);
        tv_phoneno1.setText(empPhone);
        tv_address1.setText(empAddress);
        tv_email1.setText(empMail);
        tv_position1.setText(empPosition);
        ImageView delete = findViewById(R.id.btn_empDelete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passcodeDialog = new Dialog(PersonelDetails.this);
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
                                        Toast.makeText(PersonelDetails.this, "Successfully",
                                                Toast.LENGTH_SHORT).show();
                                        passcodeDialog.dismiss();
                                        DatabaseReference conditionsRef = FirebaseDatabase.getInstance().getReference("Employee");
                                        conditionsRef.child(empID).child("empAvail").setValue(0);
                                        Intent intent = new Intent(PersonelDetails.this, Personel.class);
                                        startActivity(intent);
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                        Toast.makeText(PersonelDetails.this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        inputPassCode.requestFocus();
                                        inputPassCode.setError("Passcode is invalid!");
                                    }
                                }
                                else{
                                    Toast.makeText(PersonelDetails.this, "Failed",
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
    }
}
