package com.example.sunmoon.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.sunmoon.MainActivity;
import com.example.sunmoon.R;
import com.example.sunmoon.adapter.ConditionAdapter;
import com.example.sunmoon.models.Conditions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.locks.Condition;

public class CheckRoomPending extends AppCompatActivity {
    private Button addButton;
    private Dialog dialog;
    private EditText roomEditText, statusEditText, byEditText;
    private DatabaseReference databaseRef;
    private List<Conditions> conditionList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ConditionAdapter adapter;
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
                        Conditions conditions = new Conditions(reportId, roomNo, status, by, date, "1");
                        databaseRef = FirebaseDatabase.getInstance().getReference("Conditions");
                        databaseRef.child(reportId).setValue(conditions);
                        dialog.dismiss();
                    }
                });
            }
        });
        recyclerView = findViewById(R.id.recyclerview);
        adapter = new ConditionAdapter(conditionList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        DatabaseReference conditionsRef = FirebaseDatabase.getInstance().getReference("Conditions");
        Query query = conditionsRef.orderByChild("cAvail").equalTo(1);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    conditionList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        try {
                            Conditions condition = snapshot.getValue(Conditions.class);
                            conditionList.add(condition);
                        } catch (Exception e) {
                            e.printStackTrace();
                            // Handle the exception, display an error message, or perform any necessary error handling
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the database error
                // Display an error message or perform any necessary error handling
                String errorMessage = databaseError.getMessage();
                Log.e("Firebase Database Error", errorMessage);
            }
        });

    }

    /*private void showRecyclerView() {
        List<Conditions> conditionList = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        ConditionAdapter adapter = new ConditionAdapter(conditionList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        DatabaseReference conditionsRef = FirebaseDatabase.getInstance().getReference("Conditions");
        Query query = conditionsRef.orderByChild("cAvail").equalTo("1");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    conditionList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Conditions condition = snapshot.getValue(Conditions.class);
                        conditionList.add(condition);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });

    }*/

    private String generateReportId() {
        //
        int currentReportId = 4;
        currentReportId++;
        String formattedReportId = String.format(Locale.getDefault(), "c%03d", currentReportId);
        return formattedReportId;
    }
}