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
import com.genius.payhrms.activity.leaveapplication.OtherLeaveApproverFragment;
import com.genius.payhrms.activity.model.OtherApproverModel;
import com.genius.payhrms.activity.utility.FindDocumentInformation;

import java.io.IOException;
import java.util.ArrayList;

public class OtherApproverAdapter extends RecyclerView.Adapter<OtherApproverAdapter.MyViewHolder>{
    private static final String TAG = "OtherApproverAdapter";
    Context context;
    Fragment fContext;
    ArrayList<OtherApproverModel> otherApproverList;

    public OtherApproverAdapter(Context context, Fragment fContext, ArrayList<OtherApproverModel> otherApproverList) {
        this.context = context;
        this.fContext = fContext;
        this.otherApproverList = otherApproverList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.other_leave_approver_raw,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.lnDelete.setVisibility(View.VISIBLE);
        holder.tvEmpName.setText(otherApproverList.get(position).Name);
        holder.tvType.setText("Adjustment Type:");
        holder.tvLeaveType.setText(otherApproverList.get(position).AdjustmentType);

        holder.tvStrtDate.setText(otherApproverList.get(position).StartDate);
        if (otherApproverList.get(position).AdjustmentType.equals("Compensatory off")){
            holder.tvStrtDate.setText(otherApproverList.get(position).offdate);
        } else {
            holder.tvStrtDate.setText(otherApproverList.get(position).StartDate);
        }
        holder.tvEndDate.setText(otherApproverList.get(position).EndDate);
        holder.tvReason.setText(otherApproverList.get(position).Reason);
        holder.tvStatus.setText(otherApproverList.get(position).ApprovalStatus);
        Log.e(TAG, "onBindViewHolder: "+otherApproverList.get(position).getApprovedBY());
        if (!otherApproverList.get(position).getApprovedBY().equals("null")){
            holder.tvApprovedBy.setText(otherApproverList.get(position).getApprovedBY());
        } else {
            holder.tvApprovedBy.setText("--");
        }

        final OtherApproverModel otherApproverModel = otherApproverList.get(position);

        if (otherApproverList.get(position).getApprovalStatus().equals("Pending") ||otherApproverList.get(position).getApprovalStatus().contains("Cancel") ){
            holder.llTick.setVisibility(View.VISIBLE);
            holder.llGreen.setVisibility(View.GONE);
            holder.llYellow.setVisibility(View.GONE);
        }else {
            holder.llTick.setVisibility(View.GONE);
            holder.llGreen.setVisibility(View.GONE);
            holder.llYellow.setVisibility(View.GONE);
        }

        if (otherApproverModel.isSelected()){
            holder.imgTick.setVisibility(View.VISIBLE);
        }else {
            holder.imgTick.setVisibility(View.GONE);
        }

        holder.llTick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherApproverModel.setSelected(!otherApproverModel.isSelected());
                if (otherApproverModel.isSelected()) {

                    holder.imgTick.setVisibility(View.VISIBLE);
                    notifyDataSetChanged();
                    ((OtherLeaveApproverFragment) fContext).updateAttendanceStatus(position, true );
                } else {
                    /*myViewHolder.imgFrstHalf.setVisibility(View.GONE);
                    myViewHolder.imgScndHalf.setVisibility(View.GONE);
                    myViewHolder.imgFull.setVisibility(View.GONE);*/
                    holder.imgTick.setVisibility(View.GONE);
                    ((OtherLeaveApproverFragment) fContext).updateAttendanceStatus(position, false);
                    otherApproverList.get(position).setSelected(false);
                    notifyDataSetChanged();
                }
            }
        });

        if (otherApproverList.get(position).getApprovalStatus().contains("Cancel")){
            holder.tvStatus.setText("Cancel request from applicant");
            holder.llTick.setVisibility(View.GONE);
            holder.lnDelete.setVisibility(View.VISIBLE);
        }else if (otherApproverList.get(position).getApprovalStatus().contains("Approved")){
            holder.tvStatus.setText(otherApproverList.get(position).getApprovalStatus());
            holder.llTick.setVisibility(View.GONE);
            holder.lnDelete.setVisibility(View.GONE);
        }else if (otherApproverList.get(position).getApprovalStatus().contains("Rejected")){
            holder.tvStatus.setText(otherApproverList.get(position).getApprovalStatus());
            holder.llTick.setVisibility(View.GONE);
            holder.lnDelete.setVisibility(View.GONE);
        }else {
            holder.tvStatus.setText(otherApproverList.get(position).getApprovalStatus());
            holder.llTick.setVisibility(View.VISIBLE);
            holder.lnDelete.setVisibility(View.VISIBLE);
        }

        holder.lnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((OtherLeaveApproverFragment) fContext).deleteFunction(otherApproverList.get(position).getAID());
            }
        });

        holder.llValue.setVisibility(View.GONE);

        if (otherApproverList.get(position).Documentlink != null || !otherApproverList.get(position).Documentlink.isEmpty()){
            holder.tvDocument.setVisibility(View.VISIBLE);
        } else {
            holder.tvDocument.setVisibility(View.GONE);
        }

        holder.llDocumentName.setVisibility(View.GONE);
        holder.tvDocumentName.setText(otherApproverList.get(position).getDocumentName());

        holder.tvDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ((OtherLeaveApproverFragment) fContext).showPdfView(FindDocumentInformation.getFileType(otherApproverList.get(position).Documentlink),FindDocumentInformation.getBase64Url(otherApproverList.get(position).Documentlink));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return otherApproverList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvEmpName, tvType, tvLeaveType,tvleaveStrtDate, tvStrtDate, tvLeaveEndDate, tvEndDate,
                tvReason,tvStatus,tvApprovedBy,tvDocument,tvDocumentName;
        LinearLayout llTick,llGreen,llYellow,lnDelete,llValue,llDocumentName;
        ImageView imgTick;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmpName = itemView.findViewById(R.id.tvEmpName);
            tvType = itemView.findViewById(R.id.tvType);
            tvLeaveType = itemView.findViewById(R.id.tvLeaveType);
            tvleaveStrtDate = itemView.findViewById(R.id.tvleaveStrtDate);
            tvStrtDate = itemView.findViewById(R.id.tvStrtDate);
            tvLeaveEndDate = itemView.findViewById(R.id.tvLeaveEndDate);
            tvEndDate = itemView.findViewById(R.id.tvEndDate);
            tvReason = itemView.findViewById(R.id.tvReason);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvApprovedBy = itemView.findViewById(R.id.tvApprovedBy);
            imgTick = itemView.findViewById(R.id.imgTick);
            lnDelete = itemView.findViewById(R.id.lnDelete);
            llTick = itemView.findViewById(R.id.llTick);
            llGreen = itemView.findViewById(R.id.llGreen);
            llYellow = itemView.findViewById(R.id.llYellow);
            lnDelete = itemView.findViewById(R.id.lnDelete);
            llValue = itemView.findViewById(R.id.llValue);
            tvDocument = itemView.findViewById(R.id.tvDocument);
            tvDocumentName = itemView.findViewById(R.id.tvDocumentName);
            llDocumentName = itemView.findViewById(R.id.llDocumentName);
        }
    }
}
