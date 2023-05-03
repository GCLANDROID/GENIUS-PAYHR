package com.genius.hrms.activity.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import com.genius.hrms.activity.model.TabularTeamAttendaceReportModel;

import java.util.ArrayList;


public class TabularTeamAttendanceReportAdapter extends RecyclerView.Adapter<TabularTeamAttendanceReportAdapter.MyViewHolder> {
    ArrayList<TabularTeamAttendaceReportModel> activityList = new ArrayList<>();
    Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tabular_attendance_report_row, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        myViewHolder.tvDate.setText(activityList.get(i).getDate());
        myViewHolder.tvPunchTime.setText(activityList.get(i).getPunchTime());

        if (activityList.get(i).getStatus().equalsIgnoreCase("P")){

            myViewHolder.lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F20BCA03")));
            myViewHolder.tvStatus.setText("Present");

        }else if (activityList.get(i).getStatus().equalsIgnoreCase("A")){

            myViewHolder.lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F2FA0209")));
            myViewHolder.tvStatus.setText("Absent");
        }else if (activityList.get(i).getStatus().equalsIgnoreCase("L")){

            myViewHolder.lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F2DD7C03")));
            myViewHolder.tvStatus.setText("Leave");

        }else if (activityList.get(i).getStatus().equalsIgnoreCase("HDL")){

            myViewHolder.lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#C09A72")));
            myViewHolder.tvStatus.setText("Half Day \nLeave");

        }else if (activityList.get(i).getStatus().equalsIgnoreCase("H")){

            myViewHolder.lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFED45")));
            myViewHolder.tvStatus.setText("Holiday");
        }else if (activityList.get(i).getStatus().equalsIgnoreCase("WO")){

            myViewHolder.lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1589FF")));
            myViewHolder.tvStatus.setText("Weekly Off");
        }else if (activityList.get(i).getStatus().equalsIgnoreCase("WC")){

            myViewHolder.lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F20C5EDA")));
            myViewHolder.tvStatus.setText("Present");
        }else if (activityList.get(i).getStatus().equalsIgnoreCase("M")){

            myViewHolder.lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#AC4782")));
            myViewHolder.tvStatus.setText("Missed \nPunch");
        }else if (activityList.get(i).getStatus().equalsIgnoreCase("HD")){

            myViewHolder.lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFCA61")));
            myViewHolder.tvStatus.setText("Half Day");
        }else if (activityList.get(i).getStatus().equalsIgnoreCase("UL")){
            myViewHolder.lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DE2852")));
            myViewHolder.tvStatus.setText("Leave");
        }else if (activityList.get(i).getStatus().equalsIgnoreCase("C")){
            myViewHolder.lnStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#010360")));
            myViewHolder.tvStatus.setText("Current Day");
        }

    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvPunchTime,tvStatus;
        LinearLayout lnStatus;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            lnStatus = (LinearLayout) itemView.findViewById(R.id.lnStatus);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvPunchTime = (TextView) itemView.findViewById(R.id.tvPunchTime);
            tvStatus=(TextView) itemView.findViewById(R.id.tvStatus);

        }
    }

    public TabularTeamAttendanceReportAdapter(ArrayList<TabularTeamAttendaceReportModel> activityList, Context context) {
        this.activityList = activityList;
        this.context = context;
    }
}
