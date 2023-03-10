package com.genius.hrms.activity.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.hrms.R;
import com.genius.hrms.activity.model.AddedLocationModel;
import com.genius.hrms.activity.model.TeamReportModel;

import java.util.ArrayList;


public class TeamReportAdapter extends RecyclerView.Adapter<TeamReportAdapter.MyViewHolder> {
    ArrayList<TeamReportModel>itemList=new ArrayList<>();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.team_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvEmpName.setText(itemList.get(i).getEmpName());
        myViewHolder.tvDept.setText(itemList.get(i).getDeptName());
        myViewHolder.tvBranch.setText(itemList.get(i).getBranchName());
        myViewHolder.tvAttnDate.setText(itemList.get(i).getAttenDate());
        myViewHolder.tvInTime.setText("In Time:"+itemList.get(i).getInTime());
        myViewHolder.tvOutTime.setText("Out Time:"+itemList.get(i).getOutTime());
        myViewHolder.tvAddress.setText(itemList.get(i).getAddress());
        myViewHolder.tvStatus.setText(itemList.get(i).getStatus());




    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmpName,tvBranch,tvDept,tvAttnDate,tvInTime,tvOutTime,tvAddress,tvStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmpName=(TextView)itemView.findViewById(R.id.tvEmpName);
            tvBranch=(TextView)itemView.findViewById(R.id.tvBranch);
            tvDept=(TextView)itemView.findViewById(R.id.tvDept);
            tvAttnDate=(TextView)itemView.findViewById(R.id.tvAttnDate);
            tvInTime=(TextView)itemView.findViewById(R.id.tvInTime);
            tvOutTime=(TextView)itemView.findViewById(R.id.tvOutTime);
            tvAddress=(TextView)itemView.findViewById(R.id.tvAddress);
            tvStatus=(TextView)itemView.findViewById(R.id.tvStatus);


        }
    }

    public TeamReportAdapter(ArrayList<TeamReportModel> itemList) {
        this.itemList = itemList;
    }

    public void updateList(ArrayList<TeamReportModel> list){
        itemList = list;
        notifyDataSetChanged();
    }
}
