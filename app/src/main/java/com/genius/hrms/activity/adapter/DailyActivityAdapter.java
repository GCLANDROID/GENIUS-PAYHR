package com.genius.hrms.activity.adapter;

import android.content.Context;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.hrms.R;
import com.genius.hrms.activity.dailyactivity.DailyTaskReportActivity;
import com.genius.hrms.activity.model.DailyActivityModel;


import java.util.ArrayList;


public class DailyActivityAdapter extends RecyclerView.Adapter<DailyActivityAdapter.MyViewHolder> {
    ArrayList<DailyActivityModel>activityList=new ArrayList<>();
    Context context;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dailyactiviy_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.tvDate.setText(activityList.get(i).getActivityDate());
        myViewHolder.tvInTime.setText(activityList.get(i).getActivityInTime());
        myViewHolder.tvOutTime.setText(activityList.get(i).getActivityOutTime());
        myViewHolder.tvProject.setText(activityList.get(i).getProjectName());
        myViewHolder.tvSubProject.setText(activityList.get(i).getSubProjectName());
        myViewHolder.tvLocation.setText(activityList.get(i).getLocation());
        myViewHolder.user_profile_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DailyTaskReportActivity) context).openBrowser(i);
            }
        });



    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate,tvInTime,tvOutTime,tvLocation,tvProject,tvSubProject;
        ImageButton user_profile_photo;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvInTime=(TextView)itemView.findViewById(R.id.tvInTime);
            tvOutTime=(TextView)itemView.findViewById(R.id.tvOutTime);
            tvLocation=(TextView)itemView.findViewById(R.id.tvLocation);
            tvProject=(TextView)itemView.findViewById(R.id.tvProject);
            tvSubProject=(TextView)itemView.findViewById(R.id.tvSubProject);
            user_profile_photo=(ImageButton)itemView.findViewById(R.id.user_profile_photo);


        }
    }

    public DailyActivityAdapter(ArrayList<DailyActivityModel> activityList, Context context) {
        this.activityList = activityList;
        this.context = context;
    }
}
