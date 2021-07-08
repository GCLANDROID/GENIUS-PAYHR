package com.genius.hrms.activity.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.hrms.R;
import com.genius.hrms.activity.dailylog.DailyLogReportActivity;
import com.genius.hrms.activity.model.DailyLogModel;


import java.util.ArrayList;


public class DailyLogAdapter extends RecyclerView.Adapter<DailyLogAdapter.MyViewHolder> {
    ArrayList<DailyLogModel>activityList=new ArrayList<>();
    Context context;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dailylog_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.tvDate.setText(activityList.get(i).getActivityDate());
        myViewHolder.tvInTime.setText(activityList.get(i).getActivityInTime());
        myViewHolder.tvOutTime.setText(activityList.get(i).getActivityOutTime());
        myViewHolder.tvInLocation.setText(activityList.get(i).getActivityInLocation());
        myViewHolder.tvOutLocation.setText(activityList.get(i).getActivityOutLocation());
        if (activityList.get(i).getActivityOutTime().equals("")){
            myViewHolder.llOutImage.setVisibility(View.GONE);
        }else {
            myViewHolder.llOutImage.setVisibility(View.VISIBLE);
        }

        myViewHolder.llInImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DailyLogReportActivity) context).openInBrowser(i);
            }
        });

        myViewHolder.llOutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DailyLogReportActivity) context).openOutBrowser(i);
            }
        });

        if (activityList.get(i).getInRemarks().equals("")){
            myViewHolder.llInRemark.setVisibility(View.GONE);
        }else {
            myViewHolder.llInRemark.setVisibility(View.VISIBLE);
            myViewHolder.tvRemarksIn.setText(activityList.get(i).getInRemarks());
        }

        if (activityList.get(i).getOutRemarks().equals("")){
            myViewHolder.llOutRemark.setVisibility(View.GONE);
        }else {
            myViewHolder.llOutRemark.setVisibility(View.VISIBLE);
            myViewHolder.tvRemarksOut.setText(activityList.get(i).getOutRemarks());
        }

        if (activityList.get(i).getActivityOutTime().equals("")){
            myViewHolder.llOutTime.setVisibility(View.GONE);
        }else {
            myViewHolder.llOutTime.setVisibility(View.VISIBLE);
        }

        if (activityList.get(i).getActivityOutLocation().equals("")){
            myViewHolder.llOutLocation.setVisibility(View.GONE);
        }else {
            myViewHolder.llOutLocation.setVisibility(View.VISIBLE);
        }



    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate,tvInTime,tvOutTime,tvInLocation,tvOutLocation,tvRemarksIn,tvRemarksOut;
        LinearLayout llInImage,llOutImage,llInRemark,llOutRemark,llOutTime,llOutLocation;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvInTime=(TextView)itemView.findViewById(R.id.tvInTime);
            tvOutTime=(TextView)itemView.findViewById(R.id.tvOutTime);
            tvInLocation=(TextView)itemView.findViewById(R.id.tvInLocation);
            tvOutLocation=(TextView)itemView.findViewById(R.id.tvOutLocation);
            tvRemarksIn=(TextView)itemView.findViewById(R.id.tvRemarksIn);
            tvRemarksOut=(TextView)itemView.findViewById(R.id.tvRemarksOut);
            llInImage=(LinearLayout)itemView.findViewById(R.id.llInImage);
            llOutImage=(LinearLayout)itemView.findViewById(R.id.llOutImage);
            llInRemark=(LinearLayout)itemView.findViewById(R.id.llInRemraks);
            llOutRemark=(LinearLayout)itemView.findViewById(R.id.llOutRemraks);
            llOutTime=(LinearLayout)itemView.findViewById(R.id.llOutTime);
            llOutLocation=(LinearLayout)itemView.findViewById(R.id.llOutLocation);



        }
    }

    public DailyLogAdapter(ArrayList<DailyLogModel> activityList, Context context) {
        this.activityList = activityList;
        this.context = context;
    }
}
