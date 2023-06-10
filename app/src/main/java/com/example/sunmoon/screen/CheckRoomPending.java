package com.example.sunmoon.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sunmoon.R;
import com.example.sunmoon.adapter.RecyclerViewAdapter;
import com.example.sunmoon.models.Conditions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CheckRoomPending extends AppCompatActivity implements RecyclerViewAdapter.OnButtonClickListener{
    private Button addButton;
    private Dialog dialog;
    private EditText roomEditText, statusEditText, byEditText;
    private DatabaseReference databaseRef;
    private List<Conditions> conditionList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private AppCompatButton handledButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkroom_peding);
        addButton = findViewById(R.id.btn_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a dialog
                dialog = new Dialog(CheckRoomPending.this);
                dialog.setContentView(R.layout.checkroom_adding_popup);
                dialog.show();

                ImageButton closeButton = dialog.findViewById(R.id.imageButtonClose);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                roomEditText = dialog.findViewById(R.id.box_room);
                statusEditText = dialog.findViewById(R.id.box_status);
                byEditText = dialog.findViewById(R.id.box_by);
                Button doneButton = dialog.findViewById(R.id.btn_Done);
                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String roomNo = roomEditText.getText().toString();
                        String status = statusEditText.getText().toString();
                        String by = byEditText.getText().toString();
                        String date = new SimpleDateFormat("HH:mm    dd/MM/yyyy", Locale.getDefault()).format(new Date());
                        String reportId = generateReportId();
                        int avail = 1;
                        Conditions conditions = new Conditions(reportId, roomNo, status, by, date, avail);
                        databaseRef = FirebaseDatabase.getInstance().getReference("Conditions");
                        databaseRef.child(reportId).setValue(conditions);
                        dialog.dismiss();
                    }
                });
            }
        });
        handledButton = findViewById(R.id.btn_Handled);
        handledButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckRoomPending.this, CheckRoomHandled.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        recyclerView = findViewById(R.id.recyclerview);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnButtonClickListener(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference conditionsRef = database.getReference("Conditions");
        conditionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    List<Conditions> filteredConditions = new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Conditions condition = snapshot.getValue(Conditions.class);

                        if (condition != null && condition.getAvail() == 1) {
                            filteredConditions.add(condition);
                        }
                    }
                    adapter.setData(filteredConditions);
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(CheckRoomPending.this, "Error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
                Toast.makeText(CheckRoomPending.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onDeleteButtonClick(String reportId) {
        DatabaseReference conditionsRef = FirebaseDatabase.getInstance().getReference("Conditions");
        conditionsRef.child(reportId).child("avail").setValue(0)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CheckRoomPending.this, "Avail value set to 0", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CheckRoomPending.this, "Failed to set avail value: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void onDoneButtonClick(String reportId) {
        DatabaseReference conditionsRef = FirebaseDatabase.getInstance().getReference("Conditions");
        conditionsRef.child(reportId).child("avail").setValue(2)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CheckRoomPending.this, "Avail value updated successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CheckRoomPending.this, "Failed to update avail value: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private String generateReportId() {
        //
        int currentReportId = 2;
        currentReportId++;
        String formattedReportId = String.format(Locale.getDefault(), "c%03d", currentReportId);
        return formattedReportId;
    }
}