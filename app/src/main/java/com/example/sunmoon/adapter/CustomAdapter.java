package com.example.sunmoon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunmoon.R;
import com.example.sunmoon.models.Booking;
import com.example.sunmoon.models.Room;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_VACANT = 1;
    private static final int VIEW_TYPE_BOOKED = 2;

    private List<Room> dataList;
    private Context context;

    public CustomAdapter(Context context, List<Room> dataList) {
        this.context = context;
        this.dataList = dataList;
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
            // Bind data and set up views for allrooms_vacant_item layout
            // Use `booking` object to access the data for the vacant item
        } else if (holder instanceof BookedViewHolder) {
            BookedViewHolder bookedViewHolder = (BookedViewHolder) holder;
            // Bind data and set up views for allrooms_booked_item layout
            // Use `booking` object to access the data for the booked item
        }
    }

    @Override
    public int getItemViewType(int position) {
        Room room = dataList.get(position);

        //if (/* Condition 1: Replace with your actual condition to determine if it should be vacant */) {
        //    return VIEW_TYPE_VACANT;
        //} else if (/* Condition 2: Replace with your actual condition to determine if it should be booked */) {
        //    return VIEW_TYPE_BOOKED;
        //} else {
        //    throw new IllegalArgumentException("Invalid item at position: " + position);
        //}
        return 1;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class VacantViewHolder extends RecyclerView.ViewHolder {
        public VacantViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views for allrooms_vacant_item layout
        }
    }

    public static class BookedViewHolder extends RecyclerView.ViewHolder {
        // Views for allrooms_booked_item layout

        public BookedViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views for allrooms_booked_item layout
        }
    }
}
