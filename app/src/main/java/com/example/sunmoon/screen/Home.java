package com.example.sunmoon.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunmoon.MainActivity;
import com.example.sunmoon.R;
import com.example.sunmoon.models.Booking;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private boolean mSlideState=false;
    private ImageView imageView;
    private ImageView closeDrawer;
    private NavigationView navigationView;
    private  Booking booking;
    private TextView tv_checkin;
    private TextView tv_checkout;
    private DatabaseReference bookingRef;
    private int checkinNo = 0;
    private int checkoutNo = 0;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        imageView = findViewById(R.id.imageView3);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);

        mToggle.syncState();
        navigationView = findViewById(R.id.nav_view);
//        closeDrawer = findViewById(R.id.imageView18);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mDrawerLayout.isDrawerOpen(GravityCompat.START)) mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });


        View headerView = navigationView.getHeaderView(0);

        closeDrawer = headerView.findViewById(R.id.imageView18);

        closeDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        NavigationView mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(this);
        }

        Button Sales = findViewById(R.id.boxSales);
        Sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SalesOverview.class);
                startActivity(intent);
                finish();
            }
        });
        Button Rooms = findViewById(R.id.boxRooms);
        Rooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AllRoom.class);
                startActivity(intent);
                finish();
            }
        });
        Button Booking = findViewById(R.id.boxBooking);
        Booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BookingForm.class);
                startActivity(intent);
                finish();

            }
        });
        Button GuestList = findViewById(R.id.boxGuestList);
        GuestList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GuessList.class);
                startActivity(intent);
                finish();
            }
        });

        TextView housekeeping = findViewById(R.id.tvHousekeeping);
        housekeeping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),HomeHouseKeeping.class);
                startActivity(intent);
                finish();
            }
        });
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy");
        String time = timeFormat.format(new Date());
        String date = dateFormat.format(new Date());
        TextView tv_time = findViewById(R.id.tv2);
        tv_time.setText(time);
        String setday = "";
        String setmonth = "";
        String[] dateComponents = date.split("/");
        int day = Integer.parseInt(dateComponents[0]);
        int month = Integer.parseInt(dateComponents[1]);
        switch (day){
            case 1: setday = "st";break;
            case 2: setday = "nd";break;
            case 3: setday = "rd";break;
            default: setday = "th";break;
        }

        switch (month){
            case 1: setmonth = "January";break;
            case 2: setmonth = "Frebuary";break;
            case 3: setmonth = "March";break;
            case 4: setmonth = "April";break;
            case 5: setmonth = "May";break;
            case 6: setmonth = "June";break;
            case 7: setmonth = "July";break;
            case 8: setmonth = "August";break;
            case 9: setmonth = "September";break;
            case 10: setmonth = "October";break;
            case 11: setmonth = "November";break;
            case 12: setmonth = "December";break;
        }
        String dateA = day + setday + " " + setmonth;
        TextView tv_date = findViewById(R.id.tv3);
        tv_date.setText(dateA);
        tv_checkin = findViewById(R.id.tvTextAppearBooking);
        tv_checkout = findViewById(R.id.tvTextAppearForecast);
        bookingRef = FirebaseDatabase.getInstance().getReference("Booking");
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        String today = convertDateToString(currentDate);
        Query query = bookingRef.orderByChild("checkinDate").equalTo(today);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot bookingSnapshot : dataSnapshot.getChildren()) {
                    String status = bookingSnapshot.child("status").getValue(String.class);
                    if ("Check in".equals(status)) {
                        checkinNo++;
                    }
                    else if("checkout".equals(status)) {
                        checkoutNo++;
                    }
                }
                String cinNo = String.valueOf(checkinNo);
                String coutNo = String.valueOf(checkoutNo);
                tv_checkin.setText(cinNo);
                tv_checkout.setText(coutNo);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error querying bookings: " + databaseError.getMessage());
            }
        });
    }
    private String convertDateToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(date);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(getApplicationContext(),Home.class);
            startActivity(intent);
            finish();
        };
        if (id == R.id.nav_listroom) {
            Intent intent = new Intent(getApplicationContext(),AllRoom.class);
            startActivity(intent);
            finish();
        };
        if (id == R.id.nav_checkroom) {
            Intent intent = new Intent(getApplicationContext(),CheckRoomPending.class);
            startActivity(intent);
            finish();
        };
        if (id == R.id.nav_personnel) {
            Intent intent = new Intent(getApplicationContext(),Personel.class);
            startActivity(intent);
            finish();
        };
        if (id == R.id.nav_Logout) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        };
        if (id == R.id.nav_settings) {
            Intent intent = new Intent(getApplicationContext(), Setting.class);
            startActivity(intent);
            finish();
        };
        return true;
    }
}