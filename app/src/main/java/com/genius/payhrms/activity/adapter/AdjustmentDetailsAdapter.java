package com.genius.payhrms.activity.adapter;

import android.annotation.SuppressLint;
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
import com.genius.payhrms.activity.leaveapplication.OtherApplicationDetailsFragment;
import com.genius.payhrms.activity.model.AdjustmentDetailsModel;

import java.util.ArrayList;

public class AdjustmentDetailsAdapter extends RecyclerView.Adapter<AdjustmentDetailsAdapter.MyViewHolder>{

    Context context;
    ArrayList<AdjustmentDetailsModel> adjustmentList;
    Fragment fContext;
    public AdjustmentDetailsAdapter(Context context, ArrayList<AdjustmentDetailsModel> adjustmentList, Fragment fContext) {
        this.context = context;
        this.adjustmentList = adjustmentList;
        this.fContext = fContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.adjustment_report_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvAdjustmentType.setText(adjustmentList.get(position).AdjustmentType);
        if (adjustmentList.get(position).AdjustmentType.equals("Compensatory off")){
            holder.tvLeaveStrtDate.setText("Off Date:");
            holder.tvLeaveEndDate.setText("Leave Date:");
        } else {
            holder.tvLeaveStrtDate.setText("Start Date:");
            holder.tvLeaveEndDate.setText("End Date:");
        }
        holder.tvAppliedDate.setText(adjustmentList.get(position).AppliedDate);
        if (adjustmentList.get(position).AdjustmentType.equals("Compensatory off")){
            holder.tvStrtDate.setText(adjustmentList.get(position).offdate);
        } else {
            holder.tvStrtDate.setText(adjustmentList.get(position).StartDate);
        }
        holder.tvEndDate.setText(adjustmentList.get(position).EndDate);
        holder.tvReason.setText(adjustmentList.get(position).Reason);
        holder.tvStatus.setText(adjustmentList.get(position).ApprovalStatus);
        holder.tvApprovedBy.setText((adjustmentList.get(position).ApprovedBY.equals("null"))?"--":adjustmentList.get(position).ApprovedBY);
        holder.tvApprover.setText((adjustmentList.get(position).ApprovedBY.equals("null"))?"--":adjustmentList.get(position).ApprovedBY);
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OtherApplicationDetailsFragment) fContext).deleteAdjustments(adjustmentList.get(position).AID);
            }
        });
    }

    @Override
    public int getItemCount() {
        return adjustmentList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvAdjustmentType,tvAppliedDate,tvStrtDate,tvEndDate,tvLeaveStrtDate,tvLeaveEndDate,tvReason,tvStatus,tvApprovedBy,tvApprover;
        ImageView imgDelete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAdjustmentType = itemView.findViewById(R.id.tvAdjustmentType);
            tvAppliedDate = itemView.findViewById(R.id.tvAppliedDate);
            tvStrtDate = itemView.findViewById(R.id.tvStrtDate);
            tvEndDate = itemView.findViewById(R.id.tvEndDate);
            tvReason = itemView.findViewById(R.id.tvReason);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvApprovedBy = itemView.findViewById(R.id.tvApprovedBy);
            tvLeaveStrtDate = itemView.findViewById(R.id.tvLeaveStrtDate);
            tvLeaveEndDate = itemView.findViewById(R.id.tvLeaveEndDate);
            tvApprover = itemView.findViewById(R.id.tvApprover);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
}
