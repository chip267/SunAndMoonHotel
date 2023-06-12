package com.example.sunmoon.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.sunmoon.R;
import com.example.sunmoon.adapter.HouseKeepingAdapter;
import com.example.sunmoon.adapter.ReportAdapter;
import com.example.sunmoon.models.Booking;
import com.example.sunmoon.models.Conditions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SalesReport extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ReportAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_report);
        recyclerView = findViewById(R.id.recyclerviewReport);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ReportAdapter();
        recyclerView.setAdapter(adapter);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference bookingRef = database.getReference("Booking");
        bookingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    List<Booking> filteredBooking = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Booking booking = snapshot.getValue(Booking.class);
                        if (booking != null){//&& ("checkout".equals(booking.getStatus()))) {
                            //if (isDateToday(condition.getDate())) {
                                filteredBooking.add(booking);
                           // }
                        }
                    }
                    adapter.setData(filteredBooking);
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SalesReport.this, "Error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
                Toast.makeText(SalesReport.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}