package com.example.sunmoon.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunmoon.R;
import com.example.sunmoon.models.Employee;
import com.example.sunmoon.models.UserSingleton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class Bill extends AppCompatActivity implements Serializable {
    private TextView tvCheckin, tvCheckout, tvRoomNum, tvType, tvRoomCharge, tvDetails, tvSurcharge, tvTotal, tvBookID, tvNameCus, tvPhoneCus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_bill_large);
        ImageView imageViewBack;
        imageViewBack = findViewById(R.id.imageViewBack);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Bill.this, SalesReport.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        tvCheckin = findViewById(R.id.tv_checkintime);
        tvCheckout = findViewById(R.id.tv_checkouttime);
        tvRoomNum = findViewById(R.id.tv_roomnum);
        tvType = findViewById(R.id.tv_tob);
        tvRoomCharge = findViewById(R.id.tv_roomchargemoney);
        tvDetails = findViewById(R.id.tv_details);
        tvSurcharge = findViewById(R.id.tv_surchargemoney);
        tvTotal = findViewById(R.id.tv_totalsurchargemoney);
        tvBookID = findViewById(R.id.tv_idbill);
        tvNameCus = findViewById(R.id.tv_nameCus);
        tvPhoneCus = findViewById(R.id.tv_phonenocus);
        Intent i = getIntent();
        String bcheckIn = i.getStringExtra("CheckIn");
        String bcheckOut = i.getStringExtra("CheckOut");
        String broomNum = i.getStringExtra("RoomNum");
        String btypeBook = i.getStringExtra("Type");
        String broomCharge = i.getStringExtra("RoomCharge");
        //String bdetails = i.getStringExtra("Details");
        String bsurCharge = i.getStringExtra("Surcharge");
        String btotal = i.getStringExtra("Total");
        String bbookID = i.getStringExtra("BookID");
        String bnameCus = i.getStringExtra("NameCus");
        String bphoneCus = i.getStringExtra("PhoneCus");
        tvCheckin.setText(bcheckIn);
        tvCheckout.setText(bcheckOut);
        tvRoomNum.setText(broomNum);
        tvType.setText(btypeBook);
        tvRoomCharge.setText(broomCharge);
        tvDetails.setText("+");
        tvSurcharge.setText(bsurCharge);
        tvTotal.setText(btotal);
        tvBookID.setText(bbookID);
        tvNameCus.setText(bnameCus);
        tvPhoneCus.setText(bphoneCus);
    }
}
