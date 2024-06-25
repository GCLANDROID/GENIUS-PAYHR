package com.genius.payhrms.activity.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.model.SubordinateReportDetailsModel;


import java.util.ArrayList;


public class SubordinateReportAdapter extends RecyclerView.Adapter<SubordinateReportAdapter.MyViewHolder> {
    ArrayList<SubordinateReportDetailsModel>itemList=new ArrayList<>();
    Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.subordinate_report_details_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.tvEmpName.setText(itemList.get(i).getEmpName());
        myViewHolder.tvAddress.setText(itemList.get(i).getAddress());
        myViewHolder.tvFenceTime.setText(itemList.get(i).getFenceTime());
        if (itemList.get(i).getAddress().equals("")){
            myViewHolder.llAddress.setVisibility(View.GONE);
        }else {
            myViewHolder.llAddress.setVisibility(View.VISIBLE);
        }

        if (itemList.get(i).getFenceTime().equals("")){
            myViewHolder.llTime.setVisibility(View.GONE);
        }else {
            myViewHolder.llTime.setVisibility(View.VISIBLE);
        }

        if (itemList.get(i).getFenceType().equals("IN")){
            myViewHolder.llGreen.setVisibility(View.VISIBLE);
            myViewHolder.llYellow.setVisibility(View.GONE);
            myViewHolder.llRed.setVisibility(View.GONE);
        }else if (itemList.get(i).getFenceType().equals("OUT")){
            myViewHolder.llGreen.setVisibility(View.GONE);
            myViewHolder.llYellow.setVisibility(View.VISIBLE);
            myViewHolder.llRed.setVisibility(View.GONE);
        }else {
            myViewHolder.llGreen.setVisibility(View.GONE);
            myViewHolder.llYellow.setVisibility(View.GONE);
            myViewHolder.llRed.setVisibility(View.VISIBLE);
        }





    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmpName,tvAddress,tvFenceTime;
        LinearLayout llYellow,llRed,llGreen,llTime,llAddress;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmpName=(TextView)itemView.findViewById(R.id.tvEmpName);
            tvAddress=(TextView)itemView.findViewById(R.id.tvAddress);
            tvFenceTime=(TextView)itemView.findViewById(R.id.tvFenceTime);

            llYellow=(LinearLayout)itemView.findViewById(R.id.llYellow);
            llRed=(LinearLayout)itemView.findViewById(R.id.llRed);
            llGreen=(LinearLayout)itemView.findViewById(R.id.llGreen);
            llTime=(LinearLayout)itemView.findViewById(R.id.llTime);
            llAddress=(LinearLayout)itemView.findViewById(R.id.llAddress);





        }
    }

    public SubordinateReportAdapter(ArrayList<SubordinateReportDetailsModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
