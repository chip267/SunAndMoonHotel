package com.example.sunmoon.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunmoon.R;
import com.example.sunmoon.models.Booking;
import com.example.sunmoon.models.Conditions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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
        String total= Double.toString(booking.getTotal());
        holder.tvrooomcharge.setText(total);
        holder.tvsurcharge.setText("0");
        holder.tvtotal.setText(total);
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvID, tvtime, tvcustomer, tvrooom, tvrooomcharge, tvsurcharge, tvtotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvID = itemView.findViewById(R.id.tv_ID);
            tvtime = itemView.findViewById(R.id.tv_time);
            tvcustomer = itemView.findViewById(R.id.tv_customer1);
            tvrooom = itemView.findViewById(R.id.tv_rooom1);
            tvrooomcharge = itemView.findViewById(R.id.tv_rooomcharge1);
            tvsurcharge = itemView.findViewById(R.id.tv_surcharge1);
            tvtotal = itemView.findViewById(R.id.tv_total1);
        }
    }
}
