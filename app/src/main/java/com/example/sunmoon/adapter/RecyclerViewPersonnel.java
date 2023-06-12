package com.example.sunmoon.adapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunmoon.R;
import com.example.sunmoon.models.Conditions;
import com.example.sunmoon.models.Employee;
import com.example.sunmoon.screen.PersonelDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewPersonnel extends RecyclerView.Adapter<RecyclerViewPersonnel.ViewHolder> {
    private List<Employee> employees = new ArrayList<>();
    public void setData(List<Employee> employees) {
        this.employees = employees;
    }
    @NonNull
    @Override
    public RecyclerViewPersonnel.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.personnel_item_view, parent, false);
        return new RecyclerViewPersonnel.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewPersonnel.ViewHolder holder, int position) {
        Employee employee = employees.get(position);
        holder.tvName.setText(String.format("%s%s", employee.getEmpLastName(), employee.getEmpFirstName()));
        holder.tvPosition.setText(employee.getEmpPosition());
        holder.tvID.setText(employee.getEmpID());
        holder.tvEmail.setText(employee.getEmpMail());
        holder.tvPhone.setText(employee.getEmpPhone());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                String employeeId = employee.getEmpIDCard();
                String empName = String.format("%s%s", employee.getEmpLastName(), employee.getEmpFirstName());
                String empPhone = employee.getEmpPhone();
                String empPosition = employee.getEmpPosition();
                String empBirthday = employee.getEmpBirthday();
                String empMail = employee.getEmpMail();
                String empAddress = employee.getEmpAddress();
                String empID = employee.getEmpID();
                Intent intent = new Intent(context, PersonelDetails.class);
                intent.putExtra("employeeId", employeeId);
                intent.putExtra("empName", empName);
                intent.putExtra("empPhone", empPhone);
                intent.putExtra("empPosition", empPosition);
                intent.putExtra("empBirthday", empBirthday);
                intent.putExtra("empMail", empMail);
                intent.putExtra("empAddress", empAddress);
                intent.putExtra("empID", empID);

                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return employees.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvPosition;
        TextView tvID;
        TextView tvEmail;
        TextView tvPhone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvNamePersonel1);
            tvPosition= itemView.findViewById(R.id.tvPositionPersonel1);
            tvID = itemView.findViewById(R.id.tvIDofPersonel1);
            tvEmail= itemView.findViewById(R.id.tvEmailofPersonel1);
            tvPhone = itemView.findViewById(R.id.tvPhoneofPersonel1);
        }
    }
}
