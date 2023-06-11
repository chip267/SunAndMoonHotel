package com.example.sunmoon.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunmoon.MainActivity;
import com.example.sunmoon.R;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private boolean mSlideState=false;
    private ImageView imageView;
    private ImageView closeDrawer;
    private NavigationView navigationView;
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
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mDrawerLayout.isDrawerOpen(GravityCompat.START)) mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        /*closeDrawer = findViewById(R.id.imageView18);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });*/

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

        Calendar c = Calendar.getInstance();

        int minutes = c.get(Calendar.MINUTE);
        int hour = c.get(Calendar.HOUR);
        String AMPM;
        if (c.get(Calendar.AM_PM) == 0) {
            AMPM = "AM";
        } else {
            AMPM = "PM";
        }
        String time = hour + ":" + minutes +"    "+AMPM;

        TextView tv_time = findViewById(R.id.tv2);
        tv_time.setText(time);

        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        String setday = "";
        String setmonth = "";
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

        String date = day + setday + " " + setmonth;
        TextView tv_date = findViewById(R.id.tv3);
        tv_date.setText(date);
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
        return true;
    }
}