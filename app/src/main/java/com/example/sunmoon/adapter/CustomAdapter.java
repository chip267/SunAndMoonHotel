package com.example.sunmoon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunmoon.R;
import com.example.sunmoon.models.Booking;
import com.example.sunmoon.models.Room;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_VACANT = 1;
    private static final int VIEW_TYPE_BOOKED = 2;
    private DatabaseReference databaseReference;
    private List<Room> dataList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onVacantItemClick(Room room);
        void onBookedItemClick(Room room);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public CustomAdapter(Context context) {
        this.context = context;
        this.dataList = new ArrayList<>();
        this.databaseReference = FirebaseDatabase.getInstance().getReference("Room");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        switch (viewType) {
            case VIEW_TYPE_VACANT:
                view = inflater.inflate(R.layout.allrooms_vacant_item, parent, false);
                return new VacantViewHolder(view);
            case VIEW_TYPE_BOOKED:
                view = inflater.inflate(R.layout.allrooms_booked_item, parent, false);
                return new BookedViewHolder(view);
            default:
                throw new IllegalArgumentException("Invalid view type: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Room room = dataList.get(position);

        if (holder instanceof VacantViewHolder) {
            VacantViewHolder vacantViewHolder = (VacantViewHolder) holder;
            vacantViewHolder.tvroomVCID.setText(room.getRoomID());
            vacantViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onVacantItemClick(room);
                    }
                }
            });
        } else if (holder instanceof BookedViewHolder) {
            BookedViewHolder bookedViewHolder = (BookedViewHolder) holder;
            bookedViewHolder.tvroomBID.setText(room.getRoomID());
            bookedViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onBookedItemClick(room);
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        Room room = dataList.get(position);
        if (room.isrAvail() == 0) {
            return VIEW_TYPE_VACANT;
        } else if (room.isrAvail() == 1) {
            return VIEW_TYPE_BOOKED;
        } else {
            throw new IllegalArgumentException("Invalid item at position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class VacantViewHolder extends RecyclerView.ViewHolder {
        TextView tvroomVCID;
        public VacantViewHolder(@NonNull View itemView) {
            super(itemView);
            tvroomVCID = itemView.findViewById(R.id.numVacant);
        }
    }

    public static class BookedViewHolder extends RecyclerView.ViewHolder {
        TextView tvroomBID;

        public BookedViewHolder(@NonNull View itemView) {
            super(itemView);
            tvroomBID = itemView.findViewById(R.id.numBooked);
        }
    }
    public void fetchData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Room room = snapshot.getValue(Room.class);
                    dataList.add(room);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error if needed
            }
        });
    }
}
