package com.genius.payhrms.activity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.leaveapplication.SortLeaveApplication.SortLeaveApproverFragment;
import com.genius.payhrms.activity.model.SortLeaveApproverModel;

import java.util.ArrayList;

public class ShortLeaveApproverAdapter extends RecyclerView.Adapter<ShortLeaveApproverAdapter.MyViewHolder>{
    Context context;
    Fragment fContext;
    ArrayList<SortLeaveApproverModel> approverList;

    public ShortLeaveApproverAdapter(Context context, Fragment fContext, ArrayList<SortLeaveApproverModel> approverList) {
        this.context = context;
        this.fContext = fContext;
        this.approverList = approverList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.sort_leave_item_approver,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvEmpName.setText(approverList.get(position).getEmployeeName());
        holder.tvApplicationDate.setText(approverList.get(position).getApplicationDate());
        holder.tvMonth.setText(approverList.get(position).getMonth());
        holder.tvYear.setText(approverList.get(position).getYearID());
        holder.tvReason.setText(approverList.get(position).getReason());
        holder.tvApproveBy.setText((approverList.get(position).getApprovedBy().equals("null")) ? "" : approverList.get(position).getApprovedBy());
        Log.e("onBindViewHolder", ": "+approverList.get(position).getStatus());
        holder.tvStatus.setText(approverList.get(position).getStatus());
        holder.tvMins.setText(approverList.get(position).getAhortLeaveMins());
        SortLeaveApproverModel SApproverModel = approverList.get(position);
        if (SApproverModel.isSelected()){
            holder.imgTick.setVisibility(View.VISIBLE);
        }else {
            holder.imgTick.setVisibility(View.GONE);
        }

        holder.llTick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SApproverModel.setSelected(!SApproverModel.isSelected());
                if (SApproverModel.isSelected()) {
                    holder.imgTick.setVisibility(View.VISIBLE);
                    notifyDataSetChanged();
                    ((SortLeaveApproverFragment) fContext).updateAttendanceStatus(position, true );
                } else {
                    holder.imgTick.setVisibility(View.GONE);
                    ((SortLeaveApproverFragment) fContext).updateAttendanceStatus(position, false);
                    approverList.get(position).setSelected(false);
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return approverList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvEmpName,tvApplicationDate,tvMonth,tvYear,tvReason,tvApproveBy,tvStatus,tvMins;
        LinearLayout llTick;
        ImageView imgTick;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmpName = itemView.findViewById(R.id.tvEmpName);
            tvApplicationDate = itemView.findViewById(R.id.tvApplicationDate);
            tvMonth = itemView.findViewById(R.id.tvMonth);
            tvYear = itemView.findViewById(R.id.tvYear);
            tvReason = itemView.findViewById(R.id.tvReason);
            tvApproveBy = itemView.findViewById(R.id.tvApproveBy);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvMins = itemView.findViewById(R.id.tvMins);
            llTick = itemView.findViewById(R.id.llTick);
            imgTick = itemView.findViewById(R.id.imgTick);
        }
    }
}
