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

import java.util.ArrayList;


public class AttendanceCalenderAdapter extends RecyclerView.Adapter<AttendanceCalenderAdapter.MyViewHolder> {
    ArrayList<AttendanceCalenderModel>itemList=new ArrayList<>();
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.attendance_calender_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        if (itemList.get(i).getStatus().equalsIgnoreCase("HDL")){

            myViewHolder.llMain.setBackgroundColor(Color.parseColor("#8C5503"));
        }else if (itemList.get(i).getStatus().equalsIgnoreCase("WO")){

            myViewHolder.llMain.setBackgroundColor(Color.parseColor("#C67907"));

        }else if (itemList.get(i).getStatus().equalsIgnoreCase("A")){

            myViewHolder.llMain.setBackgroundColor(Color.parseColor("#B34205"));

        }else if (itemList.get(i).getStatus().equalsIgnoreCase("H")){

            myViewHolder.llMain.setBackgroundColor(Color.parseColor("#868789"));

        }else if (itemList.get(i).getStatus().equalsIgnoreCase("P")){

            myViewHolder.llMain.setBackgroundColor(Color.parseColor("#04A170"));

        }else {
            myViewHolder.llMain.setBackgroundColor(Color.parseColor("#419aec"));
        }

        myViewHolder.tvStatus.setText(itemList.get(i).getStatus());
        myViewHolder.tvDay.setText(itemList.get(i).getDay());
        myViewHolder.tvDate.setText(itemList.get(i).getDate());
        myViewHolder.tvTime.setText(itemList.get(i).getTime());




    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay,tvDate,tvTime,tvStatus;
        LinearLayout llMain;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=(TextView) itemView.findViewById(R.id.tvDate);
            tvDay=(TextView) itemView.findViewById(R.id.tvDay);
            tvTime=(TextView) itemView.findViewById(R.id.tvTime);
            tvStatus=(TextView)itemView.findViewById(R.id.tvStatus);

            llMain=(LinearLayout) itemView.findViewById(R.id.llMain);

        }
    }

    public AttendanceCalenderAdapter(ArrayList<AttendanceCalenderModel> itemList) {
        this.itemList = itemList;
    }
}
