package com.example.sunmoon.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.sunmoon.MainActivity;
import com.example.sunmoon.R;
import com.example.sunmoon.models.Conditions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CheckRoomPending extends AppCompatActivity {
    private Button addButton;
    private Dialog dialog;
    private EditText roomEditText, statusEditText, byEditText;
    private DatabaseReference databaseRef;
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
                        String date = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault()).format(new Date());
                        String reportId = generateReportId();
                        Conditions conditions = new Conditions(reportId, roomNo, status, by, date, "1");
                        databaseRef = FirebaseDatabase.getInstance().getReference("Conditions");
                        databaseRef.child(reportId).setValue(conditions);
                        dialog.dismiss();
                    }
                });
            }
        });
    }
    private String generateReportId() {
        // aa
        int currentReportId = 0;
        currentReportId++;
        String formattedReportId = String.format(Locale.getDefault(), "c%03d", currentReportId);
        return formattedReportId;
    }
}