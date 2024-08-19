package com.genius.payhrms.activity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.attendance.tour.TourApprovalFragment;
import com.genius.payhrms.activity.model.TourApprovalModel;
import com.genius.payhrms.activity.model.TourViewModel;
import com.genius.payhrms.activity.utility.TimeDateConverter;

import org.json.JSONObject;

import java.util.ArrayList;

public class TourApprovalAdapter extends RecyclerView.Adapter<TourApprovalAdapter.MyViewHolder>{

    Context context;
    ArrayList<TourApprovalModel> itemList;
    Fragment fContext;
    public TourApprovalAdapter(Context context, ArrayList<TourApprovalModel> itemList,Fragment fContext) {
        this.context = context;
        this.itemList = itemList;
        this.fContext=fContext;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.tour_approval_view_row,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {





        holder.tvTourDate.setText(itemList.get(position).getEmpName()+"("+itemList.get(position).getEmpCode()+")'s Tour application from"+itemList.get(position).getStartDate()+" To "+itemList.get(position).getEndDate());

        holder.tvReason.setText(itemList.get(position).getReason());

        holder.tvApporvalStatus.setText(itemList.get(position).getApprovalStatus());


        holder.tvApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((TourApprovalFragment) fContext).TourApproval(itemList.get(position).getAID(),itemList.get(position).getTourMasterAID(),"1");
            }
        });

        holder.tvReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((TourApprovalFragment) fContext).TourApproval(itemList.get(position).getAID(),itemList.get(position).getTourMasterAID(),"-1");
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvReason,tvTourDate,tvApporvalStatus,tvApprove,tvReject;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvApporvalStatus=(TextView) itemView.findViewById(R.id.tvApporvalStatus);
            tvTourDate=(TextView) itemView.findViewById(R.id.tvTourDate);
            tvReason=(TextView) itemView.findViewById(R.id.tvReason);
            tvApprove=(TextView) itemView.findViewById(R.id.tvApprove);
            tvReject=(TextView) itemView.findViewById(R.id.tvReject);

        }
    }
}
