package com.example.sunmoon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunmoon.R;
import com.example.sunmoon.models.Guest;

import java.util.List;

public class GuestAdapter extends RecyclerView.Adapter<GuestAdapter.ViewHolder>{
    private List guest;
    private Context gContext;

    public GuestAdapter(List _guest, Context gContext) {
        this.guest = _guest;
        this.gContext = gContext;
    }

    @NonNull
    @Override
    public GuestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View guestView =
                inflater.inflate(R.layout.guess_item_view, parent, false);

        ViewHolder viewHolder = new ViewHolder(guestView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GuestAdapter.ViewHolder holder, int position) {
        Guest gGuest = (Guest) guest.get(position);

        holder.guestName.setText(gGuest.getgName());
        holder.dateOfBirth.setText(gGuest.getgBirthday());
        holder.identification.setText(gGuest.getgIDCard());
        holder.phone.setText(gGuest.getgPhone());
    }

    @Override
    public int getItemCount() {
        return guest.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        public TextView guestName;
        public TextView dateOfBirth;
        public TextView identification;
        public TextView phone;

        public ViewHolder(View itemView) {
            super(itemView);

            guestName = itemView.findViewById(R.id.tvNameGuest1);
            dateOfBirth = itemView.findViewById(R.id.tvDateofBirthGuest1);
            identification = itemView.findViewById(R.id.tvIDGuest1);
            phone = itemView.findViewById(R.id.tvPhoneGuest1);

        }
    }
}
