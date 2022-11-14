package com.genius.hrms.activity.adapter;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.hrms.R;
import com.genius.hrms.activity.model.AttendanceCalenderModel;
import com.genius.hrms.activity.model.CompanyAssetModel;

import java.util.ArrayList;


public class CompanyAssetAdapter extends RecyclerView.Adapter<CompanyAssetAdapter.MyViewHolder> {
    ArrayList<CompanyAssetModel>itemList=new ArrayList<>();
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.company_asset_row,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvAssetName.setText(itemList.get(i).getAssetName());
        myViewHolder.tvAssignedDate.setText(itemList.get(i).getAssignedDate());
        myViewHolder.tvReleasedDate.setText(itemList.get(i).getRelaseDate());




    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvAssetName,tvAssignedDate,tvReleasedDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAssetName=(TextView) itemView.findViewById(R.id.tvAssetName);
            tvAssignedDate=(TextView) itemView.findViewById(R.id.tvAssignedDate);
            tvReleasedDate=(TextView) itemView.findViewById(R.id.tvReleasedDate);


        }
    }

    public CompanyAssetAdapter(ArrayList<CompanyAssetModel> itemList) {
        this.itemList = itemList;
    }
}
