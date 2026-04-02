package com.genius.payhrms.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.leaveapplication.SortLeaveApplication.SortLeaveDetailsFragment;
import com.genius.payhrms.activity.model.SortLeaveDetailsModel;

import java.util.ArrayList;

public class SortLeaveEmployeeAdapter extends RecyclerView.Adapter<SortLeaveEmployeeAdapter.MyViewHolder>{
    Context context;
    Fragment fContext;
    ArrayList<SortLeaveDetailsModel> reportList;

    public SortLeaveEmployeeAdapter(Context context, Fragment fContext,  ArrayList<SortLeaveDetailsModel> reportList) {
        this.context = context;
        this.fContext = fContext;
        this.reportList = reportList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.sort_leave_item_employee,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvEmployee.setText(reportList.get(position).getEmployeeName());
        holder.tvApplicationDate.setText(reportList.get(position).getApplicationDate());
        holder.tvYear.setText(reportList.get(position).getYear());
        holder.tvMonth.setText(reportList.get(position).getMonth());
        holder.tvReason.setText(reportList.get(position).getReason());
        holder.tvStatus.setText(reportList.get(position).getStatus());
        holder.tvApprovedBy.setText((reportList.get(position).getApprovedBy().equals("null")) ? "" : reportList.get(position).getApprovedBy());
        holder.tvApprover.setText((reportList.get(position).getApprovedOn().equals("null")) ? "" : reportList.get(position).getApprovedOn());
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SortLeaveDetailsFragment) fContext).deleteApplication(reportList.get(position).getAID());
            }
        });
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvEmployee,tvApplicationDate,tvYear,tvMonth,tvReason,tvStatus,tvApprovedBy,tvApprover;
        ImageView imgDelete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmployee = itemView.findViewById(R.id.tvEmployee);
            tvApplicationDate = itemView.findViewById(R.id.tvApplicationDate);
            tvYear = itemView.findViewById(R.id.tvYear);
            tvMonth = itemView.findViewById(R.id.tvMonth);
            tvReason = itemView.findViewById(R.id.tvReason);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvApprovedBy = itemView.findViewById(R.id.tvApprovedBy);
            tvApprover = itemView.findViewById(R.id.tvApprover);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
}
