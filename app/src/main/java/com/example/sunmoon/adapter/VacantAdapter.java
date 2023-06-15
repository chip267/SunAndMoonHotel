package com.example.sunmoon.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunmoon.R;
import com.example.sunmoon.models.Booking;
import com.example.sunmoon.models.Conditions;
import com.example.sunmoon.models.Room;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VacantAdapter extends RecyclerView.Adapter<VacantAdapter.ViewHolder> {
    private List<Room> rooms = new ArrayList<>();
    public interface OnButtonClickListener {
        void onButtonClick(String roomID);
    }

    private OnButtonClickListener onButtonClickListener;

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.onButtonClickListener = listener;
    }
    public void setData(List<Room> rooms) {
        this.rooms = rooms;
    }

    @NonNull
    @Override
    public VacantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_vacant_item_view, parent, false);
        return new VacantAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VacantAdapter.ViewHolder holder, int position) {
        Room room = rooms.get(position);
        String roomType = room.getRoomType();
        if (roomType.equals("Standard")) {
            holder.imagePhong.setImageResource(R.drawable.standard_ava);
        } else if (roomType.equals("Deluxe")) {
            holder.imagePhong.setImageResource(R.drawable.deluxe_ava);
        }
        holder.tvKind.setText(room.roomType);
        double money = room.getPricebyDay();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
        String moneyString = decimalFormat.format(money) + " VND";
        holder.tvMoney.setText(moneyString);
        holder.tvRoomNum.setText("Room "+room.getRoomID());
        holder.btnBooknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onButtonClickListener != null) {
                    String roomID = room.getRoomID();
                    onButtonClickListener.onButtonClick(roomID);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvKind;
        TextView tvMoney;
        TextView tvRoomNum;
        AppCompatButton btnBooknow;
        ImageView imagePhong;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMoney = itemView.findViewById(R.id.tv_moneyVacant);
            tvKind = itemView.findViewById(R.id.tv_kind);
            tvRoomNum = itemView.findViewById(R.id.tv_roomVacant);
            btnBooknow = itemView.findViewById(R.id.btn_booknow);
            imagePhong = itemView.findViewById(R.id.imageViewPhong);
        }
    }
}
