package com.genius.payhrms.activity.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.attendance.AttenApprovalActivity;
import com.genius.payhrms.activity.model.AttendanceApprovalModule;

import java.util.ArrayList;


public class AttendanceApprovalForPPSAdapter extends RecyclerView.Adapter<AttendanceApprovalForPPSAdapter.MyViewHolder> {
    ArrayList<AttendanceApprovalModule>attendanceInfoList=new ArrayList<>();
    Context context;
    @NonNull
    @Override
    public AttendanceApprovalForPPSAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.approvalraw,viewGroup,false);

        return new AttendanceApprovalForPPSAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AttendanceApprovalForPPSAdapter.MyViewHolder myViewHolder, final int i) {
        final AttendanceApprovalModule attandanceModel = attendanceInfoList.get(i);

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

        myViewHolder.tvEmpId.setText(attendanceInfoList.get(i).getEmpId());
        myViewHolder.tvEmpName.setText(attendanceInfoList.get(i).getEmpName());
        myViewHolder.tvAttId.setText(attendanceInfoList.get(i).getAttId());

        if (attendanceInfoList.get(i).isSelected()){
            myViewHolder.imgLike.setVisibility(View.VISIBLE);
        }else {
            myViewHolder.imgLike.setVisibility(View.GONE);
        }
        myViewHolder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attandanceModel.setSelected(!attandanceModel.isSelected());
                // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);

                if (attandanceModel.isSelected()) {

                    myViewHolder.imgLike.setVisibility(View.VISIBLE);
                    attendanceInfoList.get(i).setSelected(true);
                    notifyDataSetChanged();

                    ((AttenApprovalActivity) context).updateAttendanceStatus(i, true );



                } else {
                    myViewHolder.imgLike.setVisibility(View.GONE);
                    ((AttenApprovalActivity) context).updateAttendanceStatus(i, false);
                    attendanceInfoList.get(i).setSelected(false);
                    notifyDataSetChanged();
                }

            }
        });




    }

    @Override
    public int getItemCount() {
        return attendanceInfoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate,tvInTime,tvOutTime,tvLocation,tvReply,tvAttendanceType,tvEmpId,tvEmpName,tvAttId;
        ImageView imgLike;
        RelativeLayout llMain;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvInTime=(TextView)itemView.findViewById(R.id.tvInTime);
            tvOutTime=(TextView)itemView.findViewById(R.id.tvOutTime);
            tvLocation=(TextView)itemView.findViewById(R.id.tvLocation);
            tvReply=(TextView)itemView.findViewById(R.id.tvReply);
            tvAttendanceType=(TextView)itemView.findViewById(R.id.tvAttendancetype);
            tvEmpId=(TextView)itemView.findViewById(R.id.tvEmpId);
            tvEmpName=(TextView)itemView.findViewById(R.id.tvEmpName);
            tvAttId=(TextView)itemView.findViewById(R.id.tvAttId);

            llMain=(RelativeLayout) itemView.findViewById(R.id.rlMain);
            imgLike=(ImageView) itemView.findViewById(R.id.imgLike);
        }
    }

    public AttendanceApprovalForPPSAdapter(ArrayList<AttendanceApprovalModule> attendanceInfoList, Context context) {
        this.attendanceInfoList = attendanceInfoList;
        this.context = context;
    }
}
