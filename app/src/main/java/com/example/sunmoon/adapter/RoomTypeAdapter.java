package com.example.sunmoon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sunmoon.R;

import java.util.ArrayList;

public class RoomTypeAdapter extends ArrayAdapter<String> {

    public RoomTypeAdapter(@NonNull Context context, ArrayList<String> typeList) {
        super(context, 0, typeList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView (int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_room_type_item, parent, false);
        }

        TextView tvType = convertView.findViewById(R.id.tv_room_type);
        String currentItem = getItem(position);

        if (currentItem != null){
            tvType.setText(currentItem);
        }

        return convertView;
    }
}
