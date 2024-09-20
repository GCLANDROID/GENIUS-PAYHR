package com.genius.payhrms.activity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
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
import com.genius.payhrms.activity.model.TourViewModel;
import com.genius.payhrms.activity.utility.TimeDateConverter;

import java.util.ArrayList;

public class TourViewAdapter extends RecyclerView.Adapter<TourViewAdapter.MyViewHolder>{

    Context context;
    ArrayList<TourViewModel> itemList;
    Fragment fContext;
    public TourViewAdapter(Context context, ArrayList<TourViewModel> itemList) {
        this.context = context;
        this.itemList = itemList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.tour_view_row,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {


        String[] split = itemList.get(position).getAppliedOn().split("T");
        String firstSubString = split[0];

        holder.tvAppliedOn.setText(TimeDateConverter.convert_Date_YYYY_MM_DD_To_dd_MMM_yyyy(firstSubString));

        holder.tvTourDate.setText("From "+itemList.get(position).getStartDate()+" To "+itemList.get(position).getEndDate());

        holder.tvReason.setText(itemList.get(position).getReason());

        holder.tvApporvalStatus.setText(itemList.get(position).getApprovalStatus());
        if (itemList.get(position).getApprovalStatus().equalsIgnoreCase("Pending")){
            holder.imgPending.setVisibility(View.VISIBLE);
            holder.imgApprove.setVisibility(View.GONE);
            holder.imgReject.setVisibility(View.GONE);
        }else if (itemList.get(position).getApprovalStatus().equalsIgnoreCase("Approved")){
            holder.imgPending.setVisibility(View.GONE);
            holder.imgApprove.setVisibility(View.VISIBLE);
            holder.imgReject.setVisibility(View.GONE);
        }else {
            holder.imgPending.setVisibility(View.GONE);
            holder.imgApprove.setVisibility(View.GONE);
            holder.imgReject.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvAppliedOn,tvReason,tvTourDate,tvApporvalStatus;
        ImageView imgReject,imgApprove,imgPending;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvApporvalStatus=(TextView) itemView.findViewById(R.id.tvApporvalStatus);
            tvTourDate=(TextView) itemView.findViewById(R.id.tvTourDate);
            tvReason=(TextView) itemView.findViewById(R.id.tvReason);
            tvAppliedOn=(TextView) itemView.findViewById(R.id.tvAppliedOn);

            imgApprove=(ImageView) itemView.findViewById(R.id.imgApprove);
            imgReject=(ImageView) itemView.findViewById(R.id.imgReject);
            imgPending=(ImageView) itemView.findViewById(R.id.imgPending);
        }
    }
}
