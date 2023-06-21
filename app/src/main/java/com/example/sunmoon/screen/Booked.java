package com.example.sunmoon.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunmoon.R;
import com.example.sunmoon.adapter.BookedRoomAdapter;
import com.example.sunmoon.adapter.GuestAdapter;

import com.example.sunmoon.adapter.RecyclerViewAdapter;
import com.example.sunmoon.adapter.RoomTypeAdapter;
import com.example.sunmoon.models.Booking;
import com.example.sunmoon.models.Guest;
import com.example.sunmoon.models.Room;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Booked extends AppCompatActivity implements BookedRoomAdapter.OnButtonClickListener, AdapterView.OnItemSelectedListener {
    List<Booking> bookedRooms;
    RecyclerView bookedRoomRecyclerView;
    BookedRoomAdapter roomAdapter;
    ImageView btnBack;
    private Dialog dialog;
    private TextView checkin,checkout,roomnum,type,roomcharge,surcharge,totalX,bookingID,phone,name, TIME;
    private String checkinA, checkinB, checkinC, checkoutA, checkoutB, checkoutC, roomnumA,
            gID, typeA, roomchargeA, surchargeA,totalA,bookingIDA,phoneA,nameA;
    public int total = 1, totalBill = 1;
    AppCompatButton btnNavAllRoom, btnNavVacant;
    SearchView searchRoom;
    private String textsrc="", filterOption;
    Spinner filter;
    RoomTypeAdapter roomTypeAdapter;
    ArrayList <String> roomTypeList;
    List<Room> rooms;
    DatabaseReference bookingRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rooms_booked);

        btnNavAllRoom = findViewById(R.id.btn_AllRooms);
        btnNavAllRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Booked.this, AllRoom.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        btnNavVacant = findViewById(R.id.btn_Occupied);
        btnNavVacant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Booked.this, Vacant.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        searchRoom = findViewById(R.id.search_booked_room);
        searchRoom.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                textsrc = newText;
                filterOption = filter.getSelectedItem().toString();
                filteredList(newText, filterOption);
                return false;
            }
        });

        filter = findViewById(R.id.sp_Filter);
        roomTypeList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.roomtype)));
        roomTypeAdapter = new RoomTypeAdapter(this, roomTypeList);
        filter.setAdapter(roomTypeAdapter);
        filter.setOnItemSelectedListener(this);

        bookedRoomRecyclerView = findViewById(R.id.booked_room);
        bookedRooms = new ArrayList<Booking>();

        roomAdapter = new BookedRoomAdapter(bookedRooms, this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        bookedRoomRecyclerView.setAdapter(roomAdapter);
        bookedRoomRecyclerView.setLayoutManager(linearLayoutManager);
        roomAdapter.setOnButtonClickListener(this);

        FirebaseDatabase.getInstance().getReference("Booking").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot bookingDataSnapshot) {
                if (bookingDataSnapshot.exists()){
                    for (DataSnapshot bookingSnapshot:bookingDataSnapshot.getChildren()){
                        Booking booking = bookingSnapshot.getValue(Booking.class);
                        booking.setBookingID(bookingSnapshot.getKey());
                        String status = bookingSnapshot.child("status").getValue(String.class);
                        String checkin="Check in";
                        if (status.equals(checkin)){
                            bookedRooms.add(booking);
                        }
                        //roomAdapter.setData(bookedRooms);
                        roomAdapter.notifyDataSetChanged();

                    }
                }
                else {
                    Toast.makeText(Booked.this, "Not exist!!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rooms = new ArrayList<Room>();

        FirebaseDatabase.getInstance().getReference("Room").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot roomDataSnapshot) {
                if (roomDataSnapshot.exists()) {
                    for (DataSnapshot roomSnapshot : roomDataSnapshot.getChildren()){
                        Room roomItem = roomSnapshot.getValue(Room.class);
                        roomItem.setRoomID(roomSnapshot.getKey());

                        rooms.add(roomItem);
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur during the query
            }
        });


        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void onCheckOutButtonClick(String bookedId, String roomID, int surchargeValue) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy");
        String timeCO = timeFormat.format(new Date());
        String dateCO = dateFormat.format(new Date());
        dialog = new Dialog(Booked.this);
        dialog.setContentView(R.layout.booking_bill);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        checkin = dialog.findViewById(R.id.tv_checkintime);
        checkout = dialog.findViewById(R.id.tv_checkouttime);
        roomnum = dialog.findViewById(R.id.tv_roomnum);
        type = dialog.findViewById(R.id.tv_tob);
        roomcharge = dialog.findViewById(R.id.tv_roomchargemoney);
        surcharge = dialog.findViewById(R.id.tv_surchargemoney);
        totalX = dialog.findViewById(R.id.tv_totalsurchargemoney);
        bookingID = dialog.findViewById(R.id.tv_idbill);
        name = dialog.findViewById(R.id.tv_nameCus);
        phone = dialog.findViewById(R.id.tv_phonenocus);
        TIME = dialog.findViewById(R.id.tv_details);
        bookingRef = FirebaseDatabase.getInstance().getReference("Booking").child(bookedId);
        bookingRef.child("checkoutDate").setValue(dateCO);
        bookingRef.child("checkoutHour").setValue(timeCO);
        bookingRef.child("surcharge").setValue(surchargeValue);
        bookingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot bookingDataSnapshot) {
                if (bookingDataSnapshot.exists()) {
                    String checkinDate = bookingDataSnapshot.child("checkinDate").getValue(String.class);
                    String checkinHour = bookingDataSnapshot.child("checkinHour").getValue(String.class);
                    String bookType = bookingDataSnapshot.child("bookingType").getValue(String.class);
                    if (checkinDate.equals(dateCO) && bookType.equals("Hour")) {
                        try {
                            Date checkin = timeFormat.parse(checkinHour);
                            Date checkout = timeFormat.parse(timeCO);
                            long differenceMillis = checkout.getTime() - checkin.getTime();
                            float differenceHoursFloat = (float) differenceMillis / (60 * 60 * 1000);
                            int differenceHours = (int) Math.ceil(differenceHoursFloat);
                            String timeBill = String.valueOf(differenceHours);
                            String details = "("+timeBill+" hour)";
                            bookingRef.child("details").setValue(details);
                            TIME.setText(details);
                            FirebaseDatabase.getInstance().getReference("Room").orderByChild("roomID").equalTo(roomID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot roomDataSnapshot) {
                                    if (roomDataSnapshot.exists()) {
                                        for (DataSnapshot snapshot : roomDataSnapshot.getChildren()) {
                                            int pricePerHour = snapshot.child("pricebyHour").getValue(Integer.class);
                                            total = differenceHours * pricePerHour;
                                            roomchargeA = String.valueOf(total);
                                            bookingRef.child("total").setValue(total);
                                            totalBill = total + surchargeValue;
                                            totalA = String.valueOf(totalBill);
                                            bookingRef.child("totalBill").setValue(totalBill);
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Handle any errors that occur during the query
                                }
                            });
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else if (!checkinDate.equals(dateCO)) {
                        try {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            Date checkin = dateFormat.parse(checkinDate);
                            Date checkout = dateFormat.parse(dateCO);
                            long differenceMillis = checkout.getTime() - checkin.getTime();
                            long differenceDays = TimeUnit.MILLISECONDS.toDays(differenceMillis);
                            String timeBill = String.valueOf(differenceDays);
                            String details = "("+timeBill+" day)";
                            bookingRef.child("details").setValue(details);
                            TIME.setText(details);
                            FirebaseDatabase.getInstance().getReference("Room").orderByChild("roomID").equalTo(roomID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot roomDataSnapshot) {
                                    if (roomDataSnapshot.exists()) {
                                        for (DataSnapshot snapshot : roomDataSnapshot.getChildren()) {
                                            int pricePerDay = snapshot.child("pricebyDay").getValue(Integer.class);
                                            total = (int) (differenceDays * pricePerDay);
                                            roomchargeA = String.valueOf(total);
                                            bookingRef.child("total").setValue(total);
                                            totalBill = total + surchargeValue;
                                            totalA = String.valueOf(totalBill);
                                            bookingRef.child("totalBill").setValue(totalBill);
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Handle any errors that occur during the query
                                }
                            });
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (checkinDate.equals(dateCO) && bookType.equals("Day"))
                    {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        long differenceDays = 1;
                        String details = "(1 day)";
                        bookingRef.child("details").setValue(details);
                        TIME.setText(details);
                        FirebaseDatabase.getInstance().getReference("Room").orderByChild("roomID").equalTo(roomID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot roomDataSnapshot) {
                                if (roomDataSnapshot.exists()) {
                                    for (DataSnapshot snapshot : roomDataSnapshot.getChildren()) {
                                        int pricePerDay = snapshot.child("pricebyDay").getValue(Integer.class);
                                        total = (int) (differenceDays * pricePerDay);
                                        roomchargeA = String.valueOf(total);
                                        bookingRef.child("total").setValue(total);
                                        totalBill = total + surchargeValue;
                                        totalA = String.valueOf(totalBill);
                                        bookingRef.child("totalBill").setValue(totalBill);
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle any errors that occur during the query
                            }
                        });
                    }
                    checkinA = bookingDataSnapshot.child("checkinHour").getValue(String.class);
                    checkinB = bookingDataSnapshot.child("checkinDate").getValue(String.class);
                    checkinC = checkinA + "   " + checkinB;
                    checkoutA = bookingDataSnapshot.child("checkoutHour").getValue(String.class);
                    checkoutB = bookingDataSnapshot.child("checkoutDate").getValue(String.class);
                    checkoutC = checkoutA + "   " + checkoutB;
                    roomnumA = bookingDataSnapshot.child("rid").getValue(String.class);
                    surchargeA = String.valueOf(bookingDataSnapshot.child("surcharge").getValue(Integer.class));
                    bookingIDA = bookingDataSnapshot.child("bookingID").getValue(String.class);
                    typeA = bookingDataSnapshot.child("bookingType").getValue(String.class);
                    gID = bookingDataSnapshot.child("gid").getValue(String.class);
                    DatabaseReference guestRef = FirebaseDatabase.getInstance().getReference("Guest");
                    guestRef.child(gID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot guestDataSnapshot) {
                            if (guestDataSnapshot.exists()) {
                                nameA = guestDataSnapshot.child("gName").getValue(String.class);
                                phoneA = guestDataSnapshot.child("gPhone").getValue(String.class);
                                name.setText(nameA);
                                phone.setText(phoneA);
                            } else {
                                // Handle the case where the guest data does not exist
                            }
                            checkin.setText(checkinC);
                            checkout.setText(checkoutC);
                            roomnum.setText(roomnumA);
                            type.setText(typeA);
                            roomcharge.setText(roomchargeA);
                            surcharge.setText(surchargeA);
                            totalX.setText(totalA);
                            bookingID.setText(bookingIDA);
                            dialog.show();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle any errors that occur during the query
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur during the query
            }
        });
        AppCompatButton cancelButton = dialog.findViewById(R.id.btn_cancelbill);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        AppCompatButton doneButton = dialog.findViewById(R.id.btn_donebilladd);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("Booking").child(bookedId).child("status").setValue("checkout");
                FirebaseDatabase.getInstance().getReference("Room").orderByChild("roomID").equalTo(roomID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot roomDataSnapshot) {
                        if (roomDataSnapshot.exists()) {
                            FirebaseDatabase.getInstance().getReference("Room").child(roomID).child("rAvail").setValue(0);
                            for (int i = 0; i < bookedRooms.size(); i++){
                                if(bookedRooms.get(i).getBookingID() == bookedId){
                                    bookedRooms.remove(i);
                                    //roomAdapter.setData(bookedRooms);
                                    roomAdapter.notifyItemRemoved(i);
                                    break;
                                }
                            }
                            filterOption = filter.getSelectedItem().toString();
                            filteredList(textsrc, filterOption);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle any errors that occur during the query
                    }
                });
                dialog.dismiss();
            }
        });
    }

    private String roomType;
    private void filteredList (String searchText, String option){
        List<Booking> filterList  = new ArrayList<>();

        for (Booking item : bookedRooms){
            if (item.getRid().toLowerCase().contains(searchText.toLowerCase())){
                if (option.equalsIgnoreCase("All")){
                    filterList.add(item);

                }
                else{
                    for (Room r : rooms){
                        if (option.equalsIgnoreCase(r.getRoomType()) && item.getRid().equalsIgnoreCase(r.getRoomID())){
                            filterList.add(item);
                        }
                    }
                }
            }
        }
        if (searchText.isEmpty()){
            if (option.equalsIgnoreCase("All")){
                filterList = bookedRooms;
            }
            else {
                filterList.clear();
                for (Booking item : bookedRooms){
                    if (item.getRid().toLowerCase().contains(searchText.toLowerCase())){
                        for (Room r : rooms){
                            if (option.equalsIgnoreCase(r.getRoomType()) && item.getRid().equalsIgnoreCase(r.getRoomID())){
                                filterList.add(item);
                            }
                        }
                    }
                }
            }

            //searchRoom.clearFocus();
        }
        else if (filterList.isEmpty() && !searchText.isEmpty()){
            filterList.clear();
        }

        roomAdapter.setFilteredItem(filterList);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        filterOption = filter.getSelectedItem().toString();
        searchRoom.clearFocus();
        filteredList(textsrc, filterOption);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}