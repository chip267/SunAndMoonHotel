package com.example.sunmoon.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sunmoon.MainActivity;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


public class CheckRoomPending extends AppCompatActivity implements RecyclerViewAdapter.OnButtonClickListener{
    private Uri selectedImageUri;
    private Button addButton;
    private Dialog dialog, dialog1;
    private EditText roomEditText, statusEditText, byEditText, updateStatus, updateName;
    private DatabaseReference databaseRef;
    private List<Conditions> conditionList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private AppCompatButton handledButton, cancelUpdate, confirmUpdate;
    ImageView imageViewBack, updateImage;
    private static final int REQUEST_IMAGE_PICKER = 1;
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
                        String roomC,statusC,byC;
                        roomC = roomEditText.getText().toString().trim();
                        statusC = statusEditText.getText().toString().trim();
                        byC = byEditText.getText().toString().trim();
                        if (TextUtils.isEmpty(roomC)){
                            Toast.makeText(CheckRoomPending.this, "Enter Room No",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(statusC)){
                            Toast.makeText(CheckRoomPending.this, "Enter Status",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(byC)){
                            Toast.makeText(CheckRoomPending.this, "Enter Name By",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String roomNo = roomEditText.getText().toString();
                        String status = statusEditText.getText().toString();
                        String by = byEditText.getText().toString();
                        String date = new SimpleDateFormat("HH:mm    dd/MM/yyyy", Locale.getDefault()).format(new Date());
                        String image = "";
                        String statusUpdate = "";
                        String nameUpdate = "";
                        String dateUpdate = "";
                        generateReportId(new ReportIdCallback() {
                            @Override
                            public void onReportIdGenerated(String reportId) {
                                int avail = 1;
                                Conditions conditions = new Conditions(reportId,roomNo,status,by,date,image,statusUpdate,nameUpdate,dateUpdate,avail);
                                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Conditions");
                                databaseRef.child(reportId).setValue(conditions);
                                dialog.dismiss();
                            }
                            @Override
                            public void onReportIdGenerationFailed(String errorMessage) {
                                // Handle the report ID generation failure
                                // You can display an error message or perform error handling logic here
                            }
                        });
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
        imageViewBack = findViewById(R.id.imageViewBack);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckRoomPending.this, Home.class);
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
                            if (isDateInCurrentWeekday(condition.getDate())) {
                                filteredConditions.add(condition);
                            }
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
                        Toast.makeText(CheckRoomPending.this, "Đã xóa thành công", Toast.LENGTH_SHORT).show();
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
        dialog1 = new Dialog(CheckRoomPending.this);
        dialog1.setContentView(R.layout.checkroom_update_popup);
        dialog1.show();
        updateStatus = dialog1.findViewById(R.id.box_updatestatus);
        updateName = dialog1.findViewById(R.id.box_by);
        updateImage = dialog1.findViewById(R.id.uploadhere);
        cancelUpdate = dialog1.findViewById(R.id.btn_cancelupdate);
        cancelUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });
        confirmUpdate = dialog1.findViewById(R.id.btn_confirmupdate);
        confirmUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updateStatus1, updateName1;
                updateStatus1 = updateStatus.getText().toString().trim();
                updateName1 = updateName.getText().toString().trim();
                if (TextUtils.isEmpty(updateStatus1)){
                    Toast.makeText(CheckRoomPending.this, "Enter Status",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(updateName1)){
                    Toast.makeText(CheckRoomPending.this, "Enter Name",Toast.LENGTH_SHORT).show();
                    return;
                }
                /**/
                if (selectedImageUri != null) {
                    // Upload the selected image to Firebase Storage
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                    StorageReference imageRef = storageRef.child("images/" + UUID.randomUUID().toString());
                    UploadTask uploadTask = imageRef.putFile(selectedImageUri);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUri) {
                                    DatabaseReference conditionsRef = FirebaseDatabase.getInstance().getReference("Conditions");
                                    conditionsRef.child(reportId).child("imageURL").setValue(downloadUri.toString())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Image URL saved successfully
                                                    Toast.makeText(CheckRoomPending.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Failed to save image URL
                                                    Toast.makeText(CheckRoomPending.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                        }
                    });
                } else {
                    // No image selected
                    Toast.makeText(CheckRoomPending.this, "Please select an image", Toast.LENGTH_SHORT).show();
                    return;
                }
                /**/
                String updateStatus2 = updateStatus.getText().toString();
                String updateName2 = updateName.getText().toString();
                String updateDate = new SimpleDateFormat("HH:mm    dd/MM/yyyy", Locale.getDefault()).format(new Date());
                DatabaseReference conditionsRef = FirebaseDatabase.getInstance().getReference("Conditions");
                conditionsRef.child(reportId).child("statusUpdate").setValue(updateStatus2);
                conditionsRef.child(reportId).child("nameUpdate").setValue(updateName2);
                conditionsRef.child(reportId).child("dateUpdate").setValue(updateDate);
                conditionsRef.child(reportId).child("avail").setValue(2)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(CheckRoomPending.this, "Successful", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CheckRoomPending.this, "Failed to update avail value: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                dialog1.dismiss();
            }
        });
    }
    private void generateReportId(final ReportIdCallback callback) {
        final DatabaseReference conditionsRef = FirebaseDatabase.getInstance().getReference().child("Conditions");
        conditionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int highestNumber = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String reportId = snapshot.getKey();
                    if (reportId.startsWith("c")) {
                        try {
                            int number = Integer.parseInt(reportId.substring(1));
                            if (number > highestNumber) {
                                highestNumber = number;
                            }
                        } catch (NumberFormatException e) {
                            // Ignore invalid report IDs
                        }
                    }
                }
                String newReportId = String.format("c%03d", highestNumber + 1);
                callback.onReportIdGenerated(newReportId);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
                callback.onReportIdGenerationFailed(databaseError.getMessage());
            }
        });
    }
    interface ReportIdCallback {
        void onReportIdGenerated(String reportId);
        void onReportIdGenerationFailed(String errorMessage);
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
            String conditionDateOnly = sdfDateOnly.format(conditionDate);
            String currentDateTimeOnly = sdfDateOnly.format(currentDate);
            startOfWeek = sdfDateOnly.parse(sdfDateOnly.format(startOfWeek));
            endOfWeek = sdfDateOnly.parse(sdfDateOnly.format(endOfWeek));
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
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_PICKER);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICKER && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            updateImage.setImageURI(selectedImageUri);
        }
    }
}