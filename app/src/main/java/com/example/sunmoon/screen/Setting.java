package com.example.sunmoon.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunmoon.R;
import com.example.sunmoon.models.UserSingleton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Setting extends AppCompatActivity {
    private Dialog changePasscodeDialog;
    private TextView btnChangePasscode;
    private String getPasscode = "";
    ImageView imageViewBack;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        imageViewBack = findViewById(R.id.imageView14);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting.this, Home.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        btnChangePasscode = findViewById(R.id.change_passcode);
        btnChangePasscode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePasscodeDialog = new Dialog(Setting.this);
                changePasscodeDialog.setContentView(R.layout.setting_pop_up);
                changePasscodeDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_vacant_room);
                changePasscodeDialog.show();

                EditText editTextOldPassCode = changePasscodeDialog.findViewById(R.id.edit_passcode);
                EditText editTextNewPassCode = changePasscodeDialog.findViewById(R.id.edit_newpass);
                EditText editTextConfirmPassCode = changePasscodeDialog.findViewById(R.id.edit_confirmpass);

                AppCompatButton btnDone = changePasscodeDialog.findViewById(R.id.btn_donechangepass);
                AppCompatButton btnCancel = changePasscodeDialog.findViewById(R.id.btn_cancel);
                String usr = UserSingleton.getInstance().getUserName();
                Query checkUserDatabase = FirebaseDatabase.getInstance().getReference("Account").orderByChild("aUsername").equalTo(usr);
                checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            getPasscode = snapshot.child(usr).child("aPasscode").getValue(String.class);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changePasscodeDialog.dismiss();
                    }
                });
                btnDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String oldPasscode = editTextOldPassCode.getText().toString().trim();
                        String newPasscode = editTextNewPassCode.getText().toString().trim();
                        String confirmPasscode = editTextConfirmPassCode.getText().toString().trim();

                        if (oldPasscode.isEmpty()){
                            editTextOldPassCode.requestFocus();
                            editTextOldPassCode.setError("Please enter old passcode!");

                            editTextNewPassCode.setText("");
                            editTextConfirmPassCode.setText("");
                        }
                        else {
                            if (newPasscode.isEmpty()){
                                editTextNewPassCode.requestFocus();
                                editTextNewPassCode.setError("Please enter new passcode!");
                            }
                            else if (confirmPasscode.isEmpty()){
                                editTextConfirmPassCode.requestFocus();
                                editTextConfirmPassCode.setError("Please enter confirm passcode!");
                            }
                            else if (!confirmPasscode.equalsIgnoreCase(newPasscode)){
                                editTextConfirmPassCode.requestFocus();
                                editTextConfirmPassCode.setError("Please confirm passcode!");
                            }
                            else if (oldPasscode.equalsIgnoreCase(getPasscode)){
                                if (!confirmPasscode.equalsIgnoreCase(newPasscode)){
                                    editTextConfirmPassCode.requestFocus();
                                    editTextConfirmPassCode.setError("Passcode is invalid!");
                                }
                                else {
                                    FirebaseDatabase.getInstance().getReference("Account").child(usr).child("aPasscode").setValue(newPasscode);
                                    changePasscodeDialog.dismiss();
                                    Toast.makeText(Setting.this, "Change passcode successfully!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                editTextOldPassCode.requestFocus();
                                editTextOldPassCode.setError("Passcode is incorrect!!!");
                            }
                        }
                    }
                });
            }
        });

    }
}