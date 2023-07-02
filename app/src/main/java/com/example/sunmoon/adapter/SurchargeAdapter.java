package com.example.sunmoon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunmoon.R;
import com.example.sunmoon.models.Surcharge;

import java.util.ArrayList;
import java.util.List;

public class SurchargeAdapter extends RecyclerView.Adapter<SurchargeAdapter.ViewHolder>{
    private List<Surcharge> surcharges;
    private Context context;

    public SurchargeAdapter(List _surcharges, Context _context){
        this.surcharges = _surcharges;
        this.context = _context;
    }

    @NonNull
    @Override
    public SurchargeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View surchargeView = inflater.inflate(R.layout.room_subitem_view, parent, false);

        ViewHolder viewHolder = new ViewHolder(surchargeView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SurchargeAdapter.ViewHolder holder, int position) {
        Surcharge surcharge = surcharges.get(position);
        holder.detail.setText(surcharge.getDetail());
        holder.cost.setText(surcharge.getPrice());
    }

    @Override
    public int getItemCount() {
        return surcharges.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView detail;
        public TextView cost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            detail = itemView.findViewById(R.id.tv_name);
            cost = itemView.findViewById(R.id.tv_cost);
        }
    }
}
