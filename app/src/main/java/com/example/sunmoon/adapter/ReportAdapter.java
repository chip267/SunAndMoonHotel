package com.example.sunmoon.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunmoon.R;
import com.example.sunmoon.models.Booking;
import com.example.sunmoon.models.Conditions;
import com.example.sunmoon.screen.Bill;
import com.example.sunmoon.screen.PersonelDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {
    private List<Booking> bookings = new ArrayList<>();
    public void setData(List<Booking> bookings) {
        this.bookings = bookings;
    }
    @NonNull
    @Override
    public ReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sales_report_item_view, parent, false);
        return new ReportAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ReportAdapter.ViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        holder.tvID.setText(booking.getBookingID());
        String gidCard = booking.getGid();
        holder.tvtime.setText(booking.getCheckoutHour()+"   "+booking.getCheckoutDate());
        DatabaseReference guestRef = FirebaseDatabase.getInstance().getReference("Guest");
        guestRef.orderByChild("gIDCard").equalTo(gidCard).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot guestDataSnapshot) {
                if (guestDataSnapshot.exists()) {
                    for (DataSnapshot guestSnapshot : guestDataSnapshot.getChildren()) {
                        String gName = guestSnapshot.child("gName").getValue(String.class);
                        holder.tvcustomer.setText(gName);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur during the query
            }
        });
        holder.tvrooom.setText(booking.getRid());
        holder.checkin.setText(booking.getCheckinHour()+"   "+booking.getCheckinDate());
        int roomC = booking.getTotal();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
        String roomCharge = decimalFormat.format(roomC);
        int surC = booking.getSurcharge();
        DecimalFormatSymbols symbolsA = new DecimalFormatSymbols(Locale.getDefault());
        symbolsA.setGroupingSeparator('.');
        DecimalFormat decimalFormatA = new DecimalFormat("#,###", symbolsA);
        String surCharge = decimalFormatA.format(surC);
        int totalC = booking.getTotalBill();
        DecimalFormatSymbols symbolsB = new DecimalFormatSymbols(Locale.getDefault());
        symbolsB.setGroupingSeparator('.');
        DecimalFormat decimalFormatB = new DecimalFormat("#,###", symbolsB);
        String total = decimalFormatB.format(totalC);
        holder.tvrooomcharge.setText(roomCharge);
        holder.tvsurcharge.setText(surCharge);
        holder.tvtotal.setText(total);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                String checkIn = booking.getCheckinHour()+"   "+booking.getCheckinDate();
                String checkOut = booking.getCheckoutHour()+"   "+booking.getCheckoutDate();
                String roomNum = booking.getRid();
                String typeBook = booking.getBookingType();
                int roomC = booking.getTotal();
                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
                symbols.setGroupingSeparator('.');
                DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
                String roomCharge = decimalFormat.format(roomC);
                int surC = booking.getSurcharge();
                DecimalFormatSymbols symbolsA = new DecimalFormatSymbols(Locale.getDefault());
                symbolsA.setGroupingSeparator('.');
                DecimalFormat decimalFormatA = new DecimalFormat("#,###", symbolsA);
                String surCharge = decimalFormatA.format(surC);
                int totalC = booking.getTotalBill();
                DecimalFormatSymbols symbolsB = new DecimalFormatSymbols(Locale.getDefault());
                symbolsB.setGroupingSeparator('.');
                DecimalFormat decimalFormatB = new DecimalFormat("#,###", symbolsB);
                String total = decimalFormatB.format(totalC);
                String bookID = booking.getBookingID();
                String gidCard = booking.getGid();
                String details = booking.getDetails();
                DatabaseReference guestRef = FirebaseDatabase.getInstance().getReference("Guest");
                guestRef.orderByChild("gIDCard").equalTo(gidCard).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot guestDataSnapshot) {
                        if (guestDataSnapshot.exists()) {
                            for (DataSnapshot guestSnapshot : guestDataSnapshot.getChildren()) {
                                String nameCus = guestSnapshot.child("gName").getValue(String.class);
                                String phoneCus = guestSnapshot.child("gPhone").getValue(String.class);
                                Intent intent = new Intent(context, Bill.class);
                                intent.putExtra("CheckIn", checkIn);
                                intent.putExtra("CheckOut", checkOut);
                                intent.putExtra("RoomNum", roomNum);
                                intent.putExtra("Type", typeBook);
                                intent.putExtra("RoomCharge", roomCharge);
                                intent.putExtra("Details", details);
                                intent.putExtra("Surcharge", surCharge);
                                intent.putExtra("Total", total);
                                intent.putExtra("BookID", bookID);
                                intent.putExtra("NameCus", nameCus);
                                intent.putExtra("PhoneCus", phoneCus);
                                context.startActivity(intent);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle any errors that occur during the query
                    }
                });
            }
        });
    }
    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvID, tvtime, tvcustomer, tvrooom, tvrooomcharge, tvsurcharge, tvtotal, checkin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvID = itemView.findViewById(R.id.tv_ID);
            tvtime = itemView.findViewById(R.id.tv_time);
            tvcustomer = itemView.findViewById(R.id.tv_customer1);
            tvrooom = itemView.findViewById(R.id.tv_rooom1);
            tvrooomcharge = itemView.findViewById(R.id.tv_rooomcharge1);
            tvsurcharge = itemView.findViewById(R.id.tv_surcharge1);
            tvtotal = itemView.findViewById(R.id.tv_total1);
            checkin = itemView.findViewById(R.id.tv_checkin1);
        }
    }
}
