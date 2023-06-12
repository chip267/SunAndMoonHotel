package com.example.sunmoon.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SalesReport extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ReportAdapter adapter;
    ImageView imageViewBack;
    private AppCompatButton saleOverviewButton;
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
                        if (booking != null && ("checkout".equals(booking.getStatus()))) {
                            if (isDateInCurrentWeekday(booking.getCheckoutDate())) {
                                filteredBooking.add(booking);
                            }
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
        imageViewBack = findViewById(R.id.imageViewBackhome);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SalesReport.this, Home.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        saleOverviewButton = findViewById(R.id.btn_Overview);
        saleOverviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SalesReport.this, SalesOverview.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }
    private boolean isDateInCurrentWeekday(String date) {
        SimpleDateFormat sdfDateOnly = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date currentDate = new Date();

        try {
            // Parse the condition's date
            Date bookingDate = sdfDateOnly.parse(date);

            // Get the start and end dates of the current week
            Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            Date startOfWeek = calendar.getTime();
            calendar.add(Calendar.DAY_OF_WEEK, 6);
            Date endOfWeek = calendar.getTime();

            // Truncate time portion for comparison
            String bookingDateOnly = sdfDateOnly.format(bookingDate);
            String currentDateTimeOnly = sdfDateOnly.format(currentDate);
            startOfWeek = sdfDateOnly.parse(sdfDateOnly.format(startOfWeek));
            endOfWeek = sdfDateOnly.parse(sdfDateOnly.format(endOfWeek));

            // Check if the condition's date falls within the current week and not in the future
            return bookingDateOnly != null &&
                    bookingDateOnly.equals(sdfDateOnly.format(startOfWeek)) ||
                    bookingDateOnly.equals(sdfDateOnly.format(endOfWeek)) ||
                    (bookingDateOnly.compareTo(sdfDateOnly.format(startOfWeek)) > 0 &&
                            bookingDateOnly.compareTo(sdfDateOnly.format(endOfWeek)) < 0) &&
                            currentDateTimeOnly.compareTo(bookingDateOnly) >= 0;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}