package com.genius.hrms.activity.adapter;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.hrms.R;

import com.genius.hrms.activity.model.AttendanceModuleForPPS;

import java.util.ArrayList;


public class AttendanceForPPSAdapter extends RecyclerView.Adapter<AttendanceForPPSAdapter.MyViewHolder> {
    ArrayList<AttendanceModuleForPPS>attendanceInfoList=new ArrayList<>();
    @NonNull
    @Override
    public AttendanceForPPSAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.attendance_report_pps_raw,viewGroup,false);

        return new AttendanceForPPSAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceForPPSAdapter.MyViewHolder myViewHolder, int i) {

        if (!attendanceInfoList.get(i).getAttendanceDate().equals("")) {
            myViewHolder.tvDate.setText(attendanceInfoList.get(i).getAttendanceDate());
        }else {
            myViewHolder.tvDate.setText("N/A");
        }
        if (!attendanceInfoList.get(i).getAttendanceInTime().equals("")) {

            myViewHolder.tvInTime.setText(attendanceInfoList.get(i).getAttendanceInTime());
        }else {
            myViewHolder.tvInTime.setText("N/A");
        }
        if (!attendanceInfoList.get(i).getAttendanceOutTime().equals("")) {
            myViewHolder.tvOutTime.setText(attendanceInfoList.get(i).getAttendanceOutTime());
        }
        else {
            myViewHolder.tvOutTime.setText("N/A");
        }
        if (!attendanceInfoList.get(i).getAttendanceLocation().equals("")) {
            myViewHolder.tvLocation.setText(attendanceInfoList.get(i).getAttendanceLocation());
        }else {
            myViewHolder.tvLocation.setText("N/A");
        }
        if (!attendanceInfoList.get(i).getAttendanceReply().equals("")) {
            myViewHolder.tvReply.setText(attendanceInfoList.get(i).getAttendanceReply());
        }else {
            myViewHolder.tvReply.setText("N/A");
        }
        if (!attendanceInfoList.get(i).getAttendanceType().equals("")) {
            myViewHolder.tvAttendanceType.setText(attendanceInfoList.get(i).getAttendanceType());
        }else {
            myViewHolder.tvAttendanceType.setText("N/A");
        }

    }

    @Override
    public int getItemCount() {
        return attendanceInfoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate,tvInTime,tvOutTime,tvLocation,tvReply,tvAttendanceType;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvInTime=(TextView)itemView.findViewById(R.id.tvInTime);
            tvOutTime=(TextView)itemView.findViewById(R.id.tvOutTime);
            tvLocation=(TextView)itemView.findViewById(R.id.tvLocation);
            tvReply=(TextView)itemView.findViewById(R.id.tvReply);
            tvAttendanceType=(TextView)itemView.findViewById(R.id.tvAttendancetype);
        }
    }

    public AttendanceForPPSAdapter(ArrayList<AttendanceModuleForPPS> attendanceInfoList) {
        this.attendanceInfoList = attendanceInfoList;
    }
}
