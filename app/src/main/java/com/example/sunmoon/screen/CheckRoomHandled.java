package com.example.sunmoon.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sunmoon.R;
import com.example.sunmoon.adapter.RecyclerViewAdapter;
import com.example.sunmoon.adapter.RecyclerViewHandledAdapter;
import com.example.sunmoon.models.Conditions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CheckRoomHandled extends AppCompatActivity implements RecyclerViewHandledAdapter.OnButtonClickListener {
    private AppCompatButton pendingButton;
    private DatabaseReference databaseRef;
    private List<Conditions> conditionList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerViewHandledAdapter adapter;
    ImageView imageViewBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkroom_handled);
        pendingButton = findViewById(R.id.btn_Pending);
        pendingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckRoomHandled.this, CheckRoomPending.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        imageViewBack = findViewById(R.id.imageViewBack);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckRoomHandled.this, Home.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        recyclerView = findViewById(R.id.recyclerview);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewHandledAdapter();
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

                        if (condition != null && condition.getAvail() == 2) {
                            if (isDateInCurrentWeekday(condition.getDate())) {
                                filteredConditions.add(condition);
                            }
                        }
                    }
                    adapter.setData(filteredConditions);
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(CheckRoomHandled.this, "Error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
                Toast.makeText(CheckRoomHandled.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onDeleteButtonClick(String reportId) {
        DatabaseReference conditionsRef = FirebaseDatabase.getInstance().getReference("Conditions");
        conditionsRef.child(reportId).child("avail").setValue(0)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CheckRoomHandled.this, "Đã xóa thành công", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CheckRoomHandled.this, "Failed to set avail value: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private boolean isDateInCurrentWeekday(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat sdfDateOnly = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date currentDate = new Date();

        try {
            // Parse the condition's date
            Date conditionDate = sdf.parse(date);

            // Get the start and end dates of the current week
            Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            Date startOfWeek = calendar.getTime();
            calendar.add(Calendar.DAY_OF_WEEK, 6);
            Date endOfWeek = calendar.getTime();

            // Truncate time portion for comparison
            String conditionDateOnly = sdfDateOnly.format(conditionDate);
            String currentDateTimeOnly = sdfDateOnly.format(currentDate);
            startOfWeek = sdfDateOnly.parse(sdfDateOnly.format(startOfWeek));
            endOfWeek = sdfDateOnly.parse(sdfDateOnly.format(endOfWeek));

            // Check if the condition's date falls within the current week and not in the future
            return conditionDateOnly != null &&
                    conditionDateOnly.equals(sdfDateOnly.format(startOfWeek)) ||
                    conditionDateOnly.equals(sdfDateOnly.format(endOfWeek)) ||
                    (conditionDateOnly.compareTo(sdfDateOnly.format(startOfWeek)) > 0 &&
                            conditionDateOnly.compareTo(sdfDateOnly.format(endOfWeek)) < 0) &&
                            currentDateTimeOnly.compareTo(conditionDateOnly) >= 0;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}