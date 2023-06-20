package com.example.sunmoon.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sunmoon.R;
import com.example.sunmoon.adapter.BookedRoomAdapter;
import com.example.sunmoon.adapter.GuestAdapter;
import com.example.sunmoon.adapter.RecyclerViewAdapter;
import com.example.sunmoon.adapter.RecyclerViewHandledAdapter;
import com.example.sunmoon.adapter.RoomSortAdapter;
import com.example.sunmoon.adapter.RoomTypeAdapter;
import com.example.sunmoon.adapter.VacantAdapter;
import com.example.sunmoon.models.Booking;
import com.example.sunmoon.models.Conditions;
import com.example.sunmoon.models.Guest;
import com.example.sunmoon.models.Room;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Vacant extends AppCompatActivity implements VacantAdapter.OnButtonClickListener {

    List<Room> listroom = new ArrayList<>();
    RecyclerView recyclerView;
    VacantAdapter adapter;
    ImageView btn_back;

    Button allroom, booked;

    Spinner filter, sort;
    RoomTypeAdapter roomTypeAdapter;
    RoomSortAdapter roomSortAdapter;
    ArrayList <String> roomTypeList;
    SearchView searchRoom;
    private String textsrc="", filterOption, sortOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_vacant);

        btn_back = findViewById(R.id.imageViewBackhome);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
                finish();
            }
        });

        allroom = findViewById(R.id.btn_AllRooms);
        allroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AllRoom.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        booked = findViewById(R.id.btn_Booked);
        booked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Booked.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        recyclerView = findViewById(R.id.list_vacant_room);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new VacantAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnButtonClickListener(Vacant.this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference roomRef = database.getReference("Room");
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    List<Room> filteredRoom = new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Room room = snapshot.getValue(Room.class);

                        if (room != null && room.isrAvail() == 0) {
                            filteredRoom.add(room);
                        }
                    }
                    listroom = filteredRoom;
                    adapter.setData(filteredRoom);
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Vacant.this, "Error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
                Toast.makeText(Vacant.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        searchRoom = findViewById(R.id.search_vacant);
        searchRoom.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                textsrc = newText;
                filterOption = filter.getSelectedItem().toString();
                sortOption = sort.getSelectedItem().toString();
                filteredList(newText, filterOption, sortOption);
                return false;
            }
        });

        filter = findViewById(R.id.sp_Filter);
        roomTypeList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.roomtype)));
        roomTypeAdapter = new RoomTypeAdapter(this, roomTypeList);
        filter.setAdapter(roomTypeAdapter);
        filter.setOnItemSelectedListener(new RoomTypeSpinnerClass());

        sort = findViewById(R.id.sp_Sort);
        roomSortAdapter = new RoomSortAdapter(this, new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.roomsort))));
        sort.setAdapter(roomSortAdapter);
        sort.setOnItemSelectedListener(new RoomSortSpinnerClass());


    }
    public void onButtonClick(String roomID) {
        Intent intent = new Intent(getApplicationContext(), BookingForm.class);
        intent.putExtra("roomID", roomID);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    private void filteredList (String searchText, String option, String _sortOption){
        List<Room> filterList  = new ArrayList<>();

        for (Room item : listroom){
            if (item.getRoomID().toLowerCase().contains(searchText.toLowerCase())){
                if (option.equalsIgnoreCase("All")){
                    filterList.add(item);
                }
                else{
                    if (option.equalsIgnoreCase(item.getRoomType())){
                        filterList.add(item);
                    }
                }
            }
        }
        if (searchText.isEmpty()){
            if (option.equalsIgnoreCase("All")){
                filterList = listroom;
            }
            else {
                filterList.clear();
                for (Room item : listroom){
                    if (item.getRoomID().toLowerCase().contains(searchText.toLowerCase())){
                        if (option.equalsIgnoreCase(item.getRoomType())){
                            filterList.add(item);
                        }
                    }
                }
            }

            //searchRoom.clearFocus();
        }
        else if (filterList.isEmpty() && !searchText.isEmpty()){
            filterList.clear();
        }
        if (_sortOption.equalsIgnoreCase("Ascending price")){
            Collections.sort(filterList, Room.AscendingPriceComparator);
        }
        else if (_sortOption.equalsIgnoreCase("Descending price")){
            Collections.sort(filterList, Room.DescendingPriceComparator);
        }

        adapter.setFilteredItem(filterList);
    }

    class RoomTypeSpinnerClass implements AdapterView.OnItemSelectedListener
    {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            filterOption = filter.getSelectedItem().toString();
            sortOption = sort.getSelectedItem().toString();
            searchRoom.clearFocus();
            filteredList(textsrc, filterOption, sortOption);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    class RoomSortSpinnerClass implements AdapterView.OnItemSelectedListener
    {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            filterOption = filter.getSelectedItem().toString();
            sortOption = sort.getSelectedItem().toString();
            searchRoom.clearFocus();
            filteredList(textsrc, filterOption, sortOption);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

}